package com.example.MyStore.aop;

import com.example.MyStore.service.UserService;

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

   @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) " +
                     "&& !execution(* *..login(..))" +
           "&& !execution(* *..register(..))")
    private void getMappingMethods() {
    }

    @Before("getMappingMethods()")
    private void beforeGetMappingMethodInvocation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && (authentication.getPrincipal() instanceof UserDetails)) {
            String username = authentication.getName();
            userService.updateShoppingCartWithProductQuantities(username);
        }
    }
}
