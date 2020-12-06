package com.zhenz.divvydose.challenge.controller;

import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.service.BitbucketService;
import com.zhenz.divvydose.challenge.service.GitHubService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryControllerTest {

	private static final String NAME = "test_org";

	@Mock
	public GitHubService gitHubService;
	@Mock
	public BitbucketService bitbucketService;
	@InjectMocks
	public RepositoryController repositoryController;

	@Test
	public void test_gitHubOnly() throws InterruptedException, ExecutionException {
		when(gitHubService.getRepositorySummary(NAME)).thenReturn(buildRepositorySummary());
		when(bitbucketService.getRepositorySummary(NAME)).thenReturn(buildEmptyRepositorySummary());

		RepositorySummary combined = repositoryController.getRepositoryInfo(NAME);

		assertEquals(10L, (long) combined.getPublicRepoCount());
		assertEquals(10L, (long) combined.getForkRepoCount());
		assertEquals(10L, (long) combined.getWatcherCount());
		assertEquals(1L, (long) combined.getLanguageCount());
		assertEquals(1L, (long) combined.getTopicCount());
		assertTrue(CollectionUtils.isNotEmpty(combined.getLanguages()));
		assertTrue(CollectionUtils.isNotEmpty(combined.getTopics()));
	}

	@Test
	public void test_bitbucketOnly() throws InterruptedException, ExecutionException {
		when(gitHubService.getRepositorySummary(NAME)).thenReturn(buildEmptyRepositorySummary());
		when(bitbucketService.getRepositorySummary(NAME)).thenReturn(buildRepositorySummary());

		RepositorySummary combined = repositoryController.getRepositoryInfo(NAME);

		assertEquals(10L, (long) combined.getPublicRepoCount());
		assertEquals(10L, (long) combined.getForkRepoCount());
		assertEquals(10L, (long) combined.getWatcherCount());
		assertEquals(1L, (long) combined.getLanguageCount());
		assertEquals(1L, (long) combined.getTopicCount());
		assertTrue(CollectionUtils.isNotEmpty(combined.getLanguages()));
		assertTrue(CollectionUtils.isNotEmpty(combined.getTopics()));
	}

	@Test
	public void test_gitHubAndBitbucket() throws InterruptedException, ExecutionException {
		when(gitHubService.getRepositorySummary(NAME)).thenReturn(buildRepositorySummary());
		when(bitbucketService.getRepositorySummary(NAME)).thenReturn(buildRepositorySummary());

		RepositorySummary combined = repositoryController.getRepositoryInfo(NAME);

		assertEquals(20L, (long) combined.getPublicRepoCount());
		assertEquals(20L, (long) combined.getForkRepoCount());
		assertEquals(20L, (long) combined.getWatcherCount());
		assertEquals(1L, (long) combined.getLanguageCount());
		assertEquals(1L, (long) combined.getTopicCount());
		assertTrue(CollectionUtils.isNotEmpty(combined.getLanguages()));
		assertTrue(CollectionUtils.isNotEmpty(combined.getTopics()));
	}

	@Test
	public void test_gitHubAndBitbucketEmpty() throws InterruptedException, ExecutionException {
		when(gitHubService.getRepositorySummary(NAME)).thenReturn(buildEmptyRepositorySummary());
		when(bitbucketService.getRepositorySummary(NAME)).thenReturn(buildEmptyRepositorySummary());

		RepositorySummary combined = repositoryController.getRepositoryInfo(NAME);

		assertEquals(0L, (long) combined.getPublicRepoCount());
		assertEquals(0L, (long) combined.getForkRepoCount());
		assertEquals(0L, (long) combined.getWatcherCount());
		assertEquals(0L, (long) combined.getLanguageCount());
		assertEquals(0L, (long) combined.getTopicCount());
		assertTrue(CollectionUtils.isEmpty(combined.getLanguages()));
		assertTrue(CollectionUtils.isEmpty(combined.getTopics()));
	}

	private RepositorySummary buildRepositorySummary() {
		RepositorySummary summary = new RepositorySummary();
		summary.setPublicRepoCount(10L);
		summary.setForkRepoCount(10L);
		summary.setWatcherCount(10L);
		summary.setLanguageCount(1L);
		summary.setLanguages(new HashSet<>(Collections.singletonList("Java")));
		summary.setTopicCount(1L);
		summary.setTopics(new HashSet<>(Collections.singletonList("Coding Challenge")));

		return summary;
	}

	private RepositorySummary buildEmptyRepositorySummary() {
		RepositorySummary summary = new RepositorySummary();
		summary.setPublicRepoCount(0L);
		summary.setForkRepoCount(0L);
		summary.setWatcherCount(0L);
		summary.setLanguageCount(0L);
		summary.setLanguages(new HashSet<>());
		summary.setTopicCount(0L);
		summary.setTopics(new HashSet<>());

		return summary;
	}
}
