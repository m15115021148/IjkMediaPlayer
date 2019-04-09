// ExoPlayer as IMediaPlayer: optional, experimental
implementation 'tv.danmaku.ijk.media:ijkplayer-exo:0.8.8'


public IjkVideoView mVideoView;
private AndroidMediaController mMediaController;
private IjkMediaPlayer ijkMediaPlayer;


private void initMediaPlayer() {
	mVideoView.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
	});
	//default url
	mMediaUrl = RequestCode.DEFAULT_RTSP_HEADER + CameraCommand.getCameraIp()+ RequestCode.DEFAULT_MJPEG_PUSH_URL ;

	mMediaController = new AndroidMediaController(this, false);


	IjkMediaPlayer.loadLibrariesOnce(null);
	IjkMediaPlayer.native_profileBegin("libijkplayer.so");
	ijkMediaPlayer = new IjkMediaPlayer();
	ijkMediaPlayer.setLogEnabled(false);

	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 10240L);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L);

	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 30);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_YV12);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 1024);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 10);
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probsize", "4096");
	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", "2000000");

	ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

	mVideoView.setMediaController(mMediaController);

	mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(IMediaPlayer iMediaPlayer) {
			LogUtil.e("setOnPreparedListener");
			mHandler.sendEmptyMessage(WHAT_START_LOADING);
		}
	});
	mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(IMediaPlayer iMediaPlayer) {
			LogUtil.e("setOnCompletionListener");
		}
	});
	mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
			LogUtil.e("setOnErrorListener");
			mContext.finish();
			return false;
		}
	});
}

private void start(){
	ijkMediaPlayer.start();
	mVideoView.setVideoURI(Uri.parse(mMediaUrl));
	mVideoView.start();
}