package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Item;
import com.example.repository.ItemRepository;

@SpringBootTest
class ShowItemListServiceTest {

	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private ShowItemListService showItemListService;

	@Test
	void 商品一覧情報取得テスト() throws Exception {
		// DBにあるものを仮で定義
		Item item = new Item();

		// itemRepositoryから返すためのList<Item>を作成
		List<Item> itemList = new ArrayList<>();
		itemList.add(item);
		// itemRepositoryの動作を制御
		Mockito.doReturn(itemList).when(itemRepository).findAll("通常");

		// findAllの呼び出し
		List<Item> resultItemList = showItemListService.findAll("通常");

		// 内容の確認
		assertEquals(itemList, resultItemList, "Itemを正しく取得できていません");
	}

	@Test
	void 商品一覧情報取得ソートテスト() throws Exception {
		// DBにあるものを仮で定義
		Item item = new Item();

		Item item2 = new Item();

		// itemRepositoryから返すためのList<Item>を作成
		List<Item> itemList = new ArrayList<>();
		itemList.add(item);
		List<Item> itemList2 = new ArrayList<>();
		itemList.add(item2);
		// itemRepositoryの動作を制御
		Mockito.doReturn(itemList).when(itemRepository).findAll("通常");
		Mockito.doReturn(itemList2).when(itemRepository).findAll("High");

		// findAllの呼び出し
		List<Item> resultItemList = showItemListService.findAll("High");

		// 内容の確認
		assertNotEquals(itemList, resultItemList, "Itemを正しく取得できていません");
		assertEquals(itemList2, resultItemList, "Itemを正しく取得できていません");
	}

	@Test
	void 商品曖昧検索一覧情報取得テスト() throws Exception {
		// DBにあるものを仮で定義
		Item item = new Item();

		// itemRepositoryから返すためのList<Item>を作成
		List<Item> itemList = new ArrayList<>();
		itemList.add(item);
		// itemRepositoryの動作を制御
		Mockito.doReturn(itemList).when(itemRepository).findByName("dummy", "通常");
		// findAllの呼び出し
		List<Item> resultItemList = showItemListService.showItemList("dummy", "通常");

		// 内容の確認
		assertEquals(itemList, resultItemList, "Itemを正しく取得できていません");
	}

	@Test
	void 商品曖昧検索一覧情報取得ソートテスト() throws Exception {
		// DBにあるものを仮で定義
		Item item = new Item();

		Item item2 = new Item();

		// itemRepositoryから返すためのList<Item>を作成
		List<Item> itemList = new ArrayList<>();
		itemList.add(item);
		List<Item> itemList2 = new ArrayList<>();
		itemList.add(item2);
		// itemRepositoryの動作を制御
		Mockito.doReturn(itemList).when(itemRepository).findByName("dummy", "通常");
		Mockito.doReturn(itemList2).when(itemRepository).findByName("dummy", "High");

		// findAllの呼び出し
		List<Item> resultItemList = showItemListService.showItemList("dummy", "High");

		// 内容の確認
		assertNotEquals(itemList, resultItemList, "Itemを正しく取得できていません");
		assertEquals(itemList2, resultItemList, "Itemを正しく取得できていません");
	}

}
