package com.bootdo.core.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.pojo.response.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Spring Security 登录成功处理器
 * 返回与前端兼容的JSON格式响应
 *
 * @author L
 * @since 2026-01-08 11:11
 */
@Component
public class LoginSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回成功的 JSON响应
        String content = JSONUtil.toJsonStr(R.ok());

        JakartaServletUtil.write(response, content, APPLICATION_JSON_VALUE);

    }
}
