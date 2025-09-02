package com.mlv.dreamshop;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.event.EventListener;

// import com.mlv.dreamshop.service.Email.EmailService;

@SpringBootApplication
@EnableConfigurationProperties
public class DreamshopApplication {
	// @Autowired
	// private EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(DreamshopApplication.class, args);
	}

	// @EventListener(ApplicationReadyEvent.class)
	// public void sendEmail() {
	// 	emailService.sendSimpleEmail(
	// 		"longvy070804@gmail.com",
	// 		"This is the first email to announce",
	// 		"Send email successfully"
	// 	);
	// }

	

}

