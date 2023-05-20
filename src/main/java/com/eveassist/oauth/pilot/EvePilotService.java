package com.eveassist.oauth.pilot;

import com.eveassist.oauth.ccp.Characters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class EvePilotService {
	private final ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	@Qualifier("otherWebClient")
	private final WebClient otherWebClient;
	private final ObjectMapper mapper = new ObjectMapper();

	public EvePilotService(ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService, WebClient otherWebClient) {
		this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
		this.otherWebClient = otherWebClient;
	}

	public Mono<Long> lookupPilotId(String name) {
		try {
			return otherWebClient.post()
					.uri("https://esi.evetech.net/latest/universe/ids/?datasource=tranquility&language=en")
					.bodyValue(mapper.writeValueAsString(List.of(name)))
					.retrieve()
					.bodyToMono(Characters.class)
					.filter(characters -> characters.characters().stream()
							.anyMatch(character -> character.name().equalsIgnoreCase(name)))
					.map(characters -> characters.characters().get(0).id())
					.defaultIfEmpty(-1L);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public Mono<OAuth2AuthorizedClient> loadPilot(String name) {
		return oAuth2AuthorizedClientService.loadAuthorizedClient("eve", name);
	}
}
