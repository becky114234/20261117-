package com.tetris.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tetris.backend.entity.GameRecord;
import com.tetris.backend.entity.User;

public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
    // 查詢某位使用者最近完成的前 20 局遊戲。
    List<GameRecord> findTop20ByUserOrderByPlayedAtDesc(User user);
}
