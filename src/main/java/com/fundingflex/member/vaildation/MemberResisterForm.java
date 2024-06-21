package com.fundingflex.member.vaildation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResisterForm {
	@Size(min = 10, max = 40)
	@NotEmpty(message = "사용자 이메일은 필수 항목입니다.")
	private String email;
	
	@NotEmpty(message = "닉네임은 필수 항목입니다.")
	private String nickname;
	
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String password1;
	
	@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
	private String password2;
}
