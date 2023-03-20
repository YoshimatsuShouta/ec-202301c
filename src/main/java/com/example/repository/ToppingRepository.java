package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Topping;

/**
 * toppingsテーブルを操作するリポジトリ.
 * 
 * @author yoshimatsushouta
 *
 */
@Repository
public class ToppingRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * Toppingオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Topping> TOPPING_ROW_MAPPER = new BeanPropertyRowMapper<>(Topping.class);

	/**
	 * トッピング一覧情報をid昇順で取得します.
	 * 
	 * @return トッピング一覧情報 トッピングが存在しない場合はサイズ0件のトッピング一覧を返します
	 */
	public List<Topping> findAll() {
		String sql = "SELECT id,name,price_m,price_l FROM toppings ORDER BY id;";

		List<Topping> ToppingList = template.query(sql, TOPPING_ROW_MAPPER);

		return ToppingList;
	}

	/**
	 * 主キー検索を行います.
	 * 
	 * @param id 検索したい主キーの値
	 * @return 検索された商品情報(検索されなかった場合は非検査例外が発生します)
	 */
	public Topping load(Integer id) {
		String sql = "SELECT id,name,price_m,price_l FROM toppings WHERE id =:id;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Topping topping = template.queryForObject(sql, param, TOPPING_ROW_MAPPER);

		return topping;
	}

}
