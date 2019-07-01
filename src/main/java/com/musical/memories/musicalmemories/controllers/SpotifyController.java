package com.musical.memories.musicalmemories.controllers;

import com.musical.memories.musicalmemories.services.GeniusService;
import com.musical.memories.musicalmemories.services.SpotifyService;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import javafx.scene.effect.Light;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RestController
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;
    private Logger logger = LoggerFactory.getLogger(SpotifyController.class);
    private String lyric;

    @PostMapping("/v1/lyric")
    public void getLyric(@RequestParam("lyric") String lyric, HttpServletResponse response) throws IOException
    {
        logger.info(String.format("lyric: %s", lyric));
        setLyric(lyric);
        //response.sendRedirect("http://localhost:3000/authorization");
    }

    @RequestMapping("/callback")
    public void callback(@RequestParam(value="code", defaultValue = "failed") String code) {
        try {
            logger.info("Callback: was successfully called");
            if (!code.equals("failed")) {
                spotifyService.setCode(code);
                spotifyService.authorizationCode();
                spotifyService.refresh();
                String trackId = spotifyService.findTrackId(lyric);
                spotifyService.insertTrack("TestPlaylist", trackId);
            }
        }
        catch(IOException | SpotifyWebApiException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in callback", e);
        }
    }

    @GetMapping("/authorization")
    public String authorization(HttpServletResponse response) throws IOException{
        //try {
            logger.info("Spotify Authorization started");
            URI authorizationURI = spotifyService.authorization();
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
            //response.sendRedirect(authorizationURI.toString());
            return authorizationURI.toString();
//        }
//        catch(IOException ex)
//        {
//            logger.error("Spotify authorization failed");
//            throw ex;
//        }
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

}
