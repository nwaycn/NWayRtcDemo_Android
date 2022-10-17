package com.nwayrtc.NWayRtcDemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

import com.nwayrtc.NWayRtcDemo.R;

//import org.webrtc.Camera2Enumerator;
//import org.webrtc.voiceengine.WebRtcAudioUtils;

public  class ReferencesSettings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private String keyprefVideoCall;
	private String keyprefScreencapture;
	private String keyprefCamera2;
	private String keyprefResolution;
	private String keyprefFps;
	private String keyprefCaptureQualitySlider;
	private String keyprefMaxVideoBitrateType;
	private String keyprefMaxVideoBitrateValue;
	private String keyPrefVideoCodec;
	private String keyprefHwCodec;
	private String keyprefCaptureToTexture;
	private String keyprefFlexfec;

	private String keyprefStartAudioBitrateType;
	private String keyprefStartAudioBitrateValue;
	private String keyPrefAudioCodec;
	private String keyprefNoAudioProcessing;
	private String keyprefAecDump;
	private String keyprefOpenSLES;
	private String keyprefDisableBuiltInAEC;
	private String keyprefDisableBuiltInAGC;
	private String keyprefDisableBuiltInNS;
	private String keyprefEnableLevelControl;
	private String keyprefDisableWebRtcAGCAndHPF;
	private String keyprefSpeakerphone;

	//private String keyPrefTracing;

	private String keyprefEnableDataChannel;
	private String keyprefOrdered;
	private String keyprefMaxRetransmitTimeMs;
	private String keyprefMaxRetransmits;
	private String keyprefDataProtocol;
	private String keyprefNegotiated;
	private String keyprefDataId;

    private  Bundle bundle;
    private Intent intent;
    private boolean ProfileValueChange;

    public void onCreate(Bundle savedInstanceState)
    { 
        super.onCreate(savedInstanceState);  
        addPreferencesFromResource(R.xml.preferences);
        intent = this.getIntent();
        bundle = intent.getExtras(); /* 取得Bundle对象中的数据 */
        boolean ProfileValueChange = bundle.getBoolean("ProfileValueChange");

        SharedPreferences sharedPreferences = this.getPreferenceManager().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

		keyprefVideoCall = getString(R.string.pref_videocall_key);
		keyprefScreencapture = getString(R.string.pref_screencapture_key);
		keyprefCamera2 = getString(R.string.pref_camera2_key);
		keyprefResolution = getString(R.string.pref_resolution_key);
		keyprefFps = getString(R.string.pref_fps_key);
		keyprefCaptureQualitySlider = getString(R.string.pref_capturequalityslider_key);
		keyprefMaxVideoBitrateType = getString(R.string.pref_maxvideobitrate_key);
		keyprefMaxVideoBitrateValue = getString(R.string.pref_maxvideobitratevalue_key);
		keyPrefVideoCodec = getString(R.string.pref_videocodec_key);
		keyprefHwCodec = getString(R.string.pref_hwcodec_key);
		keyprefCaptureToTexture = getString(R.string.pref_capturetotexture_key);
		keyprefFlexfec = getString(R.string.pref_flexfec_key);

		keyprefStartAudioBitrateType = getString(R.string.pref_startaudiobitrate_key);
		keyprefStartAudioBitrateValue = getString(R.string.pref_startaudiobitratevalue_key);
		keyPrefAudioCodec = getString(R.string.pref_audiocodec_key);
		keyprefNoAudioProcessing = getString(R.string.pref_noaudioprocessing_key);
		keyprefAecDump = getString(R.string.pref_aecdump_key);
		keyprefOpenSLES = getString(R.string.pref_opensles_key);
		keyprefDisableBuiltInAEC = getString(R.string.pref_disable_built_in_aec_key);
		keyprefDisableBuiltInAGC = getString(R.string.pref_disable_built_in_agc_key);
		keyprefDisableBuiltInNS = getString(R.string.pref_disable_built_in_ns_key);
		keyprefEnableLevelControl = getString(R.string.pref_enable_level_control_key);
		keyprefDisableWebRtcAGCAndHPF = getString(R.string.pref_disable_webrtc_agc_and_hpf_key);
		keyprefSpeakerphone = getString(R.string.pref_speakerphone_key);

		keyprefEnableDataChannel = getString(R.string.pref_enable_datachannel_key);
		keyprefOrdered = getString(R.string.pref_ordered_key);
		keyprefMaxRetransmitTimeMs = getString(R.string.pref_max_retransmit_time_ms_key);
		keyprefMaxRetransmits = getString(R.string.pref_max_retransmits_key);
		keyprefDataProtocol = getString(R.string.pref_data_protocol_key);
		keyprefNegotiated = getString(R.string.pref_negotiated_key);
		keyprefDataId = getString(R.string.pref_data_id_key);

		//keyPrefTracing = getString(R.string.pref_tracing_key);

        boolean value = sharedPreferences.getBoolean("sip_account_use_webrtc_mode", false);
		System.out.println(value? "True" : "false");
		findPreference("sip_account_username").setSummary(sharedPreferences.getString("sip_account_username", "Empty"));  
        findPreference("sip_account_password").setSummary(sharedPreferences.getString("sip_account_password", "Empty"));
        findPreference("sip_account_domain").setSummary(sharedPreferences.getString("sip_account_domain", "Empty"));
        findPreference("sip_account_proxy").setSummary(sharedPreferences.getString("sip_account_proxy", "Empty"));
        findPreference("sip_account_displayname").setSummary(sharedPreferences.getString("sip_account_displayname", "Empty"));
        findPreference("sip_account_auth_name").setSummary(sharedPreferences.getString("sip_account_auth_name", "Empty"));
        findPreference("sip_account_stun_server").setSummary(sharedPreferences.getString("sip_account_stun_server", "stun.l.google.com:19302"));
        findPreference("sip_account_turn_server").setSummary(sharedPreferences.getString("sip_account_turn_server", "Empty"));
        findPreference("sip_account_turn_user").setSummary(sharedPreferences.getString("sip_account_turn_user", "Empty"));
        findPreference("sip_account_turn_password").setSummary(sharedPreferences.getString("sip_account_turn_password", "Empty"));
        //findPreference("sip_account_expire").setSummary(sharedPreferences.getString("sip_account_expire", "600"));

		//ListPreference pref = (ListPreference)findPreference("sip_account_video_size");
		//{
		//pref.setValue(sharedPreferences.getString("sip_account_video_size", "vga"));
		//pref.setSummary(pref.getEntry());
        //}
        
        ListPreference pref = (ListPreference)findPreference("sip_account_transport");  
        {
        	pref.setValue(sharedPreferences.getString("sip_account_transport", "tcp"));
        	pref.setSummary(pref.getEntry());
        }
        
		EditTextPreference etp = (EditTextPreference) findPreference("sip_account_password");

		{
			String value_str = sharedPreferences.getString("sip_account_password", "Empty");

			if (value_str.length() > 0 && !value_str.equals("Empty")) {
				etp.setSummary("******");
			} else {
				etp.setSummary(value_str);
			}
		}
        
		etp = (EditTextPreference) findPreference("sip_account_turn_password");

		{
			String value_str = sharedPreferences.getString("sip_account_turn_password", "Empty");

			if (value_str.length() > 0 && !value_str.equals("Empty")) {
				etp.setSummary("******");
			} else {
				etp.setSummary(value_str);
			}
		}

		updateSummaryB(sharedPreferences, keyprefVideoCall);
		updateSummaryB(sharedPreferences, keyprefScreencapture);
		updateSummaryB(sharedPreferences, keyprefCamera2);
		updateSummary(sharedPreferences, keyprefResolution);
		updateSummary(sharedPreferences, keyprefFps);
		updateSummaryB(sharedPreferences, keyprefCaptureQualitySlider);
		updateSummary(sharedPreferences, keyprefMaxVideoBitrateType);
		updateSummaryBitrate(sharedPreferences, keyprefMaxVideoBitrateValue);
		setVideoBitrateEnable(sharedPreferences);
		updateSummary(sharedPreferences, keyPrefVideoCodec);
		updateSummaryB(sharedPreferences, keyprefHwCodec);
		updateSummaryB(sharedPreferences, keyprefCaptureToTexture);
		updateSummaryB(sharedPreferences, keyprefFlexfec);

		updateSummary(sharedPreferences, keyprefStartAudioBitrateType);
		updateSummaryBitrate(sharedPreferences, keyprefStartAudioBitrateValue);
		setAudioBitrateEnable(sharedPreferences);
		updateSummary(sharedPreferences, keyPrefAudioCodec);
		updateSummaryB(sharedPreferences, keyprefNoAudioProcessing);
		updateSummaryB(sharedPreferences, keyprefAecDump);
		updateSummaryB(sharedPreferences, keyprefOpenSLES);
		updateSummaryB(sharedPreferences, keyprefDisableBuiltInAEC);
		updateSummaryB(sharedPreferences, keyprefDisableBuiltInAGC);
		updateSummaryB(sharedPreferences, keyprefDisableBuiltInNS);
		updateSummaryB(sharedPreferences, keyprefEnableLevelControl);
		updateSummaryB(sharedPreferences, keyprefDisableWebRtcAGCAndHPF);
		updateSummaryList(sharedPreferences, keyprefSpeakerphone);

		updateSummaryB(sharedPreferences, keyprefEnableDataChannel);
		updateSummaryB(sharedPreferences, keyprefOrdered);
		updateSummary(sharedPreferences, keyprefMaxRetransmitTimeMs);
		updateSummary(sharedPreferences, keyprefMaxRetransmits);
		updateSummary(sharedPreferences, keyprefDataProtocol);
		updateSummaryB(sharedPreferences, keyprefNegotiated);
		updateSummary(sharedPreferences, keyprefDataId);
		setDataChannelEnable(sharedPreferences);

		//updateSummaryB(sharedPreferences, keyPrefTracing);

		/*if (!Camera2Enumerator.isSupported(this)) {
			Preference camera2Preference = findPreference(keyprefCamera2);

			camera2Preference.setSummary(getString(R.string.pref_camera2_not_supported));
			camera2Preference.setEnabled(false);
		}

		// Disable forcing WebRTC based AEC so it won't affect our value.
		// Otherwise, if it was enabled, isAcousticEchoCancelerSupported would always return false.
		WebRtcAudioUtils.setWebRtcBasedAcousticEchoCanceler(false);
		if (!WebRtcAudioUtils.isAcousticEchoCancelerSupported()) {
			Preference disableBuiltInAECPreference =
					findPreference(keyprefDisableBuiltInAEC);

			disableBuiltInAECPreference.setSummary(getString(R.string.pref_built_in_aec_not_available));
			disableBuiltInAECPreference.setEnabled(false);
		}

		WebRtcAudioUtils.setWebRtcBasedAutomaticGainControl(false);
		if (!WebRtcAudioUtils.isAutomaticGainControlSupported()) {
			Preference disableBuiltInAGCPreference =
					findPreference(keyprefDisableBuiltInAGC);

			disableBuiltInAGCPreference.setSummary(getString(R.string.pref_built_in_agc_not_available));
			disableBuiltInAGCPreference.setEnabled(false);
		}

		WebRtcAudioUtils.setWebRtcBasedNoiseSuppressor(false);
		if (!WebRtcAudioUtils.isNoiseSuppressorSupported()) {
			Preference disableBuiltInNSPreference =
					findPreference(keyprefDisableBuiltInNS);

			disableBuiltInNSPreference.setSummary(getString(R.string.pref_built_in_ns_not_available));
			disableBuiltInNSPreference.setEnabled(false);
		}*/
	  }
	  
    private boolean peference_is_change = false;
    private boolean peference_is_media_change = false;
	public boolean isPeferencesChange()
	{
		return peference_is_change;
	}
	
	public void resetPeferencesFlag()
	{
		 peference_is_change = false;
	}
	
	public boolean isMediaPeferencesChange()
	{
		return peference_is_media_change;
	}
	
	public void resetMediaPeferencesFlag()
	{
		peference_is_media_change = false;
	}
      
    @Override  
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {  
        Preference pref = findPreference(key);  
        if (pref instanceof EditTextPreference) {  
            EditTextPreference etp = (EditTextPreference) pref;
            String value = etp.getText();
            if(value.length() > 0 )
     	   {
     		   if(key.equals("sip_account_password") || key.equals("sip_account_turn_password"))
     			   pref.setSummary("******"); 
     		   else
     			   pref.setSummary(value); 
     	   } else
     	   {
     		  pref.setSummary("Empty");
		}
            
            peference_is_change = true;
        }  
        
       // if (pref instanceof ListPreference) { 
        //	ListPreference listp = (ListPreference) pref;
        	//CharSequence value = listp.getEntry();
        	//listp.setSummary(value);
        //}

        if (key.equals(keyprefResolution)
                || key.equals(keyprefFps)
                || key.equals(keyprefMaxVideoBitrateType)
                || key.equals(keyPrefVideoCodec)
                || key.equals(keyprefStartAudioBitrateType)
                || key.equals(keyPrefAudioCodec)
                || key.equals(keyprefMaxRetransmitTimeMs)
                || key.equals(keyprefMaxRetransmits)
                || key.equals(keyprefDataProtocol)
                || key.equals(keyprefDataId)) {
            peference_is_media_change = true;
            updateSummary(sharedPreferences, key);
        } else if (key.equals(keyprefMaxVideoBitrateValue)
                || key.equals(keyprefStartAudioBitrateValue)) {
            peference_is_media_change = true;
            updateSummaryBitrate(sharedPreferences, key);
        } else if (key.equals(keyprefVideoCall)
                || key.equals(keyprefScreencapture)
                || key.equals(keyprefCamera2)
                || key.equals(keyprefCaptureQualitySlider)
                || key.equals(keyprefHwCodec)
                || key.equals(keyprefCaptureToTexture)
                || key.equals(keyprefFlexfec)
                || key.equals(keyprefNoAudioProcessing)
                || key.equals(keyprefAecDump)
                || key.equals(keyprefOpenSLES)
                || key.equals(keyprefDisableBuiltInAEC)
                || key.equals(keyprefDisableBuiltInAGC)
                || key.equals(keyprefDisableBuiltInNS)
                || key.equals(keyprefEnableLevelControl)
                || key.equals(keyprefDisableWebRtcAGCAndHPF)
                || key.equals(keyprefEnableDataChannel)
                || key.equals(keyprefOrdered)
                || key.equals(keyprefNegotiated)) {
            peference_is_media_change = true;
            updateSummaryB(sharedPreferences, key);
        } else if (key.equals(keyprefSpeakerphone)) {
            peference_is_media_change = true;
            updateSummaryList(sharedPreferences, key);
        }
        // clang-format on
        if (key.equals(keyprefMaxVideoBitrateType)) {
            setVideoBitrateEnable(sharedPreferences);
        }
        if (key.equals(keyprefStartAudioBitrateType)) {
            setAudioBitrateEnable(sharedPreferences);
        }
        if (key.equals(keyprefEnableDataChannel)) {
            setDataChannelEnable(sharedPreferences);
        }
        
        sharedPreferences.edit().commit();
    }

	private void updateSummary(SharedPreferences sharedPreferences, String key) {
		Preference updatedPref = findPreference(key);
		// Set summary to be the user-description for the selected value
		updatedPref.setSummary(sharedPreferences.getString(key, ""));
	}

	private void updateSummaryBitrate(SharedPreferences sharedPreferences, String key) {
		Preference updatedPref = findPreference(key);
		updatedPref.setSummary(sharedPreferences.getString(key, "") + " kbps");
	}

	private void updateSummaryB(SharedPreferences sharedPreferences, String key) {
		Preference updatedPref = findPreference(key);
		updatedPref.setSummary(sharedPreferences.getBoolean(key, true)
				? getString(R.string.pref_value_enabled)
				: getString(R.string.pref_value_disabled));
	}

	private void updateSummaryList(SharedPreferences sharedPreferences, String key) {
		ListPreference updatedPref = (ListPreference) findPreference(key);
		updatedPref.setSummary(updatedPref.getEntry());
	}

	private void setVideoBitrateEnable(SharedPreferences sharedPreferences) {
		Preference bitratePreferenceValue =
				findPreference(keyprefMaxVideoBitrateValue);
		String bitrateTypeDefault = getString(R.string.pref_maxvideobitrate_default);
		String bitrateType =
				sharedPreferences.getString(keyprefMaxVideoBitrateType, bitrateTypeDefault);
		if (bitrateType.equals(bitrateTypeDefault)) {
			bitratePreferenceValue.setEnabled(false);
		} else {
			bitratePreferenceValue.setEnabled(true);
		}
	}

	private void setAudioBitrateEnable(SharedPreferences sharedPreferences) {
		Preference bitratePreferenceValue =
				findPreference(keyprefStartAudioBitrateValue);
		String bitrateTypeDefault = getString(R.string.pref_startaudiobitrate_default);
		String bitrateType =
				sharedPreferences.getString(keyprefStartAudioBitrateType, bitrateTypeDefault);
		if (bitrateType.equals(bitrateTypeDefault)) {
			bitratePreferenceValue.setEnabled(false);
		} else {
			bitratePreferenceValue.setEnabled(true);
		}
	}

	private void setDataChannelEnable(SharedPreferences sharedPreferences) {
		boolean enabled = sharedPreferences.getBoolean(keyprefEnableDataChannel, true);
		findPreference(keyprefOrdered).setEnabled(enabled);
		findPreference(keyprefMaxRetransmitTimeMs).setEnabled(enabled);
		findPreference(keyprefMaxRetransmits).setEnabled(enabled);
		findPreference(keyprefDataProtocol).setEnabled(enabled);
		findPreference(keyprefNegotiated).setEnabled(enabled);
		findPreference(keyprefDataId).setEnabled(enabled);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            bundle.putBoolean("ProfileValueChange", peference_is_change);
            intent.putExtras(bundle);
            this.setResult(RESULT_OK, intent);
            peference_is_change = false;
            this.finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
} 