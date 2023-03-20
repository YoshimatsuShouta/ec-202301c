package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.ShoppingCartForm;
import com.example.repository.ItemRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private OrderItemRepository orderitemRepository;

	@Mock
	private OrderToppingRepository orderToppingRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private ShoppingCartService shoppingCartService = new ShoppingCartService();

	@Test
	public void インサートカートの動作テスト() {
		// DBに入れる体の仮のOrderItem型の引数
		ArgumentCaptor<OrderItem> orderitemCaptor = ArgumentCaptor.forClass(OrderItem.class);

		// input
		ShoppingCartForm form = new ShoppingCartForm();
		form.setItemId(1);
		form.setQuantity(1);

		// mock
		Order order = new Order();
		order.setId(1);
		order.setUserId(1);
		order.setStatus(0);
		order.setTotalPrice(0);
		OrderItem orderItem = new OrderItem();
		orderItem.setId(1);
		orderItem.setItemId(1);
		orderItem.setOrderId(1);
		orderItem.setQuantity(1);
		orderItem.setSize("M");
		orderItem.setItem(itemRepository.load(1));

		List<OrderItem> orderItemList = new ArrayList<>();
		orderItemList.add(orderItem);
		OrderTopping orderTopping = new OrderTopping();
		orderTopping.setId(1);
		orderTopping.setOrderItemId(1);
		orderTopping.setToppingId(1);
		List<OrderTopping> orderToppingList = new ArrayList<>();
		orderToppingList.add(orderTopping);
		orderItem.setOrderToppingList(orderToppingList);
		order.setOrderItemList(orderItemList);

		// 上記の仮のorderをorderservice内で呼び出す設定
		Mockito.doReturn(orderItem).when(orderitemRepository).insert(orderitemCaptor.capture());

		when(orderRepository.findByUserIdAndStatus(1, 0)).thenReturn(order);

		// execute
		shoppingCartService.insertCat(form, 1);

		// assert
		assertEquals(1, order.getUserId());
	}

	@Test
	void カート内商品一覧表示のテスト() throws Exception {
		// モック設定
		Order order = new Order();
		order.setId(1);
		when(orderRepository.findByUserIdAndStatus(1, 0)).thenReturn(order);

		// 実行
		Order result = shoppingCartService.showCart(1);

		// 検証
		assertEquals(1, result.getId(), "カートの中を表示できていません");
	}

}
