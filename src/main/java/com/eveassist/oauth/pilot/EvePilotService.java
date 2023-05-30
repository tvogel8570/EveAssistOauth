package com.eveassist.oauth.pilot;

import com.eveassist.oauth.ccp.Characters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class EvePilotService {
	private final ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	private final ReactiveClientRegistrationRepository clientRegistrations;
	@Qualifier("otherWebClient")
	private final WebClient otherWebClient;
	private final ObjectMapper mapper = new ObjectMapper();
	private static final String baseUri = "https://esi.evetech.net/latest/characters";

	public EvePilotService(ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService,
			ReactiveClientRegistrationRepository clientRegistrations, WebClient otherWebClient) {
		this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
		this.clientRegistrations = clientRegistrations;
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

	public Mono<String> getNotifications(String name) {
		String dataUri = "/{id}/notifications";
		Mono<OAuth2AuthorizedClient> pilotOauth = oAuth2AuthorizedClientService.loadAuthorizedClient("eve", name);
		Mono<Long> pilotId = this.lookupPilotId(name);

		return Mono.zip(pilotId, pilotOauth).checkpoint().flatMap(tuple2 ->
				this.getPilotWebClient().get()
						.uri(uriBuilder -> uriBuilder
								.path(dataUri)
								.queryParam("datasource", "tranquility")
								.build(tuple2.getT1().toString()))
						.attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(tuple2.getT2()))
						.retrieve()
						.bodyToMono(String.class)
		);

		//		 Mono.zip(pilotId, pilotOauth, (id, oauth) ->
		//		{
		//			return this.getPilotWebClient(name).get()
		//					.uri(uriBuilder -> uriBuilder
		//							.path(dataUri)
		//							.queryParam("datasource", "tranquility")
		//							.build(id.toString()))
		//					.attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(oauth))
		//					.retrieve()
		//					.bodyToMono(String.class);
		//		});
	}

	public Mono<String> getPortraits(String name) {
		String dataUri = "/{id}/portrait";

		return this.lookupPilotId(name)
				.flatMap(p -> this.getPilotWebClient().get()
						.uri(uriBuilder -> uriBuilder
								.path(dataUri)
								.queryParam("datasource", "tranquility")
								.build(p.toString()))
						.retrieve()
						.bodyToMono(String.class)
				);
	}

	private WebClient getPilotWebClient() {
		var authorizedClients = new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(oAuth2AuthorizedClientService);
		var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrations, authorizedClients);
		return WebClient.builder()
				.baseUrl(baseUri)
				.filter(oauth)
				.build();
	}
}
