package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.AiRequestDTO;
import com.tomobs.ecommerce.service.AiService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/chat")
    public ResponseEntity<String> getAiResponse(
            @RequestBody AiRequestDTO userRequest,
            HttpSession session) {

        String response = aiService.getAiResponse(userRequest, session.getId());

        return ResponseEntity.ok(response);
    }
}