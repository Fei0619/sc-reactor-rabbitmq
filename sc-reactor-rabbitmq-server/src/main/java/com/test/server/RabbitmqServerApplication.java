package com.test.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 费世程
 * @date 2021/3/16 16:03
 */
@EnableDiscoveryClient
@SpringBootApplication
public class RabbitmqServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(RabbitmqServerApplication.class, args);
  }

}
