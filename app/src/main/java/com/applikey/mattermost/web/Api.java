package com.applikey.mattermost.web;

import com.applikey.mattermost.models.auth.AuthenticationRequest;
import com.applikey.mattermost.models.auth.AuthenticationResponse;
import com.applikey.mattermost.models.team.Team;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface Api {

    @POST("/api/v3/users/login")
    Observable<Response<AuthenticationResponse>> authorize(
            @Body AuthenticationRequest authenticationRequest);

    @GET("/api/v3/teams/all")
    Observable<Map<String, Team>> listTeams();

    @GET("/api/v3/users/me")
    Observable<Response> getMe();

    @GET("/api/v3/users/initial_load")
    Observable<Response> getInitialLoad();

    @GET("/api/v3/users/direct_profiles")
    Observable<Response> getDirectProfiles();

    @GET("/api/v3/users/profiles/{teamId}")
    Observable<Response> getTeamProfiles(@Path("teamId") String teamId);

    @GET("/api/v3/users/statuses")
    Observable<Response> getUserStatuses();
}
