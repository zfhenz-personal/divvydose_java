package com.zhenz.divvydose.challenge.domain;

import com.zhenz.divvydose.challenge.domain.bitbucket.RepositoryPage;
import com.zhenz.divvydose.challenge.domain.github.Repository;
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

	public RepositorySummary(final List<Repository> gitHubRepositories, final RepositoryPage bitbucketRepositoryPage) {
		//todo separate by original repos vs forked repos?
		this.setPublicRepoCount((long) gitHubRepositories.size());
		this.setPublicRepoCount(this.getPublicRepoCount() + bitbucketRepositoryPage.getSize());

		this.setWatcherCount((long) gitHubRepositories
				.stream()
				.mapToInt(Repository::getWatchersCount)
				.sum());

		this.setLanguages(gitHubRepositories
				.stream()
				.map(Repository::getLanguage)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()));

		this.setTopics(gitHubRepositories
				.stream()
				.map(Repository::getTopics)
				.filter(Objects::nonNull)
				.flatMap(List::stream)
				.collect(Collectors.toSet()));

		this.setLanguageCount((long) this.getLanguages().size());
		this.setTopicCount((long) this.getTopics().size());
	}
}
