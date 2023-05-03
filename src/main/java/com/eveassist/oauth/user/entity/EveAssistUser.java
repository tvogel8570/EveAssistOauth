package com.eveassist.oauth.user.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "eve_assist_user")
public class EveAssistUser implements Serializable, UserDetails {
	@Serial
	private static final long serialVersionUID = 4756246154027625809L;
	@Id
	private Long id;
	@Length(min = 30, max = 30)
	@NotEmpty
	@Column
	private String uniqueUser;
	@NotEmpty
	@Email(message = "You must enter a valid email")
	@Column
	private String email;
	@NotEmpty
	@Column
	private String screenName;
	@Column
	private String password;
	@CreatedDate
	private LocalDateTime createTimestamp;
	@LastModifiedDate
	private LocalDateTime updateTimestamp;
	@Column
	boolean accountNonExpired;
	@Column
	boolean accountNonLocked;
	@Column
	boolean credentialsNonExpired;
	@Column
	boolean enabled;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		EveAssistUser that = (EveAssistUser) o;

		return Objects.equals(uniqueUser, that.uniqueUser);
	}

	@Override
	public int hashCode() {
		return uniqueUser != null ? uniqueUser.hashCode() : 0;
	}

	// TODO hardcoded to have USER
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		GrantedAuthority authority = new SimpleGrantedAuthority("USER");
		return Set.of(authority);
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
