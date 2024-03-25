package com.alioth.server.domain.login.controller;

import com.alioth.server.domain.login.dto.LoginReqDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("api/login")
    public void login(LoginReqDto dto) {

    }
}
