package com.zhenz.divvydose.challenge.controller;

import com.zhenz.divvydose.challenge.domain.GitHubRepository;
import com.zhenz.divvydose.challenge.domain.RepositorySummary;
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

	private GitHubService gitHubService;

	public RepositoryController(final GitHubService gitHubService) {
		this.gitHubService = gitHubService;
	}

	@GetMapping()
	public void test() {
		log.info("test");
	}

	@GetMapping("/info/{name}")
	public RepositorySummary getRepositoryInfo(@PathVariable String name) {
		List<GitHubRepository> gitHubRepository = gitHubService.getRepositoryInfo(name);

		RepositorySummary summary = new RepositorySummary(gitHubRepository);


		return summary;
	}
}
