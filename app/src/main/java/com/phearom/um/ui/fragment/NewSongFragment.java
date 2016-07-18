package com.phearom.um.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phearom.api.adapter.RecyclerBrowseAdapter;
import com.phearom.api.handler.ClickHandler;
import com.phearom.um.R;
import com.phearom.um.databinding.FragmentSingerBinding;
import com.phearom.um.model.MyMusicProvider;
import com.phearom.um.server.OnResultCallback;
import com.phearom.um.server.RequestGetNewSong;
import com.phearom.um.utils.LogHelper;
import com.phearom.um.utils.MediaIDHelper;
import com.phearom.um.utils.NetworkHelper;

import java.util.List;

import static com.phearom.um.utils.MediaIDHelper.MEDIA_ID_ROOT;

/**
 * Created by phearom on 7/15/16.
 */
public class NewSongFragment extends BaseFragment {
    private static final String TAG = NewSongFragment.class.getSimpleName();
    private FragmentSingerBinding mBinding;

    private RecyclerBrowseAdapter mBrowserAdapter;
    private String mMediaId = MEDIA_ID_ROOT;

    private MediaFragmentListener mMediaFragmentListener;
    private View mErrorView;
    private TextView mErrorMessage;

    private final BroadcastReceiver mConnectivityChangeReceiver = new BroadcastReceiver() {
        private boolean oldOnline = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mMediaId != null) {
                boolean isOnline = NetworkHelper.isOnline(context);
                if (isOnline != oldOnline) {
                    oldOnline = isOnline;
                    checkForUserVisibleErrors(false);
                    if (isOnline) {
                        mBrowserAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    // Receive callbacks from the MediaController. Here we update our state such as which queue
    // is being shown, the current title and description and the PlaybackState.
    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                    if (metadata == null) {
                        return;
                    }
                    LogHelper.d(TAG, "Received metadata change to media ",
                            metadata.getDescription().getMediaId());
                    mBrowserAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                    LogHelper.d(TAG, "Received state change: ", state);
                    checkForUserVisibleErrors(false);
                    mBrowserAdapter.notifyDataSetChanged();
                }
            };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMediaFragmentListener = (MediaFragmentListener) context;
    }

    @Override
    public String getTitle() {
        return "New Song";
    }

    @Override
    public Integer getIcon() {
        return null;
    }

    public void refresh() {
        MyMusicProvider.init().reset();
        mBrowserAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_singer, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mErrorView = mBinding.playbackError;
        mErrorMessage = mBinding.errorMessage;

        mBrowserAdapter = new RecyclerBrowseAdapter(getContext());
        mBinding.recyclerSinger.setAdapter(mBrowserAdapter);

        mBrowserAdapter.setClickHandler(new ClickHandler<MediaMetadataCompat>() {
            @Override
            public void onClick(final MediaMetadataCompat item, View v) {
                checkForUserVisibleErrors(false);
                if (!MyMusicProvider.init().isInitialized()) {
                    MyMusicProvider.init().retrieveMedia(mBrowserAdapter.getItems().iterator(), new MyMusicProvider.Callback() {
                        @Override
                        public void onMusicCatalogReady(boolean success) {
                            mMediaFragmentListener.onMediaItemSelected(mBrowserAdapter.getMediaItem(item));
                            mBrowserAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    mMediaFragmentListener.onMediaItemSelected(mBrowserAdapter.getMediaItem(item));
                    mBrowserAdapter.notifyDataSetChanged();
                }
            }
        });
        onConnected();
    }


    @Override
    public void onConnected() {
        RequestGetNewSong getNewSong = new RequestGetNewSong(getContext());
        getNewSong.setOnResultCallback(new OnResultCallback<List<MediaMetadataCompat>>() {
            @Override
            public void onResult(List<MediaMetadataCompat> result) {
                mBrowserAdapter.setItems(result);
            }
        });
        getNewSong.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(mConnectivityChangeReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaControllerCompat controller = getActivity()
                .getSupportMediaController();
        if (controller != null) {
            controller.unregisterCallback(mMediaControllerCallback);
        }
        getContext().unregisterReceiver(mConnectivityChangeReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMediaFragmentListener = null;
    }

    public String getMediaId() {
        return mMediaId;
    }

    public void setMediaId(String mediaId) {
        this.mMediaId = mediaId;
    }

    private void checkForUserVisibleErrors(boolean forceError) {
        boolean showError = forceError;
        if (!NetworkHelper.isOnline(getActivity())) {
            mErrorMessage.setText(R.string.error_no_connection);
            showError = true;
        } else {
            MediaControllerCompat controller = getActivity()
                    .getSupportMediaController();
            if (controller != null
                    && controller.getMetadata() != null
                    && controller.getPlaybackState() != null
                    && controller.getPlaybackState().getState() == PlaybackStateCompat.STATE_ERROR
                    && controller.getPlaybackState().getErrorMessage() != null) {
                mErrorMessage.setText(controller.getPlaybackState().getErrorMessage());
                showError = true;
            } else if (forceError) {
                mErrorMessage.setText(R.string.error_loading_media);
                showError = true;
            }
        }
        mErrorView.setVisibility(showError ? View.VISIBLE : View.GONE);
        LogHelper.d(TAG, "checkForUserVisibleErrors. forceError=", forceError,
                " showError=", showError,
                " isOnline=", NetworkHelper.isOnline(getActivity()));
    }

    private void updateTitle() {
        if (MediaIDHelper.MEDIA_ID_ROOT.equals(mMediaId)) {
            mMediaFragmentListener.setToolbarTitle(null);
            return;
        }

        MediaBrowserCompat mediaBrowser = mMediaFragmentListener.getMediaBrowser();
        mediaBrowser.getItem(mMediaId, new MediaBrowserCompat.ItemCallback() {
            @Override
            public void onItemLoaded(MediaBrowserCompat.MediaItem item) {
                mMediaFragmentListener.setToolbarTitle(
                        item.getDescription().getTitle());
            }
        });
    }
}
