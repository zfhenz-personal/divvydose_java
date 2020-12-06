package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.bitbucket.Page;
import com.zhenz.divvydose.challenge.domain.bitbucket.Repository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class BitbucketService {

	private final RestClient restClient;

	public BitbucketService(final RestClient restClient) {
		this.restClient = restClient;
	}

	public RepositorySummary getRepositorySummary(final String name) {
		int page = 1;
		Page<Repository> repositoryPage = getRepositoryPage(name, page);
		RepositorySummary repositorySummary = new RepositorySummary(repositoryPage);
		repositorySummary.setWatcherCount(0L);
		repositorySummary.setForkRepoCount(0L);

		List<Repository> allRepositories = repositoryPage.getValues();
		while (repositoryPage.getSize() > 10) {
			page++;
			repositoryPage = getRepositoryPage(name, page);
			allRepositories.addAll(repositoryPage.getValues());
		}

		for (Repository repository : allRepositories) {
			Page<?> watchers = restClient.get(repository.getLinks().getWatchers().getHref(), new ParameterizedTypeReference<>() {});
			repositorySummary.setWatcherCount(repositorySummary.getWatcherCount() + watchers.getSize());

			Page<?> forks = restClient.get(repository.getLinks().getForks().getHref(), new ParameterizedTypeReference<>() {});
			repositorySummary.setForkRepoCount(repositorySummary.getWatcherCount() + forks.getSize());
		}

		return repositorySummary;
	}

	private Page<Repository> getRepositoryPage(final String name, final int page) {
		final UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("api.bitbucket.org")
				.path("/2.0/repositories/{name}")
				.queryParam("page", page)
				.buildAndExpand(name);

		return restClient.get(uriComponents.toUriString(), new ParameterizedTypeReference<>() {});
	}
}
