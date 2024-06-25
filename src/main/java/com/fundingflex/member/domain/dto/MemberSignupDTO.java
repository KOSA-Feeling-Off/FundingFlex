package com.fundingflex.member.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSignupDTO {
	private Long userId;
    private String email;
    private String nickname;
    private String password;
    private String profileUrl;
    private String createdBy;
    private String updatedBy;
    private List<String> roles;
}
