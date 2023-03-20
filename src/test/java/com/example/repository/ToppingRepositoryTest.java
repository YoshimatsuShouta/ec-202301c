package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Topping;

@SpringBootTest
class ToppingRepositoryTest {

	@Autowired
	private ToppingRepository toppingRepository;

	@Test
	void findAllのテスト() {
		System.out.println("全件検索するテスト開始");

		List<Topping> resultToppingList = toppingRepository.findAll();
		assertEquals(28, resultToppingList.size(), "件数が一致しません");
		assertEquals("オニオン", resultToppingList.get(0).getName(), "ID順に並んでいません");
		assertEquals("チーズ増量", resultToppingList.get(27).getName(), "ID順に並んでいません");

		System.out.println("全件検索するテスト終了");
	}

	@Test
	void loadのテスト() {
		System.out.println("主キー検索するテスト開始");

		Topping resultTopping = toppingRepository.load(1);
		assertEquals("オニオン", resultTopping.getName(), "トッピング名が登録されていません");
		assertEquals(200, resultTopping.getPriceM(), "Mサイズの価格が登録されていません");
		assertEquals(300, resultTopping.getPriceL(), "Lサイズの価格が登録されていません");

		System.out.println("主キー検索するテスト終了");
	}

}
