package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.UserOnline;
import com.bootdo.modular.system.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Tag(name = "在线用户")
@RequestMapping("/sys/online")
@Controller
public class SessionController {
    @Resource
    SessionService sessionService;

    @GetMapping()
    public String online() {
        return "system/online/online";
    }

    @ResponseBody
    @RequestMapping("/list")
    public List<UserOnline> list(@RequestParam Map<String, Object> params) {
        return sessionService.list(params);
    }

    @ResponseBody
    @RequestMapping("/forceLogout/{sessionId}")
    public R forceLogout(@PathVariable String sessionId) {
        try {
            sessionService.forceLogout(sessionId);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/sessionList")
    public Collection<Session> sessionList() {
        return sessionService.sessionList();
    }
}
