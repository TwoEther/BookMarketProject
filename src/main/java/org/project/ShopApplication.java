package org.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Enumeration;
import java.util.Properties;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class ShopApplication {
	public static void main(String[] args) {
		System.setProperty("spring.profiles.default", "dev");
		Properties p = System.getProperties();
		Enumeration keys = p.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) p.get(key);
			System.out.println(key + ": " + value);
		}

		SpringApplication.run(ShopApplication.class, args);
	}

}
