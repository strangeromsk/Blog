package main.controllers;

import main.model.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    private Tag tag = new Tag();

    @GetMapping(value = "/api/tag")
    public Tag getTags(@RequestParam String query) {


        return tag;
    }
}
