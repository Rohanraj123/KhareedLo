package org.khareedlo.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"org.khareedlo.common.entity", "org.khareedlo.admin.user", "org.khareedlo.admin.security"})
public class KhareedLoBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(KhareedLoBackendApplication.class, args);
    }
}