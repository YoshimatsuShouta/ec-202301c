package com.example.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.UserInfo;
import com.example.service.ShoppingCartService;

@Controller
@RequestMapping("/OrderConfirm")
public class OrderConfirmController {

	@Autowired
	private ShoppingCartService shoppingcartService;

	@PostMapping("/toOrderConfirm")
	public String Order(Integer id, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		UserInfo user = loginUser.getUserInfo();

		Order orderList = shoppingcartService.showCart(id);

		model.addAttribute("order", orderList);
		model.addAttribute(user);

		return "/materialize-version/order_confirm";
	}

}
