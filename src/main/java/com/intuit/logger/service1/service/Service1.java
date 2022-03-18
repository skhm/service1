package com.intuit.logger.service1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Service1 {

    private RestTemplate restTemplate = new RestTemplate();

    public static final String TRACER = "tracer";
    public static final String CALLING_SERVICE = "callingService";


    public void callOtherServices(HttpServletRequest request) {
        List<CompletableFuture<String>> result = new ArrayList<>();
        result.add(makeRestCalls("http://localhost:9999/service2/success", request));
        result.add(makeRestCalls("http://localhost:9999/service2/fail", request));

        result.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    @Async("threadPool")
    private CompletableFuture<String> makeRestCalls(String uri, HttpServletRequest request) {
        final HttpEntity<String> httpEntity = generateHeader(request);
        String status = "fail";
        try {
            status = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();
        }
        catch (Exception e) {
            log.error("Error calling service", e);
        }
        return CompletableFuture.completedFuture(status);
    }

    private HttpEntity<String> generateHeader(HttpServletRequest request) {
        final var headers = new HttpHeaders();
        headers.add(TRACER, request.getAttribute(TRACER) == null ?
                String.valueOf(UUID.randomUUID()) : String.valueOf(request.getAttribute(TRACER)));
        headers.add(CALLING_SERVICE, "service1");
        return new HttpEntity<>(headers);
    }
}
