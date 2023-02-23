# 내배캠 4기 스프링 A-2 < Let`s Party > 팀프로젝트

내일배움캠프 4기 스프링 A반 2조의 1회성 모임 매칭 서비스 < Let`s Party >입니다.  
현재 개발 작업중이며, 아래 내용은 변경될 수 있습니다.


## 1. 작업환경
- 백엔드 환경  
[![My Skills](https://skillicons.dev/icons?i=java,spring,mysql,redis)](https://skillicons.dev)
- DB 환경  
[![My Skills](https://skillicons.dev/icons?i=mysql,redis)](https://skillicons.dev)
- 프론트엔드  
[![My Skills](https://skillicons.dev/icons?i=css,javascript)](https://skillicons.dev)
- 협업 도구  
[![My Skills](https://skillicons.dev/icons?i=git,github)](https://skillicons.dev)


- JDK 11
- SpringBoot 2.7.8
- 현재 로컬 MYSQL, REDIS 저장소를 사용해 개발중입니다. 
- Spring Thymleaf 를 사용해 뷰 구현
- AWS EC2를 통해 배포 예정이며, 추후 GithubAction을 활용한 CI/CD 방식 도입 예정


##  2. 팀원
1. 리더 - 조운 [깃허브](https://github.com/jwoon1013)   
2. 부리더 - 최찬호 [깃허브](https://github.com/chanoChoi) 
3. 팀원   
김민재 [깃허브](https://github.com/hobakk)    
예진선 [깃허브](https://github.com/JinseonYe)    
이상환 [깃허브](https://github.com/sang-hwann)    


## 3.  API 명세
https://www.notion.so/47a47b7b603c42f0b99f898e98fe49b3?v=0d0802a2acd34da68bbabbab16b43350

## 4. ERD 구조도
아래 링크 참고

## 5. 구현 목표
### 1. 회원가입 & 로그인
- [v]  Spring Security 를 통한 인증/인가 구현 
- [v]  JWT 사용(Access Token & RefreshToken 구현 
: RefreshToken은 Redis로 관리)
### 2. 유저 관련
- [v] 내 프로필 정보 조회 & 수정 기능
- [v] 다른 유저 프로필 정보 조회
- 회원 탈퇴 기능
- [v]내가 작성한 모집글 조회 기능
- [v]내가 참석한 모집글 조회 기능

### 3. 모집글 관련 기능
- [v]모집글 작성 & 수정 기능
- [v]작성시 입력한 [모임시간] 기준 [15분전]이 [모집마감시간]
- [v]모집글 검색 기능 (주소 & 장소명 & 제목)
- [v]카테고리별 모집글 조회 기능
- [v]핫한 모집글 조회 기능 (모집글 조회수 반영)
- 마음에 드는 모집글에 좋아요&취소 기능 (스크랩)
- 내가 좋아요한 모집글 조회 기능


### 4. 모임참가 관련 기능
- 유저는 한번에 한 모집글에만 참석 가능
- [v]참가신청이 들어오면 파티장은 수락/거부 가능
- [v]참가인원이 꽉 찬경우 모집글이 [모집마감] 상태로 변경
- [v]참가인원이 차지 않은경우 모집글은 자동으로 [종료] 처리 됨

### 5. 노쇼 시스템 기능
- 참가자는 [모임시간] 에서 1시간동안 노쇼 신고가 가능
- 노쇼 신고 시간이 종료되면, 참가자의 과반수(반올림)에게 신고받은 유저는 노쇼포인트가 +1 됨

### 6. 유저 차단 기능
- [v]유저-유저 개인간의 차단 기능
- [v]나의 차단리스트 관리 기능
- 차단한 유저의 모집글이 보이지 않는 기능

### 7. 1:1 채팅 기능
- 모집참가신청시, 파티장과 채팅 기능

### 8. 신고 기능
- [v] 문제성 모집글 or 유저 신고 기능
- 신고 10회 누적시 모집글 삭제 처리
- 신고 10회 누적시 유저 계정정지 처리

### 9. 관리자 기능
- 전체 유저 조회 기능
- 전체 게시글 조회 기능
- 특정 유저 계정 정지 기능
- 특정 게시글 삭제 기능
- 모집글 신고 로그 조회 기능
- 특정유저 신고로그 조회 기능

