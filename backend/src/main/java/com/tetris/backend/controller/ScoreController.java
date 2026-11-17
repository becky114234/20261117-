package com.tetris.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.tetris.backend.dto.GameDtos.ResultRequest;
import com.tetris.backend.dto.GameDtos.ResultResponse;
import com.tetris.backend.entity.Score;
import com.tetris.backend.entity.User;
import com.tetris.backend.repository.ScoreRepository;
import com.tetris.backend.service.AuthService;

@RestController
// 處理分數保存，以及目前使用者的個人分數查詢。
public class ScoreController {
	private final ScoreRepository scores;
	private final AuthService auth;
	public ScoreController(ScoreRepository scores, AuthService auth) { this.scores=scores; this.auth=auth; }

	// 驗證登入後保存一筆最高分資料。
	@PostMapping("/api/scores")
	@ResponseStatus(HttpStatus.CREATED)
	public ResultResponse save(@RequestHeader("Authorization") String token, @RequestBody ResultRequest request) {
		User user = auth.requireUser(token); if (user == null) throw unauthorized();
		return toResponse(scores.save(new Score(user, request.points(), request.lines(), request.level())), null);
	}

	// 取得目前使用者分數最高的 20 筆紀錄。
	@GetMapping("/api/scores/me")
	public List<ResultResponse> mine(@RequestHeader("Authorization") String token) {
		User user = auth.requireUser(token); if (user == null) throw unauthorized();
		return scores.findTop20ByUserOrderByPointsDesc(user).stream().map(score -> toResponse(score, null)).collect(Collectors.toList());
	}

	// 將資料庫 Entity 轉成可以回傳給前端的 DTO。
	private ResultResponse toResponse(Score score, String result) { return new ResultResponse(score.getId(), score.getPoints(), score.getLines(), score.getLevel(), result, score.getCreatedAt().toString()); }
	private org.springframework.web.server.ResponseStatusException unauthorized() { return new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required"); }
}
