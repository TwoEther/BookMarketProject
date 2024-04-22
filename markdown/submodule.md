# 개발 환경 분리와 보안을 위한 방법들
AWS RDS Password, Email Server address 등 중요 정보가 GItHub Repository에 commit 되면 안되는 상황이다, 이를 해결하기 위해 GitHub에 직접 환경변수를 등록해도 되지만 유지보수를 위해 Git Submodule을 사용하기로 했다.



### Git Submodule
Git Submodule은 저장소 아래 다른 저장소를 두어 마치 부모와 자식과 같은 관계를 가진다,
이 프로젝트에서는 application.config 라는 서브 저장소를 두었다.

gradle 환경에서 개발 환경 분리를 위해 다음과 같이 3개의 properties 파일을 작성하였다.
* application-dev.properties (로컬)
* application-prod.properties (배포)
* application-test.properties (테스트)

properties 파일에는 개발 환경을 명시하기 위한 속성을 지정해주어야 한다,
application-prod.properties 파일의 경우
```
spring.profiles.active = prod
```
다른 파일 또한 마찬가지로 active 속성을 명시해주면 된다.

Spring 에서는 defalut로 application.properites 파일을 참조한다, defalut로 사용하는 properties를 변경하기 위해서는 여러가지 방법이 존재한다, 그 중에서도 2가지 방법을 소개하고자 한다.

### Intellj Run/Debug 설정 변경
다음과 같이 Active Profiles에 적용하고자 하는 환경을 넣으면 
그 application.properties 속성을 읽어오게됨
![submodule1](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/423d1a98-76a9-4a5f-9cea-7b99bada85f5)

### System.setProperty 설정 
spring.profile.default 속성을 변경하는 코드를 Application.Java에 추가하는 방법이다.
```java
@SpringBootApplication
public class ShopApplication {
	public static void main(String[] args) {
        // default properties를 dev로 설정
		System.setProperty("spring.profiles.default", "dev");
		Properties p = System.getProperties();
		Enumeration keys = p.keys();
		
        // properties에 대한 정보 출력
        while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) p.get(key);
			System.out.println(key + ": " + value);
		}

		SpringApplication.run(ShopApplication.class, args);
	}

}
```
