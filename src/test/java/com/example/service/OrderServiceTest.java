package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.OrderForm;
import com.example.repository.ItemRepository;
import com.example.repository.OrderRepository;

@SpringBootTest
public class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	public void 注文情報登録テスト() throws Exception {
		// Orderオブジェクトをモックから実際のインスタンスに変更
		Order order = new Order();
		order.setId(1);
		order.setUserId(1);
		order.setTotalPrice(0);
		OrderItem orderItem = new OrderItem();
		orderItem.setId(1);
		orderItem.setItemId(1);
		orderItem.setOrderId(1);
		orderItem.setQuantity(1);
		orderItem.setSize("M");
		orderItem.setId(1);
		orderItem.setId(1);
		orderItem.setItem(itemRepository.load(1));
		List<OrderItem> orderItemList = new ArrayList<>();
		orderItemList.add(orderItem);
		OrderTopping orderTopping = new OrderTopping();
		orderTopping.setId(1);
		orderTopping.setOrderItemId(1);
		orderTopping.setToppingId(1);
		List<OrderTopping> orderToppingList = new ArrayList<>();
		orderItem.setOrderToppingList(orderToppingList);
		order.setOrderItemList(orderItemList);
		// 上記の仮のorderをorderservice内で呼び出す設定
		when(orderRepository.load(anyInt())).thenReturn(order);
		// 注文フォームを設定
		OrderForm form = new OrderForm();
		form.setId(1);
		form.setTotalPrice(0);
		form.setDestinationName("山田 太郎");
		form.setDestinationEmail("yamada@example.com");
		form.setDestinationZipcode("100-0001");
		form.setDestinationAddress("東京都千代田区千代田1-1");
		form.setDestinationTel("03-1111-2222");
		form.setPaymentMethod(0);
		form.setDeliveryDate("2023-04-01");
		form.setDeliveryTime(12);
		System.out.println(form);

		// 注文
		Order result = orderService.order(form);
		System.out.println(result);
		// 結果の検証
		assertEquals("山田 太郎", result.getDestinationName(), "宛名氏名が登録されていません");
		assertEquals("yamada@example.com", result.getDestinationEmail(), "宛先Eメールが登録されていません");
		assertEquals("100-0001", result.getDestinationZipcode(), "宛先郵便番号が登録されていません");
		assertEquals("東京都千代田区千代田1-1", result.getDestinationAddress(), "宛先住所が登録されていません");
		assertEquals("03-1111-2222", result.getDestinationTel(), "宛先電話番号が登録されていません");
		assertEquals(0, result.getPaymentMethod(), "paymentMethodが登録されていません");

		final String yyyyMMddhh = form.getDeliveryDate() + "-" + form.getDeliveryTime();
		Date deliveryTime = new SimpleDateFormat("yyyy-MM-dd-hh").parse(yyyyMMddhh);
		Timestamp expectedDeliveryTime = new Timestamp(deliveryTime.getTime());
		assertEquals(expectedDeliveryTime, result.getDeliveryTime(), "DeliveryTimeが登録されていません");
	}
}
