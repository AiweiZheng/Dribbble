package com.zheng.project.android.dribbble.dribbble.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.models.Bucket;
import com.zheng.project.android.dribbble.models.Like;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.models.User;
import com.zheng.project.android.dribbble.utils.AuthUtils;
import com.zheng.project.android.dribbble.utils.ModelUtils;

import java.io.IOException;
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

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "name";

    private static final TypeToken<User> USER_TYPE = new TypeToken<User>(){};
    private static final TypeToken<List<Shot>> SHOT_LIST_TYPE = new TypeToken<List<Shot>>(){};
    private static final TypeToken<List<Bucket>> BUCKET_LIST_TYPE = new TypeToken<List<Bucket>>(){};
    private static final TypeToken<Bucket> BUCKET_TYPE = new TypeToken<Bucket>(){};
    private static final TypeToken<List<Like>> LIKE_LIST_TYPE = new TypeToken<List<Like>>(){} ;

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
        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url);
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

    private static Response makeRequest(Request request) throws DribbbleException {
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
        Log.d(TAG, response.header("X-RateLimit-Remaining"));
        return response;
    }

    private static <T> T parseResponse(Response response,
                                       TypeToken<T> typeToken) throws DribbbleException {
        String responseString = null;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
        Log.d(TAG, responseString);
        return ModelUtils.toObject(responseString, typeToken);
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

    public static List<Shot> getShots(int page) throws DribbbleException {
        String url = SHOTS_END_POINT + "?page=" + page;
        return parseResponse(makeGetRequest(url), SHOT_LIST_TYPE);
    }

    public static List<Shot> getLikedShots(int page) throws DribbbleException {
        List<Like> likes = getLikes(page);
        List<Shot> likedShots = new ArrayList<>();
        for (Like like : likes) {
            likedShots.add(like.shot);
        }
        return likedShots;
    }

    public static List<Bucket> getUserBuckets(int page) throws DribbbleException {
        String url = USER_END_POINT + "/" + "buckets?page=" + page;
        return parseResponse(makeGetRequest(url), BUCKET_LIST_TYPE);
    }

    public static List<Bucket> getUserBuckets(@NonNull String userId,
                                              int page) throws DribbbleException {
        String url = USERS_END_POINT + "/" + userId + "/buckets?page=" + page;
        return parseResponse(makeGetRequest(url), BUCKET_LIST_TYPE);
    }

    public static List<Shot> getBucketShots(@NonNull String bucketId,
                                            int page) throws DribbbleException {
        String url = BUCKETS_END_POINT + "/" + bucketId + "/shots";
        return parseResponse(makeGetRequest(url), SHOT_LIST_TYPE);
    }

    public static Bucket newBucket(@NonNull String name,
                                   @NonNull String description) throws DribbbleException {
        FormBody formBody = new FormBody.Builder()
                .add(KEY_NAME, name)
                .add(KEY_DESCRIPTION, description)
                .build();
        return parseResponse(makePostRequest(BUCKETS_END_POINT, formBody), BUCKET_TYPE);
    }

    public static List<Like> getLikes(int page) throws DribbbleException {
        String url = USER_END_POINT + "/likes?page=" + page;
        return parseResponse(makeGetRequest(url), LIKE_LIST_TYPE);
    }
}
