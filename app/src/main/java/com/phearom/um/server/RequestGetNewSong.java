package com.phearom.um.server;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.phearom.um.model.MusicProviderSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by phearom on 7/18/16.
 */
public class RequestGetNewSong extends ServerRequest<List<MediaMetadataCompat>> {

    private static final String JSON_MUSIC = "result";
    private static final String JSON_TITLE = "music_title";
    private static final String JSON_ALBUM = "image_album";
    private static final String JSON_ARTIST = "singer_name";
    private static final String JSON_GENRE = "albums";
    private static final String JSON_SOURCE = "music_path";
    private static final String JSON_IMAGE = "image_thumb";
    private static final String JSON_DURATION = "music_duration";

    private long duration = 0;

    public RequestGetNewSong(Context context) {
        super(context);
    }

    @Override
    public String getFunctionName() {
        return "getkhmernewmusic";
    }

    @Override
    public Map<String, String> getParameter(Map<String, String> params) {
        params.put("limit", "25");
        params.put("offset", "0");
        return params;
    }

    @Override
    public List<MediaMetadataCompat> getDataResponse(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_MUSIC);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    getMetadataCompatList().add(buildFromJSON(jsonArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getMetadataCompatList();
    }

    private MediaMetadataCompat buildFromJSON(JSONObject json) throws JSONException {
        String title = json.getString(JSON_TITLE);
        String album = json.getString(JSON_ALBUM);
        String artist = json.getString(JSON_ARTIST);
        String genre = json.getString(JSON_GENRE);
        String source = json.getString(JSON_SOURCE);
        String iconUrl = json.getString(JSON_IMAGE);
        try {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(source);
            String time = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Log.i("Duration", time);
            duration = Long.valueOf(time);
        } catch (Exception e) {
            e.printStackTrace();
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
}
