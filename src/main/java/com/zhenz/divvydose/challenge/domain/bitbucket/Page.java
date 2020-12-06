package com.zhenz.divvydose.challenge.domain.bitbucket;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
	private Integer pagelen;
	@JsonAlias("page")
	private Integer pageNumber;
	private Integer size;
	private List<T> values;
}
