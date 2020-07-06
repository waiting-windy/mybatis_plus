package com.ze.mybatis_plus_chapter1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ze.mybatis_plus_chapter1.mapper")
public class MybatisPlusChapter1Application {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusChapter1Application.class, args);
    }

}
