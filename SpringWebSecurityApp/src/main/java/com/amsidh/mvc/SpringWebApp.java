package com.amsidh.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;

@SpringBootApplication
public class SpringWebApp {
    static {
        String kerbPath = Paths.get(".\\kdc-work\\krb5.conf").normalize().toAbsolutePath().toString();
        System.setProperty("java.security.krb5.conf", kerbPath);
        System.setProperty("sun.security.krb5.debug", "true");
    }

    public static void main(String... args) {
        SpringApplication.run(SpringWebApp.class, args);
    }
}
