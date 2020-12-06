package com.zhenz.divvydose.challenge.domain;

import com.zhenz.divvydose.challenge.domain.bitbucket.Page;
import com.zhenz.divvydose.challenge.domain.bitbucket.Repository;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RepositorySummary {
	private Long publicRepoCount;
	private Long forkRepoCount;
	private Long watcherCount;
	private Long languageCount;
	private Set<String> languages;
	private Long topicCount;
	private Set<String> topics;

	public RepositorySummary(final List<com.zhenz.divvydose.challenge.domain.github.Repository> gitHubRepositories) {
		this.setPublicRepoCount((long) gitHubRepositories.size());

		this.setForkRepoCount((long) gitHubRepositories
				.stream()
				.mapToInt(com.zhenz.divvydose.challenge.domain.github.Repository::getForksCount)
				.sum());

		this.setWatcherCount((long) gitHubRepositories
				.stream()
				.mapToInt(com.zhenz.divvydose.challenge.domain.github.Repository::getWatchersCount)
				.sum());

		this.setLanguages(gitHubRepositories
				.stream()
				.map(com.zhenz.divvydose.challenge.domain.github.Repository::getLanguage)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()));

		this.setTopics(gitHubRepositories
				.stream()
				.map(com.zhenz.divvydose.challenge.domain.github.Repository::getTopics)
				.filter(Objects::nonNull)
				.flatMap(List::stream)
				.collect(Collectors.toSet()));

		this.setLanguageCount((long) this.getLanguages().size());
		this.setTopicCount((long) this.getTopics().size());
	}

	public RepositorySummary(final Page<Repository> bitbucketRepositoryPage) {
		this.setPublicRepoCount((long) bitbucketRepositoryPage.getSize());

		this.setLanguages(bitbucketRepositoryPage.getValues()
				.stream()
				.map(Repository::getLanguage)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()));

		this.setTopics(bitbucketRepositoryPage.getValues()
				.stream()
				.map(Repository::getDescription)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()));

		this.setLanguageCount((long) this.getLanguages().size());
		this.setTopicCount((long) this.getTopics().size());
	}

	public RepositorySummary combine(final RepositorySummary alternateSummary) {
		this.setPublicRepoCount(getSafeLong(this.getPublicRepoCount()) + getSafeLong(alternateSummary.getPublicRepoCount()));
		this.setForkRepoCount(getSafeLong(this.getForkRepoCount()) + getSafeLong(alternateSummary.getForkRepoCount()));
		this.setWatcherCount(getSafeLong(this.getWatcherCount()) + getSafeLong(alternateSummary.getWatcherCount()));
		this.setLanguageCount(getSafeLong(this.getLanguageCount()) + getSafeLong(alternateSummary.getLanguageCount()));
		this.setTopicCount(getSafeLong(this.getTopicCount()) + getSafeLong(alternateSummary.getTopicCount()));
		this.getLanguages().addAll(alternateSummary.getLanguages());
		this.getTopics().addAll(alternateSummary.getTopics());

		return this;
	}

	private Long getSafeLong(Long input) {
		return input == null ? 0 : input;
	}
}
