package com.zhenz.divvydose.challenge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("repository")
public class RepositoryController {

	@GetMapping("/info/{name}")
	public void getRepositoryInfo(@PathVariable String name) {
		log.info(name);
	}
}
