package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloWorldController {

  @GetMapping("/")
  fun index() = "Hello from Spring Boot!"
}