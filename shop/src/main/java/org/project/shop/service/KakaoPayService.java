package org.project.shop.service;

import org.project.shop.kakaopay.KakaoApproveResponse;
import org.project.shop.kakaopay.KakaoReadyResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class KakaoPayService {
    static final String cid = "TC0ONETIME";         // 가맹점 테스트 코드
    static final String admin_key = "";

    private KakaoReadyResponse kakaoReady;

    public KakaoReadyResponse kakaoPayReady(String itemName, String count, String total_price) {

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("item_name", itemName);
        parameters.add("quantity", count);
        parameters.add("total_amount", total_price);
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", "http://localhost:8080/kakaopay/paySuccess"); // 성공 시 redirect url
        parameters.add("cancel_url", "http://localhost:8080/kakaopay/payCancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://localhost:8080/kakaopay/payFail"); // 실패 시 redirect url
        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);
        return kakaoReady;
    }

    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK "+admin_key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }

    public KakaoApproveResponse ApproveResponse(String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        return approveResponse;
    }
}
