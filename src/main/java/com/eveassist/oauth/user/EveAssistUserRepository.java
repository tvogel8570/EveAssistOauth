package com.eveassist.oauth.user;

import com.eveassist.oauth.user.dto.EveAssistUserListDto;
import com.eveassist.oauth.user.entity.EveAssistUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EveAssistUserRepository extends JpaRepository<EveAssistUser, Long> {
	EveAssistUser findByEmailIgnoreCase(String email);

	EveAssistUser findByUniqueUser(String uniqueUser);

	@Query("select new com.eveassist.oauth.user.dto.EveAssistUserListDto(e.uniqueUser, e.email, e.screenName) from EveAssistUser e")
	List<EveAssistUserListDto> getAllUsersList();

	@Query("select new com.eveassist.oauth.user.dto.EveAssistUserListDto(e.uniqueUser, e.email, e.screenName) from EveAssistUser e")
	EveAssistUserListDto getUserList();
}
