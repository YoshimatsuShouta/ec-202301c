package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

@SpringBootTest
class OrderConfirmServiceTest {

	@Autowired
	private OrderConfirmService orderConfirmService;

	@MockBean
	private OrderRepository orderRepository;

	@Test
	public void testGetOrderId() {
		Order order = new Order();
		order.setId(1);
		order.setStatus(0);
		when(orderRepository.load(1)).thenReturn(order);

		Order result = orderConfirmService.GetOrderId(1);

		assertEquals(1, result.getId(), "IDが取得できていません");
		assertEquals(0, result.getStatus(), "statusが取得できていません");
	}
}
