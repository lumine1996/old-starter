package com.cgm.starter.config.handler;

import com.alibaba.fastjson.JSON;
import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.base.ErrorCode;
import com.cgm.starter.base.ResponseData;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成令牌，验证等等一些操作
 *
 * @author K. L. Mao
 * @author cgm
 * @date 2019/1/10
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {
    @Resource
    private MessageSource messageSource;

    private String secret = "cgm-starter-secret";

    /**
     * token有效时间
     */
    private Long validMillis = 2100000L;

    /**
     * 临期刷新时间
     * token剩余有效期小于此值时才会触发刷新, 避免刷新token过于频繁
     */
    private Long refreshMillis = 1800000L;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + validMillis);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成令牌
     *
     * @param userDetails 用户
     * @return 令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims == null ? null : claims.getSubject();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token    令牌
     * @param request  请求
     * @param response 响应
     * @return 用户名
     */
    public String getUsernameFromToken(String token, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String errorCode;
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims == null ? null : claims.getSubject();
        } catch (ExpiredJwtException e) {
            errorCode = ErrorCode.USER_JWT_EXPIRED;
        } catch (UnsupportedJwtException e) {
            errorCode = ErrorCode.USER_JWT_UNSUPPORTED;
        } catch (SignatureException e) {
            errorCode = ErrorCode.USER_JWT_SIGNATURE_ERROR;
        } catch (Exception e) {
            errorCode = ErrorCode.SYS_JWT_EXCEPTION;
        }

        // 将异常信息返回前端，之后需要终止拦截链
        String localeMessage = messageSource.getMessage(errorCode, null, RequestContextUtils.getLocale(request));
        ResponseData responseData = new ResponseData(errorCode, localeMessage, null, false);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(responseData));
        return null;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return false;
        }
        Date expiration = claims.getExpiration();
        return new Date().after(expiration);
    }

    /**
     * 判断令牌是否需要刷新
     *
     * @param token 令牌
     * @return 是否需要刷新
     */
    public boolean isTokenNeedRefresh(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return false;
        }
        Date expiration = claims.getExpiration();
        Instant fewTimeAfter = Instant.now().plusMillis(refreshMillis);
        return Date.from(fewTimeAfter).after(expiration);
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        SysUser user = (SysUser) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
