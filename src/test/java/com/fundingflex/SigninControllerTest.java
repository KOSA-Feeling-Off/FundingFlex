package com.fundingflex;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SigninControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	private String validToken;
	
	@BeforeEach
	public void setUp() {
		//토큰을 생성하거나 유효한 토큰을 설정하는 로직 
			validToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InRlc3QxMEBuYXZlci5jb20iLCJyb2xlIjoiVVNFUiIsImlhdCI6MTcxOTU2NDQzNiwiZXhwIjoxNzE5NTY4MDM2fQ.QVg_XGfr16WeS8xidQ5oR79cKVkbJO7_HqnS95XpN1l2Vnq84Js6hRzWtfh1A_QWoAkymVAKBm8rnmQWpPP5Tg";
	}
	
	@Test
	@WithMockUser
	public void testJwt_withValidToken() throws Exception{
		mockMvc.perform(get("/api/test_jwt")
                .header("Authorization", validToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("user10@naver.com"));
	}
}
