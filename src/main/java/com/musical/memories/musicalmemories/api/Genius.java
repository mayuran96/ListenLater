package com.musical.memories.musicalmemories.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musical.memories.musicalmemories.api.auth.Authentication;
import com.musical.memories.musicalmemories.datatransferobjects.GeniusResponse;
import com.musical.memories.musicalmemories.datatransferobjects.Hits;
import com.musical.memories.musicalmemories.datatransferobjects.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Genius {
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(Genius.class);
    private String accessToken;

    public Genius(){
        accessToken = new Authentication().getAPIKey("client-access-token");
    }



    public HttpHeaders createHeaders()
    {
        HttpHeaders httpHeaders = new  HttpHeaders();
        httpHeaders.set("Authorization", "Bearer "+accessToken);
        httpHeaders.set("Accept", "application/json");
        httpHeaders.set("Content-Type", "application/json");
        //httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return httpHeaders;
    }

    public Hits findSong(String searchString)
    {
        GeniusResponse response = null;
        try {
            URI uri = new URI(String.format("https://api.genius.com/search?q=%s", URLEncoder.encode(searchString, "UTF-8")));
            logger.info(uri.toString());
            logger.info("headers: "+createHeaders());
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<GeniusResponse> findSongResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, GeniusResponse.class);
            response = findSongResponse.getBody();
            logger.info((response.getResponse().getHits().get(0).toString()));

        }
        catch(URISyntaxException | IOException ex)
        {
            logger.error(ex.toString());
        }
        return null;

    }
}
