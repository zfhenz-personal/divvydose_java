package com.zhenz.divvydose.challenge.domain.github;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
	private String id;
	@JsonAlias("watchers_count")
	private Integer watchersCount;
	@JsonAlias("forks_count")
	private Integer forksCount;
	private String language;
	private List<String> topics;
}
