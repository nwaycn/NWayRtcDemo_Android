package com.nwayrtc.NWayRtcDemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nwayrtc.sdk.CallInfoBean;
import com.nwayrtc.sdk.CallLogBean;
import com.nwayrtc.sdk.NWayRtc;
import com.nwayrtc.util.Contacts;

//SipCallConnectedListener,
//        SipCallEndListener, CallStreamListener,

public class CallActivity extends AppCompatActivity implements  View.OnClickListener {
    private static final String TAG = "CallActivity";
    private static CallActivity the_callActivity_instance_;
    private boolean muteAudio = false;
    private boolean muteVideo = false;
    private boolean isVideo = false;
    private Handler timerHandler = new Handler();
    int secondsElapsed = 0;
    private long timeConnected = 0;
    public static final String LIVE_CALL_PAUSE_TIME = "live-call-pause-time";
    private int callDirection = Contacts.RECEIVE_VIDEO_REQUEST;
    private String peer_number = null, localUserNumber = null;
    private int call_id = -1;

    ImageButton btnMuteAudio, btnMuteVideo;
    ImageButton btnHangup;
    ImageButton btnAnswer, btnUnlock;
    //, btnAnswerAudio;
    //ImageButton btnKeypad;
    //KeypadFragment keypadFragment;
    TextView lblCall, lblStatus, lblTimer;

    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 2;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;

    private FrameLayout localRenderLayout;
    private FrameLayout remoteRenderLayout;
    private SurfaceView remoteVideo;
    private String RemocteDeviceType;
    private boolean buttonShow = false;

    public static CallActivity instance() {
        return the_callActivity_instance_;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        the_callActivity_instance_ = this;
        Log.e(TAG, "============onCreate===========");
        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_call);
        // Initialize UI
        btnHangup = (ImageButton) findViewById(R.id.button_hangup);
        btnHangup.setOnClickListener(this);
        btnAnswer = (ImageButton) findViewById(R.id.button_answer);
        btnAnswer.setOnClickListener(this);
        //btnAnswerAudio = (ImageButton) findViewById(R.id.button_answer_audio);
        //btnAnswerAudio.setOnClickListener(this);
        btnMuteAudio = (ImageButton) findViewById(R.id.button_mute_audio);
        btnMuteAudio.setOnClickListener(this);
        btnMuteVideo = (ImageButton) findViewById(R.id.button_mute_video);
        btnMuteVideo.setOnClickListener(this);
        btnUnlock = (ImageButton) findViewById(R.id.button_unlock);
        btnUnlock.setOnClickListener(this);
        //btnKeypad = (ImageButton) findViewById(R.id.button_keypad);
        //btnKeypad.setOnClickListener(this);
        lblCall = (TextView) findViewById(R.id.label_call);
        lblStatus = (TextView) findViewById(R.id.label_status);
        lblTimer = (TextView) findViewById(R.id.label_timer);

        // Get Intent parameters.
        final Intent intent = getIntent();
        callDirection = intent.getIntExtra(Contacts.PHONESTATE, 0);
        peer_number = intent.getStringExtra(Contacts.PHONNUMBER);
        call_id = intent.getIntExtra(Contacts.PHONECALLID, -1);
        localUserNumber = intent.getStringExtra("PHONLOCALNUMBER");

        if (callDirection == Contacts.INVITE_VIDEO_REQUEST) {
            btnAnswer.setVisibility(View.INVISIBLE);
            //btnAnswerAudio.setVisibility(View.INVISIBLE);
        } else {
            btnAnswer.setVisibility(View.VISIBLE);
            //btnAnswerAudio.setVisibility(View.VISIBLE);
        }
        //btnAnswerAudio.setVisibility(View.VISIBLE);
        //keypadFragment = new KeypadFragment();
        lblTimer.setVisibility(View.INVISIBLE);
        // these might need to be moved to Resume()
        btnMuteAudio.setVisibility(View.INVISIBLE);
        btnMuteVideo.setVisibility(View.INVISIBLE);
        btnUnlock.setVisibility(View.INVISIBLE);
        //btnKeypad.setVisibility(View.INVISIBLE);

