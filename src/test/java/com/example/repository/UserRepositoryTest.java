package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.domain.UserInfo;

@SpringBootTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NamedParameterJdbcTemplate template;

	@BeforeEach
	void testSave() throws Exception {
		System.out.println("DB初期化処理開始");
		UserInfo user = new UserInfo();
		user.setName("dummy名");
		user.setEmail("dummy@xxx.com");
		user.setPassword("dummy");
		user.setZipcode("123-4567");
		user.setAddress("dummy県");
		user.setTelephone("123-4566-7890");

		userRepository.save(user);

		System.out.println("インサートが完了しました。");

		System.out.println("DB初期化処理終了");
	}

	@Test
	void ログイン確認のテスト() throws Exception {
		System.out.println("メールアドレスとパスワードで検索するテスト開始");

		UserInfo resultUserInfo = userRepository.findByPasswordAndEmail("dummy", "dummy@xxx.com");

		assertEquals("dummy名", resultUserInfo.getName(), "名前が登録されていません");
		assertEquals("dummy@xxx.com", resultUserInfo.getEmail(), "メールアドレスが登録されていません");
		assertEquals("dummy", resultUserInfo.getPassword(), "パスワードが登録されていません");
		assertEquals("123-4567", resultUserInfo.getZipcode(), "郵便番号が登録されていません");
		assertEquals("dummy県", resultUserInfo.getAddress(), "住所が登録されていません");
		assertEquals("123-4566-7890", resultUserInfo.getTelephone(), "電話番号が登録されていません");

		System.out.println("メールアドレスとパスワードで検索するテスト開始");
	}

	@Test
	void メール確認のテスト１() throws Exception {
		System.out.println("メールアドレスで検索するテスト開始");

		UserInfo resultUserInfo = userRepository.findByEmail("dummy@xxx.com");

		assertEquals("dummy名", resultUserInfo.getName(), "名前が登録されていません");
		assertEquals("dummy@xxx.com", resultUserInfo.getEmail(), "メールアドレスが登録されていません");
		assertEquals("dummy", resultUserInfo.getPassword(), "パスワードが登録されていません");
		assertEquals("123-4567", resultUserInfo.getZipcode(), "郵便番号が登録されていません");
		assertEquals("dummy県", resultUserInfo.getAddress(), "住所が登録されていません");
		assertEquals("123-4566-7890", resultUserInfo.getTelephone(), "電話番号が登録されていません");

		System.out.println("メールアドレスで検索するテスト終了");
	}

	@AfterEach
	void tearDown() throws Exception {
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("email", "dummy@xxx.com");
		template.update("delete from users where email = :email", param);
		System.out.println("入れたデータを削除しました。");
	}
}
