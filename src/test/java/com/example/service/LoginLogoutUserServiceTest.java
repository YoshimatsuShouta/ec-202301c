package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.repository.UserRepository;

@SpringBootTest
public class LoginLogoutUserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private LoginLogoutUserService loginLogoutUserService;

	// Captorを定義
	@Captor
	ArgumentCaptor<UserInfo> userInfoCaptor;

	@Test
	public void ログイン成功時のテスト() {
		// 仮のformを定義
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		form.setEmail("test@example.com");
		form.setPassword("testpassword");

		// 仮のDB内データを定義
		UserInfo user = new UserInfo();
		user.setEmail("test@example.com");
		user.setPassword("$2a$10$J/cGLUl5a9m5F5Kjdc5OBeJbViSyO1lHdFRfctYJcLXY24H0X3vuC"); // Encrypted "testpassword"

		// Service内のRepositoryをEncoderの挙動を定義
		Mockito.when(userRepository.findByEmail(form.getEmail())).thenReturn(user);
		Mockito.when(passwordEncoder.matches(form.getPassword(), user.getPassword())).thenReturn(true);

		// サービスを実行
		UserInfo result = loginLogoutUserService.login(form);

		// 検証
		assertNotNull(result, "ユーザー情報が正しく返されていません");
		assertEquals(user, result, "正しいユーザー情報が返されていません");
	}

	@Test
	public void ログイン失敗時のテスト() {
		// 仮のformを定義
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		form.setEmail("test@example.com");
		form.setPassword("invalidPassword");

		// 仮のDB内データを定義
		UserInfo user = new UserInfo();
		user.setName("dummy名");
		user.setEmail("test@example.com");
		user.setPassword(passwordEncoder.encode("password"));
		user.setZipcode("123-4567");
		user.setAddress("dummy県");
		user.setTelephone("123-4566-7890");

		// Service内のRepositoryをEncoderの挙動を定義
		Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
		Mockito.when(userRepository.findByEmail(form.getEmail())).thenReturn(user);

		// 実行&検証
		assertNull(loginLogoutUserService.login(form), "間違ったパスワードでログインができています");
	}

	@Test
	public void Eメール間違いのテスト() {
		// 仮のform情報を定義
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		form.setEmail("testfailed@example.com");
		form.setPassword("testpassword");

		// 仮のDB内情報を定義
		UserInfo user = new UserInfo();
		user.setEmail("test@example.com");
		user.setPassword("$2a$10$J/cGLUl5a9m5F5Kjdc5OBeJbViSyO1lHdFRfctYJcLXY24H0X3vuC"); // Encrypted "testpassword"

		// Service内のRepositoryをEncoderの挙動を定義
		Mockito.when(userRepository.findByEmail(form.getEmail())).thenReturn(null);
		Mockito.when(passwordEncoder.matches(form.getPassword(), user.getPassword())).thenReturn(true);

		// サービスを実行
		UserInfo result = loginLogoutUserService.login(form);

		// 検証
		assertNull(result, "間違ったEメールでログインができています");
	}

}
