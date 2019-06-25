package com.musical.memories.musicalmemories.api;

import com.musical.memories.musicalmemories.api.auth.Authentication;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.AddTracksToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import com.wrapper.spotify.requests.data.users_profile.GetUsersProfileRequest;
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
    private String accessToken;
    private String refreshToken;
    private SpotifyApi spotApi;

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
            final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().scope("playlist-modify-public").build();
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
            this.accessToken = authorizationCodeCredentials.getAccessToken();
            this.refreshToken = authorizationCodeCredentials.getRefreshToken();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchTrack(String queryString)
    {
        try {
//            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
//                    .setAccessToken(accessToken)
//                    .build();
            final String type = ModelObjectType.TRACK.getType();
            final SearchItemRequest searchItemRequest = spotApi.searchItem(queryString, type).build();
            final SearchResult searchResult = searchItemRequest.execute();
            System.out.println("Total tracks: " + searchResult);
            String id = (searchResult.getTracks().getItems()[0]).getId();
//            GetTrackRequest getTrackRequest = spotifyApi.getTrack(id).build();
//            String trackId = null;
//            try {
//                final Track track = getTrackRequest.execute();
//                trackId = track.getId();
//                System.out.println("Name: " + track.getName());
//            } catch (IOException | SpotifyWebApiException e) {
//                System.out.println("Error: " + e.getMessage());
//            }

            String[] uris = new String[]{"spotify:track:"+id};
            getPlaylistId("TestPlaylist", uris);
            //String playlistId = getPlaylistId();
            //AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyApi
            //        .addTracksToPlaylist(playlistId, uris)
        }catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void getPlaylistId(String playListName, String[] uris)
    {
//        SpotifyApi spotifyApi = new SpotifyApi.Builder()
//                .setAccessToken(accessToken)
//                .build();
        final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotApi
                .getListOfCurrentUsersPlaylists().build();
        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();
            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
            PlaylistSimplified play = null;
            for(PlaylistSimplified playlist : playlistSimplifiedPaging.getItems())
            {
                if(playlist.getName().equals(playListName))
                {
                    play = playlist;
                }
            }
            String playListId = play.getId();
            System.out.println(playListId);
            final AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotApi
                    .addTracksToPlaylist(playListId, uris).build();
            final SnapshotResult snapshotResult = addTracksToPlaylistRequest.execute();

            System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());


        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }

    public void refresh()
    {
        spotApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();
        final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotApi.authorizationCodeRefresh()
                .build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

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
