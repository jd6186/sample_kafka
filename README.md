# 제작 취지
Docker를 통한 Zookeeper 앙상블 구현 및 Spring Kafka를 활용한 Kafka 구현 예시 제작.

아직 Kafka를 경험해보지 못한 분들을 위해 간단한 예시를 제작하였습니다.

## 내용
메시지를 어떻게 쪼개서 병렬적으로 작업을 처리하는지에 집중해서 봐주시면 감사하겠습니다.

- 주제는 추천 광고 서비스입니다.
- Consumer는 예외처리를 위해 수동 커밋으로 진행합니다.
- Producer와 Consumer가 공유할 topic은 크게 2종류입니다.
   - 1) **"ad-manager"**: 이벤트 기반 동작 설명을 위해 제작한 topic입니다. 특히, 광고를 클릭한 유저에게 추천 광고를 생성하는 방법을 집중적으로 보시면 좋겠습니다.
   - 2) **"ad-scheduled-data-transfer"**: Batch 성으로 동작하는 topic입니다. 어제 하루동안 저장된 광고 데이터를 Redis에서 조회하여, MySQL에 마이그레이션합니다.
- Consumer 예외처리
   - 로깅은 반드시 진행합니다.
   - 예외 발생 시, 해당 메시지를 다시 한번만 더 처리할 수 있도록 구현합니다.
   - 2차 시도 시에도 실패할 경우, 2차 시도 로그를 남기고 해당 메시지는 강제로 소비처리합니다.

## 참고
관련 문서는 제가 작성한 다음 두 블로그 글을 먼저 읽고 코드를 보시면 더 이해하기 좋으실 것 같습니다. 참고해주세요 ^^

1. [Kafka 개념](https://jd6186.github.io/KafkaBasic/)
2. [Kafka 클러스터 구축 및 활용 방법](https://jd6186.github.io/MakeKafkaCluster/)
<br/><br/><br/><br/>


# 개발 환경
- Spring Kafka
- gradle
- redis
- mysql
- docker
<br/><br/><br/><br/>


# 실행 방법
1. docker-compose.yml 파일을 이용하여 테스트용 redis, mysql 데이터베이스를 실행합니다.

    ```shell
    docker-compose -f docker-compose-database.yml up -d
    ```

2. src/main/resources/sql 폴더에 있는 sql 파일을 이용하여 테이블 및 테스트용 데이터를 생성합니다.

3. docker-compose.yml 파일을 이용하여 Zookeeper 앙상블을 구현합니다.

    ```shell
    docker-compose up -d
    ```

4. IntelliJ IDEA를 통해 Spring Kafka 프로젝트를 실행합니다.

5. 사용하시는 API 테스트 툴을 통해 API를 호출합니다.