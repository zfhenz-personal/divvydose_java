package com.zhenz.divvydose.challenge.domain.bitbucket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Repository {
	private Links links;
	private String language;
	private String description;

	@Data
	@AllArgsConstructor
	public static class Links {
		private Link watchers;
		private Link forks;

		@Data
		@AllArgsConstructor
		public static class Link {
			private String href;
		}
	}
}
