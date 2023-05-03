package com.eveassist.oauth.user;

import com.eveassist.oauth.user.dto.EveAssistUserListDto;
import com.eveassist.oauth.user.entity.EveAssistUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;

public interface EveAssistUserRepository extends ReactiveCrudRepository<EveAssistUser, Long> {
	EveAssistUser findByEmailIgnoreCase(String email);

	EveAssistUser findByUniqueUser(String uniqueUser);

	@Query("select e.unique_user, e.email, e.screen_name from eve_assist_user e")
	List<EveAssistUserListDto> getAllUsersList();

	@Query("select e.unique_user, e.email, e.screen_name from eve_assist_user e")
	EveAssistUserListDto getUserList();
}
