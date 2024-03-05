<h1>책 구매 사이트 구축 프로젝트</h1><hr>
<p>쇼핑몰 개인 프로젝트 입니다.</p>
<p>주소 : <a href="http://booktravel24.com/">http://booktravel24.com/</a></p>

<h5>✔ 사용한 기술스택</h5><hr>
<ul>
    <li>Java, Spring Boot, Spring Security</li>
    <li>JPA, QueryDSL</li>
    <li>MySQL</li>
</ul>

<h5>✔ 배포 환경</h5><hr>
<ul>
    <li>Git Submodule을 이용한 application.properties 관리</li>
    <li>GitHub Actions를 사용, AWS EC2와의 CI/CD 파이프 라인 구축 </li>
    <li>Route 53을 사용한 도메인 구축</li>
</ul>

<h5>✔ 구현기능</h5><hr>
<ul>
    <li><strong>계정</strong>
        <ul>
            <li><strong>Spring Security</strong>를 활용한 로그인, 로그아웃 및 회원가입 </li>
            <li><strong>Redis</strong>를 활용한 회원가입시 이메일 인증</li>
            <li>이메일과 핸드폰번호를 활용한 ID 찾기</li>
            <li>마이페이지를 통한 유저 편의성 기능 제공(주문, 문의 등)</li>
            <li>관리자 페이지를 이용한 상품, 주문, 문의 및 회원관리</li>
        </ul>
    </li>
    <li><strong>상품</strong>
        <ul>
            <li>상품 검색 및 카테고리 별 조회</li>
            <li>판매량순, 최신순 정렬 및 페이징</li>
            <li>상품 리뷰 및 문의</li>
            <li>찜하기 및 장바구니 추가</li>
        </ul>
    </li>
    <li><strong>결제</strong>
        <ul>
            <li>카카오페이 API를 활용한 결제 제공</li>
        </ul>
    </li>
</ul>

<h5>✔ 이미지</h5><hr>
<p>AWS S3 상에서 이미지를 저장하는 비용이 많이 들어 임시 이미지로 대체 하였습니다</p>

<ul>
    <li><strong>메인페이지</strong>
        <ul>
            <li>검색 및 카테고리별 조회 가능</li>
            <li>판매량 및 최신순 정렬 가능</li>
            <li>하단 페이징 버튼</li>
            <img src="![메인페이지1](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/6d50f37d-9fc6-4f30-9527-e0be3b490670)" alt="">
        </ul>
    </li>
    <li><strong>회원가입</strong>
        <ul>
            <li>아이디 중복검사 및 유효성 검사</li>
            <li>이메일 인증 기능</li>
            <img src="![회원가입](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/58518c52-fdfd-426b-b25a-87d4232fce0e)" alt="">
        </ul>
    </li>
    <li><strong>주문</strong>
        <ul>
            <li>카카오 주소 검색 API를 이용한 배송지 설정</li>
            <li>주문 정보 표시</li>
            <img src="![주문페이지](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/27ddf39d-07f0-4094-aad0-8ab87bf6ea00)" alt="">
            <img src="![결제1](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/4fc06d31-d4b1-43b3-a16e-8b14defd628d)" alt="">
        </ul>
    </li>
    <li><strong>마이페이지</strong>
        <ul>
            <li>회원 탈퇴 및 주문 상세보기 기능</li>
            <img src="![마이페이지](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/081cff0f-a94b-4b43-8139-cae3a0d233df)" alt="">
            <img src="![주문상세](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/0adc30d6-028c-4854-9d72-31d1dfce0f64)" alt="">
        </ul>
    </li>
    <li><strong>리뷰 및 문의</strong>
        <ul>
            <li>로그인 한 회원에 대해서만 작성 가능</li>
            <img src="![문의](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/60def024-5f28-4670-9565-5637768f1ae3)" alt="">
            <img src="![리뷰](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/c6bea20a-6497-462e-9009-65d48b53952b)" alt="">
        </ul>
    </li>
    <li><strong>관리자페이지</strong>
        <ul>
            <li>주문, 상품, 리뷰, 문의에 대해 관리</li>
            <img src="![관리자상품](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/72bd53af-14ef-432b-b799-b481b267b526)" alt="">
            <img src="![관리자주문](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/89196502-d2dd-4dd8-8b6e-4fdd9b71e6c9)" alt="">
            <img src="![관리자리뷰](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/d2aa19b9-9a7e-4a97-9a83-a71930233f1d)" alt="">
            <img src="![관리자문의](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/468e14cd-b757-4ab2-b213-efbef3502184)" alt="">
        </ul>
    </li>
</ul>
