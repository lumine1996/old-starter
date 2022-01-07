package com.cgm.starter.annotation;

import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.base.BaseException;
import com.cgm.starter.base.ErrorCode;
import com.cgm.starter.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * @author cgm
 */
@Aspect
@Component
@Slf4j
public class IsolationAspect {
    @Before(value = "@annotation(com.cgm.starter.annotation.Isolation)")
    public void doBefore(JoinPoint joinPoint) {
        // 获取当前用户
        SysUser user = UserUtils.getCurrentUser();
        // 超管跳过后续校验
        if (UserUtils.isSystemAdmin(user)) {
            return;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Isolation isolation = signature.getMethod().getAnnotation(Isolation.class);
        switch (isolation.value()) {
            case NONE:
                return;
            case SYS_ADMIN_ONLY:
                // 策略为仅超管可访问时, 检验是否超管
                Assert.isTrue(UserUtils.isSystemAdmin(user), ErrorCode.USER_PERMISSION_DENIED);
                break;
            case TENANT:
                this.tenancyIsolation(user);
        }
    }

    private void tenancyIsolation(SysUser user) {
        // 获取HTTP请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.isTrue(attributes != null, ErrorCode.SYS_NO_FIELD);
        HttpServletRequest request = attributes.getRequest();

        Object attribute = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (attribute instanceof LinkedHashMap<?, ?>) {
            // 获取organizationId请求参数
            LinkedHashMap<?, ?> attributeMap = (LinkedHashMap<?, ?>) attribute;
            String organizationIdParam = String.valueOf(attributeMap.get("organizationId"));
            Assert.isTrue(StringUtils.isNumeric(organizationIdParam), ErrorCode.SYS_NO_FIELD);
            Integer organizationId = Integer.parseInt(organizationIdParam);

            // 如果不一致则抛出异常
            Assert.isTrue(user.getOrganizationId().equals(organizationId), ErrorCode.USER_ORGANIZATION_MISMATCH);
        } else {
            throw new BaseException(ErrorCode.SYS_INTERNAL_ERROR);
        }
    }
}
