package com.musical.memories.musicalmemories.controllers;

import com.musical.memories.musicalmemories.api.Spotify;
import com.musical.memories.musicalmemories.api.SpotifyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UserController {
    private SpotifyService spotifyService = new SpotifyService();

    @RequestMapping("/callback")
    public String tokenValue(@RequestParam(value="code", defaultValue="Spotify access code") String value) {
        spotifyService.setCode(value);
        spotifyService.authorizationCode();
        return null;
    }

    @RequestMapping("/call")
    public String tokenValue() {
        return null;
    }

    @RequestMapping("/")
    public void val(HttpServletResponse response) throws IOException {
        SpotifyService spotifyService = new SpotifyService();
        String val = spotifyService.authorization();
        response.sendRedirect(val);
    }
}
