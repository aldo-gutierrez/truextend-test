package com.truextend.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

        Map<String, Object> map = new HashMap<>();
        if (ex instanceof BusinessException) {
            map.put("error", ex.getMessage());
            logger.warn(ex.getMessage());
            return handleExceptionInternal(ex, getStringBody(map), new HttpHeaders()
                    , HttpStatus.resolve(((BusinessException) ex).getStatus()), request);

        } else {
            if (ex.getMessage() != null) {
                map.put("error", ex.getMessage());
            } else {
                map.put("error", ex instanceof  NullPointerException ? "NullPointerException" : "Unknown Error");
            }
            return handleExceptionInternal(ex, getStringBody(map), new HttpHeaders()
                    , HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    private String getStringBody(Map<String, Object> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return "";
        }
    }
}
