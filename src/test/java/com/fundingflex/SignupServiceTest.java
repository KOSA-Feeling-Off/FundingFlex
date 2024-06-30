package com.fundingflex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fundingflex.member.domain.entity.Members;
import com.fundingflex.member.domain.form.MemberResisterForm;
import com.fundingflex.member.service.SignupService;

@SpringBootTest
public class SignupServiceTest {
	@Autowired
	private SignupService signupService;
	
	@Test
    public void testSignup() {
        MemberResisterForm form = new MemberResisterForm();
        form.setEmail("test@example.com");
        form.setNickname("testuser");
        form.setPassword1("password");
        form.setPassword2("password");

        Members result = signupService.signup(form);
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }
	
}
