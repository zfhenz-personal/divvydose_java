package com.zhenz.divvydose.challenge.domain;

import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RepositorySummary {
	private Long publicRepoCount;
	private Long watcherCount;
	private Long languageCount;
	private Set<String> languages;
	private Long topicCount;
	private Set<String> topics;

	public RepositorySummary(final List<GitHubRepository> gitHubRepositories) {
		//todo separate by original repos vs forked repos?
		this.setPublicRepoCount((long) gitHubRepositories.size());

		this.setWatcherCount((long) gitHubRepositories
				.stream()
				.mapToInt(GitHubRepository::getWatchersCount)
				.sum());

		this.setLanguages(gitHubRepositories
				.stream()
				.map(GitHubRepository::getLanguage)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()));

		this.setTopics(gitHubRepositories
				.stream()
				.map(GitHubRepository::getTopics)
				.filter(Objects::nonNull)
				.flatMap(List::stream)
				.collect(Collectors.toSet()));

		this.setLanguageCount((long) this.getLanguages().size());
		this.setTopicCount((long) this.getTopics().size());
	}
}
