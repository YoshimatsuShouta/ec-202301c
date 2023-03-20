package com.example.contorller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.domain.Item;
import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.UserInfo;
import com.example.form.OrderForm;
import com.example.repository.ItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ToppingRepository;
import com.example.service.OrderConfirmService;
import com.example.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private OrderController orderController;

	@MockBean
	private ToppingRepository toppingRepository;

	@MockBean
	private ItemRepository itemRepository;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private OrderService orderService;

	@MockBean
	private OrderConfirmService orderConfirmService;

	@MockBean
	private BindingResult bindingResult;

	@BeforeEach
	public void setUp() {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("testUser");
		userInfo.setEmail("testmail@xxx.com");
		userInfo.setPassword("testpass");
		userInfo.setZipcode("123-4567");
		userInfo.setAddress("dummy");
		userInfo.setTelephone("080-1111-1111");
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		LoginUser user = new LoginUser(userInfo, authorityList);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		TestSecurityContextHolder.setAuthentication(authentication);

	}

	@Test
	@DisplayName("注文確認画面へ遷移する")
	void testOrderConfirm() throws Exception {

		// 検証と実行
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

		Mockito.doReturn(order).when(orderConfirmService).GetOrderId(1);

		mockMvc.perform(MockMvcRequestBuilders.get("/orderCon/toOrderConfirm?orderId=1")
				.flashAttr("orderForm", new OrderForm()).with(csrf())).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/materialize-version/order_confirm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("order"))
				.andExpect(model().attributeExists("orderForm"));

		verify(orderConfirmService, times(1)).GetOrderId(1);
	}

	@Test
	@DisplayName("商品購入画面で正当な値が入力された場合、購入完了画面へ遷移")
	void testOrder() throws Exception {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("testUser");
		userInfo.setEmail("testmail@xxx.com");
		userInfo.setPassword("testpass");
		userInfo.setZipcode("123-4567");
		userInfo.setAddress("dummy");
		userInfo.setTelephone("080-1111-1111");
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		LoginUser user = new LoginUser(userInfo, authorityList);

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		TestSecurityContextHolder.setAuthentication(authentication);
		// mock
		Order order = mock(Order.class);
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
		item.setImagePath("1.jpg");
		item.setDeleted(false);

		// 通常はtoppingsテーブルにあるものを仮で用意
		Topping topping = new Topping();
		topping.setId(1);
		topping.setName("トッピング");
		topping.setPriceM(200);
		topping.setPriceL(300);
		// itemsテーブルに渡すためtoppingListに詰める
		List<Topping> toppingList = new ArrayList<>();
		toppingList.add(topping);
		item.setToppingList(toppingList);

		// Repositoryからメソッドを呼び出す動作を制御
		Mockito.doReturn(toppingList).when(toppingRepository).findAll();
		Mockito.doReturn(item).when(itemRepository).load(1);
		orderItem.setItem(itemRepository.load(1));
		OrderTopping orderTopping = new OrderTopping();
		orderTopping.setTopping(topping);
		orderTopping.setId(1);
		orderTopping.setOrderItemId(1);
		orderTopping.setToppingId(1);
		List<OrderTopping> orderToppingList = new ArrayList<>();
		orderToppingList.add(orderTopping);
		orderItem.setOrderToppingList(orderToppingList);

		List<OrderItem> orderItemList = new ArrayList<>();
		orderItemList.add(orderItem);
		order.setOrderItemList(orderItemList);

		Mockito.doReturn(order).when(orderConfirmService).GetOrderId(anyInt());

		OrderForm form = new OrderForm();
		form.setId(1);
		form.setStatus(0);
		form.setTotalPrice(3000);
		form.setDestinationName("dummy");
		form.setDestinationEmail("dummy@xxx.com");
		form.setDestinationZipcode("123-4567");
		form.setDestinationAddress("dummy県");
		form.setDestinationTel("080-0000-0000");
		form.setDeliveryDate("2023-3-20");
		form.setDeliveryTime(16);
		form.setPaymentMethod(1);
		Model model = new ExtendedModelMap();
		model.addAttribute("order", order);
		model.addAttribute("orderItemList", order.getOrderItemList());

		mockMvc.perform(MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", form)
				.param("orderId", "1").with(csrf())).andExpect(redirectedUrl("/orderCon/toFinished"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.model().hasNoErrors());
	}

}
