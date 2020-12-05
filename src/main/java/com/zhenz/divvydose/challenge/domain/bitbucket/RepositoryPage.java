package com.zhenz.divvydose.challenge.domain.bitbucket;

import lombok.Data;

import java.util.List;

@Data
public class RepositoryPage {
	private Integer pagelen;
	private Integer page;
	private Integer size;
	private List<Repository> values;

	@Data
	private static class Repository {
		private Links links;

		@Data
		private static class Links {
			private Link watchers;

			@Data
			private static class Link {
				private String href;
			}
		}
	}
}
