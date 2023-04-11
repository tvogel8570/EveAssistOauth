package com.eveassist.oauth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A Projection for the {@link com.eveassist.oauth.user.entity.EveAssistUser} entity
 */
@Getter
@AllArgsConstructor
public class EveAssistUserListDto {
	private String uniqueUser;
	private String email;
	private String screenName;
}