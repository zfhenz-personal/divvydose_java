package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.github.Repository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GitHubServiceTest {
	private static final String NAME = "test_org";

	@Mock
	private RestClient restClient;
	@InjectMocks
	private GitHubService gitHubService;

	@Test
	public void test_getRepositorySummary_standardResponse() {
		when(restClient.get(buildUriString(1), new ParameterizedTypeReference<List<Repository>>() {})).thenReturn(buildRepositoryResponse(3));
		when(restClient.get(buildUriString(2), new ParameterizedTypeReference<List<Repository>>() {})).thenReturn(new ArrayList<>());

		RepositorySummary summary = gitHubService.getRepositorySummary(NAME);

		assertEquals(3L, (long) summary.getPublicRepoCount());
		assertEquals(3L, (long) summary.getForkRepoCount());
		assertEquals(3L, (long) summary.getWatcherCount());
		assertEquals(1L, (long) summary.getLanguageCount());
		assertEquals(1L, (long) summary.getTopicCount());
		assertTrue(CollectionUtils.isNotEmpty(summary.getLanguages()));
		assertTrue(CollectionUtils.isNotEmpty(summary.getTopics()));
	}

	@Test
	public void test_getRepositorySummary_emptyResponse() {
		when(restClient.get(buildUriString(1), new ParameterizedTypeReference<List<Repository>>() {})).thenReturn(new ArrayList<>());

		RepositorySummary summary = gitHubService.getRepositorySummary(NAME);

		assertEquals(0L, (long) summary.getPublicRepoCount());
		assertEquals(0L, (long) summary.getForkRepoCount());
		assertEquals(0L, (long) summary.getWatcherCount());
		assertEquals(0L, (long) summary.getLanguageCount());
		assertEquals(0L, (long) summary.getTopicCount());
		assertTrue(CollectionUtils.isEmpty(summary.getLanguages()));
		assertTrue(CollectionUtils.isEmpty(summary.getTopics()));
	}

	@Test
	public void test_getRepositorySummary_multiplePages() {
		when(restClient.get(buildUriString(1), new ParameterizedTypeReference<List<Repository>>() {})).thenReturn(buildRepositoryResponse(3));
		when(restClient.get(buildUriString(2), new ParameterizedTypeReference<List<Repository>>() {})).thenReturn(buildRepositoryResponse(3));
		when(restClient.get(buildUriString(3), new ParameterizedTypeReference<List<Repository>>() {})).thenReturn(buildRepositoryResponse(3));
		when(restClient.get(buildUriString(4), new ParameterizedTypeReference<List<Repository>>() {})).thenReturn(new ArrayList<>());

		RepositorySummary summary = gitHubService.getRepositorySummary(NAME);

		assertEquals(9L, (long) summary.getPublicRepoCount());
		assertEquals(9L, (long) summary.getForkRepoCount());
		assertEquals(9L, (long) summary.getWatcherCount());
		assertEquals(1L, (long) summary.getLanguageCount());
		assertEquals(1L, (long) summary.getTopicCount());
		assertTrue(CollectionUtils.isNotEmpty(summary.getLanguages()));
		assertTrue(CollectionUtils.isNotEmpty(summary.getTopics()));
	}

	private String buildUriString(final int page) {
		return UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("api.github.com")
				.path("/orgs/{name}/repos")
				.queryParam("page", page)
				.buildAndExpand(NAME)
				.toUriString();
	}

	private List<Repository> buildRepositoryResponse(final int count) {
		List<Repository> allRepositories = new ArrayList<>();

		for (int i = 0; i < count; i ++) {
			Repository repository = new Repository();
			repository.setForksCount(i);
			repository.setWatchersCount(i);
			repository.setLanguage("Java");
			repository.setTopics(Collections.singletonList("Coding Challenge"));

			allRepositories.add(repository);
		}

		return allRepositories;
	}
}
