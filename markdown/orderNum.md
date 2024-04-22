# 주문번호 생성 알고리즘 리펙토링

기존의 주문 번호 생성 알고리즘 방식은 주문 객체 생성시 주문 날짜에 랜덤한 salt 값을 추가하여 생성하는 방식 이었습니다, 
이 방식은 난수의 특성상 유일키 제약 조건을 위배할 가능성이 있습니다, 아무리 큰 난수범위를 정해도 중복된 값이 존재할 가능성이 있기 때문입니다.

![orderNum2](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/6389a96c-b25f-4152-bf31-45925c67f13e)




중복된 데이터를 발생시키지 않으면서 성능 향샹을 위해 DB에 접근하지 않는 생성방법을 고려한 결과 트위터 스노우플레이크 라는 생성 시스템이 있었습니다

### 트위터 스노우플레이크
* 트위터 스노우플레이크는 트위터에서 개발한 고유 ID 생성 시스템 입니다, 분산 환경에서 빠르게 고유한 ID를 생성할 수 있습니다.
* 64비트 정수로 ID를 생성하며 현재 시간을 기반으로 ID를 생성합니다, 따라서 생성된 ID는 시간순으로 증가합니다
* 스노우플레이크는 시간 정보, 노드 정보, 시퀀스 번호의 조합으로 ID를 생성하기 때문에 고유한 ID를 보장합니다.
* 시간동기화가 필요하고 1ms 동안 생성할 수 있는 ID수가 2^12, 4,096개로 제한되어 있습니다.
  * 실제 비즈니스 에서 1ms 동안 4,096개의 주문이 생성될 가능성은 현저히 낮습니다, 따라서 스노우 플레이크의 ID 개수제한은 문제가 되기 어렵다고 할 수 있습니다, 만일 생성제한에 걸린다고 해도 지연 시간이 매우 짦기 때문에 실질적인 서비스 지연은 거의 발생하지 않습니다
  * 그렇기 때문에 스노우플레이크는 매우 효율적이며 동시성 문제도 해결할 수 있는 방법이라고 생각합니다. 

#### 트위터 스노우플레이크 적용
##### 테스트 코드
```java
/*
        테스트 시나리오
        1000명의 회원이 주문할 경우 주문번호 중복 확인
        
 */
// 동시에 여러 스레드에서 안전하게 시퀀스 번호를 관리하기 위한 AtomicInteger
private static final AtomicInteger sequenceNumber = new AtomicInteger(0);
// 마지막으로 ID를 생성한 시간을 저장하는 변수
private static long lastTimeStamp = -1;

public static synchronized String generateNumber() {
      long id = 0L;

      // 현재 시간에서 시작 시간을 뺀 값을 계산
      long timestamp = System.currentTimeMillis() - 1288834974657L;

      // 동일 밀리초에 여러건이 호출될 경우의 처리
      if (timestamp == lastTimeStamp) {
          int currentSequence = sequenceNumber.incrementAndGet();
          // 4095를 초과하면 다음 밀리초까지 기다림
          if (currentSequence > 4095) {
              while (timestamp <= lastTimeStamp) {
                  timestamp = System.currentTimeMillis() - 1288834974657L;
              }
              // 초기화
              sequenceNumber.set(0);
          }
      } else {
          sequenceNumber.set(0);
      }
      lastTimeStamp = timestamp;

      id |= (timestamp << 22);

      // region 코드와 서버 위치 값을 합쳐 할당
      int regionCode = 3;
      int serverPosition = 5;
      long serverInfo = (regionCode << 7) | serverPosition;
      id |= (serverInfo << 12);

      // 시퀀스 번호 할당
      id |= sequenceNumber.get();
      return String.valueOf(id);
  }

  
@Test
public void generateOrderNumberInTwitterSnowflake() throws InterruptedException, ExecutionException {
    int numberOfThreads = 1000;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    
    Set<Object> orderNumbers = Collections.synchronizedSet(new HashSet<>());
    List<Callable<Void>> tasks = new ArrayList<>();
    
    for (int i = 0; i < numberOfThreads; i++) {
      tasks.add(() -> {
        String orderNumber = generateNumber();
        orderNumbers.add(orderNumber);
        return null;
      });
    }
    
    List<Future<Void>> futures = executorService.invokeAll(tasks);
    
    // 모든 작업이 완료될 때까지 대기
    for (Future<Void> future : futures) {
      future.get();
    }
    
    assertThat(numberOfThreads).isEqualTo(orderNumbers.size());
    
    executorService.shutdown();
    orderNumbers.forEach(System.out::println);
}
```

#### 프로젝트 적용
Generator 클래스를 만들고 스프링 빈에 적용시켜 사용해보겠습니다.

```java
package org.project.shop.auth;

public class SnowflakeGenerator {
    private final long startTime = 1288834974657L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long maxWorkerId = ~(-1L << workerIdBits);
    private final long maxDatacenterId = ~(-1L << datacenterIdBits);
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = ~(-1L << sequenceBits);

    private long workerId;
    private long serverId;
    private long sequence = 0L;
    private static long lastTimestamp = -1;

    public SnowflakeGenerator() {
        this.workerId = 3L;
        this.serverId = 5L;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("시간이 되돌아갔습니다. " + (lastTimestamp - timestamp) + "ms만큼 기다려주세요.");
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - startTime) << timestampLeftShift) |
                (serverId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}

```
##### Spring Bean 등록
```java
@Configuration
public class AppConfig {
    @Bean
    public SnowflakeGenerator snowflakeGenerator() {
        return new SnowflakeGenerator();
    }
}
```

##### 결제 코드


```java
@GetMapping("/paySuccess")
    @Transactional
    public String successPayRequest(@RequestParam("pg_token") String pgToken,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails,
                                  Model model) {
        .
        . 
        .
  
        // 완료된 주문 건에 대해 주문을 저장
        Order paymentOrder = Order.createOrder(findMember);
        
        // 현재 시간 + 스노우플레이크
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderNumber = String.valueOf(snowflakeGenerator.nextId());
        paymentOrder.setOrderNumber(nowTime+orderNumber);
        
        .
        .
        .
  
}
```

