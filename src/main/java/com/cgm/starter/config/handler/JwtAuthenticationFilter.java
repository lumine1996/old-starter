package com.cgm.starter.config.handler;

import com.cgm.starter.account.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器
 *
 * @author K. L. Mao
 * @author cgm
 * @date 2019/1/11
 */
@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        // 接收header和url中的token
        String token = request.getHeader("X-Token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        String undefined = "undefined";
        if (StringUtils.hasText(token) && !undefined.equals(token)) {
            String username = jwtUtils.getUsernameFromToken(token, request, response);
            // 未获取到用户名，说明之前发生了异常，结束拦截链
            if (username == null) {
                return;
            }
            // 刷新token
            if (jwtUtils.isTokenNeedRefresh(token)) {
                String refreshToken = jwtUtils.refreshToken(token);
                response.setHeader("refresh-token", refreshToken);
            }
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = sysUserService.loadUserByUsername(username);
                if (jwtUtils.validateToken(token, userDetails)) {
                    // 将用户信息存入 authentication，方便后续校验
                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
