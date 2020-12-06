package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.bitbucket.Page;
import com.zhenz.divvydose.challenge.domain.bitbucket.Repository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BitbucketServiceTest {

	private static final String NAME = "test_org";
	private static final String WATCHER_LINK = "https://api.bitbucket.org/repositories/watchers";
	private static final String FORK_LINK = "https://api.bitbucket.org/repositories/fork";

	@Mock
	private RestClient restClient;
	@InjectMocks
	private BitbucketService bitbucketService;

	@Test
	public void test_getRepositorySummary_standardResponse() throws InterruptedException, ExecutionException {
		when(restClient.get(buildUriString(1), new ParameterizedTypeReference<Page<Repository>>() {})).thenReturn(buildRepositoryResponse(1, 3, 3));
		when(restClient.get(buildUriString(2), new ParameterizedTypeReference<Page<Repository>>() {})).thenReturn(buildEmptyRepositoryResponse(2));

		when(restClient.getAsync(WATCHER_LINK, new ParameterizedTypeReference<Page<Object>>() {})).thenReturn(buildDeepLinkedResponse(2));
		when(restClient.getAsync(FORK_LINK, new ParameterizedTypeReference<Page<Object>>() {})).thenReturn(buildDeepLinkedResponse(3));

		RepositorySummary summary = bitbucketService.getRepositorySummary(NAME);

		assertEquals(3L, (long) summary.getPublicRepoCount());
		assertEquals(9L, (long) summary.getForkRepoCount());
		assertEquals(6L, (long) summary.getWatcherCount());
		assertEquals(1L, (long) summary.getLanguageCount());
		assertEquals(1L, (long) summary.getTopicCount());
		assertTrue(CollectionUtils.isNotEmpty(summary.getLanguages()));
		assertTrue(CollectionUtils.isNotEmpty(summary.getTopics()));
	}

	@Test
	public void test_getRepositorySummary_emptyResponse() throws InterruptedException, ExecutionException {
		when(restClient.get(buildUriString(1), new ParameterizedTypeReference<Page<Repository>>() {})).thenReturn(buildEmptyRepositoryResponse(1));


		RepositorySummary summary = bitbucketService.getRepositorySummary(NAME);

		assertEquals(0L, (long) summary.getPublicRepoCount());
		assertEquals(0L, (long) summary.getForkRepoCount());
		assertEquals(0L, (long) summary.getWatcherCount());
		assertEquals(0L, (long) summary.getLanguageCount());
		assertEquals(0L, (long) summary.getTopicCount());
		assertTrue(CollectionUtils.isEmpty(summary.getLanguages()));
		assertTrue(CollectionUtils.isEmpty(summary.getTopics()));
	}

	@Test
	public void test_getRepositorySummary_multiplePages() throws InterruptedException, ExecutionException {
		when(restClient.get(buildUriString(1), new ParameterizedTypeReference<Page<Repository>>() {})).thenReturn(buildRepositoryResponse(1, 10, 15));
		when(restClient.get(buildUriString(2), new ParameterizedTypeReference<Page<Repository>>() {})).thenReturn(buildRepositoryResponse(2, 5, 15));
		when(restClient.get(buildUriString(3), new ParameterizedTypeReference<Page<Repository>>() {})).thenReturn(buildEmptyRepositoryResponse(3));

		when(restClient.getAsync(WATCHER_LINK, new ParameterizedTypeReference<Page<Object>>() {})).thenReturn(buildDeepLinkedResponse(2));
		when(restClient.getAsync(FORK_LINK, new ParameterizedTypeReference<Page<Object>>() {})).thenReturn(buildDeepLinkedResponse(3));

		RepositorySummary summary = bitbucketService.getRepositorySummary(NAME);

		assertEquals(15L, (long) summary.getPublicRepoCount());
		assertEquals(45L, (long) summary.getForkRepoCount());
		assertEquals(30L, (long) summary.getWatcherCount());
		assertEquals(1L, (long) summary.getLanguageCount());
		assertEquals(1L, (long) summary.getTopicCount());
		assertTrue(CollectionUtils.isNotEmpty(summary.getLanguages()));
		assertTrue(CollectionUtils.isNotEmpty(summary.getTopics()));
	}

	private String buildUriString(final int page) {
		return UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("api.bitbucket.org")
				.path("/2.0/repositories/{name}")
				.queryParam("page", page)
				.buildAndExpand(NAME)
				.toUriString();
	}

	private Page<Repository> buildRepositoryResponse(final int pageNumber, final int pagelen, final int size) {
		Page<Repository> repositoryPage = new Page<>();
		repositoryPage.setPageNumber(pageNumber);
		repositoryPage.setPagelen(pagelen);
		repositoryPage.setSize(size);

		List<Repository> values = new ArrayList<>();
		for (int i = 0; i < pagelen; i ++) {
			Repository repository = new Repository();
			repository.setLanguage("Java");
			repository.setDescription("Coding Challenge");

			Repository.Links.Link watcherLink = new Repository.Links.Link(WATCHER_LINK);
			Repository.Links.Link forkLink = new Repository.Links.Link(FORK_LINK);

			repository.setLinks(new Repository.Links(watcherLink, forkLink));

			values.add(repository);
		}

		repositoryPage.setValues(values);

		return repositoryPage;
	}

	private Page<Repository> buildEmptyRepositoryResponse(final int page) {
		Page<Repository> repositoryPage = new Page<>();
		repositoryPage.setPageNumber(page);
		repositoryPage.setPagelen(0);
		repositoryPage.setSize(0);
		repositoryPage.setValues(new ArrayList<>());

		return repositoryPage;
	}

	private CompletableFuture<Page<Object>> buildDeepLinkedResponse(final int size) {
		Page<Object> deepLinkedPage = new Page<>();
		deepLinkedPage.setPageNumber(1);
		deepLinkedPage.setSize(size);

		return CompletableFuture.completedFuture(deepLinkedPage);
	}
}
