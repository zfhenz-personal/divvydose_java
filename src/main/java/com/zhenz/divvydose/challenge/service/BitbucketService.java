package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.bitbucket.Page;
import com.zhenz.divvydose.challenge.domain.bitbucket.Repository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BitbucketService {

	private final RestClient restClient;

	public BitbucketService(final RestClient restClient) {
		this.restClient = restClient;
	}

	public RepositorySummary getRepositorySummary(final String name) {
		// todo handle paging where repo count > 10
		Page<Repository> repositoryPage = getRepositoryPage(name);
		RepositorySummary repositorySummary = new RepositorySummary(repositoryPage);
		repositorySummary.setWatcherCount(0L);

		// todo make async
		for (Repository repository : repositoryPage.getValues()) {
			Page watchers = restClient.get(repository.getLinks().getWatchers().getHref(), new ParameterizedTypeReference<>() {});
			repositorySummary.setWatcherCount(repositorySummary.getWatcherCount() + watchers.getSize());
		}

		return repositorySummary;
	}

	private Page<Repository> getRepositoryPage(final String name) {
		final UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("api.bitbucket.org")
				.path("/2.0/repositories/{name}")
				.buildAndExpand(name);

		return restClient.get(uriComponents.toUriString(), new ParameterizedTypeReference<>() {});
	}
}
