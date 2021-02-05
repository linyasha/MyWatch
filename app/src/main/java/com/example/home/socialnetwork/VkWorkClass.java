package com.example.home.socialnetwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.home.socialnetwork.fragments.AlbumFragment;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class VkWorkClass {

    ArrayList<String> name, url, id_album;

    public VkWorkClass(ArrayList<String> name, ArrayList<String> url, ArrayList<String> id_album) {
        this.name = name;
        this.url = url;
        this.id_album = id_album;
    }

    void getVkAlbums() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("need_covers", 1);
        map.put("need_system", 1);
        VKRequest request = new VKRequest("photos.getAlbums", new VKParameters(map));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject js = response.json.getJSONObject("response");
                    JSONArray json = js.getJSONArray("items");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject oneAlbum = json.getJSONObject(i);
                        id_album.add(oneAlbum.getString("id"));
                        name.add(oneAlbum.getString("title"));
                        url.add(oneAlbum.getString("thumb_src"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }
        });

    }

    void getVkAlbumPhoto(String albumID) {
        if (albumID.equals("-6")) {
            albumID = "profile";
        } else if (albumID.equals("-7")) {
            albumID = "wall";
        } else if (albumID.equals("-15")) {
            albumID = "saved";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("album_id", albumID);
        map.put("photo_sizes", 1);
        VKRequest request = new VKRequest("photos.get", new VKParameters(map));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject js = response.json.getJSONObject("response");
                    Log.v("photos",js.toString());
                    JSONArray json = js.getJSONArray("items");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject onePhoto = json.getJSONObject(i);
                        JSONArray sizes = onePhoto.getJSONArray("sizes");
                        name.add(onePhoto.getString("text"));
                        long unixSec = onePhoto.getInt("date");
                        Date date = new Date(unixSec*1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                        String formatted_date=simpleDateFormat.format(date);
                        id_album.add(formatted_date);
                        for (int j = 0; j < sizes.length(); j++) {
                            JSONObject types = sizes.getJSONObject(j);
                            if (types.getString("type").equals("x")) {
                                url.add(types.getString("src"));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }
        });
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<String> getId_album() {
        return id_album;
    }

}
