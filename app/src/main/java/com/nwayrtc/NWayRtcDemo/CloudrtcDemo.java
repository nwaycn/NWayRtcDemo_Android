package com.nwayrtc.NWayRtcDemo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nwayrtc.receiver.PushBootReceiver;
import com.nwayrtc.sdk.CallLogBean;
import com.nwayrtc.sdk.NWayRtc;
import com.nwayrtc.service.PhoneService;
import com.nwayrtc.util.Contacts;
import com.nwayrtc.sdk.NWayRtc;
import com.nwayrtc.sdk.INWayRtcEvent;

public class CloudrtcDemo extends BaseActivity implements OnClickListener, INWayRtcEvent {
    private static CloudrtcDemo the_cloudrtcDemo_instance_;
    private Button mBtnRegister, mBtnCall, mVideoCall;
    private EditText mEditPwd, mEditUser, mEditCall;
     //mEditPeerIp, mEditPeerPort;
    private TextView mTextViewLocalIp, mTextViewLocalPort;
    private String mPwd, mUser, mPeerNumber, mPeerIp, mLocalIp, mPeerPortString, mLocalPortString;
    private int mPeerPort, mLocalPort;
    private PhoneServiceBootReceiver phoneServiceBootReceiver;
    private String peer_caller;
    private boolean ProfileValueChange;
    private SharedPreferences sharedPref;
    private String keyprefVideoCallEnabled;
    private String keyprefScreencapture;
    private String keyprefCamera2;
    private String keyprefResolution;
    private String keyprefFps;
    private String keyprefCaptureQualitySlider;
    private String keyprefVideoBitrateType;
    private String keyprefVideoBitrateValue;
    private String keyprefVideoCodec;
    private String keyprefAudioBitrateType;
    private String keyprefAudioBitrateValue;
    private String keyprefAudioCodec;
    private String keyprefHwCodecAcceleration;
    private String keyprefCaptureToTexture;
    private String keyprefFlexfec;
    private String keyprefNoAudioProcessingPipeline;
    private String keyprefAecDump;
    private String keyprefOpenSLES;
    private String keyprefDisableBuiltInAec;
    private String keyprefDisableBuiltInAgc;
    private String keyprefDisableBuiltInNs;
    private String keyprefEnableLevelControl;
    private String keyprefDisableWebRtcAGCAndHPF;
    private String keyprefEnableDataChannel;
    private String keyprefOrdered;
    private String keyprefMaxRetransmitTimeMs;
    private String keyprefMaxRetransmits;
    private String keyprefDataProtocol;
    private String keyprefNegotiated;
    private String keyprefDataId;
    private int acc_id = -1, call_id = 1;
    private boolean isRegistrationSuccess = false;
    String phoneNumber = null;
    String password = null;
    String sip_server = null;
    String transport_type = null;

