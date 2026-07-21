# board03 구조 설명

## 전체 흐름

```
브라우저 요청
  → Controller   (요청 받기)
  → Service      (규칙 처리)
  → Repository   (DB 접근)
  → DB (MySQL)
```

---

## 파일별 역할

### Controller
| 파일 | 역할 |
|------|------|
| `BoardController.java` | 브라우저 요청을 받아서 Service에게 넘기고, 결과를 템플릿으로 반환 |

---

### Service (비즈니스 로직)
| 파일 | 역할 |
|------|------|
| `PostService.java` | 인터페이스 — 어떤 기능이 있는지 규칙 정의 |
| `PostServiceImpl.java` | 실제 구현 — Controller 요청을 받아서 Repository 호출 |

> **인터페이스가 있는 이유**: 나중에 구현체를 바꿔도 Controller 코드는 안 건드려도 됨

---

### Repository (DB 접근)
| 파일 | 역할 | 실제 사용 |
|------|------|----------|
| `PostRepository.java` | 인터페이스 — findAll, findById, save, update, deleteById 규칙 정의 | - |
| `MemoryPostRepository.java` | List(메모리)에 저장 — DB 없이 테스트용 (board02 방식) | 학습용 |
| `PureJdbcPostRepository.java` | 순수 JDBC 직접 사용 (DriverManager, Connection, Statement) | 학습용 |
| `JdbcTemplatePostRepository.java` | JdbcTemplate 사용 — Spring이 JDBC 반복 코드를 대신 처리 | **실제 사용** |

> `PostServiceImpl`에서 `@Qualifier("jdbcTemplatePostRepository")`로 JdbcTemplate 버전을 지정해서 사용 중

---

### DTO
| 파일 | 역할 |
|------|------|
| `PostDto.java` | 게시글 데이터를 담는 객체 (id, title, content, author, secret, createdAt) |

---

### Exception (예외 처리)
| 파일 | 역할 |
|------|------|
| `GlobalExceptionHandler.java` | 전체 앱에서 발생하는 예외를 한 곳에서 처리. `IllegalArgumentException` 발생 시 400 에러 페이지로 이동 |

---

## Repository 발전 순서 (학습 순서)

```
MemoryPostRepository       →   PureJdbcPostRepository   →   JdbcTemplatePostRepository
메모리(List)에 저장              직접 JDBC 코드 작성              Spring이 대신 처리 (간결)
(DB 없이 테스트 가능)           (Connection, Statement 직접)    (SQL만 작성하면 됨)
```

---

## 다음에 게시판 만들 때 실제로 필요한 파일

### Java 코드
```
controller/  BoardController.java
service/     PostService.java + PostServiceImpl.java
repository/  PostRepository.java + JdbcTemplatePostRepository.java
dto/         PostDto.java
exception/   GlobalExceptionHandler.java  (선택)
```

### HTML 템플릿 (templates/board/)
| 파일 | 역할 |
|------|------|
| `list.html` | 게시글 목록 화면 |
| `detail.html` | 게시글 상세 화면 |
| `write.html` | 게시글 등록 + 수정 공용 화면 (id==0 이면 등록, id!=0 이면 수정) |

### SQL 파일 (resources/)
| 파일 | 역할 |
|------|------|
| `schema.sql` | 테이블 생성 SQL — 앱 시작할 때 자동으로 실행됨 |
| `data.sql` | 샘플 데이터 INSERT — 앱 시작할 때 자동으로 실행됨 |

> `application.properties`에 아래 설정이 있어야 자동 실행됨
> ```properties
> spring.sql.init.mode=always
> ```

### schema.sql 예시
```sql
CREATE TABLE IF NOT EXISTS post2 (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    author     VARCHAR(10)  NOT NULL,
    title      VARCHAR(200) NOT NULL,
    content    TEXT         NOT NULL,
    secret     BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP
);
```

### data.sql 예시
```sql
INSERT INTO post2 (secret, title, content, created_at, author) VALUES
    (TRUE,  '첫번째 게시글', '내용1', CURRENT_TIMESTAMP, '하루'),
    (FALSE, '두번째 게시글', '내용2', CURRENT_TIMESTAMP, '나무');
```
