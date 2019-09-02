package com.wzz.video;

import org.n3r.idworker.Sid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.wzz.video","org.n3r.idworker"})
@MapperScan(basePackages = {"com.wzz.video.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public Sid sid() {
        return new Sid();
    }

}
