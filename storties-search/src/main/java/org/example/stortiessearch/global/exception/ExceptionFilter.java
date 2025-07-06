package org.example.stortiessearch.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.global.exception.error.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (StortiesException stortiesException) {
            errorToJson(stortiesException.getErrorCodes(), response);
        }
        catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof StortiesException) {
                errorToJson(((StortiesException) cause).getErrorCodes(), response);
            } else {
                log.error(e.getMessage(), e);
                errorToJson(ErrorCodes.INTERNAL_SERVER_ERROR, response);
            }
        }
    }

    private void errorToJson(ErrorCodes errorProperty, HttpServletResponse response) throws IOException {
        response.setStatus(errorProperty.getStatus());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(errorProperty)));
    }
}
