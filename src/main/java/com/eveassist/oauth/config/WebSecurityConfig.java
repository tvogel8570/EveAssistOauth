package com.eveassist.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
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
				.oauth2Login()
		//				.and()
		//				.addFilterAfter(rememberMeAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
		//				.authenticationSuccessHandler(loginSuccessHandler)
		;

		return http.build();
	}

}
