package com.eveassist.oauth.user;

import com.eveassist.oauth.user.dto.EveAssistUserListDto;
import com.eveassist.oauth.user.entity.EveAssistUser;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@DataR2dbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EveAssistUserRepositoryTest {
	@Container
	static PostgreSQLContainer container = new PostgreSQLContainer("postgres:13.3")
			.withDatabaseName("test")
			.withUsername("postgres")
			.withPassword("pass");

	@DynamicPropertySource
	static void properties(@NotNull DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", container::getUsername);
		registry.add("spring.datasource.password", container::getPassword);
	}

	@Autowired
	EveAssistUserRepository repository;
	LocalDateTime testTime = LocalDateTime.now();

	@BeforeEach
	void setup() {
		EveAssistUser eveAssistUser = EveAssistUser.builder()
				.uniqueUser("123456789012345678901234567890")
				.screenName("me")
				.createTimestamp(testTime)
				.updateTimestamp(testTime)
				.email("tim@test.com").build();
		repository.save(eveAssistUser);
		eveAssistUser = EveAssistUser.builder()
				.uniqueUser("123456789012345678901234567891")
				.screenName("me1")
				.createTimestamp(testTime)
				.updateTimestamp(testTime)
				.email("tim1@test.com").build();
		repository.save(eveAssistUser);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void shouldRetrieveCorrectTime() {
		@Length(min = 30, max = 30) @NotEmpty String uniqueUser = "123456789012345678901234567890";
		Mono<EveAssistUser> retrievedUser = repository.findByUniqueUser(uniqueUser);
		StepVerifier.create(retrievedUser)
				.expectNextMatches(t -> t.getUniqueUser().equals(uniqueUser))
				.expectNextMatches(t -> t.getCreateTimestamp().isEqual(testTime));
	}

	@Test
	void shouldReturnListOfAllUsers() {
		Flux<EveAssistUserListDto> allUserInfo = repository.getAllUsersList();
		StepVerifier.create(allUserInfo)
				.expectNextCount(2)
				.expectComplete();
	}

/*
	@Test
	void shouldReturnUserDto() {

		EveAssistUser retrievedUser = testEntityManager.persistFlushFind(eveAssistUser);
		EveAssistUserListDto userInfo = repository.getUserList();
		assertThat(userInfo).isNotNull();
		assertThat(userInfo.);
	}
*/

	@Test
	void shouldFindEmailCaseInsensitive() {

		Mono<EveAssistUser> user = repository.findByEmailIgnoreCase("TIM@TEST.com");
		StepVerifier.create(user)
				.expectNextCount(1)
				.expectComplete();

	}

/*
	@Test
	void shouldThrowUserNotFoundException_whenSearchForEmailFails() {
		EveAssistUser missingUser = repository.findByEmailIgnoreCase("asdf@asdf.com");
		assertThat(missingUser).isNull();
	}
	*/
}