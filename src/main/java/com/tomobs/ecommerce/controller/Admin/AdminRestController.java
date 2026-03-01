package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;

    @PostMapping("/users/toggle-block/{id}")
    public ResponseEntity<Map<String, Boolean>> toggleUserBlock(@PathVariable Long id) {
        userService.toggleUserBlockStatus(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
