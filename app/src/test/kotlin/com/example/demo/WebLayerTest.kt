package com.example.demo

import com.example.demo.controllers.HelloWorldController

import org.hamcrest.Matchers.containsString;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloWorldController::class)
class WebLayerTest(
  @Autowired
  private val mockMvc: MockMvc
) {

  @Test
  @Throws(Exception::class)
  fun shouldReturnHelloWorldMessage() {
    this.mockMvc.perform(get("/"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("Hello from Spring Boot!")));
  }
}