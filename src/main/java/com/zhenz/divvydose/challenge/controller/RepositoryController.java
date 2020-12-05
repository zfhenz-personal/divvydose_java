package com.zhenz.divvydose.challenge.controller;

import com.zhenz.divvydose.challenge.domain.github.Repository;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
import com.zhenz.divvydose.challenge.domain.bitbucket.RepositoryPage;
import com.zhenz.divvydose.challenge.service.BitBucketService;
import com.zhenz.divvydose.challenge.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("repository")
public class RepositoryController {

	private final GitHubService gitHubService;
	private final BitBucketService bitBucketService;

	public RepositoryController(final GitHubService gitHubService,
								final BitBucketService bitBucketService) {
		this.gitHubService = gitHubService;
		this.bitBucketService = bitBucketService;
	}

	@GetMapping()
	public void test() {
		log.info("test");
	}

	@GetMapping("/info/{name}")
	public RepositorySummary getRepositoryInfo(@PathVariable String name) {
		List<Repository> gitHubRepositories = gitHubService.getRepositoryInfo(name);
		RepositoryPage bitBucketRepositoryPage = bitBucketService.getRepositoryInfo(name);

		RepositorySummary summary = new RepositorySummary(gitHubRepositories, bitBucketRepositoryPage);


		return summary;
	}
}
