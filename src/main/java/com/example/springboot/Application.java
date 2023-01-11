package com.example.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootApplication
public class Application {

	static double coins[] = { 0.01, 0.05, 0.10, 0.25 };
	static int coinsLength = coins.length;
	static int maxNumberOfEachCoin = 100;
	static Map<Double,Integer> coinsLeft = new HashMap<Double,Integer>();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			initiateMachineCoins();
		};
	}

	@RestController
	public class CoinMachineController {

		@GetMapping("/coinMachine/{value}")
		public ResponseEntity coinMachine(@PathVariable Long value) {
			StringBuilder result = new StringBuilder();
			result.append("Following is minimal number of coins for " + value + ": \n");
			try {
				result.append(findMin(value)+ "\n");	
			} catch (Exception ex) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
            		.body(ex.getMessage());
			}
			result.append("Machine still have: \n");
			for (Double key : coinsLeft.keySet()) {
				result.append(coinsLeft.get(key) + " coins of " + key + "\n");
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED)                 
            	.body(result.toString());
		}

		@GetMapping("/resetCoins")
		public void resetCoins() {
			initiateMachineCoins();
		}

	}

	static List<String> findMin(double value) throws Exception {
		// Initialize result
		Map<Double,Integer> numberOfCoins = new HashMap<Double,Integer>();
		double originalValue = value;
		List<String> resultStrings = new ArrayList<String>();

		for (int i = coinsLength - 1; i >= 0; i--) {
			if (value < 0)
				throw new Exception("Machine does not accept negative values");
			int coinsCount = 0;
			int amountOfCoinsLeft = coinsLeft.get(coins[i]);
			while (value >= coins[i] && coinsCount < amountOfCoinsLeft) {
				value -= coins[i];
				coinsCount++;
			}
			numberOfCoins.put(coins[i], coinsCount);
			coinsLeft.put(coins[i], amountOfCoinsLeft - coinsCount);
		}

		if (value > 0.01) {
			throw new Exception("There are no coins enough to change " + originalValue);
		} else {
			for (Double key : numberOfCoins.keySet()) {
				resultStrings.add(numberOfCoins.get(key) + " coins of " + key + "\n");
			}
		}

		return resultStrings;
	}

	static void initiateMachineCoins() {
		for (int i = coinsLength - 1; i >= 0; i--) {
			coinsLeft.put(coins[i], maxNumberOfEachCoin);
		}
	}
}
