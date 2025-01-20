package com.aivle08.big_project_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @GetMapping("/notices")
    public String notices() {
        return "notices";
    }
}
