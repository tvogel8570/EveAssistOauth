package com.eveassist.oauth.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ReactiveArgumentsTest {

	@Test
	void shouldCallTwoMethodsUsingZipWith() {
		Mono<String> firstMono = Mono.just("asdf");
		Mono<String> secondMono = Mono.just("qwer");

		Mono<Mono<Object>> testMono = firstMono.zipWith(secondMono, (first, second) -> {
			singleParm(first);
			singleParm(second);
			return Mono.empty();
		});

		StepVerifier.create(testMono)
				.expectNext(Mono.empty())
				.verifyComplete();
	}

	@Test
	void shouldCallTwoMethodsUsingZip() {
		Mono<String> firstMono = Mono.just("asdf");
		Mono<String> secondMono = Mono.just("qwer");

		Mono.zip(firstMono, secondMono, (first, second) -> {
			singleParm(first);
			singleParm(second);
			return Mono.empty();
		}).subscribe();
	}

	//	private String singleParm(String parm) {
	//	private void singleParm(String parm) {
	private Mono<String> singleParm(String parm) {
		System.out.println("in method " + parm);
		return Mono.just(parm);
		//		return parm;
	}

}
