package com.musical.memories.musicalmemories.api;

import com.musical.memories.musicalmemories.api.auth.Authentication;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class SpotifyService {

    private SpotifyApi spotifyApi;
    private static final String redirectURI = "http://localhost:8080/callback";
    private Logger logger = LoggerFactory.getLogger(SpotifyService.class);
    private String clientId;
    private String clientSecret;
    private String code;

    public SpotifyService()
    {
        this.clientId = new Authentication().getAPIKey("client-id");
        this.clientSecret = new Authentication().getAPIKey("client-secret");
        try {
            spotifyApi = new SpotifyApi.Builder()
                    .setClientId(this.clientId)
                    .setClientSecret(this.clientSecret)
                    .setRedirectUri(new URI(redirectURI))
                    .build();
        }
        catch(Exception ex){
            logger.info("error in creating spotify api obj: "+ex.toString());
        }
    }

    public String authorization() {
        try {
            final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().build();
            final URI uri = authorizationCodeUriRequest.execute();
            URL myURL = new URL(uri.toString());
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            System.out.println("URI: " + uri.toString());
            return uri.toString();
        }
        catch(Exception ex)
        {

        }
        return null;
    }



    public void authorizationCode() {
        try {
            final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                    .build();
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }
}
