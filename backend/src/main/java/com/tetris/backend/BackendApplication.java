package com.tetris.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Spring Boot 的啟動類別，程式會從這裡開始執行。
public class BackendApplication {

	// 啟動 Spring Boot，建立後端伺服器並載入所有設定。
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
