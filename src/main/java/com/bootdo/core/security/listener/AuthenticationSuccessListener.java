package com.bootdo.core.security.listener;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.bootdo.core.security.context.CacheContextHolder;
import com.bootdo.core.utils.HttpServletUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.bootdo.core.security.context.CacheContextHolder.SESSION_KEY_IP;
import static com.bootdo.core.security.context.CacheContextHolder.SESSION_KEY_LOGIN_DATE;

/**
 * Spring Security 认证成功事件监听器
 * <p>
 * 核心功能：监听用户认证成功事件，执行登录后的自定义业务逻辑
 * <p>
 * 触发时机：
 * 1. 用户通过用户名密码成功登录
 * 2. 用户通过第三方认证成功登录
 * 3. 用户通过记住我功能自动登录成功
 * <p>
 * 实现原理：
 * - 实现Spring的{@link ApplicationListener}接口，监听{@link AuthenticationSuccessEvent}事件
 * - 在用户认证成功后自动被Spring容器调用
 * - 支持获取认证上下文信息和HTTP请求信息
 * <p>
 * 主要功能：
 * - 将会话信息注册到{@link SessionRegistry}（用于在线用户管理）
 * - 记录用户登录IP地址和登录时间
 * - 支持扩展其他登录后业务逻辑（如登录日志记录、通知推送等）
 * <p>
 * 使用场景：
 * - 在线用户管理系统
 * - 用户登录行为审计
 * - 登录后的个性化处理
 * - 会话信息扩展存储
 *
 * @author L
 * @since 2026-01-08 11:11
 */
@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Resource
    private SessionRegistry sessionRegistry;

    /**
     * 处理认证成功事件
     * 在用户成功登录后，将用户主体信息注册到会话注册表中
     */
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // 获取客户端 IP
        HttpServletRequest request = HttpServletUtil.getRequest();
        String clientIp = JakartaServletUtil.getClientIP(request);

        Object principal = event.getAuthentication().getPrincipal();
        String sessionId = request.getSession().getId();

        // 注册会话时携带IP信息（需自定义存储机制）
        sessionRegistry.registerNewSession(sessionId, principal);

        // 可以考虑将IP信息存储到缓存中，与sessionId关联
        CacheContextHolder.me().put(SESSION_KEY_IP, sessionId, clientIp);
        CacheContextHolder.me().put(SESSION_KEY_LOGIN_DATE, sessionId, new Date());

    }
}