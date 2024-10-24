package com.iGoose.Kok;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.iGoose.Kok.iGoose.auth.mapper")  // 매퍼 인터페이스가 있는 패키지
@EnableScheduling
public class GooseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GooseApplication.class, args);
    }
}
