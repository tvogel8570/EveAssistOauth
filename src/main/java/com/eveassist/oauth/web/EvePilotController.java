package com.eveassist.oauth.web;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("pilot")
public class EvePilotController {
	@PostMapping("new/")
	public Mono<String> postNewPilot(@RequestBody Object body) {
		System.out.println("postNewPilot");
		return Mono.just("post to pilot/new/");
	}

	@GetMapping("new/")
	public Mono<String> getNewPilot() {
		System.out.println("getNewPilot");
		return Mono.just("get to pilot/new/");
	}
}
