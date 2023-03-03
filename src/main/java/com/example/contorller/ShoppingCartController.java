package com.example.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.UserInfo;
import com.example.form.ShoppingCartForm;
import com.example.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

/**
 * カート関連コントローラー.
 * 
 * @author matsumotoyuyya
 *
 */
@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private HttpSession session;

	@Autowired
	private ShoppingCartService shoppingcartService;

	/**
	 * 商品をカートに追加する.
	 * 
	 * @param form ショッピングカートフォーム
	 * @return toCartへリダイレクト
	 */
	@PostMapping("/insertCart")
	public String insertCart(@Validated ShoppingCartForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "redirect:/ShowItemDetail/ToItemDetail";
		}

		UserInfo user = (UserInfo) session.getAttribute("User");
		shoppingcartService.insertCat(form, 1);
		// 税金計算
		Order order = new Order();
		OrderItem orderItem = new OrderItem();
//		 order.setCalcTotalPrice(form.getToppingIdList(), form.getQuantity(), form.getSize());
//		 order.setTax(order.getCalcTotalPrice());
		return "redirect:/shoppingCart/toCart";
	}

	/**
	 * カートリストを表示.
	 * 
	 * @return カートリスト
	 */
	@GetMapping("/toCart")
	public String toCartList(Model model) {

		UserInfo user = (UserInfo) session.getAttribute("User");

		Order orderList = shoppingcartService.showCart(1);

		model.addAttribute("order", orderList);
		System.out.println(orderList);

		return "/materialize-version/cart_list";
	}

	/**
	 * カートの中身表示.
	 * 
	 * @return カートリスト
	 */
	@GetMapping("/showCart")
	public String showCart(Model model) {
		UserInfo user = (UserInfo) session.getAttribute("User");

		Order orderList = shoppingcartService.showCart(1);

		model.addAttribute("order", orderList);
		return "/materialize-version/cart_list";
	}

	/**
	 * 注文商品削除.
	 * 
	 * @param orderItemId 注文商品ID
	 * @return toCartへリダイレクト
	 */
	@GetMapping("/deleteCart")
	public String deleteCartItems(Integer orderItemId) {
		shoppingcartService.deleteCartContents(orderItemId);
		return "redirect:/shoppingCart/toCart";
	}

}
