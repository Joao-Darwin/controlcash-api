package com.controlcash.app.controllers;

import com.controlcash.app.exceptions.ResponseEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(value = "${path-api}/helloWorld")
public class HelloWorldController {

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseEntityException(Instant.now(), "", ""));
    }
}
