package com.example.demo

import com.example.demo.controllers.HelloWorldController

import org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DemoApplicationTests(
	@Autowired
	private val controller: HelloWorldController
) {

	@Test
	fun contextLoads() {
		assertThat(controller).isNotNull()
	}
}
