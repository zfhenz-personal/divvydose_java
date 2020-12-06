package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.bitbucket.Page;
import com.zhenz.divvydose.challenge.domain.bitbucket.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class BitbucketService {

	private final RestClient restClient;

	@Autowired
	public BitbucketService(final RestClient restClient) {
		this.restClient = restClient;
	}

	public RepositorySummary getRepositorySummary(final String name) throws InterruptedException, ExecutionException {
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

		List<CompletableFuture<Page<Object>>> allWatcherPages = new ArrayList<>();
		List<CompletableFuture<Page<Object>>> allForkPages = new ArrayList<>();

		for (Repository repository : allRepositories) {
			// diamond expression intentionally unused to avoid bug in jdk11
			// https://bugs.openjdk.java.net/browse/JDK-8220578
			allWatcherPages.add(restClient.getAsync(repository.getLinks().getWatchers().getHref(),
					new ParameterizedTypeReference<Page<Object>>() {}));

			allForkPages.add(restClient.getAsync(repository.getLinks().getForks().getHref(),
					new ParameterizedTypeReference<Page<Object>>() {}));
		}

		updateWatcherCount(repositorySummary, allWatcherPages);
		updateForkCount(repositorySummary, allForkPages);

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

	private void updateWatcherCount(final RepositorySummary repositorySummary,
									final List<CompletableFuture<Page<Object>>> watcherPages) throws InterruptedException, ExecutionException {
		CompletableFuture.allOf(watcherPages.toArray(new CompletableFuture[0])).join();

		for (CompletableFuture<Page<Object>> watcherPage : watcherPages) {
			repositorySummary.setWatcherCount(repositorySummary.getWatcherCount() + watcherPage.get().getSize());
		}
	}

	private void updateForkCount(final RepositorySummary repositorySummary,
								 final List<CompletableFuture<Page<Object>>> forkPages) throws InterruptedException, ExecutionException {
		CompletableFuture.allOf(forkPages.toArray(new CompletableFuture[0])).join();

		for (CompletableFuture<Page<Object>> forkPage : forkPages) {
			repositorySummary.setForkRepoCount(repositorySummary.getForkRepoCount() + forkPage.get().getSize());
		}
	}
}
