package com.musical.memories.musicalmemories.services;

import com.musical.memories.musicalmemories.services.auth.Authentication;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.AddTracksToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Service
public class SpotifyService {

    private SpotifyApi spotifyApi;
    private String clientId;
    private String clientSecret;
    private String code;
    private String accessToken;
    private String refreshToken;
    private SpotifyApi spotApi;
    private static final String redirectURI = "http://localhost:8080/callback";
    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);

    public SpotifyService() throws URISyntaxException
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
        catch(URISyntaxException ex)
        {
            logger.error("Could not initialize the Spotify object");
            throw ex;
        }
    }

    public URI authorization() {
        final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().scope("playlist-modify-public").build();
        return authorizationCodeUriRequest.execute();

    }


    public void authorizationCode() throws IOException, SpotifyWebApiException{
        try {
            final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                    .build();
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            String accessToken = authorizationCodeCredentials.getAccessToken();
            String refreshToken = authorizationCodeCredentials.getRefreshToken();
            setAccessToken(accessToken);
            setRefreshToken(refreshToken);
            spotifyApi.setAccessToken(accessToken);
            spotifyApi.setRefreshToken(refreshToken);
            logger.info("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            logger.error("Failed to obtain the access token and refresh token");
            throw e;
        }
    }

    public String findTrackId(String trackName)
    {
        String id = null;
        try {
            final String type = ModelObjectType.TRACK.getType();
            final SearchItemRequest searchItemRequest = spotifyApi.searchItem(trackName, type).build();
            final SearchResult searchResult = searchItemRequest.execute();
            System.out.println("Total tracks: " + searchResult);
            id = (searchResult.getTracks().getItems()[0]).getId();
        }
        catch (IOException | SpotifyWebApiException e) {
            logger.error(String.format("Failed to find the track %s", trackName));
            logger.error(e.getMessage());
        }
        return id;
    }

    public void insertTrack(String playListName, String trackId)
    {
        String[] uris = new String[]{"spotify:track:"+ trackId};
        final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists().build();
        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();
            logger.info("No. of playlists: " + playlistSimplifiedPaging.getTotal());
            PlaylistSimplified playlistMatch = null;
            for (PlaylistSimplified playlist : playlistSimplifiedPaging.getItems()) {
                if (playlist.getName().equals(playListName)) {
                    playlistMatch = playlist;
                }
            }
            if (playlistMatch != null) {
                String playListId = playlistMatch.getId();
                final AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyApi
                        .addTracksToPlaylist(playListId, uris).build();
                final SnapshotResult snapshotResult = addTracksToPlaylistRequest.execute();
                logger.info("Snapshot ID: " + snapshotResult.getSnapshotId());
            } else
            {
                logger.error("Could not find playlist");
            }

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }

    public void refresh()
    {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();
        final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh()
                .build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            logger.info("Refresh token expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            logger.error("Unable to refresh: "+e.getMessage());
        }
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