    @SuppressLint("HandlerLeak")

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x111) {
                mBtnRegister.setText("正在注册...");
                mBtnRegister.setEnabled(false);
            } else if (msg.what == 0x222) {
                mBtnRegister.setEnabled(true);
                mBtnRegister.setText("注销");
            } else if (msg.what == 0x333) {
                mBtnRegister.setEnabled(true);
                mBtnRegister.setText("注册失败,自动重连");
            } else if (msg.what == 0x444) {
                mBtnRegister.setEnabled(true);
                mBtnRegister.setText("注册");
            } else if (msg.what == 0x555) {
                mBtnCall.setText("挂断");
            } else if (msg.what == 0x666) {
                mBtnCall.setText("对讲");
            }
        }
    };

    /*@Override
    public void onRegistrationProgress(int acc_id) {
        this.acc_id = acc_id;
        isRegistrationSuccess = false;
        Message msg = new Message();
        msg.what = 0x111;
        handler.sendMessage(msg);
    }

    @Override
    public void onRegistrationSuccess(int acc_id) {
        isRegistrationSuccess = true;
        this.acc_id = acc_id;
        Message msg = new Message();
        msg.what = 0x222;
        handler.sendMessage(msg);
    }*/

    @Override
    public void onRegistrationState(int acc_id, int code, String reason) {
        if(code == 1) {
             isRegistrationSuccess = true;
             this.acc_id = acc_id;
             Message msg = new Message();
             msg.what = 0x222;
             System.out.println("RegistrationSuccess");
             handler.sendMessage(msg);
        } else {
            isRegistrationSuccess = false;
            this.acc_id = acc_id;
            Message msg = new Message();
            msg.what = 0x444;
            handler.sendMessage(msg);
        }
    }

    /*@Override
    public void onRegistrationCleared(int acc_id) {
        isRegistrationSuccess = false;
        this.acc_id = acc_id;
        Message msg = new Message();
        msg.what = 0x444;
        handler.sendMessage(msg);
    }*/

    public static CloudrtcDemo instance() {
        return the_cloudrtcDemo_instance_;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudrtc_demo);
        the_cloudrtcDemo_instance_ = this;
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PhoneServiceBoot");
        phoneServiceBootReceiver= new PhoneServiceBootReceiver();
        registerReceiver(phoneServiceBootReceiver, filter);
        PushBootReceiver pushBootReceiver = new PushBootReceiver();
        IntentFilter pushBootReceiverfilter = new IntentFilter();
        pushBootReceiverfilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        //pushBootReceiverfilter.addAction(Intent.ACTION_SCREEN_OFF);
        pushBootReceiverfilter.addAction(Intent.ACTION_USER_PRESENT);
        getApplicationContext().registerReceiver(pushBootReceiver, pushBootReceiverfilter);

        mBtnRegister = (Button) this.findViewById(R.id.btnRegister);
        mBtnRegister.setOnClickListener(this);
        mBtnCall = (Button) this.findViewById(R.id.btncall);

        mBtnCall.setOnClickListener(this);
        //mbtnDirectCall.setOnClickListener(this);

        mEditPwd = (EditText) this.findViewById(R.id.editPwd);
        mEditUser = (EditText) this.findViewById(R.id.editUser);
        mEditCall = (EditText) this.findViewById(R.id.editCall);
        mTextViewLocalIp = (TextView)this.findViewById(R.id.LocalIP);
        mTextViewLocalPort = (TextView)this.findViewById(R.id.LocalPort);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        keyprefVideoCallEnabled = getString(R.string.pref_videocall_key);
        keyprefScreencapture = getString(R.string.pref_screencapture_key);
        keyprefCamera2 = getString(R.string.pref_camera2_key);
        keyprefResolution = getString(R.string.pref_resolution_key);
        keyprefFps = getString(R.string.pref_fps_key);
        keyprefCaptureQualitySlider = getString(R.string.pref_capturequalityslider_key);
        keyprefVideoBitrateType = getString(R.string.pref_maxvideobitrate_key);
        keyprefVideoBitrateValue = getString(R.string.pref_maxvideobitratevalue_key);
        keyprefVideoCodec = getString(R.string.pref_videocodec_key);
        keyprefHwCodecAcceleration = getString(R.string.pref_hwcodec_key);
        keyprefCaptureToTexture = getString(R.string.pref_capturetotexture_key);
        keyprefFlexfec = getString(R.string.pref_flexfec_key);
        keyprefAudioBitrateType = getString(R.string.pref_startaudiobitrate_key);
        keyprefAudioBitrateValue = getString(R.string.pref_startaudiobitratevalue_key);
        keyprefAudioCodec = getString(R.string.pref_audiocodec_key);
        keyprefNoAudioProcessingPipeline = getString(R.string.pref_noaudioprocessing_key);
        keyprefAecDump = getString(R.string.pref_aecdump_key);
        keyprefOpenSLES = getString(R.string.pref_opensles_key);
        keyprefDisableBuiltInAec = getString(R.string.pref_disable_built_in_aec_key);
        keyprefDisableBuiltInAgc = getString(R.string.pref_disable_built_in_agc_key);
        keyprefDisableBuiltInNs = getString(R.string.pref_disable_built_in_ns_key);
        keyprefEnableLevelControl = getString(R.string.pref_enable_level_control_key);
        keyprefDisableWebRtcAGCAndHPF = getString(R.string.pref_disable_webrtc_agc_and_hpf_key);
        keyprefEnableDataChannel = getString(R.string.pref_enable_datachannel_key);
        keyprefOrdered = getString(R.string.pref_ordered_key);
        keyprefMaxRetransmitTimeMs = getString(R.string.pref_max_retransmit_time_ms_key);
        keyprefMaxRetransmits = getString(R.string.pref_max_retransmits_key);
        keyprefDataProtocol = getString(R.string.pref_data_protocol_key);
        keyprefNegotiated = getString(R.string.pref_negotiated_key);
        keyprefDataId = getString(R.string.pref_data_id_key);

        NWayRtc.init(this, 4, "0.0.0.0", 5060);
        NWayRtc.setNWayRtcEvent(this);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //if(PhoneService.instance().isRegistered(0)) {
                    //mBtnRegister.setEnabled(true);
                    //mBtnRegister.setText("注销");
                //}
                mTextViewLocalIp.setText(PhoneService.instance().getLocalIPAddress());
                mTextViewLocalPort.setText("" +PhoneService.instance().getLocalSipListenPort());
            }
        }, 1500);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(phoneServiceBootReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                forwardToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    /**
     * Get a value from the shared preference or from the intent, if it does not
     * exist the default is used.
     */
    private String sharedPrefGetString(
            int attributeId, int defaultId) {
        String defaultValue = getString(defaultId);
        {
            String attributeName = getString(attributeId);
            return sharedPref.getString(attributeName, defaultValue);
        }
    }

    /**
     * Get a value from the shared preference or from the intent, if it does not
     * exist the default is used.
     */
    private boolean sharedPrefGetBoolean(
            int attributeId, int defaultId) {
        boolean defaultValue = Boolean.valueOf(getString(defaultId));
        {
            String attributeName = getString(attributeId);
            return sharedPref.getBoolean(attributeName, defaultValue);
        }
    }

    /**
     * Get a value from the shared preference or from the intent, if it does not
     * exist the default is used.
     */
    private int sharedPrefGetInteger(
            int attributeId, int defaultId) {
        String defaultString = getString(defaultId);
        int defaultValue = Integer.parseInt(defaultString);
        {
            String attributeName = getString(attributeId);
            String value = sharedPref.getString(attributeName, defaultString);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Log.e(TAG, "Wrong setting for: " + attributeName + ":" + value);
                return defaultValue;
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        this.mPwd = this.mEditPwd.getText().toString();
        this.mUser = this.mEditUser.getText().toString();

        if (v.getId() == R.id.btnRegister) {
            boolean ret = false;// = PhoneService.instance().isRegistered(acc_id);
            if (!ret) {
                //if(!TextUtils.isEmpty(mUser))
                phoneNumber = sharedPref.getString("sip_account_username", "");
                password = sharedPref.getString("sip_account_password", "");
                sip_server = sharedPref.getString("sip_account_domain", "");
                transport_type = sharedPref.getString("sip_account_transport", "tcp");

                if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(sip_server)) {
                    NWayRtc.login(sip_server, phoneNumber, password, "test", 3600,
                            false, "fcm", "1003", null);
                }
            } else {
                System.out.println("======unRegisterSipAccount=:");
            }
        } else if (v.getId() == R.id.btncall) {
            //if (!isRegistrationSuccess) {
              //  Toast.makeText(CloudrtcDemo.this, "请先注册再呼叫", Toast.LENGTH_SHORT).show();
                //return;
            //}
            //if (!PhoneService.instance().isCallActive(call_id)) {
                this.mPeerNumber = this.mEditCall.getText().toString();
                if (!TextUtils.isEmpty(mPeerNumber)) {

                    String phoneNumber = sharedPref.getString("sip_account_username", "");
                    if (mPeerNumber.equals(phoneNumber)) {
                        Toast.makeText(CloudrtcDemo.this, "不能呼叫自己", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Video call enabled flag.
                    boolean videoCallEnabled = sharedPrefGetBoolean(R.string.pref_videocall_key,
                            R.string.pref_videocall_default);

                    // Use screencapture option.
                    boolean useScreencapture = sharedPrefGetBoolean(R.string.pref_screencapture_key,
                            R.string.pref_screencapture_default);

                    // Use Camera2 option.
                    boolean useCamera2 = sharedPrefGetBoolean(R.string.pref_camera2_key,
                            R.string.pref_camera2_default);

                    // Get default codecs.
                    String videoCodec = sharedPrefGetString(R.string.pref_videocodec_key,
                            R.string.pref_videocodec_default);
                    String audioCodec = sharedPrefGetString(R.string.pref_audiocodec_key,
                            R.string.pref_audiocodec_default);

                    // Check HW codec flag.
                    boolean hwCodec = sharedPrefGetBoolean(R.string.pref_hwcodec_key,
                            R.string.pref_hwcodec_default);

                    // Check Capture to texture.
                    boolean captureToTexture = sharedPrefGetBoolean(R.string.pref_capturetotexture_key,
                            R.string.pref_capturetotexture_default);

                    // Check FlexFEC.
                    boolean flexfecEnabled = sharedPrefGetBoolean(R.string.pref_flexfec_key, R.string.pref_flexfec_default);

                    // Check Disable Audio Processing flag.
                    boolean noAudioProcessing = sharedPrefGetBoolean(R.string.pref_noaudioprocessing_key,
                            R.string.pref_noaudioprocessing_default);

                    // Check Disable Audio Processing flag.
                    boolean aecDump = sharedPrefGetBoolean(R.string.pref_aecdump_key,
                            R.string.pref_aecdump_default);

                    // Check OpenSL ES enabled flag.
                    boolean useOpenSLES = sharedPrefGetBoolean(R.string.pref_opensles_key,
                            R.string.pref_opensles_default);

                    // Check Disable built-in AEC flag.
                    boolean disableBuiltInAEC = sharedPrefGetBoolean(R.string.pref_disable_built_in_aec_key,
                            R.string.pref_disable_built_in_aec_default
                    );

                    // Check Disable built-in AGC flag.
                    boolean disableBuiltInAGC = sharedPrefGetBoolean(R.string.pref_disable_built_in_agc_key,
                            R.string.pref_disable_built_in_agc_default
                    );

                    // Check Disable built-in NS flag.
                    boolean disableBuiltInNS = sharedPrefGetBoolean(R.string.pref_disable_built_in_ns_key,
                            R.string.pref_disable_built_in_ns_default);

                    // Check Enable level control.
                    boolean enableLevelControl = sharedPrefGetBoolean(R.string.pref_enable_level_control_key,
                            R.string.pref_enable_level_control_key
                    );

                    // Check Disable gain control
                    boolean disableWebRtcAGCAndHPF = sharedPrefGetBoolean(
                            R.string.pref_disable_webrtc_agc_and_hpf_key,
                            R.string.pref_disable_webrtc_agc_and_hpf_key);

                    // Get video resolution from settings.
                    int videoWidth = 0;
                    int videoHeight = 0;

                    if (videoWidth == 0 && videoHeight == 0) {
                        String resolution =
                                sharedPref.getString(keyprefResolution, getString(R.string.pref_resolution_default));
                        String[] dimensions = resolution.split("[ x]+");
                        if (dimensions.length == 2) {
                            try {
                                videoWidth = Integer.parseInt(dimensions[0]);
                                videoHeight = Integer.parseInt(dimensions[1]);
                            } catch (NumberFormatException e) {
                                videoWidth = 0;
                                videoHeight = 0;
                                //Log.e(TAG, "Wrong video resolution setting: " + resolution);
                            }
                        }
                    }

                    // Get camera fps from settings.
                    int cameraFps = 0;
                    if (cameraFps == 0) {
                        String fps = sharedPref.getString(keyprefFps, getString(R.string.pref_fps_default));
                        String[] fpsValues = fps.split("[ x]+");
                        if (fpsValues.length == 2) {
                            try {
                                cameraFps = Integer.parseInt(fpsValues[0]);
                            } catch (NumberFormatException e) {
                                cameraFps = 0;
                                //Log.e(TAG, "Wrong camera fps setting: " + fps);
                            }
                        }
                    }

                    // Check capture quality slider flag.
                    boolean captureQualitySlider = sharedPrefGetBoolean(R.string.pref_capturequalityslider_key,
                            R.string.pref_capturequalityslider_default);

                    // Get video and audio start bitrate.
                    int videoStartBitrate = 0;
                    if (videoStartBitrate == 0) {
                        String bitrateTypeDefault = getString(R.string.pref_maxvideobitrate_default);
                        String bitrateType = sharedPref.getString(keyprefVideoBitrateType, bitrateTypeDefault);
                        if (!bitrateType.equals(bitrateTypeDefault)) {
                            String bitrateValue = sharedPref.getString(
                                    keyprefVideoBitrateValue, getString(R.string.pref_maxvideobitratevalue_default));
                            videoStartBitrate = Integer.parseInt(bitrateValue);
                        }
                    }

                    int audioStartBitrate = 0;
                    if (audioStartBitrate == 0) {
                        String bitrateTypeDefault = getString(R.string.pref_startaudiobitrate_default);
                        String bitrateType = sharedPref.getString(keyprefAudioBitrateType, bitrateTypeDefault);
                        if (!bitrateType.equals(bitrateTypeDefault)) {
                            String bitrateValue = sharedPref.getString(
                                    keyprefAudioBitrateValue, getString(R.string.pref_startaudiobitratevalue_default));
                            audioStartBitrate = Integer.parseInt(bitrateValue);
                        }
                    }

                    //callConfig = new CallConfig(videoCallEnabled,
                            //videoWidth, videoHeight, cameraFps,
                           // videoCodec, videoStartBitrate, audioCodec, audioStartBitrate);

                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PhoneService.checkAndRequestPermission(CloudrtcDemo.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
                    } else if (PhoneService.isVoicePermission() && PhoneService.cameraIsCanUse()) {*/
                    //if(PhoneService.instance().isNetworkReachable()) {
                        //("sip:"+mPeerNumber+"@"+sip_server);
                        makeCall(mPeerNumber);
                        //makeCall("sip:" +mPeerNumber +";transport=tcp");
                        Log.e("CloudrtcDemo", "===========makeCall=========:" +mPeerNumber);
                    //}
                    //else {
                       // Toast.makeText(CloudrtcDemo.this, "网络没有连接", Toast.LENGTH_SHORT).show();
                    //}makeCall
                   /* } else {
                        Toast.makeText(CloudrtcDemo.this, "请打开相机和录音权限", Toast.LENGTH_SHORT).show();
                    }*/
                }
            //}
        }
    }

    private void makeCall(String mPeerNumber) {
        this.peer_caller = mPeerNumber;
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(Contacts.PHONECALLID, this.call_id);
        intent.putExtra(Contacts.PHONNUMBER, peer_caller);
        intent.putExtra("PHONLOCALNUMBER", mUser);
        intent.putExtra(Contacts.PHONESTATE, Contacts.INVITE_VIDEO_REQUEST);
        intent.putExtra(Contacts.ACTION_FROM_SERVICE,
                Contacts.ACTION_FROM_PHONE_SERVICE);
        startActivity(intent);
    }

    @Override
    public void onCallIncoming(int callId, String peerCallerUri,
                               String peerDisplayName, String peerDeviceType, boolean existsAudio,
                               boolean existsVideo) {
        if(this.call_id > 0 && callId != this.call_id) {
            NWayRtc.reject(callId, 486, "Busy here");
            return;
        }
        this.call_id = callId;
        this.peer_caller = peerCallerUri;
        if(this.peer_caller != null) {
            String[] peercaller = peer_caller.split("@");
            if(peercaller != null) {
                this.peer_caller =  peercaller[0];
                callInComing(peer_caller);
            }
        }
        Log.e("CloudrtcDemo", "===========onCallIncoming=========:" +this.call_id);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PhoneService.checkAndRequestPermission(CloudrtcDemo.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
        } else if (PhoneService.isVoicePermission() && PhoneService.cameraIsCanUse()) {*/
        /*} else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CloudrtcDemo.this, "请打开相机和录音权限", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    private void callInComing(String peer_number) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(Contacts.PHONECALLID, this.call_id);
        intent.putExtra(Contacts.PHONNUMBER, peer_number);
        intent.putExtra("PHONLOCALNUMBER", mUser);
        intent.putExtra(Contacts.PHONESTATE, Contacts.RECEIVE_VIDEO_REQUEST);
        intent.putExtra(Contacts.ACTION_FROM_SERVICE,
                Contacts.ACTION_FROM_PHONE_SERVICE);
        startActivity(intent);
    }

    @Override
    public void onCallOutgoing(int call_id, String peerCallerUri, String peerDisplayName) {
        Log.e("CloudrtcDemo", "=============:" +call_id);
    }

    public void forwardToSettings() {
        Intent intent = new Intent(this, ReferencesSettings.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("ProfileValueChange", false);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                ProfileValueChange = bundle.getBoolean("ProfileValueChange");
                System.out.println("=============ProfileValueChange=======:" + ProfileValueChange);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CloudrtcDemo.this, "相机录音已授权", Toast.LENGTH_SHORT).show();
                    System.out.println("=====================");
                } else {
                    Toast.makeText(CloudrtcDemo.this, "相机或录音未授权", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CloudrtcDemo.this, "相机录音已授权", Toast.LENGTH_SHORT).show();
                    callInComing(peer_caller);
                } else {
                    Toast.makeText(CloudrtcDemo.this, "相机或录音未授权", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSipMessage(String fromUri, String sipid, String message) {
        System.out.println("=============onSipMessage=======:" + message);
    }

    @Override
    public void onTransportState(boolean b) {

    }

    @Override
    public void onApplicationTimer(int i, int i1, int i2) {

    }

    @Override
    public void onLocalVideoReady(int i) {
        CallActivity.instance().onLocalVideoReady(call_id);
    }

    @Override
    public void onRemoteVideoReady(int callId) {
        CallActivity.instance().onRemoteVideoReady(call_id);
    }

    @Override
    public void onUpdatedByRemote(int i, boolean b) {

    }

    @Override
    public void onUpdatedByLocal(int i, boolean b) {

    }

    @Override
    public void onCallConnected(int call_id, boolean isCallBridge, String remoteBridgeUser) {
        Log.e("CloudrtcDemo", "======onCallConnected=======:"+call_id);
        CallActivity.instance().onCallConnected(call_id);
    }

    @Override
    public void onCallEnd(int call_id, int status, CallLogBean callLogBean) {
         CallActivity.instance().onCallEnd(call_id, status, callLogBean);
         this.call_id = -1;
    }

    public void hangupCall(int call_id) {
        this.call_id = -1;
    }

    @Override
    public void onNetworkStateChange(boolean b) {

    }

    class PhoneServiceBootReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
}
