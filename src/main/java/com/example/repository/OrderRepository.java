package com.example.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;

/**
 * 注文関連レポジトリー.
 * 
 * @author matsumotoyuyya
=======
import org.springframework.stereotype.Repository;

import com.example.domain.Order;
/**
 * Orderドメインのリポジトリー.
 * @author yamasakimanahito
>>>>>>> develop
 *
 */
@Repository
public class OrderRepository {

	private static final ResultSetExtractor<List<Order>> ORDER_RESULT_SET_EXTRACTOR = (rs) -> {
		// 記事一覧が入るarticleListを生成
		List<Order> orderList = new LinkedList<Order>();
		List<OrderItem> orderItemList = null;
		List<OrderTopping> orderToppingsList = null;

		// 前の行の記事IDを退避しておく変数
		long beforeorderId = 0;
		long beforeOrderItemId = 0;

		while (rs.next()) {
			// 現在検索されている記事IDを退避
			int nowOrderId = rs.getInt("id");

			// 現在の記事IDと前の記事IDが違う場合はArticleオブジェクトを生成
			if (nowOrderId != beforeorderId) {
				Order order = new Order();
				order.setId(nowOrderId);
				order.setUserId(rs.getInt("user_id"));
				order.setTotalPrice(rs.getInt("total_price"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setDestinationName(rs.getString("destination_name"));
				order.setDestinationEmail(rs.getString("destination_email"));
				order.setDestinationZipcode(rs.getString("destination_zipcode"));
				order.setDestinationAddress(rs.getString("destination_address"));
				order.setDestinationTel(rs.getString("destination_tel"));
				order.setDeliveryTime(rs.getTimestamp("delivery_time"));
				order.setPaymentMethod(rs.getInt("payment_method"));
				// 空のコメントリストを作成しArticleオブジェクトにセットしておく
				orderItemList = new ArrayList<OrderItem>();
				order.setOrderItemList(orderItemList);
				// コメントがセットされていない状態のArticleオブジェクトをarticleListオブジェクトにadd
				orderList.add(order);
			}
			
			int nowOrderItemId = rs.getInt("oi_id");


			// 記事だけあってコメントがない場合はCommentオブジェクトは作らない
			if (beforeOrderItemId != nowOrderItemId) {
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rs.getInt("oi_id"));
				orderItem.setItemId(rs.getInt("oi_item_id"));
				orderItem.setOrderId(rs.getInt("oi_order_id"));
				orderItem.setQuantity(rs.getInt("oi_quantity"));
				orderItem.setSize(rs.getString("oi_size"));
				Item item = new Item();
				item.setId(rs.getInt("i_id"));
				item.setName(rs.getString("i_name"));
				item.setDescription(rs.getString("i_description"));
				item.setPriceM(rs.getInt("i_price_m"));
				item.setPriceL(rs.getInt("i_price_l"));
				item.setImagePath(rs.getString("i_image_path"));
				orderItem.setItem(item);
				orderToppingsList = new ArrayList<OrderTopping>();
				orderItem.setOrderToppingList(orderToppingsList);
				// コメントをarticleオブジェクト内にセットされているcommentListに直接addしている(参照型なのでこのようなことができる)
				orderItemList.add(orderItem);
			}
			if (rs.getInt("ot_id") != 0) {
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setId(rs.getInt("ot_id"));
				orderTopping.setToppingId(rs.getInt("ot_topping_id"));
				orderTopping.setOrderItemId(rs.getInt("ot_order_item_id"));
				Topping topping = new Topping();
				topping.setId(rs.getInt("t_id"));
				topping.setName(rs.getString("t_name"));
				topping.setPriceM(rs.getInt("t_price_m"));
				topping.setPriceL(rs.getInt("t_price_l"));
				orderTopping.setTopping(topping);
				// コメントをarticleオブジェクト内にセットされているcommentListに直接addしている(参照型なのでこのようなことができる)
				orderToppingsList.add(orderTopping);
			}

			// 現在の記事IDを前の記事IDを入れる退避IDに格納
			beforeorderId = nowOrderId;
			beforeOrderItemId = nowOrderItemId;
		}
		return orderList;
	};
	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 注文商品を登録します.
	 * 
	 * @param orderTopping 注文商品
	 * @return インサートした注文商品
	 */
	synchronized public Order insert(Order order) {

		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		// インサート処理
		if (order.getId() == null) {
			String insertOrder = "INSERT INTO orders( user_id, status, total_price,order_date,destination_name,destination_email,destination_zipcode,destination_address,destination_tel,delivery_time,payment_method) "
					+ " VALUES(:userId,:status,:totalPrice,:orderDate,:destinationName,:destinationEmail,:destinationZipcode,:destinationAddress,:destinationTel,:deliveryTime,:paymentMethod);";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String[] keyColumnNames = { "id" };
			template.update(insertOrder, param, keyHolder, keyColumnNames);
			order.setId(keyHolder.getKey().intValue());
			System.out.println(keyHolder.getKey() + "が割り当てられました");
		}

		
		return order;
	}
	
	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee 従業員情報
	 */
	public void update(Order order) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);

