package com.eveassist.oauth.user;

import com.eveassist.oauth.user.impl.EveAssistUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig
class EveAssistUserServiceTest {
	@Configuration
	@ComponentScan(basePackages = { "com.eveassist.oauth.user" },
			includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
					classes = { EveAssistUserServiceImpl.class, }), useDefaultFilters = false)
	@ImportAutoConfiguration(ValidationAutoConfiguration.class)
	static class ConfigMe {
	}

	@MockBean
	private EveAssistUserRepository repo;
	@Autowired
	private EveAssistUserServiceImpl userService;

	@Test
	void contextLoads() {
	}
}