package com.rc.mambasaerchbackend.aop;

import com.rc.mambasaerchbackend.annotation.AuthCheck;
import com.rc.mambasaerchbackend.common.BusinessException;
import com.rc.mambasaerchbackend.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Set;

/**
 * 权限校验 AOP 切面
 */
@Slf4j
@Aspect
@Component
public class AuthAspect {

    @Around("@annotation(authCheck)")
    public Object checkAuth(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 从 Session 中获取当前登录用户角色
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        String[] requiredRoles = authCheck.role();
        if (requiredRoles.length == 0) {
            // 不限制角色，只要登录即可
            return joinPoint.proceed();
        }

        // 检查角色是否匹配
        Set<String> roleSet = Set.of(requiredRoles);
        if (!roleSet.contains(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "需要角色: " + Arrays.toString(requiredRoles));
        }

        return joinPoint.proceed();
    }
}
