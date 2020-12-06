package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.github.Repository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {

	private final RestClient restClient;

	@Autowired
	public GitHubService(final RestClient restClient) {
		this.restClient = restClient;
	}

	public RepositorySummary getRepositorySummary(final String name) {
		int page = 1;
		List<Repository> allRepositories = new ArrayList<>();
		List<Repository> repositoryPage = getRepositoryInfo(name, page);

		while (CollectionUtils.isNotEmpty(repositoryPage)) {
			allRepositories.addAll(repositoryPage);
			page++;
			repositoryPage = getRepositoryInfo(name, page);
		}

		return new RepositorySummary(allRepositories);
	}

	private List<Repository> getRepositoryInfo(final String name, final int page) {
		final UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("api.github.com")
				.path("/orgs/{name}/repos")
				.queryParam("page", page)
				.buildAndExpand(name);

		return restClient.get(uriComponents.toUriString(), new ParameterizedTypeReference<>() {});
	}
}
