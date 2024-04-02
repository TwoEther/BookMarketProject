# 카카오 페이 결제 API 적용기

카카오페이 공식문서 에는 다음과 같이 정의되어 있다.

1. Secret key를 헤더에 담아 파라미터 값들과 함께 POST로 요청합니다.
2. 요청이 성공하면 응답 바디에 JSON 객체로 다음 단계 진행을 위한 값들을 받습니다.
3. 서버(Server)는 tid를 저장하고, 클라이언트는 사용자 환경에 맞는 URL로 리다이렉트(redirect)합니다.



### Request Syntax
```java
POST /online/v1/payment/ready HTTP/1.1
Host: open-api.kakaopay.com
Authorization: SECRET_KEY ${SECRET_KEY}
Content-Type: application/json
```

### Request Body Payload
| Name                  | Data Type                | Required | Description                                                                                                                                                                                                                                                                              |   |
|-----------------------|--------------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---|
| cid                   | String                   | O        | 가맹점 코드, 10자                                                                                                                                                                                                                                                                        |   |
| cid_secret            | String                   | X        | 가맹점 코드 인증키, 24자, 숫자와 영문 소문자 조합                                                                                                                                                                                                                                        |   |
| partner_order_id      | String                   | O        | 가맹점 주문번호, 최대 100자                                                                                                                                                                                                                                                              |   |
| partner_user_id       | String                   | O        | 가맹점 회원 id, 최대 100자                                                                                                                                                                                                                                                               |   |
| item_name             | String                   | O        | 상품명, 최대 100자                                                                                                                                                                                                                                                                       |   |
| item_code             | String                   | X        | 상품코드, 최대 100자                                                                                                                                                                                                                                                                     |   |
| quantity              | Integer                  | O        | 상품 수량                                                                                                                                                                                                                                                                                |   |
| total_amount          | Integer                  | O        | 상품 총액                                                                                                                                                                                                                                                                                |   |
| tax_free_amount       | Integer                  | O        | 상품 비과세 금액                                                                                                                                                                                                                                                                         |   |
| vat_amount            | Integer                  | X        | 상품 부가세 금액 값을 보내지 않을 경우 다음과 같이 VAT 자동 계산 (상품총액 - 상품 비과세 금액)/11 : 소숫점 이하 반올림                                                                                                                                                                   |   |
| green_deposit         | Integer                  | X        | 컵 보증금                                                                                                                                                                                                                                                                                |   |
| approval_url          | String                   | O        | 결제 성공 시 redirect url, 최대 255자                                                                                                                                                                                                                                                    |   |
| cancel_url            | String                   | O        | 결제 취소 시 redirect url, 최대 255자                                                                                                                                                                                                                                                    |   |
| fail_url              | String                   | O        | 결제 실패 시 redirect url, 최대 255자                                                                                                                                                                                                                                                    |   |
| available_cards       | JSON Array               | X        | 결제 수단으로써 사용 허가할 카드사를 지정해야 하는 경우 사용 카카오페이와 사전 협의 필요 사용 허가할 카드사 코드*의 배열 ex) ["HANA", "BC"] (기본값: 모든 카드사 허용)                                                                                                                   |   |
| payment_method_type   | String                   | X        | 사용 허가할 결제 수단, 지정하지 않으면 모든 결제 수단 허용 CARD 또는 MONEY 중 하나                                                                                                                                                                                                       |   |
| install_month         | Integer                  | X        | 카드 할부개월, 0~12                                                                                                                                                                                                                                                                      |   |
| use_share_installment | String                   | X        | 분담무이자 설정 (Y/N), 사용시 사전 협의 필요                                                                                                                                                                                                                                             |   |
| custom_json           | JSON Map {String:String} | X        | 사전에 정의된 기능 1. 결제 화면에 보여줄 사용자 정의 문구, 카카오페이와 사전 협의 필요 2. iOS에서 사용자 인증 완료 후 가맹점 앱으로 자동 전환 기능(iOS만 처리가능, 안드로이드 동작불가) ex) return_custom_url 과 함께 key 정보에 앱스킴을 넣어서 전송 "return_custom_url":"kakaotalk://" |   |



Body에 들어갈 값을 넣어서 Payload 하면 될것 같다.
