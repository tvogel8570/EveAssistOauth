package com.eveassist.oauth.user;

import com.eveassist.oauth.user.dto.EveAssistUserListDto;
import com.eveassist.oauth.user.entity.EveAssistUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EveAssistUserRepository extends ReactiveCrudRepository<EveAssistUser, Long> {
	Mono<EveAssistUser> findByEmailIgnoreCase(String email);

	Mono<EveAssistUser> findByUniqueUser(String uniqueUser);

	@Query("select e.unique_user, e.email, e.screen_name from eve_assist_user e")
	Flux<EveAssistUserListDto> getAllUsersList();

	@Query("select e.unique_user, e.email, e.screen_name from eve_assist_user e")
	Flux<EveAssistUserListDto> getUserList();
}
