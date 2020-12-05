package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.github.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class GitHubService {

	private final RestClient restClient;

	@Autowired
	public GitHubService(final RestClient restClient) {
		this.restClient = restClient;
	}

	public RepositorySummary getRepositorySummary(final String name) {
		List<Repository> gitHubRepositories = getRepositoryInfo(name);
		return new RepositorySummary(gitHubRepositories);
	}

	private List<Repository> getRepositoryInfo(final String name) {
		final UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("api.github.com")
				.path("/orgs/{name}/repos")
				.buildAndExpand(name);

		return restClient.get(uriComponents.toUriString(), new ParameterizedTypeReference<>() {});
	}
}
