package com.eveassist.oauth.user.dto;

/**
 * A Projection for the {@link com.eveassist.oauth.user.entity.EveAssistUser} entity
 */
public interface EveAssistUserListDto {
	String getUniqueUser();

	String getEmail();

	String getScreenName();
}