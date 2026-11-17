package com.tetris.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.tetris.backend.dto.GameDtos.ResultRequest;
import com.tetris.backend.dto.GameDtos.ResultResponse;
import com.tetris.backend.entity.GameRecord;
import com.tetris.backend.entity.User;
import com.tetris.backend.repository.GameRecordRepository;
import com.tetris.backend.service.AuthService;

@RestController
// 遊戲結束時保存完整對局紀錄，並提供玩家最近的對局資料。
public class GameRecordController {
    private final GameRecordRepository records;
    private final AuthService auth;
    public GameRecordController(GameRecordRepository records, AuthService auth) { this.records=records; this.auth=auth; }

	// 驗證登入狀態後保存一筆遊戲結果。
    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultResponse save(@RequestHeader("Authorization") String token, @RequestBody ResultRequest request) {
        User user=auth.requireUser(token); if (user==null) throw unauthorized();
        GameRecord record=records.save(new GameRecord(user, request.points(), request.lines(), request.level(), request.result()));
        return new ResultResponse(record.getId(), record.getPoints(), record.getLines(), record.getLevel(), record.getResult(), record.getPlayedAt().toString());
    }

	// 只查詢目前使用者最近 20 筆對局。
    @GetMapping("/api/games/me")
    public List<ResultResponse> mine(@RequestHeader("Authorization") String token) {
        User user=auth.requireUser(token); if (user==null) throw unauthorized();
        return records.findTop20ByUserOrderByPlayedAtDesc(user).stream().map(record -> new ResultResponse(record.getId(), record.getPoints(), record.getLines(), record.getLevel(), record.getResult(), record.getPlayedAt().toString())).toList();
    }

    private org.springframework.web.server.ResponseStatusException unauthorized() { return new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required"); }
}
