package com.tetris.backend.entity;

import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "game_records")
// 對應 game_records 資料表，保存每一局完整的遊戲結果。
public class GameRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private User user;
    @Column(nullable = false) private int points;
    @Column(name = "cleared_lines", nullable = false) private int lines;
    @Column(nullable = false) private int level;
    @Column(nullable = false, length = 20) private String result;
    @Column(nullable = false) private Instant playedAt;
    // JPA 需要無參數建構子來還原資料庫資料。
    protected GameRecord() { }

    // 建立對局紀錄時自動記錄遊玩時間。
    public GameRecord(User user, int points, int lines, int level, String result) { this.user=user; this.points=points; this.lines=lines; this.level=level; this.result=result; this.playedAt=Instant.now(); }
    public Long getId(){return id;} public int getPoints(){return points;} public int getLines(){return lines;} public int getLevel(){return level;} public String getResult(){return result;} public Instant getPlayedAt(){return playedAt;}
}
