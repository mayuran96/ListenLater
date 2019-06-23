package com.musical.memories.musicalmemories.api.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class Authentication {
    private Logger logger = LoggerFactory.getLogger(Authentication.class);

    public String getAPIKey(String apiKey)
    {
        String key = null;
        try
        {
            Yaml yaml = new Yaml();
            FileInputStream inputStream = new FileInputStream("/Users/arjunmayur/Documents/Musical-Memories/secrets/secrets.yml");
            Map<String, Object> secrets = yaml.load(inputStream);
            key = secrets.get(apiKey).toString();

            inputStream.close();
        }
        catch(IOException ex)
        {
            logger.info("Input Stream was not closed: "+ex.toString());
        }
        return key;
    }
}
