package com.eveassist.oauth.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ClientRestController {

	private static String RESOURCE_URI = "https://esi.evetech.net/latest/%s/?datasource=tranquility";
	private final WebClient webClient;
	@Qualifier("otherWebClient")
	private final WebClient otherWebClient;

	public ClientRestController(WebClient webClient, WebClient otherWebClient) {
		this.webClient = webClient;
		this.otherWebClient = otherWebClient;
	}

	//	@GetMapping("/")
	//	Mono<String> homePage() {
	//		return Mono.just("Success");
	//	}

	@GetMapping("/")
	public Mono<String> index(@AuthenticationPrincipal Mono<OAuth2User> oauth2User) {
		return oauth2User
				.map(OAuth2User::getName)
				.map(name -> String.format("Hi, %s", name));
	}

	@GetMapping("/eve/public-info")
	Mono<String> retrievePublicInfo() {
		Mono<String> retrievedResource = webClient.get()
				.uri(String.format(RESOURCE_URI, "characters/2118961003"))
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(string -> "We retrieved the following resource using Oauth:\n" + string);
	}

	@GetMapping("/eve/corp-history")
	Mono<String> retrieveCorpHistory() {
		Mono<String> retrievedResource = webClient.get()
				.uri(String.format(RESOURCE_URI, "characters/2118961003/corporationhistory"))
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(string -> "We retrieved the following resource using Oauth:\n" + string);
	}

	@GetMapping("/eve/notifications")
	Mono<String> retrieveNotifications() {
		Mono<String> retrievedResource = webClient.get()
				.uri(String.format(RESOURCE_URI, "characters/2118961003/notifications"))
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(string -> "We retrieved the following resource using Oauth:\n" + string);
	}

	@GetMapping("/eve/kill-mails")
	Mono<String> retrieveKillMails() {
		Mono<String> retrievedResource = webClient.get()
				.uri(String.format(RESOURCE_URI, "/characters/2118961003/killmails/recent/"))
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(string -> "We retrieved the following resource using Oauth:\n" + string);
	}

	@GetMapping("/auth-code")
	Mono<String> useOauthWithAuthCode() {
		Mono<String> retrievedResource = webClient.get()
				.uri(RESOURCE_URI)
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(string -> "We retrieved the following resource using Oauth: " + string);
	}

	@GetMapping("/auth-code-no-client")
	Mono<String> useOauthWithNoClient() {
		// This call will fail, since we don't have the client properly set for this webClient
		Mono<String> retrievedResource = otherWebClient.get()
				.uri(RESOURCE_URI)
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(string -> "We retrieved the following resource using Oauth: " + string);
	}

	@GetMapping("/auth-code-annotated")
	Mono<String> useOauthWithAuthCodeAndAnnotation(
			@RegisteredOAuth2AuthorizedClient("bael") OAuth2AuthorizedClient authorizedClient) {
		Mono<String> retrievedResource = otherWebClient.get()
				.uri(RESOURCE_URI)
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(
				string -> "We retrieved the following resource using Oauth: " + string + ". Principal associated: "
						+ authorizedClient.getPrincipalName() + ". Token will expire at: " + authorizedClient.getAccessToken()
						.getExpiresAt());
	}

	@GetMapping("/auth-code-explicit-client")
	Mono<String> useOauthWithExpicitClient() {
		Mono<String> retrievedResource = otherWebClient.get()
				.uri(RESOURCE_URI)
				.attributes(clientRegistrationId("bael"))
				.retrieve()
				.bodyToMono(String.class);
		return retrievedResource.map(string -> "We retrieved the following resource using Oauth: " + string);
	}
}
