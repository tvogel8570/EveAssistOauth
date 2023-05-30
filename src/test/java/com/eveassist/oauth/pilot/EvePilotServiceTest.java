package com.eveassist.oauth.pilot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EvePilotServiceTest {

	@Autowired
	EvePilotService pilotService;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldLookupPilotIdFromCcpGivenPilotName() {
		Long whereAmI = pilotService.lookupPilotId("Where Am I").block();
		assertThat(whereAmI).isEqualTo(2118961003L);
	}

	@Test
	void shouldReturnPortraits() {
		String portraits = pilotService.getPortraits("Where Am I").block();
		assertThat(portraits).isNotNull();
		assertThat(portraits).contains("2118961003");
	}

	@Test
	void shouldReturnNotifications() {
		String notifications = pilotService.getNotifications("Where Am I").block();
		assertThat(notifications).isNotNull();
	}

}
