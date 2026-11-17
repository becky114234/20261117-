package com.tetris.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tetris.backend.dto.AuthDtos.AuthResponse;
import com.tetris.backend.dto.AuthDtos.LoginRequest;
import com.tetris.backend.dto.AuthDtos.RegisterRequest;
import com.tetris.backend.dto.AuthDtos.ResetPasswordRequest;
import com.tetris.backend.dto.AuthDtos.UserResponse;
import com.tetris.backend.entity.User;
import com.tetris.backend.service.AuthService;

@RestController
// 處理註冊、登入、重設密碼、查詢目前使用者與登出 API。
public class AuthController {
	private final AuthService auth;
	// Spring 會自動注入帳戶服務，Controller 只負責接收 HTTP 請求。
	public AuthController(AuthService auth) { this.auth = auth; }

	// 建立新帳戶，成功時回傳登入 Token。
	@PostMapping("/api/auth/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(@RequestBody RegisterRequest request) { return auth.register(request); }

	// 使用帳號密碼登入。
	@PostMapping("/api/auth/login")
	public AuthResponse login(@RequestBody LoginRequest request) { return auth.login(request); }

	// 依帳號與 Email 驗證後更新密碼。
	@PostMapping("/api/auth/reset-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void resetPassword(@RequestBody ResetPasswordRequest request) { auth.resetPassword(request); }

	// 讀取 Authorization 標頭，回傳目前登入的使用者資料。
	@GetMapping("/api/auth/me")
	public UserResponse me(@RequestHeader(value = "Authorization", required = false) String authorization) {
		User user = auth.requireUser(authorization);
		if (user == null) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
		return auth.response(user);
	}

	// 移除目前 Token，使它失效。
	@PostMapping("/api/auth/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(@RequestHeader(value = "Authorization", required = false) String authorization) { auth.logout(authorization); }
}
