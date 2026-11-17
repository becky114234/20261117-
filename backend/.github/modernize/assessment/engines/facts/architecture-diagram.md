# Architecture Diagram

此專案是以 Spring Boot 建立的俄羅斯方塊 Web 應用程式。瀏覽器載入靜態遊戲頁面，透過 REST API 與後端互動，後端使用 JPA 將玩家、分數與遊戲紀錄保存至 MySQL。

## Application Architecture

<!-- mermaid-checked: no \n, no em-dash/en-dash, no {} in labels, subgraphs are id["label"], arrows are -->|"label"|, all subgraphs closed by end, ids unique -->
~~~mermaid
flowchart TD
    subgraph ClientLayer["Client Layer"]
        Browser["Web Browser"]
        GameUI["Static Tetris UI"]
    end
    subgraph AppLayer["Application Layer - Spring Boot 3.5"]
        WebAPI["Spring MVC REST API"]
        Auth["Authentication and Sessions"]
        GameLogic["Game Data Services"]
    end
    subgraph DataLayer["Data Layer"]
        JPA["Spring Data JPA"]
        MySQL[("MySQL 8")]
    end

    Browser -->|"loads page"| GameUI
    GameUI -->|"HTTP JSON requests"| WebAPI
    WebAPI -->|"account checks"| Auth
    WebAPI -->|"score and record operations"| GameLogic
    Auth -->|"user persistence"| JPA
    GameLogic -->|"score persistence"| JPA
    JPA -->|"SQL queries"| MySQL
~~~

### Technology Stack Summary

| Layer | Technology | Version | Purpose |
|---|---|---|---|
| Client | HTML, CSS, JavaScript | Browser standard | Tetris board, controls, account forms |
| Application | Spring Boot | 3.5.16 | Application runtime and dependency management |
| Web | Spring MVC | Spring Boot managed | REST endpoints and static resource serving |
| Persistence | Spring Data JPA and Hibernate | Boot managed | Repository abstraction and ORM mapping |
| Database | MySQL | 8.0.46 detected | Users, scores, and game records |
| Build | Maven Wrapper | Project managed | Compile, test, and run tasks |
| Runtime | Java | 25 configured | Application compilation target |

### Data Storage & External Services

The application uses MySQL as its persistent store through JPA. The `users`, `scores`, and `game_records` tables hold account data, game scores, and completed game records. No external API, cache, message broker, or email service is configured; browser requests reach the local Spring Boot server directly.

### Key Architectural Decisions

- Uses a layered REST architecture with controllers, a service, repositories, and JPA entities.
- Keeps database connection settings externalizable through `DB_URL`, `DB_USERNAME`, and related environment variables.
- Uses an in-memory session token map for the current application process; tokens are not persisted in MySQL.

## Component Relationships

<!-- mermaid-checked: no \n, no em-dash/en-dash, no {} in labels, subgraphs are id["label"], arrows are -->|"label"|, all subgraphs closed by end, ids unique -->
~~~mermaid
flowchart LR
    subgraph PresentationLayer["Presentation"]
        cAuthController["AuthController"]
        cGameController["GameRecordController"]
        cHelloController["HelloController"]
        cRankingController["RankingController"]
        cScoreController["ScoreController"]
        cUserController["UserController"]
    end
    subgraph BusinessLayer["Business Logic"]
        cAuthService["AuthService"]
    end
    subgraph DataAccessLayer["Data Access"]
        cUserRepository["UserRepository"]
        cScoreRepository["ScoreRepository"]
        cGameRepository["GameRecordRepository"]
    end
    subgraph DomainLayer["Domain Model"]
        cUser["User"]
        cScore["Score"]
        cGameRecord["GameRecord"]
    end
    subgraph InfraLayer["Infrastructure"]
        cMySQL[("MySQL Database")]
        cStatic["Static Index Page"]
    end

    cAuthController -->|"delegates account actions"| cAuthService
    cGameController -->|"uses authentication"| cAuthService
    cScoreController -->|"uses authentication"| cAuthService
    cRankingController -->|"uses authentication"| cAuthService
    cUserController -->|"handles user requests"| cUserRepository
    cAuthService -->|"queries and saves"| cUserRepository
    cGameController -->|"queries and saves"| cGameRepository
    cScoreController -->|"queries and saves"| cScoreRepository
    cRankingController -->|"reads rankings"| cScoreRepository
    cUserRepository -->|"maps"| cUser
    cScoreRepository -->|"maps"| cScore
    cGameRepository -->|"maps"| cGameRecord
    cUserRepository -->|"JPA SQL"| cMySQL
    cScoreRepository -->|"JPA SQL"| cMySQL
    cGameRepository -->|"JPA SQL"| cMySQL
    cStatic -->|"served by Spring Boot"| cHelloController
~~~

### Component Inventory

| Component | Layer | Type | Responsibility |
|---|---|---|---|
| AuthController | Presentation | REST Controller | Registration, login, password reset, session lookup, logout |
| GameRecordController | Presentation | REST Controller | Saves and lists the current user's game records |
| HelloController | Presentation | REST Controller | Provides the hello endpoint used by the backend test |
| RankingController | Presentation | REST Controller | Reads score ranking data |
| ScoreController | Presentation | REST Controller | Saves and lists player scores |
| UserController | Presentation | REST Controller | Handles user-related requests |
| AuthService | Business Logic | Spring Service | Validates credentials, hashes passwords, and manages in-memory sessions |
| UserRepository | Data Access | JPA Repository | Persists and queries users |
| ScoreRepository | Data Access | JPA Repository | Persists and queries scores |
| GameRecordRepository | Data Access | JPA Repository | Persists and queries game records |
| User | Domain Model | JPA Entity | Maps the users table |
| Score | Domain Model | JPA Entity | Maps the scores table |
| GameRecord | Domain Model | JPA Entity | Maps the game_records table |
| Static Index Page | Infrastructure | Static Resource | Hosts the browser Tetris UI and client-side game logic |
| MySQL Database | Infrastructure | Relational Database | Stores persistent account and gameplay data |
