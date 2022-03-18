package com.intuit.logger.service1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


@Configuration
@Slf4j
public class ReqFilter extends OncePerRequestFilter {
    public static final String TRACER = "tracer";
    public static final String CALLING_SERVICE = "callingService";
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tracer = request.getHeader(TRACER);

        String callingService = request.getHeader(CALLING_SERVICE);

        if (tracer == null) tracer = UUID.randomUUID().toString();

        if (callingService == null) callingService = "Self";


        log.info("In service service1 for request {} with tracer {}, calling service {}", request.getRequestURL(), tracer, callingService);

        filterChain.doFilter(request, response);
    }
}
