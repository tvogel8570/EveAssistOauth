package com.eveassist.oauth.user;

import com.eveassist.oauth.user.dto.EveAssistUserDto;
import com.eveassist.oauth.user.dto.EveAssistUserListDto;
import com.eveassist.oauth.user.entity.EveAssistUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EveAssistUserRepository extends JpaRepository<EveAssistUser, Long> {
	EveAssistUser findByEmailIgnoreCase(String email);

	EveAssistUser findByUniqueUser(String uniqueUser);

	@Query("select e from EveAssistUser e")
	List<EveAssistUserListDto> getUserList();

	@Query("select e from EveAssistUser e where e.email = ?1")
	EveAssistUserDto findUserDto(String email);
}
