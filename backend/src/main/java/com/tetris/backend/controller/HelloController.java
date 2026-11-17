package com.tetris.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 提供簡單的 HTML 測試頁面，用來確認後端 API 是否正常啟動。
public class HelloController {
    @GetMapping(value = "/api/hello", produces = MediaType.TEXT_HTML_VALUE)
    // 直接回傳一段 HTML，而不是回傳 JSON。
    public String hello() {
        return """
                <!DOCTYPE html>
                <html lang=\"zh-Hant\">
                <head>
                    <meta charset=\"UTF-8\" />
                    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
                    <title>Tetris Backend</title>
                    <style>
                        body { margin: 0; font-family: Arial, sans-serif; background: linear-gradient(135deg, #0f172a, #1e293b); color: #f8fafc; display: flex; justify-content: center; align-items: center; min-height: 100vh; }
                        .card { background: rgba(15, 23, 42, 0.9); padding: 32px; border-radius: 16px; box-shadow: 0 12px 40px rgba(0,0,0,0.25); text-align: center; max-width: 560px; width: 90%; }
                        h1 { margin-top: 0; font-size: 2rem; }
                        p { line-height: 1.6; color: #cbd5e1; }
                    </style>
                </head>
                <body>
                    <div class=\"card\">
                        <h1>歡迎來到 Tetris Backend</h1>
                        <p>這是一個由 Spring Boot 提供的前端頁面，現在已經可以直接在瀏覽器中開啟。</p>
                    </div>
                </body>
                </html>
                """;
    }
}
