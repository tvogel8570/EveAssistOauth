package com.eveassist.oauth.user.mapper;

import com.eveassist.oauth.user.dto.EveAssistUserDto;
import com.eveassist.oauth.user.dto.EveAssistUserListDto;
import com.eveassist.oauth.user.entity.EveAssistUser;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EveAssistUserMapper {
	EveAssistUser toEntity(EveAssistUserDto eveAssistUserDto);

	EveAssistUserDto toUserDto(EveAssistUser eveAssistUser);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	EveAssistUser partialUpdate(
			EveAssistUserDto eveAssistUserDto, @MappingTarget EveAssistUser eveAssistUser);

	EveAssistUserListDto toUserListDto(EveAssistUser eveAssistUser);

	EveAssistUser toEntity(EveAssistUserListDto eveAssistUserListDto);

	EveAssistUser toEntity1(EveAssistUserDto eveAssistUserDto);
}