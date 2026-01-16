package com.bootdo.core.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.pojo.response.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Spring Security 认证入口点实现类
 * <p>
 * 核心功能：处理未认证用户访问受保护资源的请求
 * <p>
 * 触发时机：
 * 1. 用户直接访问需要登录的URL/API
 * 2. 会话过期后继续访问受保护资源
 * 3. 未携带有效认证信息（如JWT令牌）访问API
 * <p>
 * 实现原理：
 * - 实现Spring Security的{@link AuthenticationEntryPoint}接口
 * - 通过 方法被调用
 * - 返回401 UNAUTHORIZED状态码的JSON响应
 * <p>
 * 使用场景：
 * - 前后端分离架构中的未登录访问处理
 * - RESTful API的认证失败统一处理
 * - 需要自定义登录引导逻辑的场景
 *
 * @author L
 * @since 2026-01-08 11:11
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), e);
        // AJAX 请求
        if (isAjaxRequest(request)) {
            // AJAX请求返回JSON格式的401响应
            String content = JSONUtil.toJsonStr(R.error(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase()));

            JakartaServletUtil.write(response, content, APPLICATION_JSON_VALUE);
        } else {
            // 普通请求重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
    }

}
