package com.example.MyStore.aspects;

import com.example.MyStore.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class CartRefreshAspect {

    private final UserService userService;

    public CartRefreshAspect(UserService userService) {
        this.userService = userService;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMappingMethods() {
    }

    @Before("getMappingMethods()")
    public void beforeGetMappingMethodInvocation(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && (authentication.getPrincipal() instanceof UserDetails)) {
            String username = authentication.getName();
            userService.updateShoppingCartWithProductQuantities(username);
        }
    }
}
