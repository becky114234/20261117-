package com.tetris.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.tetris.backend.dto.GameDtos.ResultResponse;
import com.tetris.backend.entity.User;
import com.tetris.backend.repository.ScoreRepository;
import com.tetris.backend.service.AuthService;

@RestController
// 提供所有玩家的排行榜資料。
public class RankingController {
	private final ScoreRepository scores; private final AuthService auth;
	public RankingController(ScoreRepository scores, AuthService auth) { this.scores=scores; this.auth=auth; }
	@GetMapping("/api/ranking")
	// 依分數由高到低排序，只回傳前 20 名。
	public List<ResultResponse> ranking() { return scores.findAll().stream().sorted((a,b)->Integer.compare(b.getPoints(), a.getPoints())).limit(20).map(score -> new ResultResponse(score.getId(), score.getPoints(), score.getLines(), score.getLevel(), null, score.getCreatedAt().toString())).toList(); }
}
