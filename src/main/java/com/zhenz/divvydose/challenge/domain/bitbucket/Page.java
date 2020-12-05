package com.zhenz.divvydose.challenge.domain.bitbucket;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
	private Integer pagelen;
	private Integer page;
	private Integer size;
	private List<T> values;
}
