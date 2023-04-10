package com.eveassist.oauth.user;

import com.eveassist.oauth.user.dto.EveAssistUserDto;
import com.eveassist.oauth.user.dto.EveAssistUserListDto;
import com.eveassist.oauth.user.entity.EveAssistUser;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
	TestEntityManager testEntityManager;
	@Autowired
	EveAssistUserRepository repository;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldRetrieveCorrectTime() {
		LocalDateTime testTime = LocalDateTime.now();
		EveAssistUser eveAssistUser = EveAssistUser.builder()
				.uniqueUser("123456789012345678901234567890")
				.screenName("me")
				.createTimestamp(testTime)
				.updateTimestamp(testTime)
				.email("tim@test.com").build();
		EveAssistUser retrievedUser = testEntityManager.persistFlushFind(eveAssistUser);
		assertThat(retrievedUser.getCreateTimestamp()).isEqualToIgnoringNanos(eveAssistUser.getCreateTimestamp());
	}

	@Test
	void shouldReturnListOfInterface() {
		LocalDateTime testTime = LocalDateTime.now();
		EveAssistUser eveAssistUser = EveAssistUser.builder()
				.uniqueUser("123456789012345678901234567890")
				.screenName("me")
				.createTimestamp(testTime)
				.updateTimestamp(testTime)
				.email("tim@test.com").build();
		EveAssistUser retrievedUser = testEntityManager.persistAndFlush(eveAssistUser);
		assertThat(retrievedUser).isNotNull();
		eveAssistUser = EveAssistUser.builder()
				.uniqueUser("123456789012345678901234567891")
				.screenName("me1")
				.createTimestamp(testTime)
				.updateTimestamp(testTime)
				.email("tim1@test.com").build();
		retrievedUser = testEntityManager.persistAndFlush(eveAssistUser);
		assertThat(retrievedUser).isNotNull();
		List<EveAssistUserListDto> userList = repository.getUserList();
		assertThat(userList).isNotNull();
		assertThat(userList.size()).isEqualTo(2);
	}

	@Test
	void shouldReturnUserDto() {
		LocalDateTime testTime = LocalDateTime.now();
		EveAssistUser eveAssistUser = EveAssistUser.builder()
				.uniqueUser("123456789012345678901234567890")
				.screenName("me")
				.createTimestamp(testTime)
				.updateTimestamp(testTime)
				.email("tim@test.com").build();
		EveAssistUser retrievedUser = testEntityManager.persistFlushFind(eveAssistUser);
		EveAssistUserDto userDto = repository.findUserDto("tim@test.com");
		assertThat(userDto).isNotNull();
		assertThat(retrievedUser.getEmail()).isEqualTo(userDto.getEmail());
	}

	@Test
	void shouldFindEmailCaseInsensitive() {
		LocalDateTime testTime = LocalDateTime.now();
		EveAssistUser eveAssistUser = EveAssistUser.builder()
				.uniqueUser("123456789012345678901234567890")
				.screenName("me")
				.createTimestamp(testTime)
				.updateTimestamp(testTime)
				.email("tim@test.com").build();
		EveAssistUser retrievedUser = testEntityManager.persistAndFlush(eveAssistUser);
		EveAssistUser user = repository.findByEmailIgnoreCase("TIM@TEST.com");
		assertThat(user).isNotNull();
	}
}