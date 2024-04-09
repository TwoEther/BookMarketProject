# QueryDSL을 이용한 페이징과 동적 정렬 방법

### QueryDSL을 사용한 이유
* Spring Data JPA 에서는 Java에서 SQL 문법을 사용하기 위해 JPQL, JPA Criteria, QueryDSL 등 다양한 방법을 제공한다
* 복잡한 쿼리의 경우 Native Query를 사용해야 성능 개선을 할 수 있지만 간단한 쿼리의 경우 JPQL에 비해 짜기 쉽고 가독성이 좋기 때문에 이번 프로젝트에서는 QueryDSL을 사용하려고 한다.
* 
### QueryDSL Bean 등록
```java
@Configuration
public class QuerydslConfig {
    @PersistenceContext
    EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
```
* @Configuration 어노테이션을 통해 Spring에 QueryDSL 환경설정을 등록
* JPAQueryFactory를 Spring Bean에 등록

### QueryDSL 사용 방법
##### 다음은 모든 상품을 조회하는 QueryDSL 코드이다
```java
@Override
public List<Item> findAllItem() {
    return queryFactory.select(item)
            .from(item)
            .fetch();
}
```
* SQL 문법과 동일하게 select, from을 통해서 해당 Entity를 조회한다
* 여러개의 데이터를 조회할 때는 fetch(), 한 개의 데이터를 조회 할 때는 fetchOne() 메소드를 사용한다.
* 만일 조건을 걸고 싶으면 where 구문을 추가 하면 된다.

##### 전체 주문 목록중에서 특정 회원의 주문 목록을 조회하는 코드
```java
@Override
public List<Order> findOrderByMemberId(Long memberId) {
    return queryFactory.selectFrom(order)
            .where(order.member.id.eq(memberId))
            .fetch();
}
```
* 물론 join을 통해서도 구현 할 수 있다, 기본적으로 QueryDSL에서는 where 절로 Query가 나갈때 연관관계가 있는 Entity들에 대해서 left Join으로 처리한다.
* ![querydsl1](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/21e9dd01-1765-4d86-8cb6-d813b996e65b)

* 한 화면에 모든 데이터를 출력한다면 엄청난 공간 낭비 일것이다, 이를 해결하기 위해 화면에 제한된 데이터를 출력하기 위해 페이징을 사용한다, QueryDSL에서는 이러한 페이징 기능을 제공한다


### QueryDSL 페이징 사용하기
* JPA 에서는 PageRequest 클래스를 통해 페이징을 제공하고 있다.
```java
public class PageRequest extends AbstractPageRequest {
    private static final long serialVersionUID = ;
    private final Sort sort;

    protected PageRequest(int page, int size, Sort sort) {
        super(page, size);
        Assert.notNull(sort, "Sort must not be null");
        this.sort = sort;
    }

    public static PageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    public static PageRequest of(int page, int size, Sort.Direction direction, String... properties) {
        return of(page, size, Sort.by(direction, properties));
    }

    public static PageRequest ofSize(int pageSize) {
        return of(0, pageSize);
    }
    ...
}
```

##### 페이징 적용 코드
```java
@Override
public Page<Item> findAllItem(PageRequest pageRequest) {
    List<Item> result = queryFactory.select(item)
            .from(item)
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize())
            .fetch();
    /*
            페이징의 경우 전체 개수를 조회 할 수 없다,
            따라서 별도의 카운트 쿼리를 실행하여 저장해야한다.
            이때 PageableExecutionUtils를 사용하여 PageImpl 객체를 반환할 수 있다.
     */
    JPAQuery<Long> countQuery = queryFactory.select(item.count())
            .from(item);

    return PageableExecutionUtils.getPage(result, pageRequest, countQuery::fetchOne);
}
```
##### PageableExecutionUtils
```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.data.support;

import java.util.List;
import java.util.function.LongSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

public abstract class PageableExecutionUtils {
    private PageableExecutionUtils() {
    }
    
    public static <T> Page<T> getPage(List<T> content, Pageable pageable, LongSupplier totalSupplier) {
        Assert.notNull(content, "Content must not be null");
        Assert.notNull(pageable, "Pageable must not be null");
        Assert.notNull(totalSupplier, "TotalSupplier must not be null");
        if (!pageable.isUnpaged() && pageable.getOffset() != 0L) {
            return content.size() != 0 && pageable.getPageSize() > content.size() ? new PageImpl(content, pageable, pageable.getOffset() + (long)content.size()) : new PageImpl(content, pageable, totalSupplier.getAsLong());
        } else {
            return !pageable.isUnpaged() && pageable.getPageSize() <= content.size() ? new PageImpl(content, pageable, totalSupplier.getAsLong()) : new PageImpl(content, pageable, (long)content.size());
        }
    }
}
```

### Spring Security 설정
```java
package org.project.shop.config;

import org.project.shop.auth.CustomAuthenticationFailureHandler;
import org.project.shop.domain.Role;
import org.project.shop.service.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/admin")).hasAuthority(Role.ROLE_ADMIN.toString())
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority(Role.ROLE_ADMIN.toString())
                        .requestMatchers(new AntPathRequestMatcher("/item/edit")).hasAuthority(Role.ROLE_ADMIN.toString())
                        .anyRequest().permitAll())
                // 로그인 처리
                .formLogin((formLogin) -> formLogin
                        // 로그인 페이지
                        .loginPage("/member/LoginForm")
                        // 로그인이 성공했을 경우 url
                        .loginProcessingUrl("/member/login")
                        .defaultSuccessUrl("/")
                        // 로그인 실패 핸들러
                        .failureHandler(customAuthenticationFailureHandler)
                        .usernameParameter("userId")
                        .passwordParameter("password"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
                .csrf(Customizer.withDefaults());
        return http.build();
    }
    
    // 해시 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PrincipalDetailService principalDetailService;


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(passwordEncoder());
    }
}
```

### 로그인 한 사용자 가져오기

#### 💎 Bean에서 직접 사용자 정보 가져오기
전역에 선언된 SecurityContextHolder 에서 가져오는 방법인다.
```java
private static String getMemberByBean() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Member member = (Member)principal;
    return member.getUserId();
}
```

#### 💎 @AuthenticationPrincipal 어노테이션 선언
전역에 선언된 SecurityContextHolder 에서 가져오는 방법이다.
```java
// 배송지 설정 함수
@GetMapping(value = "/address")
public String setDeliveryMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                Model model) throws IOException {
    // UserDetailsService의 구현체인 principalDetails 객체 정보 반환
    // 객체 정보에서 현재 로그인 한 유저 접근 가능
    String username = principalDetails.getUsername();
    Member findMember = memberServiceImpl.findByUserId(username);
    .
    .
    .
}
```

### 로그아웃 구현 함수
```java
@GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
      new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
      return "redirect:/";
  }
```
