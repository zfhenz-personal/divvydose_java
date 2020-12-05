package com.zhenz.divvydose.challenge.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClient {

	private final RestTemplate restTemplate;

	public RestClient(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public <T> T get(String uri, ParameterizedTypeReference<T> responseType) {
		return restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
	}
}
