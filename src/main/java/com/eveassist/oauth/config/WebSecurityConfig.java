package com.eveassist.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.R2dbcReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig {
	private final ServerAuthenticationSuccessHandler loginSuccessHandler;

	public WebSecurityConfig(ServerAuthenticationSuccessHandler loginSuccessHandler) {
		this.loginSuccessHandler = loginSuccessHandler;
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.authorizeExchange()
				.anyExchange()
				.authenticated()
				.and()
				//				.oauth2Login(Customizer.withDefaults())
				.oauth2Login()
				.authenticationSuccessHandler(loginSuccessHandler)
		//				.and()
		//				.addFilterAfter(rememberMeAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
		;

		return http.build();
	}

	//	@Bean
	public ReactiveOAuth2AuthorizedClientService dbOauth2AuthorizedClientService(DatabaseClient databaseClient,
			ReactiveClientRegistrationRepository clientRegistrationRepository) {
		return new R2dbcReactiveOAuth2AuthorizedClientService(databaseClient, clientRegistrationRepository);
	}
}
