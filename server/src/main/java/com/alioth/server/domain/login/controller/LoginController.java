package com.alioth.server.domain.login.controller;

import com.alioth.server.common.aws.SMSService;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.login.service.LoginService;
import com.alioth.server.domain.login.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LogoutService logoutService;
    private final SMSService smsService;
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto dto) {
        LoginResDto loginResDto = loginService.memberLogin(dto);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "로그인이 되었습니다",
                loginResDto
        );
    }

    @PostMapping("/api/{memberCode}/logout")
    public ResponseEntity<?> logout(@PathVariable String memberCode) {
        logoutService.logout(Long.valueOf(memberCode));

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "로그아웃 되었습니다"
        );
    }

    @GetMapping("/api/test")
    public String testUrl() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        smsService.sendSMS("+8201086253122","spring 에서 테스트");
        return name;
    }


    @PostMapping("/api/send-verification")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> payload) {
        String phoneNumber = payload.get("phone");
        String verificationCode = generateVerificationCode(6); // 6자리 인증번호 생성

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(phoneNumber, verificationCode, 5, TimeUnit.MINUTES); // Redis에 5분간 저장

        smsService.sendSMS(phoneNumber, "alioth 비밀번호찾기 인증번호 : " + verificationCode);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "인증번호가 발송되었습니다."
        );
    }

    private String generateVerificationCode(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10)); // 0-9 사이의 숫자 추가
        }
        return builder.toString();
    }


    @PostMapping("/api/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload) {
        String phoneNumber = payload.get("phone");
        String code = payload.get("code");
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String storedCode = ops.get(phoneNumber);

        boolean isVerified = storedCode != null && storedCode.equals(code);
        if(isVerified) {
            stringRedisTemplate.delete(phoneNumber); // 인증 후 코드 삭제
        }

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                isVerified ? "인증에 성공하였습니다." : "인증번호가 일치하지 않습니다."
        );
    }
}
