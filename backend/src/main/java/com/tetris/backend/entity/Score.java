package com.tetris.backend.entity;

import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "scores")
// 對應 scores 資料表，保存可加入排行榜的分數。
public class Score {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private User user;
    @Column(nullable = false) private int points;
    @Column(name = "cleared_lines", nullable = false) private int lines;
    @Column(nullable = false) private int level;
    @Column(nullable = false) private Instant createdAt;
    // JPA 需要無參數建構子來還原資料庫資料。
    protected Score() { }

    // 建立分數時自動記錄保存時間。
    public Score(User user, int points, int lines, int level) { this.user=user; this.points=points; this.lines=lines; this.level=level; this.createdAt=Instant.now(); }
    public Long getId(){return id;} public int getPoints(){return points;} public int getLines(){return lines;} public int getLevel(){return level;} public Instant getCreatedAt(){return createdAt;}
}
