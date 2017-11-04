package com.zheng.project.android.dribbble.dribbble.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.CookieManager;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.DribbboApplication;
import com.zheng.project.android.dribbble.models.Bucket;
import com.zheng.project.android.dribbble.models.Comment;
import com.zheng.project.android.dribbble.models.Like;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.models.ShotQueryParameter;
import com.zheng.project.android.dribbble.models.User;
import com.zheng.project.android.dribbble.utils.AuthUtils;
import com.zheng.project.android.dribbble.utils.ModelUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Dribbble {

    public static final int COUNT_PER_PAGE = 12;
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";
    private static final String TAG = "Dribbble API";

    private static final String API_URL = "https://api.dribbble.com/v1/";

    private static final String USER_END_POINT = API_URL + "user";
    private static final String USERS_END_POINT = API_URL + "users";

    private static final String SHOTS_END_POINT = API_URL + "shots";
    private static final String BUCKETS_END_POINT = API_URL + "buckets";

    public static final String SHOTS_SORT_BY_DEFAULT = "popularity";
    public static final String SHOTS_SORT_BY_COMMENTS = "comments";
    public static final String SHOTS_SORT_BY_VIEWS = "views";
    public static final String SHOTS_SORT_BY_RECENT= "recent";

    public static final String SHOTS_AT_FROM_NOW = "now";
    public static final String SHOTS_AT_LAST_WEEK = "week";
    public static final String SHOTS_AT_LAST_MONTH = "month";
    public static final String SHOTS_AT_LAST_YEAR = "year";
    public static final String SHOTS_TILL_NOW = "ever";

    public static final String SHOTS_LIST_TYPE_DEFAULT = "any";
    public static final String SHOTS_LIST_TYPE_ANIMATED = "animated";

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "name";
    private static final String KEY_SHOT_ID = "shot_id";

    private static final TypeToken<User> USER_TYPE = new TypeToken<User>(){};
    private static final TypeToken<List<Shot>> SHOT_LIST_TYPE = new TypeToken<List<Shot>>(){};
    private static final TypeToken<List<Bucket>> BUCKET_LIST_TYPE = new TypeToken<List<Bucket>>(){};
    private static final TypeToken<Bucket> BUCKET_TYPE = new TypeToken<Bucket>(){};
    private static final TypeToken<List<Like>> LIKE_LIST_TYPE = new TypeToken<List<Like>>(){} ;
    private static final TypeToken<Like> LIKE_TYPE = new TypeToken<Like>(){};
    private static final TypeToken<List<Comment>> COMMENT_LIST_TYPE = new TypeToken<List<Comment>>(){};

    private static OkHttpClient client = new OkHttpClient();

    private static String accessToken;
    private static User user;

    public static void init(@NonNull Context context) {
        accessToken = AuthUtils.loadAccessToken(context, KEY_ACCESS_TOKEN);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }

    private static Request.Builder authRequestBuilder(String url) {
        String token = accessToken == null ? Auth.CLIENT_ACCESS_TOKEN : accessToken;

        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .url(url);
    }

    private static void checkStatusCode(Response response,
                                        int statusCode) throws DribbbleException {
        if (response.code() != statusCode) {
            throw new DribbbleException(response.code() + ": " + response.message());
        }
    }

    public static void login(@NonNull Context context,
                             @NonNull String accessToken) throws DribbbleException {
        Dribbble.accessToken = accessToken;
        storeAccessToken(context, accessToken);

        Dribbble.user = getUser();
        storeUser(context, user);
    }


    public static void logout(@NonNull Context context) {
        storeAccessToken(context, null);
        storeUser(context, null);

        CookieManager cm = CookieManager.getInstance();
        cm.removeSessionCookie();
        cm.removeAllCookie();

        accessToken = null;
        user = null;
    }

    public static User getCurrentUser() {
        return user;
    }

    private static User loadUser(Context context) {
        return ModelUtils.read(context, KEY_USER, new TypeToken<User>(){});
    }

    private static void storeUser(Context context, User user) {
        ModelUtils.save(context, KEY_USER, user);
    }

    private static Response makeGetRequest(String url) throws DribbbleException {
        Request request = authRequestBuilder(url).build();
        return makeRequest(request);
    }

    private static Response makePostRequest(String url,
                                            RequestBody requestBody) throws DribbbleException {
        Request request = authRequestBuilder(url)
                .post(requestBody)
                .build();
        return makeRequest(request);
    }

    private static Response makePutRequest(String url) throws DribbbleException {
        Request request = authRequestBuilder(url).build();
        return makeRequest(request);
    }
    private static Response makePutRequest(String url,
                                           RequestBody requestBody) throws DribbbleException {
        Request request = authRequestBuilder(url)
                .put(requestBody)
                .build();
        return makeRequest(request);
    }

    private static Response makeDeleteRequest(String url,
                                              RequestBody requestBody) throws DribbbleException {
        Request request = authRequestBuilder(url)
                .delete(requestBody)
                .build();
        return makeRequest(request);
    }

    private static Response makeDeleteRequest(String url) throws DribbbleException {
        Request request = authRequestBuilder(url)
                .delete()
                .build();
        return makeRequest(request);
    }

    private static Response makeRequest(Request request) throws DribbbleException {
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
//        Log.d(TAG, response.header("X-RateLimit-Remaining"));
  //      Log.d(TAG, "code: " + response.code());
        return response;
    }

    private static <T> T parseResponse(Response response,
                                       TypeToken<T> typeToken) throws DribbbleException {
        String responseString;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
        Log.d(TAG, responseString);

        try {
            return ModelUtils.toObject(responseString, typeToken);
        }
        catch (JsonSyntaxException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static boolean isLoggedIn() {
        return accessToken != null;
    }


    public static void storeAccessToken(@NonNull Context context, String token) {
        AuthUtils.saveAccessToken(context, KEY_ACCESS_TOKEN, token);
    }

    public static User getUser() throws DribbbleException {
        return parseResponse(makeGetRequest(USER_END_POINT), USER_TYPE);
    }

    public static boolean isFollowing(String otherId) throws DribbbleException {
        String url = USERS_END_POINT + "/" + getUser().id + "/following/"+otherId;
        Response response = makeGetRequest(url);
        return response.code() == HttpURLConnection.HTTP_NO_CONTENT;
    }

    public static void follow(String otherId) throws DribbbleException {
        String url = USERS_END_POINT + "/" + otherId +"/follow";
        FormBody formBody = new FormBody.Builder().build();
        Response response = makePutRequest(url, formBody);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static void unfollow(String otherId) throws DribbbleException {
        String url = USERS_END_POINT + "/" + otherId +"/follow";
        Response response = makeDeleteRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static Like likeShot(@NonNull String id) throws DribbbleException {
        String url = SHOTS_END_POINT + "/" + id + "/like";
        Response response = makePostRequest(url, new FormBody.Builder().build());

        checkStatusCode(response, HttpURLConnection.HTTP_CREATED);

        return parseResponse(response, LIKE_TYPE);
    }

    public static void unlikeShot(@NonNull String id) throws DribbbleException {
        String url = SHOTS_END_POINT + "/" + id + "/like";
        Response response = makeDeleteRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static boolean isLikingShot(@NonNull String id) throws DribbbleException {
        String url = SHOTS_END_POINT + "/" + id + "/like";
        Response response = makeGetRequest(url);
        switch (response.code()) {
            case HttpURLConnection.HTTP_OK:
                return true;
            case HttpURLConnection.HTTP_NOT_FOUND:
                return false;
            default:
                throw new DribbbleException(response.message());
        }
    }

    public static List<Shot> getShots(int page, ShotQueryParameter parameter) throws DribbbleException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SHOTS_END_POINT);
        stringBuilder.append("?page=");
        stringBuilder.append(page);
        stringBuilder.append("&list=");
        stringBuilder.append(parameter.list);
        stringBuilder.append("&sort=");
        stringBuilder.append(parameter.sort);
        stringBuilder.append("&timeframe=");
        stringBuilder.append(parameter.timeFrame);

        Response response = makeGetRequest(stringBuilder.toString());
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, SHOT_LIST_TYPE);
    }

    public static List<Shot> getFollowingShot(int page) throws DribbbleException {
         ///user/following/shots
        String url = USER_END_POINT + "/following/shots?page=" +page;
        Response response = makeGetRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, SHOT_LIST_TYPE);
    }

    public static List<Shot> getUserShot(String userId, int page) throws DribbbleException {
        //GET /users/:user/shots
        String url = USERS_END_POINT + "/" + userId + "/shots?page=" +page;
        Response response = makeGetRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, SHOT_LIST_TYPE);
    }

    public static List<Shot> getLikedShots(int page) throws DribbbleException {
        List<Like> likes = getLikes(page);
        List<Shot> likedShots = new ArrayList<>();
        for (Like like : likes) {
            likedShots.add(like.shot);
        }
        return likedShots;
    }

    public static List<Comment> getComments(String shotId, int page) throws DribbbleException {
        //GET /shots/:shot/comments
        Response response = makeGetRequest(SHOTS_END_POINT + "/" + shotId + "/comments?page=" + page);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, COMMENT_LIST_TYPE);
    }

    public static List<Bucket> getUserBuckets(int page) throws DribbbleException {
        Response response = makeGetRequest(USER_END_POINT + "/" + "buckets?page=" + page);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, BUCKET_LIST_TYPE);
    }

    public static List<Bucket> getUserBuckets(@NonNull String userId,
                                              int page) throws DribbbleException {
        Response response = makeGetRequest(USERS_END_POINT + "/" + userId + "/buckets?page=" + page);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, BUCKET_LIST_TYPE);
    }

    /**
     * Will return all the buckets for the logged in user
     * @return
     * @throws DribbbleException
     */
    public static List<Bucket> getUserBuckets() throws DribbbleException {
        Response response = makeGetRequest(USER_END_POINT + "/" + "buckets?per_page=" + Integer.MAX_VALUE);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, BUCKET_LIST_TYPE);
    }

    /**
     * Will return all the buckets for a certain shot
     * @param shotId
     * @return
     * @throws DribbbleException
     */
    public static List<Bucket> getShotBuckets(@NonNull String shotId) throws DribbbleException {
        Response response = makeGetRequest(SHOTS_END_POINT + "/" + shotId + "/buckets?per_page=" + Integer.MAX_VALUE);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, BUCKET_LIST_TYPE);
    }

    public static List<Shot> getBucketShots(@NonNull String bucketId,
                                            int page) throws DribbbleException {
        Response response = makeGetRequest(BUCKETS_END_POINT + "/" + bucketId + "/shots");
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, SHOT_LIST_TYPE);
    }

    public static Bucket newBucket(@NonNull String name,
                                   @NonNull String description) throws DribbbleException {
        FormBody formBody = new FormBody.Builder()
                .add(KEY_NAME, name)
                .add(KEY_DESCRIPTION, description)
                .build();
        return parseResponse(makePostRequest(BUCKETS_END_POINT, formBody), BUCKET_TYPE);
    }

    public static void updateBucket(@NonNull String bucketId,
                                    @NonNull String name,
                                    @NonNull String description) throws DribbbleException {

        String url = BUCKETS_END_POINT + "/" + bucketId;
        FormBody formBody = new FormBody.Builder()
                .add(KEY_NAME, name)
                .add(KEY_DESCRIPTION, description)
                .build();

        Response response = makePutRequest(url, formBody);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    public static void deleteBucket(@NonNull String bucketId) throws DribbbleException{
        String url = BUCKETS_END_POINT + "/" + bucketId;
        Response response = makeDeleteRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static void addBucketShot(@NonNull String bucketId,
                                     @NonNull String shotId) throws DribbbleException {
        String url = BUCKETS_END_POINT + "/" + bucketId + "/shots";
        FormBody formBody = new FormBody.Builder()
                .add(KEY_SHOT_ID, shotId)
                .build();

        Response response = makePutRequest(url, formBody);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static void removeBucketShot(@NonNull String bucketId,
                                        @NonNull String shotId) throws DribbbleException {
        String url = BUCKETS_END_POINT + "/" + bucketId + "/shots";
        FormBody formBody = new FormBody.Builder()
                .add(KEY_SHOT_ID, shotId)
                .build();

        Response response = makeDeleteRequest(url, formBody);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static List<Like> getLikes(int page) throws DribbbleException {
        String url = USER_END_POINT + "/likes?page=" + page;
        Response response = makeGetRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_OK);
        return parseResponse(response, LIKE_LIST_TYPE);
    }
}
