package com.eveassist.oauth.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface EveAssistUserService extends UserDetailsService {
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
