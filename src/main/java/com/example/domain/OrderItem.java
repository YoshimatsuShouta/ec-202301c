package com.example.domain;

import java.util.List;

/**
 * 注文商品に関するドメイン.
 * 
 * @author yoshimatsushouta
 *
 */
public class OrderItem {
	/** ID */
	private Integer id;
	/** 商品ID */
	private Integer itemId;
	/** 注文ID */
	private Integer orderId;
	/** 数量 */
	private Integer quantity;
	/** 商品サイズ */
	private String size;
	/** 商品 */
	private Item item;
	/** 注文トッピングリスト */
	private List<OrderTopping> orderToppingList;

	/**
	 * カート内商品の小計金額を取得する.
	 * 
	 * @return カート内商品の合計金額
	 * @TODO 処理未
	 */
	public int getSubTotalPrice() {
		int subtotalL = 0;
		int subtotalM = 0;
		int nulltotalL = 0;
		int nulltotalM = 0;

		if (this.getOrderToppingList().isEmpty() && this.size.equals("L")) {
			nulltotalL = this.quantity * this.item.getPriceL();
			return nulltotalL;

		} else if (this.size.equals("L")) {

			subtotalL = (this.quantity * this.item.getPriceL())
					+ (this.quantity * (this.getOrderToppingList().size() * 300));
			return subtotalL;

		}

		if (this.getOrderToppingList().isEmpty() && this.size.equals("M")) {
			nulltotalM = this.quantity * this.item.getPriceM();
			return nulltotalM;
		} else if (this.size.equals("M")) {
			subtotalM = (this.quantity * this.item.getPriceM())
					+ (this.quantity * (this.getOrderToppingList().size() * 200));
			return subtotalM;
		}

		return 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", orderToppingList=" + orderToppingList + "]";
	}

}
