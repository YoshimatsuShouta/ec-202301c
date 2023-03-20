package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.repository.ItemRepository;
import com.example.repository.ToppingRepository;

@SpringBootTest
class ShowItemDetailServiceTest {

	@Mock
	private ItemRepository itemRepository;
	@Mock
	private ToppingRepository toppingRepository;
	@InjectMocks
	private ShowItemDetailService showItemDetailService;

	@Test
	void 商品詳細表示のテスト() {
		// 通常はitemsテーブルにあるものを仮で用意
		Item item = new Item();
		item.setId(1);
		item.setName("dummyピザ");
		item.setDescription("dummydummy");
		item.setPriceM(1000);
		item.setPriceL(2000);
		item.setImagePath("dummy.jpg");
		item.setDeleted(false);

		// 通常はtoppingsテーブルにあるものを仮で用意
		Topping topping = new Topping();
		topping.setId(1);
		topping.setName("トッピング");
		topping.setId(200);
		topping.setId(3000);

		// itemsテーブルに渡すためtoppingListに詰める
		List<Topping> toppingList = new ArrayList<>();
		toppingList.add(topping);

		// Repositoryからメソッドを呼び出す動作を制御
		Mockito.doReturn(toppingList).when(toppingRepository).findAll();
		Mockito.doReturn(item).when(itemRepository).load(1);

		// サービスの呼び出し
		Item resultItem = showItemDetailService.showItemDetail(1);

		// showItemDetailService.showItemDetailから返ってきたものの確認
		assertEquals(1, resultItem.getId(), "IDが取得できていません");
		assertEquals("dummyピザ", resultItem.getName(), "商品名が取得できていません");
		assertEquals("dummydummy", resultItem.getDescription(), "商品説明が取得できていません");
		assertEquals(1000, resultItem.getPriceM(), "Mサイズの価格が取得できていません");
		assertEquals(2000, resultItem.getPriceL(), "Lサイズの価格が取得できていません");
		assertEquals("dummy.jpg", resultItem.getImagePath(), "画像パスが取得できていません");
		assertEquals(toppingList, resultItem.getToppingList(), "トッピングリストが取得できていません");
	}

	@Test
	void 商品詳細表示のテスト2() {
		// 通常はitemsテーブルにあるものを仮で用意
		Item item = new Item();
		item.setId(1);
		item.setName("dummyピザ");
		item.setDescription("dummydummy");
		item.setPriceM(1000);
		item.setPriceL(2000);
		item.setImagePath("dummy.jpg");
		item.setDeleted(false);

		// 通常はtoppingsテーブルにあるものを仮で用意
		Topping topping = new Topping();
		topping.setId(1);
		topping.setName("トッピング");
		topping.setId(200);
		topping.setId(3000);

		// itemsテーブルに渡すためtoppingListに詰める
		List<Topping> toppingList = new ArrayList<>();
		toppingList.add(topping);

		// Repositoryからメソッドを呼び出す動作を制御
		Mockito.doReturn(toppingList).when(toppingRepository).findAll();
		Mockito.doReturn(item).when(itemRepository).load(2);

		// サービスの呼び出し
		Item resultItem = showItemDetailService.showItemDetail(2);

		// showItemDetailService.showItemDetailから返ってきたものの確認
		assertEquals(1, resultItem.getId(), "IDが取得できていません");
		assertEquals("dummyピザ", resultItem.getName(), "商品名が取得できていません");
		assertEquals("dummydummy", resultItem.getDescription(), "商品説明が取得できていません");
		assertEquals(1000, resultItem.getPriceM(), "Mサイズの価格が取得できていません");
		assertEquals(2000, resultItem.getPriceL(), "Lサイズの価格が取得できていません");
		assertEquals("dummy.jpg", resultItem.getImagePath(), "画像パスが取得できていません");
		assertEquals(toppingList, resultItem.getToppingList(), "トッピングリストが取得できていません");
	}

}
