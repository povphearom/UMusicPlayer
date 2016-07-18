/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phearom.um.model;

import android.support.v4.media.MediaMetadataCompat;

import com.phearom.um.config.Config;
import com.phearom.um.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class to get a list of MusicTrack's based on a server-side JSON
 * configuration.
 */
public class RemoteJSONSource implements MusicProviderSource {
    private static final String TAG = LogHelper.makeLogTag(RemoteJSONSource.class);

    protected static final String CATALOG_URL = Config.AUTH.SERVER_IP + "musicservice/web_server/getkhmernewmusic";

    private static final String JSON_MUSIC = "result";
    private static final String JSON_TITLE = "music_title";
    private static final String JSON_ALBUM = "image_album";
    private static final String JSON_ARTIST = "singer_name";
    private static final String JSON_GENRE = "albums";
    private static final String JSON_SOURCE = "music_path";
    private static final String JSON_IMAGE = "image_thumb";
    private static final String JSON_DURATION = "music_duration";

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        try {
            int slashPos = CATALOG_URL.lastIndexOf('/');
            String path = CATALOG_URL.substring(0, slashPos + 1);
            JSONObject jsonObj = sendPost(CATALOG_URL);
            ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
            if (jsonObj != null) {
                JSONArray jsonTracks = jsonObj.getJSONArray(JSON_MUSIC);
                if (jsonTracks != null) {
                    for (int j = 0; j < jsonTracks.length(); j++) {
                        tracks.add(buildFromJSON(jsonTracks.getJSONObject(j), path));
                    }
                }
            }
            return tracks.iterator();
        } catch (Exception e) {
            LogHelper.e(TAG, e, "Could not retrieve music list");
            throw new RuntimeException("Could not retrieve music list", e);
        }
    }

    private long duration = 0;

    private MediaMetadataCompat buildFromJSON(JSONObject json, String basePath) throws JSONException {
        String title = json.getString(JSON_TITLE);
        String album = json.getString(JSON_ALBUM);
        String artist = json.getString(JSON_ARTIST);
        String genre = json.getString(JSON_GENRE);
        String source = json.getString(JSON_SOURCE);
        String iconUrl = json.getString(JSON_IMAGE);
//        try {
//            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
//            metaRetriever.setDataSource(source);
//            String time = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//
//            Log.i("Duration",time);
//
//            duration = Long.valueOf(time);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        LogHelper.d(TAG, "Found music track: ", json);
        // Media is stored relative to JSON file
        if (!source.startsWith("http")) {
            source = basePath + source;
        }
        if (!iconUrl.startsWith("http")) {
            iconUrl = basePath + iconUrl;
        }
        String id = String.valueOf(source.hashCode());

        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .build();
    }

//    private JSONObject fetchJSONFromUrl(String urlString) throws JSONException {
//        BufferedReader reader = null;
//        String response = "";
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            Map<String, String> params = new HashMap<>();
//            params.put("limit", "25");
//            params.put("offset", "0");
//
//            String parameter = params.toString().replace(",", "&");
//
//            Log.i("Param", parameter);
//
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//            dStream.writeBytes(parameter);
//            dStream.flush();
//            dStream.close();
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                String line;
//                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                while ((line = bf.readLine()) != null) {
//                    response += line;
//                }
//            } else {
//                response = "";
//            }
//            return new JSONObject(response);
//        } catch (JSONException e) {
//            throw e;
//        } catch (Exception e) {
//            LogHelper.e(TAG, "Failed to parse the json for media list", e);
//            return null;
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private JSONObject sendPost(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        Map<String, String> params = new HashMap<>();
        params.put("limit", "25");
        params.put("offset", "0");

        String parameter = params.toString().replace(",", "&");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameter);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONObject(response.toString());
    }
}
