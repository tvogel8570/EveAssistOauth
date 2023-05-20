package com.eveassist.oauth.json;

import com.eveassist.oauth.ccp.Alliances;
import com.eveassist.oauth.ccp.Characters;
import com.eveassist.oauth.ccp.Identifier;
import com.eveassist.oauth.ccp.UniverseIds;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CharactersTest {

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	void shouldConvertStringToIdentifier() throws JsonProcessingException {
		String start = """
				{
				      "id": 2118961003,
				      "name": "Where Am I"
				    }
				""";
		Identifier identifier = mapper.readValue(start, Identifier.class);
		assertThat(identifier).isNotNull();
	}

	@Test
	void shouldConvertStringToCharacters() throws JsonProcessingException {
		String start = """
				 {
				"characters": [
				      {
				        "id": 2118961003,
				        "name": "Where Am I"
				      },
				      {
				        "id": 641131593,
				        "name": "Cass Tamuri"
				      }
				    ]
				  }""";
		Characters result = mapper.readValue(start, Characters.class);
		assertThat(result).isNotNull();
	}

	@Test
	void shouldConvertStringToAlliances() throws JsonProcessingException {
		String start = """
				    {
				      "ALLiances": [
				         {
				           "id": 588312332,
				           "name": "Nebula Rasa"
				         },
				         {
				           "id": 99004344,
				           "name": "Hole Control"
				         }
				       ]
				     }
				""";
		Alliances result = mapper.readValue(start, Alliances.class);
		assertThat(result).isNotNull();
	}

	@Test
	void shouldConvertStringArrayToCharacters() throws JsonProcessingException {
		String start = """
				{
				   "Alliances": [
				     {
				       "id": 588312332,
				       "name": "Nebula Rasa"
				     },
				     {
				       "id": 99004344,
				       "name": "Hole Control"
				     }
				   ],
				   "Characters": [
				     {
				       "id": 2118961003,
				       "name": "Where Am I"
				     },
				     {
				       "id": 641131593,
				       "name": "Cass Tamuri"
				     }
				   ]
				 }""";
		UniverseIds result = mapper.readValue(start, UniverseIds.class);
		assertThat(result).isNotNull();
	}

}
