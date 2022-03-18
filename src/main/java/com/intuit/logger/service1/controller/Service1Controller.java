package com.intuit.logger.service1.controller;


import com.intuit.logger.service1.service.Service1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/service1")
public class Service1Controller {

    private Service1 service;

    @Autowired
    public void Service1Controller(Service1 service) {
        this.service = service;
    }

    @GetMapping(path = "/hello")
    public ResponseEntity<String> sayHello(HttpServletRequest request) {
        service.callOtherServices(request);
        return ResponseEntity.ok("Hello");
    }

}
