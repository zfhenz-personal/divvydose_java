package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.bitbucket.RepositoryPage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BitBucketService {

	private final RestClient restClient;

	public BitBucketService(final RestClient restClient) {
		this.restClient = restClient;
	}

	public RepositoryPage getRepositoryInfo(final String name) {
		final UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("api.bitbucket.org")
				.path("/2.0/repositories/{name}")
				.buildAndExpand(name);

		return restClient.get(uriComponents.toUriString(), new ParameterizedTypeReference<>() {});
	}
}
