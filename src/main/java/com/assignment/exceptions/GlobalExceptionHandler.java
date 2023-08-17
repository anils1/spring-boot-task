package com.assignment.exceptions;

import com.assignment.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(ProductValidationException.class)
    public ApiResponse handleProductValidationException(ProductValidationException productValidationException){
        ApiResponse response = new ApiResponse();
        response.setMessage("Product price must not exceed 10,000 and if price is greater than 5000, it will go for approval");
        response.setStatus(false);
        return response;
    }
}
