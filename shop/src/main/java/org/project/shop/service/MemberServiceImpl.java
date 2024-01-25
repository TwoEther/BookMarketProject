package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.exception.BusinessLogicException;
import org.project.shop.exception.ExceptionCode;
import org.project.shop.repository.MemberRepositoryImpl;
import org.project.shop.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final RedisService redisService;
    private final MemberRepositoryImpl memberRepositoryImpl;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;


    // 로거
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 존재 시간
    @Value("${spring.mail.auth-code-expiration-millis}")
    private Long authCodeExpirationMillis;


    @Override
    @Transactional
    public Long save(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepositoryImpl.save(member);
        return member.getId();
    }

    @Override
    public List<Member> findAllMember(){
        return memberRepositoryImpl.findAllMember();
    }

    @Override
    public List<Member> findAllGeneralMember() {
        return memberRepositoryImpl.findAllGeneralMember();
    }

    @Override
    public Member findOneMember(Long memberId){
        return memberRepositoryImpl.findMember(memberId);
    }

    @Override
    public Member findByUserId(String userId) {
        return memberRepositoryImpl.findByUserId(userId);
    }

    @Override
    public void deleteMemberByMemberId(Long memberId) {
        memberRepositoryImpl.deleteMemberByMemberId(memberId);
    }

    @Override
    public String findMemberIdByEmailAndPhoneNum(String email, String phoneNum) {
        return memberRepositoryImpl.findMemberIdByEmailAndPhoneNum(email, phoneNum);
    }


    @Override
    public Member findById(Long id) {
        return memberRepositoryImpl.findById(id);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepositoryImpl.findByEmail(email);
    }


    public void sendToEmail(String toEmail) throws Exception {
        this.checkDuplicateEmail(toEmail);
        String title = "이메일 인증 번호";
        String authCode = this.createRandomCode();
        mailService.sendSimpleMessage(toEmail);

        // 인증 요청시 Redis에 저장
        redisService.setRedisTemplate(AUTH_CODE_PREFIX+toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private String createRandomCode() {
        // 코드 사이즈
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            log.debug("No_such_algorithm 오류 발생");
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

//    public EmailVerificationResult verifiedCode(String email, String authCode) {
//        this.checkDuplicateEmail(email);
//        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
//        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
//
//        return EmailVerificationResult.of(authResult);
//    }

    public void checkDuplicateEmail(String email) {
        Optional<Member> findMember = memberRepositoryImpl.findByEmail(email);
        if (findMember.isPresent()) {
            log.debug("exception email : {}", email);
            throw new BusinessLogicException(ExceptionCode.MEMBER_IS_EXISTS);
        }
    }


    @Override
    public String checkDuplicateMember(String user_id) {
        if (user_id.isEmpty()) {
            return MemberExceptionCode.EMPTY.name();
        } else if (!checkReqexId(user_id)) {
            return MemberExceptionCode.Reqex.name();
        } else if (findByUserId(user_id) != null){
            return MemberExceptionCode.Dup.name();
        } else {
            return MemberExceptionCode.OK.name();
        }
    }

    @Override
    public boolean checkPassword(String id, String pw) {
        return passwordEncoder.matches(pw, memberRepositoryImpl.findByUserId(id).getPassword());
    }

    @Override
    public boolean checkReqexId(String id) {
        String pattern = "^[a-z0-9]{6,20}";
        return Pattern.matches(pattern, id);
    }

    @Override
    public boolean checkReqexPw(String pw) {
        String pattern = "\"^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{8,16}$\"";
        return Pattern.matches(pattern, pw);
    }
}
