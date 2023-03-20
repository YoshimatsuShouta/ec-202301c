package com.example.contorller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.domain.Item;
import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.UserInfo;
import com.example.form.ShoppingCartForm;
import com.example.repository.ItemRepository;
import com.example.repository.ToppingRepository;
import com.example.service.ShoppingCartService;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShoppingCartService shoppingCartService;

	@MockBean
	private ItemRepository itemRepository;

	@MockBean
	private ToppingRepository toppingRepository;

	@BeforeEach
	void setUp() throws Exception {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("testUser");
		userInfo.setEmail("testMail");
		userInfo.setPassword("testpass");
		userInfo.setZipcode("123-4567");
		userInfo.setAddress("dummy");
		userInfo.setTelephone("080-1111-1111");
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		System.out.println(userInfo);
		LoginUser user = new LoginUser(userInfo, authorityList);

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		TestSecurityContextHolder.setAuthentication(authentication);

	}

	@Test
	void testInsertCart() throws Exception {

		ShoppingCartForm form = new ShoppingCartForm();
		form.setItemId(1);
		form.setQuantity(1);
		form.setSize("M");

		mockMvc.perform(post("/shoppingCart/insertCart").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.flashAttr("shoppingCartForm", form).with(csrf())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/shoppingCart/toCart"));

		verify(shoppingCartService, times(1)).insertCat(form, 1);
	}

	@Test
	void testToCartList() throws Exception {
		// mock
		Order order = new Order();
		order.setId(1);
		order.setUserId(1);
		order.setStatus(0);
		order.setTotalPrice(1000);
		OrderItem orderItem = new OrderItem();
		orderItem.setId(1);
		orderItem.setItemId(1);
		orderItem.setOrderId(1);
		orderItem.setQuantity(1);
		orderItem.setSize("M");
		// 通常はitemsテーブルにあるものを仮で用意
		Item item = new Item();
		item.setId(1);
		item.setName("dummyピザ");
		item.setDescription("dummydummy");
		item.setPriceM(1000);
		item.setPriceL(2000);
		item.setImagePath("dummy.jpg");
		item.setDeleted(false);

		// 通常はtoppingsテーブルにあるものを仮で用意
		Topping topping = new Topping();
		topping.setId(1);
		topping.setName("トッピング");
		topping.setId(200);
		topping.setId(3000);

		// itemsテーブルに渡すためtoppingListに詰める
		List<Topping> toppingList = new ArrayList<>();
		toppingList.add(topping);

		// Repositoryからメソッドを呼び出す動作を制御
		Mockito.doReturn(toppingList).when(toppingRepository).findAll();
		Mockito.doReturn(item).when(itemRepository).load(1);
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

		when(shoppingCartService.showCart(1)).thenReturn(order);

		mockMvc.perform(get("/shoppingCart/toCart").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("/materialize-version/cart_list")).andExpect(model().attributeExists("order"));

		verify(shoppingCartService, times(1)).showCart(1);
	}

	@Test
	void testShowCart() throws Exception {
		// mock
		Order order = new Order();
		order.setId(1);
		order.setUserId(1);
		order.setStatus(0);
		order.setTotalPrice(1000);
		OrderItem orderItem = new OrderItem();
		orderItem.setId(1);
		orderItem.setItemId(1);
		orderItem.setOrderId(1);
		orderItem.setQuantity(1);
		orderItem.setSize("M");
		// 通常はitemsテーブルにあるものを仮で用意
		Item item = new Item();
		item.setId(1);
		item.setName("dummyピザ");
		item.setDescription("dummydummy");
		item.setPriceM(1000);
		item.setPriceL(2000);
		item.setImagePath("dummy.jpg");
		item.setDeleted(false);

		// 通常はtoppingsテーブルにあるものを仮で用意
		Topping topping = new Topping();
		topping.setId(1);
		topping.setName("トッピング");
		topping.setId(200);
		topping.setId(3000);

		// itemsテーブルに渡すためtoppingListに詰める
		List<Topping> toppingList = new ArrayList<>();
		toppingList.add(topping);

		// Repositoryからメソッドを呼び出す動作を制御
		Mockito.doReturn(toppingList).when(toppingRepository).findAll();
		Mockito.doReturn(item).when(itemRepository).load(1);
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

		when(shoppingCartService.showCart(1)).thenReturn(order);

		mockMvc.perform(get("/shoppingCart/showCart").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("/materialize-version/cart_list")).andExpect(model().attributeExists("order"));

		verify(shoppingCartService, times(1)).showCart(1);
	}

	@Test
	void testDeleteCartItems() throws Exception {
		mockMvc.perform(get("/shoppingCart/deleteCart").param("orderItemId", "1")
				.with(user("user").password("password").roles("USER"))).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/shoppingCart/toCart"));

		verify(shoppingCartService, times(1)).deleteByOrderId(1);
	}

	@Test
	void testAllDeleteOrderItem() throws Exception {
		mockMvc.perform(get("/shoppingCart/allDeleteOrderItem").param("orderId", "1")
				.with(user("user").password("password").roles("USER"))).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/shoppingCart/toCart"));

		verify(shoppingCartService, times(1)).allDeleteOrderItem(1);
	}
}
