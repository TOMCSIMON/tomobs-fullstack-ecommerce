package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.UserListDTO;
import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public String getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String keyword,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model
    ) {
        Page<UserListDTO> userPage = userService.listUsers(keyword, page, size);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("keyword", keyword);

        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/user-list :: userTableFragment";
        }
        return "admin/user-list";
    }
}
