package com.zhenz.divvydose.challenge.domain.github;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * taken from https://docs.github.com/en/free-pro-team@latest/rest/reference/repos
 */
public class Repository {
	private String id;
	@JsonAlias("watchers_count")
	private Integer watchersCount;
	private String language;
	private List<String> topics;
}
