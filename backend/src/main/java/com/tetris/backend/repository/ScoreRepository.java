package com.tetris.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tetris.backend.entity.Score;
import com.tetris.backend.entity.User;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    // 查詢某位使用者分數最高的前 20 筆。
    List<Score> findTop20ByUserOrderByPointsDesc(User user);
}
