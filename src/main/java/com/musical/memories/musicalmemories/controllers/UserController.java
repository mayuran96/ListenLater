package com.musical.memories.musicalmemories.controllers;

import com.musical.memories.musicalmemories.api.Spotify;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/callback")
    public String tokenValue(@RequestParam(value="code", defaultValue="Spotify access code") String value) {
        System.out.println("value: "+value);
        return null;
    }

    @RequestMapping("/call")
    public String tokenValue() {
        return null;
    }

    @RequestMapping("/")
    public String val() {
        Spotify spotify = new Spotify();
        String val = spotify.authorize();
        return "redirect:"+val;
    }
}
