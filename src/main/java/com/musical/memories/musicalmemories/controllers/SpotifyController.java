package com.musical.memories.musicalmemories.controllers;

import com.musical.memories.musicalmemories.services.GeniusService;
import com.musical.memories.musicalmemories.services.SpotifyService;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import javafx.scene.effect.Light;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RestController
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;
    private Logger logger = LoggerFactory.getLogger(SpotifyController.class);

    @RequestMapping("/callback")
    public void callback(@RequestParam(value="code", defaultValue = "failed") String code) {
        try {
            if (!code.equals("failed")) {
                spotifyService.setCode(code);
                spotifyService.authorizationCode();
                spotifyService.refresh();
                String trackId = spotifyService.findTrackId("Don't look back in Anger");
                spotifyService.insertTrack("TestPlaylist", trackId);
            }
        }
        catch(IOException | SpotifyWebApiException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in callback", e);
        }
    }

    @RequestMapping("/")
    public void authorization(HttpServletResponse response) throws IOException{
        try {
            URI authorizationURI = spotifyService.authorization();
            response.sendRedirect(authorizationURI.toString());
        }
        catch(IOException ex)
        {
            logger.error("Spotify authorization failed");
            throw ex;
        }
    }
}
