package com.applikey.mattermost.web;

import com.applikey.mattermost.models.auth.AttachDeviceRequest;
import com.applikey.mattermost.models.auth.AuthenticationRequest;
import com.applikey.mattermost.models.auth.AuthenticationResponse;
import com.applikey.mattermost.models.auth.RestorePasswordRequest;
import com.applikey.mattermost.models.channel.Channel;
import com.applikey.mattermost.models.channel.ChannelPurposeRequest;
import com.applikey.mattermost.models.channel.ChannelRequest;
import com.applikey.mattermost.models.channel.ChannelResponse;
import com.applikey.mattermost.models.channel.ChannelTitleRequest;
import com.applikey.mattermost.models.channel.DeleteChannelResponse;
import com.applikey.mattermost.models.channel.DirectChannelRequest;
import com.applikey.mattermost.models.channel.ExtraInfo;
import com.applikey.mattermost.models.channel.Membership;
import com.applikey.mattermost.models.channel.RequestUserId;
import com.applikey.mattermost.models.commands.InviteNewMembersRequest;
import com.applikey.mattermost.models.init.InitLoadResponse;
import com.applikey.mattermost.models.post.PendingPost;
import com.applikey.mattermost.models.post.Post;
import com.applikey.mattermost.models.post.PostResponse;
import com.applikey.mattermost.models.post.PostSearchRequest;
import com.applikey.mattermost.models.team.Team;
import com.applikey.mattermost.models.user.User;
import com.applikey.mattermost.models.web.PingResponse;
import com.applikey.mattermost.utils.PrimitiveConverterFactory;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;
import rx.Single;

public class ApiDelegate implements Api {

    private final ServerUrlFactory urlFactory;
    private final OkHttpClient okHttpClient;
    private final Object mutex = new Object();
    private String serverUrl;
    private Api realApi;

    public ApiDelegate(OkHttpClient okHttpClient, ServerUrlFactory urlFactory) {
        this.urlFactory = urlFactory;
        this.okHttpClient = okHttpClient;
    }

    @Override
    public Observable<Response<AuthenticationResponse>> authorize(
            @Body AuthenticationRequest authenticationRequest) {
        return getRealApi().authorize(authenticationRequest);
    }

    @Override
    public Observable<Map<String, Team>> listTeams() {
        return getRealApi().listTeams();
    }

    @Override
    public Observable<User> getMe() {
        return getRealApi().getMe();
    }

    @Override
    public Observable<InitLoadResponse> getInitialLoad() {
        return getRealApi().getInitialLoad();
    }

    @Override
    public Observable<Map<String, User>> getDirectProfiles() {
        return getRealApi().getDirectProfiles();
    }

    @Override
    public Observable<Map<String, User>> getTeamProfiles(@Path("teamId") String teamId) {
        return getRealApi().getTeamProfiles(teamId);
    }

    @Override
    public Observable<Map<String, String>> getUserStatusesCompatible(@Body String[] userIds) {
        return getRealApi().getUserStatusesCompatible(userIds);
    }

    @Override
    public Observable<Map<String, String>> getUserStatuses() {
        return getRealApi().getUserStatuses();
    }

    @Override
    public Observable<Void> sendPasswordReset(@Body RestorePasswordRequest request) {
        return getRealApi().sendPasswordReset(request);
    }

    @Override
    public Observable<Void> deletePost(@Path("teamId") String teamId,
                                       @Path("channelId") String channelId,
                                       @Path("channelId") String postId) {
        return getRealApi().deletePost(teamId, channelId, postId);
    }

    @Override
    public Observable<Post> updatePost(@Path("teamId") String teamId,
                                       @Path("channelId") String channelId,
                                       @Body Post post) {
        return getRealApi().updatePost(teamId, channelId, post);
    }

    @Override
    public Observable<ChannelResponse> listChannels(@Path("teamId") String teamId) {
        return getRealApi().listChannels(teamId);
    }

    @Override
    public Observable<Channel> getChannelById(@Path("teamId") String teamId,
                                              @Path("channelId") String channelId) {
        return getRealApi().getChannelById(teamId, channelId);
    }

    @Override
    public Single<PingResponse> ping() {
        return getRealApi().ping();
    }

    @Override
    public Observable<PostResponse> getPostsPage(@Path("teamId") String teamId,
                                                 @Path("channelId") String channelId,
                                                 @Path("offset") int offset,
                                                 @Path("limit") int limit) {
        return getRealApi().getPostsPage(teamId, channelId, offset, limit);
    }

