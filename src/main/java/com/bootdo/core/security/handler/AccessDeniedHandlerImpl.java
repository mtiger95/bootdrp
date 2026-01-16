package com.bootdo.core.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.SecurityUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Spring Security 访问拒绝处理器实现类
 * <p>
 * 核心功能：处理已认证用户访问无权限资源的请求
 * <p>
 * 触发时机：
 * 1. 普通用户尝试访问管理员资源
 * 2. 未授权角色访问受保护API
 * 3. 用户权限变更后访问原有资源
 * <p>
 * 实现原理：
 * - 实现Spring Security的{@link AccessDeniedHandler}接口
 * - 返回403 FORBIDDEN状态码的JSON响应
 * - 记录权限不足的访问日志（用于安全审计）
 * <p>
 * 使用场景：
 * - 基于角色/权限的细粒度访问控制
 * - 企业级应用的权限管理
 * - 需要权限审计的安全场景
 *
 * @author L
 * @since 2026-01-08 11:11
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        // 打印 warn 的原因是，不定期合并 warn，看看有没恶意破坏
        log.warn("[commence][访问 URL({}) 时，用户({}) 权限不够]", request.getRequestURI(), SecurityUtils.getUserId(), e);
        // 返回 403
        String content = JSONUtil.toJsonStr(R.error(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase()));

        JakartaServletUtil.write(response, content, APPLICATION_JSON_VALUE);
    }

}
