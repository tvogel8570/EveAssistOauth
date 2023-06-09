package com.eveassist.oauth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * A Projection for the {@link com.eveassist.oauth.user.entity.EveAssistUser} entity
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EveAssistUserDto {
	@Length(min = 30, max = 30)
	private String uniqueUser;
	@NotEmpty
	@Email(message = "You must enter a valid email")
	private String email;
	@NotEmpty
	private String screenName;
	private boolean loginOk;
}