		String updateSql = "UPDATE orders SET status=:status,total_price =:totalPrice,order_date=:orderDate,destination_name=:destinationName,destination_email=:destinationEmail,destination_zipcode=:destinationZipcode,destination_address=:destinationAddress,destination_tel=:destinationTel,delivery_time=:deliveryTime,payment_method=:paymentMethod WHERE user_id =:userId;";
		template.update(updateSql, param);
	}


	/**
	 * 注文を検索します.
	 * 
	 * @param id ID
	 * @return 注文検索結果
	 */
	public Order load(Integer id) {
		String sql = "select o.id, o.user_id, o.status, o.total_price, o.order_date, o.destination_name, o.destination_email, o.destination_zipcode, o.destination_address, o.destination_tel, o.delivery_time, o.payment_method ,\n"
				+ "oi.id as oi_id , oi.item_id as oi_item_id , oi.order_id as oi_order_id , oi.quantity as oi_quantity , oi.size as oi_size,\n"
				+ "ot.id as ot_id , ot.topping_id as ot_topping_id,  ot.order_item_id  as ot_order_item_id, \n"
				+ "i.id  as i_id, i.name as i_name , i.description as i_description , i.price_m as i_price_m , i.price_l as i_price_l , i.image_path as i_image_path,\n"
				+ "t.id as t_id ,t.name as t_name , t.price_m as t_price_m , t.price_l as t_price_l\n"
				+ "from orders o\n" + "left join order_items oi on o.id = oi.order_id \n"
				+ "Left join order_toppings ot on oi.id = ot.order_item_id \n" + "left join items i on i.id = oi.item_id \n"
				+ "left join toppings t on t.id = ot.topping_id where o.id =:id;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Order> order = template.query(sql, param, ORDER_RESULT_SET_EXTRACTOR);
		if (order.size() == 0) {
			return null;
		}
		return order.get(0);
	}

	/**
	 * ？
	 * 
	 * @param userId ユーザーID
	 * @param status 状態
	 * @return
	 */
	public Order findByUserIdAndStatus(Integer userId, Integer status) {
		
		String sql = "select o.id, o.user_id, o.status, o.total_price, o.order_date, o.destination_name, o.destination_email, o.destination_zipcode, o.destination_address, o.destination_tel, o.delivery_time, o.payment_method ,\n"
				+ "oi.id as oi_id , oi.item_id as oi_item_id , oi.order_id as oi_order_id , oi.quantity as oi_quantity , oi.size as oi_size,\n"
				+ "ot.id as ot_id , ot.topping_id as ot_topping_id,  ot.order_item_id  as ot_order_item_id, \n"
				+ "i.id  as i_id, i.name as i_name , i.description as i_description , i.price_m as i_price_m , i.price_l as i_price_l , i.image_path as i_image_path,\n"
				+ "t.id as t_id ,t.name as t_name , t.price_m as t_price_m , t.price_l as t_price_l\n"
				+ "from orders o\n" + " LEFT join order_items oi on o.id = oi.order_id \n"
				+ " LEFT join order_toppings ot on oi.id = ot.order_item_id \n" + "LEFT join items i on i.id = oi.item_id \n"
				+ "LEFT join toppings t on t.id = ot.topping_id where o.user_id =:userId and o.status=:status;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		List<Order> orderList = template.query(sql, param, ORDER_RESULT_SET_EXTRACTOR);
		if (orderList.size() == 0) {
			return null;
		}
		return orderList.get(0);
	}
	
	


}
