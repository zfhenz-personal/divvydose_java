package com.zhenz.divvydose.challenge.service;

import com.zhenz.divvydose.challenge.client.RestClient;
import com.zhenz.divvydose.challenge.domain.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubService {

	private final RestClient restClient;

	@Autowired
	public GitHubService(final RestClient restClient) {
		this.restClient = restClient;
	}

	public List<GitHubRepository> getRepositoryInfo(final String name) {
		return restClient.get("https://api.github.com/orgs/" + name + "/repos", new ParameterizedTypeReference<>() {});
	}
}
