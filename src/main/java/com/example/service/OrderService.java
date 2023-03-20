package com.example.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Order;
import com.example.form.OrderForm;
import com.example.repository.OrderRepository;

/**
 * 注文手順を操作するサービスクラス.
 * 
 * @author yamasakimanahito
 *
 */
@Service
@Temperature
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	/**
	 * 注文情報を受け取り、利用者の登録を行います.
	 * 
	 * @param form オーダーフォーム
	 * @return 注文情報
	 */
	public Order order(OrderForm form) {
		Order order = orderRepository.load(form.getId());
		java.sql.Date today = new java.sql.Date(new Date().getTime());
		order.setOrderDate(today);
		order.setStatus(2);
		order.setTotalPrice(order.getCalcTotalPrice());
		order.setDestinationName(form.getDestinationName());
		order.setDestinationEmail(form.getDestinationEmail());
		order.setDestinationZipcode(form.getDestinationZipcode());
		order.setDestinationAddress(form.getDestinationAddress());
		order.setDestinationTel(form.getDestinationTel());
		order.setPaymentMethod(form.getPaymentMethod());

		try {
			final String yyyyMMddhh = form.getDeliveryDate() + "-" + form.getDeliveryTime();
			Date deliveryTime = new SimpleDateFormat("yyyy-MM-dd-hh").parse(yyyyMMddhh);
			Timestamp deliveryDateTimestamp = new Timestamp(deliveryTime.getTime());
			order.setDeliveryTime(deliveryDateTimestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		orderRepository.update(order);

		return order;
	}

}
