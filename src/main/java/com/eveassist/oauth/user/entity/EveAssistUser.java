package com.eveassist.oauth.user.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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
@Entity
@Table(name = "eve_assist_user", uniqueConstraints = {
		@UniqueConstraint(name = "uc_eveassistuser_unique_user", columnNames = { "unique_user" }),
		@UniqueConstraint(name = "uc_eveassistuser_email", columnNames = { "email" }),
		@UniqueConstraint(name = "uc_eveassistuser_screen_name", columnNames = { "screen_name" })
})
public class EveAssistUser implements Serializable, UserDetails {
	@Serial
	private static final long serialVersionUID = 4756246154027625809L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eve_assist_user_gen")
	@SequenceGenerator(name = "eve_assist_user_gen", sequenceName = "eve_assist_user_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;
	@NaturalId
	@Length(min = 30, max = 30)
	@Column(name = "unique_user", nullable = false, unique = true, length = 30)
	private String uniqueUser;
	@NotEmpty
	@Email(message = "You must enter a valid email")
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	@NotEmpty
	@Column(name = "screen_name", nullable = false, unique = true, length = 50)
	private String screenName;
	@Column(name = "password")
	private String password;
	@PastOrPresent
	@NotNull
	@Column(name = "create_timestamp", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime createTimestamp;
	@PastOrPresent
	@NotNull
	@Column(name = "update_timestamp", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
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
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		EveAssistUser that = (EveAssistUser) o;
		return getUniqueUser() != null && Objects.equals(getUniqueUser(), that.getUniqueUser());
	}

	@Override
	public int hashCode() {
		return Objects.hash(uniqueUser);
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
