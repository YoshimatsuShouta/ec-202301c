package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.domain.OrderItem;

@SpringBootTest
class OrderItemRepositoryTest {

	@Autowired
	private NamedParameterJdbcTemplate template;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("DB初期化処理開始");

		String sql = "INSERT INTO orders (user_id, status, total_price, order_date) VALUES(1, 0, 0, '1998-03-23');";
		template.update(sql, new MapSqlParameterSource());
		Integer maxId = template.queryForObject("select max(id) from orders;", new MapSqlParameterSource(),
				Integer.class);

		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(1);
		orderItem.setOrderId(maxId);
		orderItem.setQuantity(1);
		orderItem.setSize("M");
		orderItemRepository.insert(orderItem);
		System.out.println("インサートが完了しました。");

		System.out.println("DB初期化処理終了");
	}

	@Test
	void 注文商品主キー検索のテスト() {
		System.out.println("主キー検索するテスト開始");

		Integer maxId = template.queryForObject("select max(id) from order_items;", new MapSqlParameterSource(),
				Integer.class);
		OrderItem resultOrderItem = orderItemRepository.load(maxId);

		assertEquals(1, resultOrderItem.getItemId(), "商品IDが登録されていません");
		assertEquals(1, resultOrderItem.getQuantity(), "数量が登録されていません");
		assertEquals("M", resultOrderItem.getSize(), "商品サイズが登録されていません");

		System.out.println("主キー検索するテスト終了");
	}

	@AfterEach
	void tearDown() throws Exception {
		MapSqlParameterSource param = new MapSqlParameterSource();
		template.update("delete from orders", param);
		System.out.println("入れたデータを削除しました。");
	}
}
