package com.alioth.server.domain.login.service.impl;

import com.alioth.server.common.jwt.JwtTokenProvider;
import com.alioth.server.common.redis.RedisService;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.login.service.LoginService;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SalesMemberRepository salesMemberRepository;
    private final RedisService redisService;

    @Override
    public CommonResponse memberLogin(LoginReqDto dto) {
        SalesMembers findMember = salesMemberRepository.findBySalesMemberCode(dto.memberCode())
                .filter(it -> passwordEncoder.matches(dto.password(), it.getPassword()))
                .orElseThrow(() -> new EntityNotFoundException("사원번호 혹은 비밀번호를 다시 확인해주세요."));

        String memberSpecification = String.format("%s:%s", findMember.getSalesMemberCode(), findMember.getRank());
        String accessToken = jwtTokenProvider.createAccessToken(memberSpecification);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisService.setValues(findMember.getSalesMemberCode() + ":RefreshToken", refreshToken);

        LoginResDto resDto = LoginResDto.builder()
                .memberCode(findMember.getSalesMemberCode())
                .name(findMember.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return CommonResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("로그인이 완료되었습니다.")
                .result(resDto)
                .build();
    }
}
