package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.UserInfo;
import com.example.repository.UserRepository;

/**
 * ユーザー情報を操作するサービス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class ResisterUserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * ユーザー情報を登録します.
	 * 
	 * @param user ユーザー情報
	 */

	public void resisterUser(UserInfo user) {

		userRepository.save(user);
	}

	public UserInfo findByEmail(String email) {
		UserInfo user = userRepository.findByEmail(email);
		return user;

	}

}
