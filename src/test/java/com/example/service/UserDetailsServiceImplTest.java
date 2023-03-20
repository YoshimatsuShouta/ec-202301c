package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.domain.UserInfo;
import com.example.repository.UserRepository;

@SpringBootTest
class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;

	private final String EXISTING_USER_EMAIL = "existing_user@example.com";
	private final String NON_EXISTING_USER_EMAIL = "non_existing_user@example.com";
	private final String USER_PASSWORD = "password";

	@BeforeEach
	void setUp() throws Exception {

		UserInfo existingUser = new UserInfo();
		existingUser.setEmail(EXISTING_USER_EMAIL);
		existingUser.setPassword(USER_PASSWORD);

		// 存在するユーザの場合は、ユーザ情報を返すようにモックする
		when(userRepository.findByEmail(EXISTING_USER_EMAIL)).thenReturn(existingUser);

		// 存在しないユーザの場合は、nullを返すようにモックする
		when(userRepository.findByEmail(NON_EXISTING_USER_EMAIL)).thenReturn(null);
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	void ログイン情報の確認とロール付与の確認テスト() throws Exception {
		// 実行
		UserDetails loginUser = userDetailsServiceImpl.loadUserByUsername(EXISTING_USER_EMAIL);

		// 保存されているユーザ情報を正しく呼び出せているか検証
		assertEquals(EXISTING_USER_EMAIL, loginUser.getUsername(), "ログインできるユーザー名が一致しません");
		assertEquals(USER_PASSWORD, loginUser.getPassword(), "ログインできるパスワードが一致しません");

		// ユーザ情報に権限を付与できているか検証
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) loginUser.getAuthorities();
		assertEquals(1, authorities.size(), "返って来るはずのauthorities.size()がありません");
		assertEquals("ROLE_USER", authorities.iterator().next().getAuthority(), "ユーザーロールの付与が行われていません");
	}

	@Test
	void ログイン情報がない場合のテスト() throws Exception {
		// 実行＆検証
		assertThrows(UsernameNotFoundException.class,
				() -> userDetailsServiceImpl.loadUserByUsername(NON_EXISTING_USER_EMAIL),
				"存在しないはずのユーザーを呼び出した時正しくエラーが起きません");
	}

}
