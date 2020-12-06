package com.zhenz.divvydose.challenge.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class RestClient {

	private final RestTemplate restTemplate;

	public RestClient(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public <T> T get(String uri, ParameterizedTypeReference<T> responseType) {
		return restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
	}

	@Async
	public <T> CompletableFuture<T> getAsync(String uri, ParameterizedTypeReference<T> responseType) {
		return CompletableFuture.completedFuture(restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody());
	}

	public <T> T getQuietly(String uri, ParameterizedTypeReference<T> responseType) {
		try {
			return get(uri, responseType);
		} catch (Exception e) {
			log.error("Downstream exception encountered.", e);
			return null;
		}
	}
}
