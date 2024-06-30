package com.fundingflex.member.domain.dto;

import lombok.Data;

@Data
public class MemberSigninRequest {
	private String email;
    private String password;
}
