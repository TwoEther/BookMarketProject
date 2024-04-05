# Redis를 이용한 이메일 인증

## Redis 란?
* Key-value 기반의 In-Memory 데이터 구조 저장소.
  * In-Memory?
    * 주기억장치 RAM에 데이터를 저장하는 방법 
    * 하드디스크를 거치지 않아 저장/조회 시 속도가 빠름 
    * RAM은 휘발성 메모리 이므로 용량이 초과되면 데이터가 유실 될 수도 있다.
* 오픈 소스 기반의 NoSQL

#### 기존 DB가 있는데도 Redis를 사용하는 이유
* DB는 데이터를 저장/조회 할 때 디스크를 사용하기 때문에 Redis에 비해 속도가 떨어진다.
* 같은 요청이 들어왔을때 매번 조회 하지 않고 캐시 서버에 저장되어 있는 값을 가져와 사용해야 부하를 줄일수 있다.

#### Redis 사용시 주의 할 점
* Redis는 Single Thread를 사용하므로 처리 시간이 긴 요청에 대해서 장애가 발생 할 수도있다.
* 메모리 단편화가 일어날수도있다.

---
## 이메일 인증
#### Spring Bean 등록
```java
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}
```
#### RedisService 구성
```java
@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public String getRedisTemplateValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRedisTemplateValue(String key) {
        redisTemplate.delete(key);
    }

    public void setRedisTemplate(String key, String value, Duration duration) {
        if (getRedisTemplateValue(key) != null) {
            deleteRedisTemplateValue(key);
        }

        redisTemplate.opsForValue().set(key, value, duration);
    }
}
```
#### MailSendService
```java
@Service
@RequiredArgsConstructor
public class MailService{
    private final JavaMailSender javaMailSender;
    private Logger log;

    private String ePw; // 인증번호

    @Value("${spring.mail.username}")
    private String id;


    private final JavaMailSender emailsender; // Bean 등록해둔 MailConfig 를 emailsender 라는 이름으로 autowired

    // 메일 내용 작성
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);// 보내는 대상
        message.setSubject("BookMarket 회원가입 이메일 인증");// 제목
        
        // html 태그를 하드코딩
        String msgg = "";
        msgg += "<div style='margin:30px;'>";
        msgg += "<h1> 안녕하세요</h1>";
        msgg += "<h1> 모두의 책방 관리자 입니다</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>항상 당신의 꿈을 응원합니다. 감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype


        message.setFrom(new InternetAddress(this.id, "모두의 책방"));// 보내는 사람

        return message;
    }

    // 랜덤 인증 코드 전송
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    // 메일 발송
    // sendSimpleMessage 의 매개변수로 들어온 to 는 곧 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다.
    // 그리고 bean 으로 등록해둔 javaMail 객체를 사용해서 이메일 send!!
    public String sendSimpleMessage(String to) throws Exception {

        ePw = createKey(); // 랜덤 인증번호 생성

        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to); // 메일 발송
        try {// 예외처리
            emailsender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }


        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}
```
#### MainController
```java
@PostMapping("/new/email-check")
    @ResponseBody
    public void sendCodeToEmail(@RequestParam String email) throws Exception {
        if (!email.isEmpty()) {
            String code = mailService.sendSimpleMessage(email);
            redisService.setRedisTemplate(email, code, Duration.ofMillis(300 * 1000));
            String value = redisService.getRedisTemplateValue(email);
        }
    }
```

#### 회원가입시 검증
```java
@PostMapping(value = "/new")
    @Transactional
    public String signUp(HttpServletResponse response, @Valid MemberForm form, BindingResult result) throws Exception {
    if (result.hasErrors()) {
        ScriptUtils.alert(response, "값이 올바르지 않습니다, 다시 입력해주세요");
        return "member/createMemberForm";
    }

    String userId = form.getUserId();
    String password = form.getPassword1();
    String nickName = form.getNickName();
    String phoneNum = form.getPhoneNum();
    String email = form.getEmail();
    String email_check_num = form.getEmail_Check_number();

    if (memberServiceImpl.findByUserId(userId) != null) {
        ScriptUtils.alert(response, "아이디가 존재합니다");
        return "member/createMemberForm";
    } else if (email_check_num.isEmpty()) {
        ScriptUtils.alert(response, "이메일 인증후 회원가입 가능합니다");
        return "member/createMemberForm";
    } // 이메일 인증 검증 부분
    else if (!redisService.getRedisTemplateValue(email).equals(email_check_num)) {
        ScriptUtils.alert(response, "인증번호가 일치 하지 않습니다");
        return "member/createMemberForm";
    }
    /*
     * */
}
```
