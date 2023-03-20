package com.example.contorller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.service.ResisterUserService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ResisterUserControllerTest {

	@InjectMocks
	private ResisterUserController controller;

	@MockBean
	private ResisterUserService userService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void 登録画面遷移のテスト() throws Exception {
		mockMvc.perform(get("/resister/toResister")).andExpect(status().isOk())
				.andExpect(view().name("/materialize-version/register_user"))
				.andExpect(model().attributeExists("resisterUserForm"));
	}

}
