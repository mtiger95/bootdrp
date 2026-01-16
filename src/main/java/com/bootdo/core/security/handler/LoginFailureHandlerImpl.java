package com.bootdo.core.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.pojo.response.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Spring Security 登录失败处理器
 * 返回与前端兼容的JSON格式响应
 *
 * @author L
 * @since 2026-01-08 11:11
 */
@Component
public class LoginFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 返回失败的 JSON响应
        String content = JSONUtil.toJsonStr(R.error(UNAUTHORIZED.value(), e.getMessage()));

        JakartaServletUtil.write(response, content, APPLICATION_JSON_VALUE);
    }
}