    @Override
    public Observable<PostResponse> getLastPost(@Path("teamId") String teamId,
                                                @Path("channelId") String channelId) {
        return getRealApi().getLastPost(teamId, channelId);
    }

    @Override
    public Observable<ExtraInfo> getChannelExtra(@Path("teamId") String teamId,
                                                 @Path("channelId") String channelId) {
        return getRealApi().getChannelExtra(teamId, channelId);
    }

    @Override
    public Observable<Post> createPost(@Path("teamId") String teamId,
                                       @Path("channelId") String channelId,
                                       @Body PendingPost request) {
        return getRealApi().createPost(teamId, channelId, request);
    }

    @Override
    public Observable<Response<String>> updateLastViewedAt(@Path("teamId") String teamId,
                                                           @Path("channelId") String channelId) {
        return getRealApi().updateLastViewedAt(teamId, channelId);
    }

    @Override
    public Observable<Channel> createChannel(@Path("team_id") String teamId, @Body
            ChannelRequest request) {
        return getRealApi().createChannel(teamId, request);
    }

    @Override
    public Observable<Membership> addUserToChannel(@Path("team_id") String teamId,
                                                   @Path("channel_id") String channelId,
                                                   @Body RequestUserId userId) {
        return getRealApi().addUserToChannel(teamId, channelId, userId);
    }

    @Override
    public Observable<Response<AttachDeviceRequest>> attachDevice(@Body AttachDeviceRequest request) {
        return getRealApi().attachDevice(request);
    }

    @Override
    public Observable<Channel> createChannel(@Path("team_id") String teamId,
                                             @Body DirectChannelRequest request) {
        return getRealApi().createChannel(teamId, request);
    }

    @Override
    public Observable<DeleteChannelResponse> deleteChannel(@Path("teamId") String teamId,
            @Path("channelId") String channelId) {
        return getRealApi().deleteChannel(teamId, channelId);
    }

    public Single<ChannelResponse> getChannelsUserHasNotJoined(@Path("team_id") String teamId) {
        return getRealApi().getChannelsUserHasNotJoined(teamId);
    }

    @Override
    public Observable<Void> inviteNewMember(@Path("team_id") String teamId, @Body InviteNewMembersRequest body) {
        return getRealApi().inviteNewMember(teamId, body);
    }

    @Override
    public Observable<PostResponse> searchPosts(@Path("team_id") String teamId, @Body
            PostSearchRequest request) {
        return getRealApi().searchPosts(teamId, request);
    }

    @Override
    public Observable<Void> uploadImage(
            @Part MultipartBody.Part image) {

        return getRealApi().uploadImage(image);
    }

    @Override
    public Observable<User> editUser(@Body User user) {
        return getRealApi().editUser(user);
    }

    @Override
    public Single<Void> leaveChannel(@Path("team_id") String teamId,
                                         @Path("channel_id") String channelId) {
        return getRealApi().leaveChannel(teamId, channelId);
    }

    @Override
    public Observable<Channel> updateChannelTitle(@Path("teamId") String teamId,
                                                  @Body ChannelTitleRequest request) {
        return getRealApi().updateChannelTitle(teamId, request);
    }

    @Override
    public Observable<Channel> updateChannelPurpose(@Path("teamId") String teamId,
                                                    @Body ChannelPurposeRequest request) {
        return getRealApi().updateChannelPurpose(teamId, request);
    }

    private Api getRealApi() {
        final String currentServerUrl = urlFactory.getServerUrl();
        if (!isSameServerRequested(currentServerUrl)) {
            synchronized (mutex) {
                if (!isSameServerRequested(currentServerUrl)) {
                    serverUrl = currentServerUrl;
                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(serverUrl)
                            .client(okHttpClient)
                            .addConverterFactory(PrimitiveConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                    //noinspection UnnecessaryLocalVariable
                    realApi = retrofit.create(Api.class);
                }
            }
        }
        return realApi;
    }

    private boolean isSameServerRequested(String requestedServer) {
        return serverUrl != null && serverUrl.equals(requestedServer);
    }

    @Override
    public Single<Channel> joinToChannel(String teamId, String channelId) {
        return getRealApi().joinToChannel(teamId, channelId);
    }
}
