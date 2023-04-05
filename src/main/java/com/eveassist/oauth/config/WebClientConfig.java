package com.eveassist.oauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
	Logger log = LoggerFactory.getLogger(WebClientConfig.class);

	@Bean
	@Primary
	WebClient webClientForAuthorized(ReactiveClientRegistrationRepository clientRegistrations,
			ServerOAuth2AuthorizedClientRepository authorizedClients) {
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrations, authorizedClients);
		oauth.setDefaultOAuth2AuthorizedClient(true);
		return WebClient.builder()
				//				.filter(oauth)
				.filters(exchangeFilterFunctions -> {
					exchangeFilterFunctions.add(oauth);
					exchangeFilterFunctions.add(logRequest());
				})
				.build();
	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			if (log.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder("Request: \n");
				//append clientRequest method and url
				clientRequest
						.headers()
						.forEach((name, values) -> values.forEach(value -> sb.append(name).append("=").append(value)));
				log.debug(sb.toString());
			}
			return Mono.just(clientRequest);
		});
	}

	@Bean
	WebClient otherWebClient(ReactiveClientRegistrationRepository clientRegistrations,
			ServerOAuth2AuthorizedClientRepository authorizedClients) {
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrations, authorizedClients);
		return WebClient.builder()
				.filter(oauth)
				.build();
	}
}
