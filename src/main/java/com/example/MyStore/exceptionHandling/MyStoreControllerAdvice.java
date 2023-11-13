package com.example.MyStore.exceptionHandling;

import com.example.MyStore.exception.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@ControllerAdvice
public class MyStoreControllerAdvice {



    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception e, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(HttpStatusCode.valueOf(response.getStatus()));
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler({ProductNotFoundException.class, CartNotFoundException.class, DefaultPictureNotFoundException.class,
            PictureNotFoundException.class, CategoryNotFoundException.class, UsernameNotFoundException.class})
    public ModelAndView handleNotFound(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error/not-found");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }


}

