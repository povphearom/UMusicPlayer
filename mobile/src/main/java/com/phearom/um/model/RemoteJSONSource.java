///*
// * Copyright (C) 2014 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.phearom.um.model;
//
//import android.content.Context;
//import android.support.v4.media.MediaMetadataCompat;
//import android.util.Log;
//
//import com.phearom.um.config.Config;
//import com.phearom.um.utils.LogHelper;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import javax.net.ssl.HttpsURLConnection;
//
///**
// * Utility class to get a list of MusicTrack's based on a server-side JSON
// * configuration.
// */
//public class RemoteJSONSource implements MusicProviderSource {
//    private Context context;
//    public RemoteJSONSource(Context context){
//        this.context = context;
//    }
//
//    private static final String TAG = LogHelper.makeLogTag(RemoteJSONSource.class);
//
//    protected static final String CATALOG_URL = Config.AUTH.SERVER_IP + "musicservice/web_server/getkhmernewmusic";
//
//    private static final String JSON_MUSIC = "result";
//    private static final String JSON_TITLE = "music_title";
//    private static final String JSON_ALBUM = "image_album";
//    private static final String JSON_ARTIST = "singer_name";
//    private static final String JSON_GENRE = "albums";
//    private static final String JSON_SOURCE = "music_path";
//    private static final String JSON_IMAGE = "image_thumb";
//    //    private static final String JSON_TRACK_NUMBER = "trackNumber";
////    private static final String JSON_TOTAL_TRACK_COUNT = "totalTrackCount";
//    private static final String JSON_DURATION = "music_duration";
//
//    @Override
//    public Iterator<MediaMetadataCompat> iterator() {
//        try {
//            int slashPos = CATALOG_URL.lastIndexOf('/');
//            String path = CATALOG_URL.substring(0, slashPos + 1);
//            JSONObject jsonObj = fetchJSONFromUrl(CATALOG_URL);
//            ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
//            if (jsonObj != null) {
//                JSONArray jsonTracks = jsonObj.getJSONArray(JSON_MUSIC);
//                if (jsonTracks != null) {
//                    for (int j = 0; j < jsonTracks.length(); j++) {
//                        tracks.add(buildFromJSON(jsonTracks.getJSONObject(j), path));
//                    }
//                }
//            }
//            return tracks.iterator();
//        } catch (JSONException e) {
//            LogHelper.e(TAG, e, "Could not retrieve music list");
//            throw new RuntimeException("Could not retrieve music list", e);
//        }
//    }
//
//    private long duration;
//
//    private MediaMetadataCompat buildFromJSON(JSONObject json, String basePath) throws JSONException {
//        String title = json.getString(JSON_TITLE);
//        String album = json.getString(JSON_ALBUM);
//        String artist = json.getString(JSON_ARTIST);
//        String genre = json.getString(JSON_GENRE);
//        String source = json.getString(JSON_SOURCE);
//        String iconUrl = json.getString(JSON_IMAGE);
////        int trackNumber = json.getInt(JSON_TRACK_NUMBER);
////        int totalTrackCount = json.getInt(JSON_TOTAL_TRACK_COUNT);
////        try {
////            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
////            metaRetriever.setDataSource(source);
////            String time = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
////
////            Log.i("Duration",time);
////
////            duration = Long.valueOf(time);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        LogHelper.d(TAG, "Found music track: ", json);
//        // Media is stored relative to JSON file
//        if (!source.startsWith("http")) {
//            source = basePath + source;
//        }
//        if (!iconUrl.startsWith("http")) {
//            iconUrl = basePath + iconUrl;
//        }
//        // Since we don't have a unique ID in the server, we fake one using the hashcode of
//        // the music source. In a real world app, this could come from the server.
//        String id = String.valueOf(source.hashCode());
//
//        // Adding the music source to the MediaMetadata (and consequently using it in the
//        // mediaSession.setMetadata) is not a good idea for a real world music app, because
//        // the session metadata can be accessed by notification listeners. This is done in this
//        // sample for convenience only.
//        //noinspection ResourceType
//        return new MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
//                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source)
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
//                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
//                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
////                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
////                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)
//                .build();
//    }
//
//    /**
//     * Download a JSON file from a server, parse the content and return the JSON
//     * object.
//     *
//     * @return result JSONObject containing the parsed representation.
//     */
//    private JSONObject fetchJSONFromUrl(String urlString) throws JSONException {
//        BufferedReader reader = null;
//        String response = "";
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            Map<String, String> params = new HashMap<>();
//            params.put("limit", String.valueOf(25));
//            params.put("offset", String.valueOf(0));
//
//            Log.i("Param", params.toString());
//
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//            dStream.writeBytes(params.toString());
//            dStream.flush();
//            dStream.close();
//            int responseCode = connection.getResponseCode();
//
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                String line;
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                while ((line = br.readLine()) != null) {
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
//}