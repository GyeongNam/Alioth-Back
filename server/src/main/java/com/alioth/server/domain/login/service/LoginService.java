package com.alioth.server.domain.login.service;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;

public interface LoginService {
    CommonResponse memberLogin(LoginReqDto dto);
}
