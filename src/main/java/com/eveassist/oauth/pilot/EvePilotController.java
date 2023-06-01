package com.eveassist.oauth.pilot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("pilot")
public class EvePilotController {
    private static final String baseUri = "https://esi.evetech.net/latest/characters";
    private final WebClient webClient;
    private final EvePilotService pilotService;
    private final ObjectMapper mapper = new ObjectMapper();
    @Qualifier("otherWebClient")
    private final WebClient otherWebClient;
    private final ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final ReactiveClientRegistrationRepository clientRegistrations;

    public EvePilotController(WebClient webClient, EvePilotService pilotService, WebClient otherWebClient, ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService, ReactiveClientRegistrationRepository clientRegistrations) {
        this.webClient = webClient;
        this.pilotService = pilotService;
        this.otherWebClient = otherWebClient;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;

        this.clientRegistrations = clientRegistrations;
    }

    @PostMapping("/new/")
    public Mono<String> postNewPilot(@RequestBody Object body) {
        System.out.println("postNewPilot");
        return Mono.just("post to pilot/new/");
    }

    @GetMapping("/new")
    public Mono<String> getNewPilot() {
        System.out.println("/getNewPilot");
        return Mono.just("get to pilot/new/");
    }

    @GetMapping("/{name}/id")
    public Mono<Long> getPilotId(@PathVariable("name") String name) {
        return pilotService.lookupPilotId(name);
    }

    @GetMapping("/{name}/notifications")
    public Mono<String> getPilotNotifications(@PathVariable("name") String name) {
        // https://esi.evetech.net/latest/characters/{pilotId}/notifications/?datasource=tranquility
        String dataUri = "/{id}/notifications";
        Mono<OAuth2AuthorizedClient> pilotOauth = oAuth2AuthorizedClientService.loadAuthorizedClient("eve", name);
        Mono<Long> pilotId = pilotService.lookupPilotId(name);

        return Mono.zip(pilotId, pilotOauth).checkpoint().flatMap(tuple2 ->
                this.getPilotWebClient().get()
                        .uri(uriBuilder -> uriBuilder
                                .path(dataUri)
                                .queryParam("datasource", "tranquility")
                                .build(tuple2.getT1().toString()))
                        .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(tuple2.getT2()))
                        .retrieve()
                        .bodyToMono(String.class));
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
    
    public Mono<String> test(@PathVariable("name") String name) {
        return Mono.just("received");
    }

    @GetMapping("/{name}/portraits")
    public Mono<String> getPilotPortraits(@PathVariable("name") String name) {
        //https://esi.evetech.net/latest/characters/641131593/portrait/?datasource=tranquility
        String dataUri = "/{id}/portrait";

        WebClient otherWebClient = WebClient.builder().baseUrl(baseUri).build();
        return pilotService.lookupPilotId(name)
                .flatMap(p -> otherWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path(dataUri)
                                .queryParam("datasource", "tranquility")
                                .build(p.toString()))
                        .retrieve()
                        .bodyToMono(String.class)
                );
    }


	/*
	@GetMapping("/{name}/corp-history")
	public void getCorpHistory(@PathVariable("name") String name) {
		String corpHistUri = String.format(RESOURCE_URI, String.format("/v2/characters/%s/corporationhistory/"), "2118961003");
		//		OAuth2AuthorizedClient oAuth2AuthorizedClient = this.loadPilot(name).subscribe();
		webClient.get()
				.uri(String.format(RESOURCE_URI, String.format("/v2/characters/%s/corporationhistory/"), this.lookupPilotId(name)))
				.attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(this.loadPilot(name)))
				.retrieve()
				.b
	}



		//		return this.loadPilot(name).map(client -> {
			//			Map<String, Object> properties = new HashMap<>();
			//			properties.put("name", client.getPrincipalName());
			//			properties.put("refresh", client.getRefreshToken());
			//			return properties;
			//		});

			OAuth2AuthorizedClient oAuth2AuthorizedClient = this.loadPilot(name).subscribe();
			String corpHistUri = String.format(RESOURCE_URI, String.format("/v2/characters/%s/corporationhistory/"), "2118961003");
			return webClient.get().uri(corpHistUri)
					.attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(oAuth2AuthorizedClient))
					.retrieve()
					.bodyToMono(String.class);
		}


		private void lookupPilotCcpId(String name) {
			otherWebClient.post().uri("").bodyValue()
		}
	@GetMapping("/{name}/corp-history")
	public Mono<String> pilotCorpHistory(@PathVariable String name) {
*/

}
