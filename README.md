# ✨Rarely - 특수동물 정보 공유

Rare + Lovely의 합성어로, 흔치 않은 반려동물을 키우는 사람들이 모여 정보를 공유할 수 있는 공간입니다.


## 🔻프로젝트 소개

카카오테크 부트캠프 3기 - 클라우드 네이티브 과정

개인 프로젝트 (25.09.15-25.12.09, 약 3개월)

GitHub Repo: [Frontend](https://github.com/100-hours-a-week/3-ari-kim-community-FE) [Backend](https://github.com/100-hours-a-week/3-ari-kim-community-BE)

Fronend :
<img src="https://img.shields.io/badge/css-663399?style=for-the-badge&logo=css&logoColor=white">
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"> 

Backend : 
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">

Infra :
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">

## 🔻목표
단순한 기능 구현을 넘어, **대규모 사용자가 몰리는 상황을 가정**하고 시스템의 **안정성**과 **확장성**을 최우선으로 설계했습니다.

- 트래픽 시나리오 가정
  
  Target: MAU (월간 활성 사용자) 100만 명

- 주요 성능 개선 전략

1. DB 부하 분산 (Replication)

  쓰기 작업보다 읽기 작업이 90% 이상 많은 커뮤니티 특성을 고려하여 MySQL Master-Slave 구조를 적용했습니다.

  @Transactional(readOnly=true) 분기를 통해 읽기 트래픽을 Slave DB로 분산시켜 마스터 DB의 병목을 방지했습니다.

2. 수평적 확장 (Scale-out)

  트래픽 급증에 유연하게 대처하기 위해 AWS **Auto Scaling Group(ASG)**과 **Application Load Balancer(ALB)**를 구성했습니다.

  CPU 사용량 70% 이상 시 자동으로 인스턴스가 확장되도록 설정하여 가용성을 확보했습니다.

## 🔻ERD
<img width="1129" height="739" alt="스크린샷 2025-12-09 오후 5 52 34" src="https://github.com/user-attachments/assets/d5a48c22-35c2-4e6f-bc0d-ffb56b1ebc35" />

## 🔻아키텍처 구조
