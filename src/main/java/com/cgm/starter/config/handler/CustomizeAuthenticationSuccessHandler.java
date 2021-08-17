package com.cgm.starter.config.handler;

import com.alibaba.fastjson.JSON;
import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.account.service.ISysUserService;
import com.cgm.starter.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author Hutengfei
 * @author cgm
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        // 更新用户表上次登录时间、更新人、更新时间等字段
        SysUser user = (SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwtUtils.generateToken(user);
        Date now = new Date();
        user.setLastLoginTime(now);
        user.setUpdateTime(now);
        user.setUpdatedBy(user.getId());
        sysUserService.sysUpdateUser(user);

        // 将token返回给前端
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(new ResponseData(token)));
    }
}
