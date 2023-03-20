package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.example.domain.Item;

@SpringBootTest
class ItemRepositoryTest {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private NamedParameterJdbcTemplate template;

	@Test
	void loadのテスト() {
		System.out.println("主キー検索するテスト開始");
		String sql = "insert into items values(19,'dummyピザ', 'dummydummydummy', 1000, 2000, 'dummy.jpg');";
		SqlParameterSource param = new MapSqlParameterSource();
		template.update(sql, param);
		System.out.println("インサートが完了しました。");

		Item resultItem = itemRepository.load(19);
		assertEquals("dummyピザ", resultItem.getName(), "商品名が登録されていません");
		assertEquals("dummydummydummy", resultItem.getDescription(), "商品説明が登録されていません");
		assertEquals(1000, resultItem.getPriceM(), "Mサイズの値段が登録されていません");
		assertEquals(2000, resultItem.getPriceL(), "Lサイズが登録されていません");
		assertEquals("dummy.jpg", resultItem.getImagePath(), "画像パスが登録されていません");

		System.out.println("主キー検索するテスト終了");
	}

	@Test
	void findAllのID昇順テスト() {
		System.out.println("全件検索するテスト開始");

		List<Item> resultItemList = itemRepository.findAll(null);
		assertEquals(18, resultItemList.size(), "件数が一致しません");
		assertEquals("じゃがバターベーコン", resultItemList.get(0).getName(), "ID順に並んでいません");
		assertEquals("贅沢フォルマッジ", resultItemList.get(17).getName(), "ID順に並んでいません");

		System.out.println("全件検索するテスト終了");
	}

	@Test
	void findAllのpriceM降順テスト() {
		System.out.println("全件検索するテスト開始");

		List<Item> resultItemList = itemRepository.findAll("high");
		assertEquals(18, resultItemList.size(), "件数が一致しません");
		assertEquals("とろけるビーフシチュー", resultItemList.get(0).getName(), "価格降順に並んでいません");
		assertEquals("熟成ベーコンとマッシュルーム", resultItemList.get(17).getName(), "価格降順に並んでいません");

		System.out.println("全件検索するテスト終了");
	}

	@Test
	void findAllのpriceM昇順テスト() {
		System.out.println("全件検索するテスト開始");

		List<Item> resultItemList = itemRepository.findAll("low");
		assertEquals(18, resultItemList.size(), "件数が一致しません");
		assertEquals("じゃがバターベーコン", resultItemList.get(0).getName(), "価格昇順に並んでいません");
		assertEquals("とろけるビーフシチュー", resultItemList.get(17).getName(), "価格昇順に並んでいません");

		System.out.println("全件検索するテスト終了");
	}

	@Test
	void findByNameのID昇順テスト() {
		System.out.println("曖昧検索するテスト開始");

		List<Item> resultItemList = itemRepository.findByName("じゃが", null);
		assertEquals(2, resultItemList.size(), "件数が一致しません");
		assertEquals("じゃがバターベーコン", resultItemList.get(0).getName(), "ID順に並んでいません");
		assertEquals("カレーじゃがバター", resultItemList.get(1).getName(), "ID順に並んでいません");

		System.out.println("曖昧検索するテスト終了");
	}

	@Test
	void findByNameのpriceM降順テスト() {
		System.out.println("曖昧検索するテスト開始");

		List<Item> resultItemList = itemRepository.findByName("じゃが", "high");
		assertEquals(2, resultItemList.size(), "件数が一致しません");
		assertEquals("カレーじゃがバター", resultItemList.get(0).getName(), "価格降順に並んでいません");
		assertEquals("じゃがバターベーコン", resultItemList.get(1).getName(), "価格降順に並んでいません");

		System.out.println("曖昧検索するテスト終了");
	}

	@Test
	void findByNameのpriceM昇順テスト() {
		System.out.println("曖昧検索するテスト開始");

		List<Item> resultItemList = itemRepository.findByName("じゃが", "low");
		assertEquals(2, resultItemList.size(), "件数が一致しません");
		assertEquals("じゃがバターベーコン", resultItemList.get(0).getName(), "価格昇順に並んでいません");
		assertEquals("カレーじゃがバター", resultItemList.get(1).getName(), "価格昇順に並んでいません");

		System.out.println("曖昧検索するテスト終了");
	}

	@AfterEach
	void tearDown() throws Exception {
		MapSqlParameterSource deleteParam = new MapSqlParameterSource().addValue("name", "dummyピザ");
		template.update("delete from items where name = :name", deleteParam);
		System.out.println("入れたデータを削除しました。");
	}
}
