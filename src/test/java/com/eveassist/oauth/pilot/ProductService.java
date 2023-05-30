package com.eveassist.oauth.pilot;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ProductService {
	private final ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	private final ReactiveClientRegistrationRepository clientRegistrations;
	private static final String baseUri = "https://myapp.net/product";

	public ProductService(ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService,
			ReactiveClientRegistrationRepository clientRegistrations) {
		this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
		this.clientRegistrations = clientRegistrations;
	}

	public Mono<String> getNotifications(String productName, String userName) {
		String dataUri = "/{id}/notifications";
		Mono<OAuth2AuthorizedClient> userOauth = oAuth2AuthorizedClientService.loadAuthorizedClient("xxx", userName);
		Mono<Long> productId = this.lookupProductId(productName);

		return Mono.zip(productId, userOauth).checkpoint().flatMap(tuple2 ->
				this.getUserWebClient().get()
						.uri(uriBuilder -> uriBuilder
								.path(dataUri)
								.queryParam("datasource", "development")
								.build(tuple2.getT1().toString()))
						.attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(tuple2.getT2()))
						.retrieve()
						.bodyToMono(String.class)
		);
	}

	private WebClient getUserWebClient() {
		var authorizedClients = new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(oAuth2AuthorizedClientService);
		var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrations, authorizedClients);
		return WebClient.builder()
				.baseUrl(baseUri)
				.filter(oauth)
				.build();
	}

	public Mono<Long> lookupProductId(String name) {
		// business logic to lookup product based on name
		return Mono.just(-1L);
	}
}