package com.zhenz.divvydose.challenge.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepository {
	private String id;
	@JsonAlias("watchers_count")
	private Integer watchersCount;
	private String language;
	private List<String> topics;
}
