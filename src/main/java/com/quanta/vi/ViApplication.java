package com.quanta.vi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.quanta.vi.mapper") // 注册mapper(必须)
//@EnableAsync // 允许异步任务(可选)
@SpringBootApplication
public class ViApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViApplication.class, args);
    }

}
