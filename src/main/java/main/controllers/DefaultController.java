package main.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DefaultController {
    @Data
    @AllArgsConstructor
    private static class TestUser{
        private String title;
        private String subtitle;
        private String phone;
        private String email;
        private String copyright;
        private String copyrightFrom;
    }

    @GetMapping(value = "/api/init")
    public TestUser index() {
        return new TestUser("My blog","My blog", "+79996661009",
                "mail@mail.com", "Affinity C", "Affinity ltd");
    }
}