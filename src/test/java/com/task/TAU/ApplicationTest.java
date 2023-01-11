package com.task.TAU;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Greetings from Spring Boot!")));
	}

	@Test
	public void getResetCoins() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/resetCoins").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getCoinMachineValidValue() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/coinMachine/10").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Following is minimal number of coins for 10: \n[40 coins of 0.25\n, 0 coins of 0.01\n, 0 coins of 0.1\n, 0 coins of 0.05\n]\nMachine still have: \n60 coins of 0.25\n100 coins of 0.01\n100 coins of 0.1\n100 coins of 0.05\n")));
	}

	@Test
	public void getCoinMachineNegativeValue() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/coinMachine/-10").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	@Test
	public void getCoinMachineNotEnoughCoins() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/coinMachine/50").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("There are no coins enough to change 50.0")));
	}
}
