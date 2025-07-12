# Spring Batch 대용량 엑셀 → DB 연습 프로젝트

## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [주요 기능 및 구현 내용](#주요-기능-및-구현-내용)
3. [ERD 및 데이터 구조](#erd-및-데이터-구조)
4. [처리 흐름 및 시나리오](#처리-흐름-및-시나리오)
5. [기술 스택](#기술-스택)
6. [심화/트러블슈팅 경험](#심화트러블슈팅-경험)

---

## 1. 프로젝트 개요

- **대용량 인사/급여 데이터**가 담긴 엑셀 파일(최대 10만 건)을 Spring Batch로 읽어 DB에 저장하는 실습 프로젝트
- 단일 서버 기준, 대량 데이터의 읽기/검증/저장/에러 리포트/성능 튜닝까지 실전 경험 목표

---

## 2. 주요 기능 및 구현 내용

- **엑셀 → 객체 변환**: Apache POI로 대용량 엑셀 읽어서 Employee 리스트 변환
- **Spring Batch 처리**: Reader → Processor → Writer 패턴
   - *Reader*: 엑셀 파싱 후 Iterator 기반 커스텀 리더 구현
   - *Processor*: 데이터 유효성 검사(필수값, Enum 상태값, 음수 급여 등)
   - *Writer*: JdbcTemplate의 batchUpdate로 대량 Insert(직접 구현)
- **실패 데이터 분리 저장**: 유효성 검증 실패/중복 등은 InvalidEmployee 테이블 + 리포트 엑셀 따로 생성
- **Chunk 기반 처리**: 대량 데이터를 chunk(100~1000) 단위로 나눠서 커밋/롤백 제어
- **대량 데이터 성능 개선**
   - JPA → 직접 JDBC 변경, batch_size 옵션/SQL 튜닝 경험
   - 멀티스레드 TaskExecutor 적용/해제 테스트
- **모든 과정 로깅**: 건별/Chunk별 읽기, 처리, 저장 로그 남기기

---

## 3. ERD 및 데이터 구조

**Employee (정상 인사 데이터)**
- id (PK, auto increment)
- employeeNumber (사번, unique)
- name, department, salary, status, hireDate, retireDate, email, phone 등

**InvalidEmployee (비정상 데이터)**
- id (PK)
- employeeNumber, name, department, salary, status, errorReason, createdAt 등

<!-- ERD 다이어그램 있으면 첨부. 없으면 위 구조로 대체 -->

---

## 4. 처리 흐름 및 시나리오

1. **HR팀**이 대용량 엑셀 업로드 (입사, 퇴사, 급여 등 혼재)
2. **Spring Batch Job 실행**
   - Step1: 엑셀 Reader → Employee 객체 파싱
   - Step2: Processor에서 중복/음수/필수값 오류 검증
   - Step3: Writer에서 정상 데이터만 DB insert (JdbcTemplate batchUpdate)
   - Step4: 에러/비정상 데이터는 InvalidEmployee 테이블+엑셀 파일로 별도 저장
3. **최종 결과**
   - Employee(정상) / InvalidEmployee(실패) 테이블 분리 저장
   - 에러 리포트(엑셀) 자동 생성

---

## 5. 기술 스택

- Java 17
- Spring Boot 3.x, Spring Batch
- Spring Data JPA, Spring JDBC (JdbcTemplate)
- Apache POI (엑셀 파싱/생성)
- MariaDB (로컬/테스트 DB)
- Lombok

---

## 6. 심화/트러블슈팅 경험

- **JPA saveAll 한계**: 10만 건 이상에서 속도/메모리 이슈 → JDBC 직접 구현으로 전환
- **배치 Insert/트랜잭션 튜닝**: hibernate.batch_size, jdbc 옵션 변경, 쿼리/커밋 단위 조정
- **에러 상황 실습**
   - 잘못된 PK/Unique/Not Null/Type 미스 일괄 검증
   - Chunk별 rollback/skip/retry 옵션 직접 테스트
- **실제 로깅/성능 측정**: Insert 건수/속도/오류 상황까지 모두 로그/엑셀로 리포팅

---

## 기타

- (Optional) S3/FTP 등 다양한 파일 소스 확장, 멀티스레드/파티셔닝/운영모니터링 고도화 예정
- 실무 배치 설계/운영에 가까운 구조 연습 및 노하우 내재화

