package com.fundingflex.member.domain.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginForm {

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password;
}