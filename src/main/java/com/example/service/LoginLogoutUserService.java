package com.example.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.repository.UserRepository;

/**
 * 顧客情報を操作するサービス.
 * 
 * @author yoshimatsushouta
 *
 */
@Service
@Transactional
public class LoginLogoutUserService {
	@Autowired
	private UserRepository userReposiory;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 顧客のログインを行います.
	 * 
	 * @param form フォーム
	 * @return 該当する顧客情報を返します. 従業員が存在しない場合はnullを返します。
	 * 
	 */
	public UserInfo login(LoginLogoutUserForm form) {

		UserInfo user = userReposiory.findByEmail(form.getEmail());
		if (Objects.isNull(user)) {
			return null;
		}
		return passwordEncoder.matches(form.getPassword(), user.getPassword()) ? user : null;
	}
}
