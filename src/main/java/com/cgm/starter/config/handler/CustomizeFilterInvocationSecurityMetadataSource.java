package com.cgm.starter.config.handler;

import com.cgm.starter.account.service.ISysPermissionService;
import com.cgm.starter.base.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Hutengfei
 * @author cgm
 */
@Component
public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private ISysPermissionService sysPermissionService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {
        // 获取请求地址和http方法
        FilterInvocation filterInvocation = ((FilterInvocation) o);
        String requestUrl = filterInvocation.getRequest().getRequestURI();
        String httpMethod = filterInvocation.getRequest().getMethod();

        // 仅拦截/api开头的接口，标记了/public的接口不拦截，不拦截时返回空列表
        if (!requestUrl.startsWith(Constant.API_NORMAL) || requestUrl.startsWith(Constant.API_PUBLIC)) {
            return new ArrayList<>();
        }

        // 查询哪些角色具有权限
        List<String> allowRoles = sysPermissionService.listPermissionRoles(requestUrl, httpMethod);
        if (allowRoles.isEmpty()) {
            // 请求路径没有配置权限，表明该请求接口可以被任何登录用户访问，匿名用户不可访问
            return SecurityConfig.createList(Constant.ROLE_USER);
        }
        String[] attributes = new String[allowRoles.size()];
        for (int i = 0; i < allowRoles.size(); i++) {
            attributes[i] = allowRoles.get(i);
        }
        return SecurityConfig.createList(attributes);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
