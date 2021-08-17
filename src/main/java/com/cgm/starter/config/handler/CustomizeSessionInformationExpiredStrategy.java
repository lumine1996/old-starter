package com.cgm.starter.config.handler;

import com.alibaba.fastjson.JSON;
import com.cgm.starter.base.ErrorCode;
import com.cgm.starter.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * 会话策略
 *
 * 当前已禁用session，如需启用，在SecurityConfig中配置sessionManagement().expiredSessionStrategy
 *
 * @author Hutengfei
 * @author cgm
 */
@Component
public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Autowired
    private MessageSource messageSource;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent)
            throws IOException {
        Locale locale = RequestContextUtils.getLocale(sessionInformationExpiredEvent.getRequest());
        String code = ErrorCode.USER_SESSION_EXPIRED;
        String localeMessage = messageSource.getMessage(code, null, locale);
        ResponseData responseData = new ResponseData(code, localeMessage, null, false);

        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseData));
    }
}
