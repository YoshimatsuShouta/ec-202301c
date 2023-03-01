package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

/**
 * usersテーブルを操作するリポジトリ.
 * 
 * @author yoshimatsushouta
 *
 */
@Repository
public class UserRepository {

	/**
	 * Userオブジェクトを生成すｆるローマッパー.
	 */
	private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		return user;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * ユーザー情報を挿入します.
	 * 
	 * @param user ユーザー情報
	 */
	public void save(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "insert into users(name,email,password,zipcode,address,telephone)values(:name,:email,:password,:zipcode,:address,:telephone);";
		template.update(sql, param);
	}

	/**
	 * パスワードとEメールから顧客情報を取得します.
	 * 
	 * @param password フォームから受け取ったパスワード
	 * @param email    フォームから受け取ったEメール
	 * @return 存在しない場合はnullを返します
	 */
	public User findByPasswordAndEmail(String password, String email) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);

		String sql = "SELECT id, name, email, PASSWORD, zipcode, address, telephone FROM users WHERE email = :email AND PASSWORD = :password;";

		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}
	
	/**
	 * メールアドレスから顧客情報を取得します.
	 * 
	 * @param email　フォームから受け取ったEメール
	 * @return	存在しない場合はnullを返します
	 */
	public User findByEmail(String email){
		
		String sql = "SELECT id, name, email, passWord, zipcode, address, telephone from users where email = :email order by id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<User> userList= template.query(sql, param, USER_ROW_MAPPER);
		if(userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}
}