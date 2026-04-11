package lkt.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "lkt")
public class ErrorTicketManagerApplication {

	static {
		System.setProperty("user.timezone", "Asia/Ho_Chi_Minh");
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ErrorTicketManagerApplication.class, args);
	}

}

