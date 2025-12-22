package com.zy.im.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passport")
public class LoginController {
    @PostMapping("/login")
    public String login() {
        return "token-demo";
    }
}
