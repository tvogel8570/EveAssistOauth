package com.eveassist.oauth.ccp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.util.List;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
@Builder
public record Characters(List<Identifier> characters) {
}
