package com.example.contorller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.domain.Item;
import com.example.service.ShowItemDetailService;

@SpringBootTest
@AutoConfigureMockMvc
public class ShowItemDetailControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShowItemDetailService showItemDetailService;

	@Test
	public void showItemDetailTest() throws Exception {
		Item item = new Item();
		item.setId(1);
		item.setName("テスト商品");
		item.setPriceM(100);
		when(showItemDetailService.showItemDetail(1)).thenReturn(item);
		mockMvc.perform(MockMvcRequestBuilders.get("/ShowItemDetail/ToItemDetail?id=1")).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/materialize-version/item_detail.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("Item"));
	}
}
