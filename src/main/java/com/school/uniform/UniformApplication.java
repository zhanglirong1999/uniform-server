package com.school.uniform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages ="com.school.uniform.model.dao.mapper" ) // 注意，要换成 tk 提供的 @MapperScan 注解
public class UniformApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniformApplication.class, args);
    }

}
