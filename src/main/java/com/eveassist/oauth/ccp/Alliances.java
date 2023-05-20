package com.eveassist.oauth.ccp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public record Alliances(List<Identifier> alliances) {
}
