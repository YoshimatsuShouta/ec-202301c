package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.example.domain.Order;

@SpringBootTest
class OrderRepositoryTest {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private NamedParameterJdbcTemplate template;

	@Test
	void オーダーのインサートのテスト() {
		System.out.println("オーダーのインサートをテスト開始");
		// テストデータを作成する
		Order order = new Order();
		order.setUserId(1);
		order.setStatus(0);
		order.setTotalPrice(0);
		@SuppressWarnings("deprecation")
		Date date = new Date(200, 3, 23);
		order.setOrderDate(date);

		// Order をデータベースに保存する
		order = orderRepository.insert(order);
		System.out.println("インサート後のオーダー:" + order);
		Integer maxId = template.queryForObject("select max(id) from orders;", new MapSqlParameterSource(),
				Integer.class);

		// 結果が期待通りであることを検証する
		assertEquals(maxId, order.getId(), "主キーが登録されていません");
		assertEquals(1, order.getUserId(), "ユーザーIDが登録されていません");
		assertEquals(0, order.getStatus(), "ステータスが登録されていません");
		assertEquals(0, order.getTotalPrice(), "合計金額が登録されていません");

		System.out.println("オーダーのインサートをテスト終了");
	}

	@Test
	void オーダーのアップデートのテスト() {
		System.out.println("オーダーのアップデートをテスト開始");
		// テストデータを作成する
		Order order = new Order();
		order.setUserId(1);
		order.setStatus(0);
		order.setTotalPrice(0);
		@SuppressWarnings("deprecation")
		Date date = new Date(200, 3, 23);
		order.setOrderDate(date);

		// Order をデータベースに保存する
		order = orderRepository.insert(order);
		System.out.println("最初のオーダー:" + order);
		order.setDestinationName("dummy");
		order.setDestinationEmail("dummy@xxx.com");
		order.setDestinationZipcode("123-4567");
		order.setDestinationAddress("dummy県");
		order.setDestinationTel("123-4567-8901");
		order.setPaymentMethod(1);
		// タイムスタンプ型で配達日時をsetする
		try {
			final String yyyyMMddhh = "1998-03-23" + "-" + "11";
			System.out.println("yyyyMMddhh:" + yyyyMMddhh);
			java.util.Date deliveryTime = new SimpleDateFormat("yyyy-MM-dd-hh").parse(yyyyMMddhh);
			Timestamp deliveryDateTimestamp = new Timestamp(deliveryTime.getTime());
			order.setDeliveryTime(deliveryDateTimestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// updateを呼び出す
		orderRepository.update(order);

		Integer maxId = template.queryForObject("select max(id) from orders;", new MapSqlParameterSource(),
				Integer.class);
		Order resultOrder = orderRepository.load(maxId);
		System.out.println("アップデート後のオーダー:" + resultOrder);

		// 結果が期待通りであることを検証する
		assertEquals("dummy", resultOrder.getDestinationName(), "宛先氏名が登録されていません");
		assertEquals("dummy@xxx.com", resultOrder.getDestinationEmail(), "宛先Eメールが登録されていません");
		assertEquals("123-4567", resultOrder.getDestinationZipcode(), "宛先郵便番号が登録されていません");
		assertEquals("dummy県", resultOrder.getDestinationAddress(), "宛先住所が登録されていません");
		assertEquals("123-4567-8901", resultOrder.getDestinationTel(), "宛先電話番号が登録されていません");
		assertEquals(1, resultOrder.getPaymentMethod(), "支払い方法が登録されていません");

	}

	@Test
	void オーダーのfindByUserIdAndStatusのテスト() {
		System.out.println("オーダーのfindByUserIdAndStatusをテスト開始");
		// テストデータを作成する
		Order order = new Order();
		order.setUserId(1);
		order.setStatus(0);
		order.setTotalPrice(1000);

		// Order をデータベースに保存する
		orderRepository.insert(order);

		// findByUserIdAndStatus を呼び出す
		Order result = orderRepository.findByUserIdAndStatus(1, 0);

		// 結果が期待通りであることを検証する
		assertEquals(1, result.getUserId(), "ユーザーIDが不正");
		assertEquals(0, result.getStatus(), "ステータスが不正");
		assertEquals(1000, result.getTotalPrice(), "合計金額が不正");
	}

	@AfterEach
	void tearDown() throws Exception {
		String sql = "DELETE FROM orders";
		SqlParameterSource param = new MapSqlParameterSource();
		template.update(sql, param);
		System.out.println("DBの初期化終了");
	}

}
