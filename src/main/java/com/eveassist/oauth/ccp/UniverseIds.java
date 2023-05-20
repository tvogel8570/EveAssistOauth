package com.eveassist.oauth.ccp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UniverseIds(Alliances alliances, Characters characters) {
}
