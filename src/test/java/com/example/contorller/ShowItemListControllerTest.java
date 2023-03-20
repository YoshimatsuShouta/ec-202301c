package com.example.contorller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.example.domain.Item;
import com.example.service.ShowItemListService;

@SpringBootTest
@AutoConfigureMockMvc
public class ShowItemListControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private ShowItemListService showItemListService;

	@InjectMocks
	private ShowItemListController showItemListController;

	@BeforeEach
	void setUp() {

	}

	@Test
	@DisplayName("商品一覧画面に遷移する場合のテスト")
	void testShowItemList() throws Exception {
		// 事前準備
		List<Item> itemList = new ArrayList<>();
		Item item1 = new Item();
		item1.setName("item1");
		Item item2 = new Item();
		item2.setName("item2");
		itemList.add(item1);
		itemList.add(item2);
		// コントローラー内のサービスの動きを制御
		when(showItemListService.findAll(anyString())).thenReturn(itemList);

		// 遷移の検証
		mockMvc.perform(get("/")).andExpect(status().isOk())// 正しく遷移可能かを確認
				.andExpect(view().name("/materialize-version/item_list"));// 遷移先のパスが正しいかを確認
		// shouItemList内でモデルに正しくitemListが詰まっているかを検証
		// いつも引数で宣言しているModelを別個で宣言
		Model model = new ExtendedModelMap();
		// Model内に正しくitemListが入っているかを検証
		showItemListController.showItemList("", model);
		assertEquals(itemList, model.getAttribute("itemList"));
	}

	@Test
	@DisplayName("商品名で検索する場合のテスト（検索結果がある場合）")
	void testFindByName() throws Exception {
		// 事前準備
		List<Item> itemList = new ArrayList<>();
		Item item1 = new Item();
		item1.setName("item1");
		Item item2 = new Item();
		item2.setName("item2");
		itemList.add(item1);
		itemList.add(item2);
		// コントローラー内のサービスの動きを制御
		when(showItemListService.findAll(anyString())).thenReturn(itemList);
		// 遷移の検証
		mockMvc.perform(get("/findByName").param("name", "商品名")).andExpect(status().isOk())
				.andExpect(view().name("/materialize-version/item_list"));
		// shouItemList内でモデルに正しくitemListが詰まっているかを検証
		// いつも引数で宣言しているModelを別個で宣言
		Model model = new ExtendedModelMap();
		// Model内に正しくitemListが入っているかを検証
		showItemListController.findByName("商品名", "", model);
		assertEquals(itemList, model.getAttribute("itemList"));
	}

	@Test
	@DisplayName("商品名で検索する場合のテスト（検索結果が0件の場合）")
	void testFindByNameWithNoResult() throws Exception {
		// 事前準備
		List<Item> itemList = new ArrayList<>();
		Item item1 = new Item();
		item1.setName("item1");
		Item item2 = new Item();
		item2.setName("item2");
		itemList.add(item1);
		itemList.add(item2);
		// コントローラー内のサービスの動きを制御
		when(showItemListService.showItemList(anyString(), anyString())).thenReturn(itemList);
		// 遷移の検証とモデルの中にエラー文が入っているかを検証
		mockMvc.perform(get("/findByName").param("name", "存在しない商品名")).andExpect(status().isOk())
				.andExpect(view().name("/materialize-version/item_list"))
				.andExpect(model().attribute("result", "検索結果が0件の為、全件検索します"));
		// shouItemList内でモデルに正しくitemListが詰まっているかを検証
		// いつも引数で宣言しているModelを別個で宣言
		Model model = new ExtendedModelMap();
		// Model内に正しくitemListが入っているかを検証
		showItemListController.findByName("item", "", model);
		assertEquals(itemList, model.getAttribute("itemList"));
	}

}
