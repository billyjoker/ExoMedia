/*
 * Copyright (C) 2016 Brian Wernick
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

package com.devbrackets.android.exomedia.core.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.devbrackets.android.exomedia.annotation.TrackRenderType;
import com.devbrackets.android.exomedia.core.EMListenerMux;
import com.devbrackets.android.exomedia.core.EMListenerMuxDrm;
import com.devbrackets.android.exomedia.core.api.VideoViewApi;
import com.devbrackets.android.exomedia.core.builder.RenderBuilder;
import com.devbrackets.android.exomedia.core.video.delegate.ExoVideoDelegate;
import com.google.android.exoplayer.MediaFormat;

import java.util.List;
import java.util.Map;

/**
 * A {@link VideoViewApi} implementation that uses the ExoPlayer
 * as the backing media player.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ExoSurfaceVideoView extends ResizingSurfaceView implements VideoViewApi {
    protected ExoVideoDelegate delegate;

    public ExoSurfaceVideoView(Context context) {
        super(context);
        setup();
    }

    public ExoSurfaceVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public ExoSurfaceVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public ExoSurfaceVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    @Override
    public void setVideoUri(@Nullable Uri uri) {
        delegate.setVideoUri(uri);
    }

    @Override
    public void setVideoUri(@Nullable Uri uri, @Nullable RenderBuilder renderBuilder) {
        delegate.setVideoUri(uri, renderBuilder);
    }

    @Override
    public boolean restart() {
        return delegate.restart();
    }

    @Override
    public boolean setVolume(@FloatRange(from = 0.0, to = 1.0) float volume) {
        return delegate.setVolume(volume);
    }

    @Override
    public void seekTo(@IntRange(from = 0) int milliseconds) {
        delegate.seekTo(milliseconds);
    }

    @Override
    public boolean isPlaying() {
        return delegate.isPlaying();
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void pause() {
        delegate.pause();
    }

    @Override
    public void stopPlayback() {
        delegate.stopPlayback();
    }

    @Override
    public void suspend() {
        delegate.suspend();
    }

    @Override
    public int getDuration() {
        return delegate.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return delegate.getCurrentPosition();
    }

    @Override
    public int getBufferedPercent() {
        return delegate.getBufferedPercent();
    }

    @Override
    public boolean trackSelectionAvailable() {
        return delegate.trackSelectionAvailable();
    }

    @Override
    public void setTrack(@TrackRenderType int trackType, int trackIndex) {
        delegate.setTrack(trackType, trackIndex);
    }

    @Nullable
    @Override
    public Map<Integer, List<MediaFormat>> getAvailableTracks() {
        return delegate.getAvailableTracks();
    }

    @Override
    public void release() {
        delegate.release();
    }

    @Override
    public void setListenerMux(EMListenerMux listenerMux) {
        delegate.setListenerMux(listenerMux);
    }

    @Override
    public void setListenerMuxDrm(EMListenerMuxDrm listenerMux) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        if (updateVideoSize(width, height)) {
            requestLayout();
        }
    }

    /**
     * Retrieves the user agent that the EMVideoView will use when communicating
     * with media servers
     *
     * @return The String user agent for the EMVideoView
     */
    public String getUserAgent() {
        return delegate.getUserAgent();
    }

    protected void setup() {
        delegate = new ExoVideoDelegate(getContext(), this);

        getHolder().addCallback(new HolderCallback());
        updateVideoSize(0, 0);
    }

    protected class HolderCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            delegate.onSurfaceReady(holder.getSurface());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //Purposefully left blank
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            delegate.onSurfaceDestroyed();
            holder.getSurface().release();
        }
    }
}