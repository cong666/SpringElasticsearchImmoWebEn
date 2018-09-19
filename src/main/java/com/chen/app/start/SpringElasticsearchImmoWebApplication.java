package com.chen.app.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by: ccong
 * Date: 18/8/25 下午3:20
 */
@SpringBootApplication(scanBasePackages={"com.chen"})
public class SpringElasticsearchImmoWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringElasticsearchImmoWebApplication.class, args);
	}
}
