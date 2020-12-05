package com.zhenz.divvydose.challenge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("health")
public class HealthCheckController {

	@GetMapping()
	public ResponseEntity<Void> healthCheck() {
		log.info("Application is up!");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
