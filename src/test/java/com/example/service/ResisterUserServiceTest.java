package com.example.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.UserInfo;
import com.example.repository.UserRepository;

@SpringBootTest
@Transactional
public class ResisterUserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ResisterUserService resisterUserService;

	@Test
	void ユーザ登録のテスト() {
		ArgumentCaptor<UserInfo> userCaptor = ArgumentCaptor.forClass(UserInfo.class);

		resisterUserService.resisterUser(userCaptor.capture());
		verify(userRepository, times(1)).save(userCaptor.capture());

//		// テスト用のユーザー情報を作成
//		UserInfo user = new UserInfo();
//		user.setName("test user");
//		user.setEmail("test@example.com");
//		user.setPassword("testtest");
//		user.setZipcode("123-4567");
//		user.setAddress("test県");
//		user.setTelephone("123-4567-8901");
//
//		// ユーザーを登録
//		resisterUserService.resisterUser(user);
//
//		// 登録したユーザーを取得
//		UserInfo savedUser = resisterUserService.findByEmail("test@example.com");
//
//		// 登録したユーザーが正しく取得できたことを検証する
//		assertEquals("test user", savedUser.getName(), "ユーザーが一致しません");
//		assertEquals("test@example.com", savedUser.getEmail(), "Eメールが一致しません");
//		assertEquals("testtest", savedUser.getPassword(), "パスワードが一致しません");
//		assertEquals("123-4567", savedUser.getZipcode(), "郵便番号が一致しません");
//		assertEquals("test県", savedUser.getAddress(), "住所が一致しません");
//		assertEquals("123-4567-8901", savedUser.getTelephone(), "電話番号が一致しません");
	}

}
