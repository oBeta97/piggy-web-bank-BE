package oBeta.PiggyWebBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PiggyWebBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiggyWebBankApplication.class, args);
	}

}
