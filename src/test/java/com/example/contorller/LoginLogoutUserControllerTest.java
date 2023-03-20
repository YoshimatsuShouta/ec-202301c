package com.example.contorller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class LoginLogoutUserControllerTest {

	@InjectMocks
	private LoginLogoutUserController controller;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void ログインページ遷移のテスト() throws Exception {
		// パラメーターにerrorが含まれない場合のテスト
		mockMvc.perform(get("/Login/toLogin")).andExpect(status().isOk()) // getしたパスの遷移先が存在するか確認
				.andExpect(view().name("/materialize-version/login.html")) // toLoginから返されるhtmlの確認
				.andExpect(model().attributeDoesNotExist("errorMessage"));// model内にエラーメッセージが存在するか確認 DoesNotで無いことが正の意味

		// パラメーターにerrorが含まれる場合のテスト
		mockMvc.perform(get("/Login/toLogin?error=true")).andExpect(status().isOk())// getしたパスの遷移先が存在するか確認
				.andExpect(view().name("/materialize-version/login.html"))// toLoginから返されるhtmlの確認
				.andExpect(model().attributeExists("errorMessage"));// model内にエラーメッセージが存在するか確認
	}
}
