package com.eveassist.oauth.stream;

import com.eveassist.oauth.ccp.Characters;
import com.eveassist.oauth.ccp.Identifier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;

public class CharacterStreamTest {
	private final String name = "asdf";
	private Characters chars;

	@BeforeEach
	void testSetup() {
		chars = new Characters(List.of(
				Identifier.builder().id(1L).name(name).build(),
				Identifier.builder().id(2L).name("asdfqwer").build())
		);
	}

	@Test
	void shouldFilterToMatchingName() {
		Long id = Mono.just(chars)
				.filter(characters -> characters.characters().stream().anyMatch(character -> character.name().equals(name)))
				.map(characters -> characters.characters().get(0).id()).block();

		Assertions.assertThat(id).isEqualTo(1L);
	}

	@Test
	void shouldThrowWhat() {
		Long id = Mono.just(chars)
				.filter(characters -> characters.characters().stream()
						.anyMatch(character -> character.name().equals("qwerty")))
				.map(characters -> characters.characters().get(0).id())
				.block();

		Assertions.assertThat(id).isNull();
	}
}
