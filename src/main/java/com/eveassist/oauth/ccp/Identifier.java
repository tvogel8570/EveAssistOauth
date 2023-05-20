package com.eveassist.oauth.ccp;

import lombok.Builder;

@Builder
public record Identifier(long id, String name) {
}