        // open keypad
        /*FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.keypad_fragment_container, keypadFragment);
        ft.hide(keypadFragment);
        ft.commit();*/
        initializeVideo(true);
    }

    private void initializeVideo(boolean videoEnable)
    {
        this.localRenderLayout = (FrameLayout) findViewById(R.id.local_video_layout);
        this.remoteRenderLayout = (FrameLayout)findViewById(R.id.remote_video_layout);
        remoteVideo = NWayRtc.createRendererView(this);
        this.remoteRenderLayout.addView(remoteVideo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "%% onPause");
    }

    @Override
    protected void onStart()
    {
       super.onStart();
       Log.i(TAG, "%% onStart");
       startCall();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "%% onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // The activity has become visible (it is now "resumed").
        Log.i(TAG, "%% onResume");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i(TAG, "%% onDestroy");

        if (timerHandler != null) {
            timerHandler.removeCallbacksAndMessages(null);
        }
    }

    private void startCall()
    {
        isVideo = true;
        if (callDirection == Contacts.INVITE_VIDEO_REQUEST) {
            String text;
            if (isVideo) {
                text = "Video Calling ";
            }
            else {
                text = "Audio Calling ";
            }

            lblCall.setText(text + peer_number);
            lblStatus.setText("Initiating Call");
            NWayRtc.call(peer_number, true, false,
                    640, 480, 15, 512, false);
        }
        if (callDirection == Contacts.RECEIVE_VIDEO_REQUEST) {
            String text;
            if (isVideo) {
                text = "Video Call from ";
            }
            else {
                text = "Audio Call from ";
            }
            lblStatus.setText("Call Received");
        }
    }

    // UI Events
    public void onClick(View view) {
        if (view.getId() == R.id.button_hangup) {
            Log.e("callActivity", "====1==view.getId() == R.id.button_hangup====");
            if (call_id > 0) {
                Log.e("callActivity", "====2==view.getId() == R.id.button_hangup====");
                // incoming ringing
                CloudrtcDemo.instance().hangupCall(call_id);
                NWayRtc.hangup(call_id);
            }
            finish();
        } else if (view.getId() == R.id.button_answer) {
            if (call_id > 0) {
                //lblStatus.setText("Answering Call:" +call_id);
                Log.e(TAG, "=======Answering Call..." +call_id);
                btnAnswer.setVisibility(View.INVISIBLE);
                NWayRtc.answer(call_id, true,
                        false, 640, 480, 15, 512, false);
            }
        } /*else if (view.getId() == R.id.button_answer_audio) {
            if (call_id > 0) {
                lblStatus.setText("Answering Call...");
                btnAnswer.setVisibility(View.INVISIBLE);
                //btnAnswerAudio.setVisibility(View.INVISIBLE);

            }
        } else if (view.getId() == R.id.button_keypad) {
            keypadFragment.setCallId(call_id);

            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

            // show keypad
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.show(keypadFragment);
            ft.commit();
        } */ else if(view.getId() == R.id.button_unlock) {
            //if(this.RemocteDeviceType != null && this.RemocteDeviceType.equals("dev_outdoor")) {
                NWayRtc.sendMessage("1007", localUserNumber, "UNLOCK");
            //}
        } else if (view.getId() == R.id.button_mute_audio) {
            if (call_id > 0) {
                if (!muteAudio) {
                    btnMuteAudio.setImageResource(R.drawable.audio_muted);
                } else {
                    btnMuteAudio.setImageResource(R.drawable.audio_unmuted);
                }
                muteAudio = !muteAudio;
                if(muteAudio) {
                    NWayRtc.enableReceiveAudio(call_id, false);
                } else {
                    NWayRtc.enableReceiveAudio(call_id, true);
                }
            }
        } else if (view.getId() == R.id.button_mute_video) {
            if (call_id > 0) {
                muteVideo = !muteVideo;
                if (muteVideo) {
                    btnMuteVideo.setImageResource(R.drawable.video_muted);
                    Log.e(TAG, "=======sendMessage====:" +peer_number +":" +localUserNumber);
                    NWayRtc.enableReceiveVideo(call_id, false);
                } else {
                    btnMuteVideo.setImageResource(R.drawable.video_unmuted);
                    NWayRtc.enableReceiveVideo(call_id, true);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
                if(buttonShow) {
                    buttonShow = false;
                } else {
                    buttonShow = true;
                }

                //buttonShow = true;
                if(buttonShow) {
                  lblTimer.setVisibility(View.VISIBLE);
                  // these might need to be moved to Resume()
                  btnMuteAudio.setVisibility(View.VISIBLE);
                  btnMuteVideo.setVisibility(View.VISIBLE);
                  btnUnlock.setVisibility(View.VISIBLE);
                  lblStatus.setVisibility(View.VISIBLE);
                } else {
                    lblTimer.setVisibility(View.INVISIBLE);
                    // these might need to be moved to Resume()
                    btnMuteAudio.setVisibility(View.INVISIBLE);
                    btnMuteVideo.setVisibility(View.INVISIBLE);
                    btnUnlock.setVisibility(View.INVISIBLE);
                    lblStatus.setVisibility(View.INVISIBLE);

                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onFragmentInteraction(String action) {
        if (action.equals("cancel")) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(keypadFragment);
            ft.commit();

        }
    }*/

    public void startTimer(int startSeconds) {
        secondsElapsed = startSeconds;
        String time = String.format("%02d:%02d:%02d", secondsElapsed / 3600, (secondsElapsed % 3600) / 60, secondsElapsed % 60);
        lblTimer.setText(time);
        secondsElapsed++;
        timerHandler.removeCallbacksAndMessages(null);
        // schedule a registration update after 'registrationRefresh' seconds
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                startTimer(secondsElapsed);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    public void onCallConnected(int call_id) {
        //lblStatus.setText("======Connected=======:"  +this.call_id);
        Log.e("CallActivity", "======onCallConnected=======:"+call_id);
        this.call_id = call_id;
        CallInfoBean mCallInfoBean = NWayRtc.getCallInfo(-1, call_id);
        this.RemocteDeviceType = mCallInfoBean.getRemoteDisplayName();
        //btnMuteAudio.setVisibility(View.VISIBLE);
        //if (!isVideo) {
         //   btnMuteVideo.setEnabled(false);
          //  btnMuteVideo.setColorFilter(Color.parseColor(getString(R.string.string_color_filter_video_disabled)));
        //}
        //btnMuteVideo.setVisibility(View.VISIBLE);
        //btnUnlock.setVisibility(View.VISIBLE);
        //btnKeypad.setVisibility(View.VISIBLE);
        //lblTimer.setVisibility(View.VISIBLE);
        //startTimer(0);
        // reset to no mute at beggining of new call
        muteAudio = false;
        muteVideo = false;
        NWayRtc.enablevideo(this.call_id, true);
        NWayRtc.enableaudio(this.call_id, true);
        NWayRtc.enableReceiveAudio(this.call_id, true);
        NWayRtc.enableReceiveVideo(this.call_id, true);
        NWayRtc.setupRemoteVideo(this.call_id, remoteVideo, true);
    }

    public void onCallEnd(int call_id, int status, CallLogBean mCallLogBean) {
        Log.i(TAG, "====onCallEnd====");
        //lblStatus.setText("onCallEnd===:" +mCallLogBean.getReason());
        btnMuteAudio.setVisibility(View.INVISIBLE);
        btnMuteVideo.setVisibility(View.INVISIBLE);
        btnUnlock.setVisibility(View.INVISIBLE);
        this.call_id = -1;
        finish();
    }

    public void onLocalVideoReady(int callId) {
        NWayRtc.enablevideo(this.call_id, true);
        NWayRtc.enableaudio(this.call_id, true);
    }

    public void onRemoteVideoReady(int callId) {
        Log.e("callActivity", "====onRemoteVideoReady========");
        NWayRtc.enableReceiveAudio(this.call_id, true);
        NWayRtc.enableReceiveVideo(this.call_id, true);
    }

    public void onUpdatedByRemote(int callId, boolean video) {

    }

    public void onUpdatedByLocal(int callId, boolean video) {

    }
}
