package com.know.wenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主程序入口
 *
 * @author hlb
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class KnownoWendaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnownoWendaApplication.class, args);
    }
}
