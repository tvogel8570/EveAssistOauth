package com.eveassist.oauth.user.impl;

import com.eveassist.oauth.user.EveAssistUserRepository;
import com.eveassist.oauth.user.EveAssistUserService;
import com.eveassist.oauth.user.entity.EveAssistUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EveAssistUserServiceImpl implements UserDetailsService, EveAssistUserService {
	private final EveAssistUserRepository userRepository;

	public EveAssistUserServiceImpl(EveAssistUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @param username
	 * 		- user's email is default username. the username identifying the user whose data is required.  Case-insensitive search
	 * @return fully populated user record (never null)
	 * @throws UsernameNotFoundException
	 * 		– if the user could not be found or the user has no GrantedAuthorities
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EveAssistUser eveAssistUser = userRepository.findByEmailIgnoreCase(username);
		if (eveAssistUser == null || eveAssistUser.getAuthorities().size() == 0)
			throw new UsernameNotFoundException(String.format("user with email [%s] not found", username));
		return eveAssistUser;
	}
}
