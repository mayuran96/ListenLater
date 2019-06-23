package com.musical.memories.musicalmemories.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musical.memories.musicalmemories.api.auth.Authentication;
import com.musical.memories.musicalmemories.datatransferobjects.GeniusResponse;
import com.musical.memories.musicalmemories.datatransferobjects.Hit;
import com.musical.memories.musicalmemories.datatransferobjects.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

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
        //httpHeaders.set("Content-Type", "application/json");

        return httpHeaders;
    }

    private List<Hit> findHits(String searchString)
    {
        List<Hit> hits = new LinkedList<>();

        try {
            URI uri = new URI(String.format("https://api.genius.com/search?q=%s", URLEncoder.encode(searchString, "UTF-8")));
            logger.info(uri.toString());
            logger.info("headers: "+createHeaders());
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<GeniusResponse> findGeniusResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, GeniusResponse.class);
            if(findGeniusResponse!=null){
                GeniusResponse geniusResponse = findGeniusResponse.getBody();
                Response response = geniusResponse.getResponse();
                if(response!=null)
                {
                   hits = response.getHits();
                }
            }


        }
        catch(URISyntaxException | IOException ex)
        {
            logger.error(ex.toString());
        }
        return hits;
    }

    public String findTopHitTitle(String searchString)
    {
        List<Hit> hits = findHits(searchString);
        String title = "";
        if(hits.isEmpty())
        {
            title = "Song was not found";
        }
        else
        {
            title = hits.get(0).getResult().getTitle();
        }
        return title;
    }





}
