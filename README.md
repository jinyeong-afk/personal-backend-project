## 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)
EC2에 배포된 서버 주소 http://54.210.130.176:8080
### 1. 회원가입
<img width="461" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/7d147709-1575-4529-a8ee-abc923fee198">

회원가입 할 이메일(이메일 양식)과 비밀번호(8자 이상)를 입력하여 회원가입

### 2. 로그인
<img width="451" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/f6bef5e6-7963-43d4-9bdb-c5c2362c14c2">

로그인 할 이메일(이메일 양식)과 비밀번호(8자 이상)를 입력하여 로그인

<img width="942" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/f2fe515b-92a9-47d6-9fa9-9fcfb392a171">

로그인 후 받은 Authorization 토큰 값을 Header에 넣어 권한을 인증해야 게시글 생성, 수정, 삭제가 가능하다.

### 3. 게시글 생성
<img width="685" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/09888271-cda7-458d-81b6-cfb9b263d45c">
<img width="448" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/744cea57-e759-4e4e-a097-ad5495fcf8d1">

1. 로그인 후 받은 토큰 값을 Bearer Token에 넣는다.
2. 작성할 게시글 제목과 내용을 Request(Json)에 담아 보낸다.

### 4. 게시글 목록 조회
<img width="436" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/c8dd2c11-1e28-4a6c-9216-4585cf630d43">

pagination Parameter에 페이지 번호(0부터 시작)를 입력하여 원하는 페이지의 게시글 목록을 불러온다.

### 5. 특정 게시글 조회
<img width="426" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/24da95d1-ec9a-4731-8a73-708348ac4c00">

조회할 게시글 아이디를 PathVariable로 입력한다.

### 6. 특정 게시글 수정
<img width="691" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/aa21d338-34f1-4739-a6fe-01ed0a010762">

<img width="464" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/ebeb493c-a99e-4b08-9b96-299d8a70d558">

1. 로그인 후 받은 토큰 값을 Bearer Token에 넣는다.
2. 수정할 게시글 아이디를 PathVariable로 입력한다.
3. 수정할 정보를 Request(Json)에 담아 보낸다(이때 제목만 보내거나 내용만 보내 원하는 정보만 수정하는 것이 가능하다).

### 7. 특정 게시글 삭제
1. 로그인 후 받은 토큰 값을 Beaer Token에 넣는다.
2. 삭제할 게시글 아이디를 PathVariable로 입력한다.

## 데이터베이스 테이블 구조
<img width="387" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/e81524f1-c489-444c-be17-db0b1a806a10">

## 구현한 API의 동작을 촬영한 데모 영상 링크
[데모 영상 링크](https://youtu.be/fIpj3xQ9Dks)

## 구현 방법 및 이유에 대한 간략한 설명
### 공통 내용
- MVC 패턴을 활용하여 각 코드 간의 의존성 분리 및 유지보수성, 가독성 향상
- Request, Response 객체를 따로 구분하여 코드의 가독성, 유지보수성을 향상시킴
- JWT, Spring Security를 사용하여 보안을 강화
- 로그인, 회원가입, 게시글 조회를 제외한 기능에서는 로그인 한 유저만 가능하도록 권한 검증하여 보안을 강화
- 에러에 따른 에러(400, 401, 403, 404, 409)로 구분하여 에러 추적 및 처리에 용이하도록 구현
- Mockito 테스트 코드를 작성하여 단위 테스트 및 의존성 분리 및 유지보수성 향상

### 1. 사용자 회원가입 엔드포인트 ([POST] /v1/users)
- 이메일(이메일 형식), 비밀번호(8자 이상) Validation을 이용한 유효성 검사
- JPA를 사용하여 해당 이메일이 DB에 이미 존재하는지 확인(중복 확인)
- PasswordEncoder는 BCryptPasswordEncoder를 사용하여 비밀번호를 암호화

### 2. 사용자 로그인 엔드포인트 ([POST] /v1/authenticate)
- 이메일(이메일 형식), 비밀번호(8자 이상) Validation을 이용한 유효성 검사
- 로그인 정보가 일치할 경우 JWT 토큰을 생성하여 클라이언트에게 Header에 담아서 반환

### 3. 새로운 게시글을 생성하는 엔드포인트 ([POST] /v1/boards)
- 로그인 할 때 받은 JWT Token을 Header로 받아 권한이 있는 사용자인지 검증
- Validation을 활용하여 필수 입력 값(제목, 내용)을 입력하였는지 검사

### 4. 게시글 목록을 조회하는 엔드포인트 ([GET] /v1/boards)
- JPA Page 및 Pageable 객체를 사용하여 원하는 페이지의 게시글 목록을 불러와 필요한 정보만 Response 객체에 담아 반환

### 5. 특정 게시글을 조회하는 엔드포인트 ([GET] /v1/boards/{id})
- PathVariable을 통해 게시글 id를 받아 보일러플레이트가 안 생기도록 함

### 6. 특정 게시글을 수정하는 엔드포인트 ([PATCH] /v1/boards/{id})
- PathVariable을 통해 게시글 id를 받아 보일러플레이트가 안 생기도록 함
- JWT 토큰을 확인하고, authentication에 유저 정보를 저장한 것을 토대로 유저의 email을 가져와 해당 게시글 작성자인지 판단 후, 수정

### 7. 특정 게시글을 삭제하는 엔드포인트 ([DELETE] /v1/boards/{id})
- PathVariable을 통해 게시글 id를 받아 보일러플레이트가 안 생기도록 함
- JWT 토큰을 확인하고, authentication에 유저 정보를 저장한 것을 토대로 유저의 email을 가져와 해당 게시글 작성자인지 판단 후, 수정

## API 명세(request/response 포함)
<img width="770" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/789ae3a6-e7b5-4536-9352-3896600439d2">

## 가산점 추가 기능
### 단위 테스트 추가
<img width="803" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/8b0af748-30b0-46f8-98d1-b380791ab538">

Mokcito 테스트 코드를 작성하여 모든 기능 및 예외 처리에 대한 단위 테스

### docker-compose
#### 개발 환경
```
docker build -t xoeh7002/jinyeong . // 도커 허브에 이미지 빌드
docker push xoeh7002/jinyeong // 도커 허브에 이미지 푸시
```
#### 배포 서버
vi 편집기를 통해 docker-compose.yml 내용 복사
```
docker-compose pull // 도커 허브 이미지 가져오기
docker-compose up // 가져온 이미지 컨테이너로 생성하여 실행
```

### 클라우드 환경(AWS, GCP)에 배포 환경을 설계하고 애플리케이션을 배포
http://54.210.130.176
Port : 8080
<img width="326" alt="image" src="https://github.com/jinyeong-afk/wanted-pre-onboarding-backend/assets/77527453/2fd8187c-10e6-41f5-b0dd-9d5b104a65b1">
