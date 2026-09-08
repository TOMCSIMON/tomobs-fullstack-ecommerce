package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.AiRequestDTO;
import com.tomobs.ecommerce.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user/ai")
public class UserAiController {

    private final AiService aiService;

    @GetMapping("/test")
    public String test() {
        return "AI endpoint is working";
    }

    @PostMapping("/chat")
    public ResponseEntity<String> getAiResponse(@RequestBody AiRequestDTO userRequest) {

        log.info("AI request received: {}", userRequest);

        String response = aiService.getAiResponse(userRequest);

        return ResponseEntity.ok(response);
    }
}