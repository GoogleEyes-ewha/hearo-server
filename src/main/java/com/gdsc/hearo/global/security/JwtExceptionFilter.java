package com.gdsc.hearo.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private int code;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
        } catch (JwtException | IOException | ServletException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse res, Throwable ex) throws IOException {

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (ex.getMessage().equals(BaseResponseStatus.INVALID_TOKEN.getMessage())) {
            code = BaseResponseStatus.INVALID_TOKEN.getCode();
        } else if (ex.getMessage().equals(BaseResponseStatus.EXPIRED_TOKEN.getMessage())) {
            code = BaseResponseStatus.EXPIRED_TOKEN.getCode();
        } else if (ex.getMessage().equals(BaseResponseStatus.UNSUPPORTED_TOKEN.getMessage())) {
            code = BaseResponseStatus.UNSUPPORTED_TOKEN.getCode();
        } else {
            code = BaseResponseStatus.ACCESS_DENIED.getCode();
        }
        final Map<String, Object> body = new LinkedHashMap<>();

        body.put("code", code);
        body.put("inSuccess", false);
        body.put("message", ex.getMessage());
        body.put("result", "Unauthorized error");
        res.setStatus(status.value());

        objectMapper.writeValue(res.getOutputStream(), body);
    }
}