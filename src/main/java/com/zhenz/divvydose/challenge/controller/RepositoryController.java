package com.zhenz.divvydose.challenge.controller;

import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.service.BitbucketService;
import com.zhenz.divvydose.challenge.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("repository")
public class RepositoryController {

	private final GitHubService gitHubService;
	private final BitbucketService bitBucketService;

	public RepositoryController(final GitHubService gitHubService,
								final BitbucketService bitBucketService) {
		this.gitHubService = gitHubService;
		this.bitBucketService = bitBucketService;
	}

	@GetMapping("/overview/{name}")
	public RepositorySummary getRepositoryInfo(@PathVariable String name) {
		RepositorySummary gitHubSummary = gitHubService.getRepositorySummary(name);
		RepositorySummary bitbucketSummary = bitBucketService.getRepositorySummary(name);

		return gitHubSummary.combine(bitbucketSummary);
	}
}
