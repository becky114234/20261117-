package com.tetris.backend.dto;

public final class GameDtos {
    private GameDtos() { }
	// 前端送出的遊戲成績資料。
    public record ResultRequest(int points, int lines, int level, String result) { }
	// 後端回傳的成績或對局紀錄資料。
    public record ResultResponse(Long id, int points, int lines, int level, String result, String time) { }
}
