package com.alioth.server.domain.login.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto dto) {
        CommonResponse commonResponse = loginService.memberLogin(dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commonResponse);
    }
}
