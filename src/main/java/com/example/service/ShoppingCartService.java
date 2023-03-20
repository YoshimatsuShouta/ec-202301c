package com.example.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.ShoppingCartForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

/**
 * カート関連サービス.
 * 
 * @author matsumotoyuyya
 *
 */

@Service
@Transactional
public class ShoppingCartService {

	@Autowired
	private OrderToppingRepository orderToppingRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderitemRepository;

	/**
	 * 注文情報インサート業務処理.
	 * 
	 * @param form   ショッピングカートフォーム
	 * @param userId ユーザーID
	 */
	public void insertCat(ShoppingCartForm form, Integer userId) {
		Order order = orderRepository.findByUserIdAndStatus(userId, 0);
		// オーダーテーブルにユーザー情報がない場合注文テーブにインサート
		if (order == null) {
			Order orderObject = new Order();
			orderObject.setUserId(userId);
			orderObject.setStatus(0);
			orderObject.setTotalPrice(0);
			order = orderRepository.insert(orderObject);
		}
		// 注文商品へインサート
		OrderItem orderItem = new OrderItem();
		BeanUtils.copyProperties(form, orderItem);

		orderItem.setOrderId(order.getId());

		orderItem = orderitemRepository.insert(orderItem);

		// 注文トッピングインサート, nullの時はインサートなし
		OrderTopping orderTopping = new OrderTopping();
		if (form.getToppingIdList() != null) {
			for (Integer t : form.getToppingIdList()) {
				orderTopping.setToppingId(t);
				orderTopping.setOrderItemId(orderItem.getId());
				orderToppingRepository.insert(orderTopping);
			}
		}
	}

	/**
	 * カートに中身表示サービス.
	 * 
	 * @param userId ユーザーID
	 * @return 注文オーダ情報
	 */
	public Order showCart(Integer userId) {
		Order order = orderRepository.findByUserIdAndStatus(userId, 0);
		if (order == null) {
			// 注文デーブルにインサートなしでカートの中身をみた時
			Order orderObject = new Order();
			orderObject.setUserId(userId);
			orderObject.setStatus(0);
			orderObject.setTotalPrice(0);
			order = orderRepository.insert(orderObject);
		}
		return order;

	}

	/**
	 * 注文商品削除.
	 * 
	 * @param orderItemId 注文商品ID
	 */
	public void deleteByOrderId(Integer orderItemId) {
		orderitemRepository.deleteByOrderId(orderItemId);
	}

	/**
	 * 商品を一括削除する.
	 * 
	 * @param orderId オーダーアイテムId
	 */
	public void allDeleteOrderItem(Integer orderId) {
		orderitemRepository.allDeleteOrderItem(orderId);
	}

}
