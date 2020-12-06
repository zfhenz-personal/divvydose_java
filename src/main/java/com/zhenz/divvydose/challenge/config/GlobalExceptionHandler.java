package com.zhenz.divvydose.challenge.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = HttpClientErrorException.TooManyRequests.class)
	public ResponseEntity<String> handleTooManyRequests(final HttpClientErrorException.TooManyRequests e) {
		log.error("Too many requests to downstream API:", e);
		return new ResponseEntity<>("Too many requests, please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(value = HttpClientErrorException.NotFound.class)
	public ResponseEntity<String> handleNotFound(final HttpClientErrorException.NotFound e) {
		log.error("Repository not found:", e);
		return new ResponseEntity<>("Unable to find repository, please check provided name.", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handleGenericException(final Exception e) {
		log.error("Generic exception encountered:", e);
		return new ResponseEntity<>("Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
	}
}
