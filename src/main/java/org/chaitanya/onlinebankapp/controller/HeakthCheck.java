package org.chaitanya.onlinebankapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ok")
public class HeakthCheck {
    @GetMapping
    public String ok() {
        return "ok";
    }
}
