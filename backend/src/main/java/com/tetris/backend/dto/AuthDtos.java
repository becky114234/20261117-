package com.tetris.backend.dto;

public final class AuthDtos {
	// DTO 只負責在前端與後端之間傳遞帳戶資料，不負責商業邏輯。
    private AuthDtos() { }

	// 註冊、登入與重設密碼時，前端送來的資料格式。
    public record RegisterRequest(String username, String email, String password) { }
    public record LoginRequest(String username, String password) { }
    public record ResetPasswordRequest(String username, String email, String newPassword) { }
	// 回傳給前端的使用者資料，不包含密碼。
    public record UserResponse(Long id, String username, String email) { }
	// 登入或註冊成功後回傳 Token 與使用者資料。
    public record AuthResponse(String token, UserResponse user) { }
}
