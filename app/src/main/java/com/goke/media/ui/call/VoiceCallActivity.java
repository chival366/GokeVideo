/*
 *  Author: chival366
 *  QQ: 794960056
 *  Email: 794960056@qq.com; chival366@qq.com
 *  Source: github.com/chival366
 *  Copyright (c) 2020 The Goke project authors. All Rights Reserved.
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.goke.media.ui.call;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goke.media.R;
import com.goke.media.jni.C2JavaAPI;
import com.goke.media.jni.Java2CAPI;
import com.goke.media.ui.call.SipEvent;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class VoiceCallActivity extends AppCompatActivity implements IStateListener {
    private static Java2CAPI j2c = null;
    PeerInfo voicelocal = null, voiceremote = null;
    String callInfoList = "";
    private EditText editTextCallee = null;
    private Button buttonAnswer = null;
    private Button buttonInvite = null;
    private Button buttonHangup = null;
    private TextView textViewVersion = null;
    private TextView textViewCallState = null;
    private TextView textViewMediaStats = null;

    private String username, password;
    private String caller, callee;
    private int callid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_voicecall );
        editTextCallee = (EditText) findViewById( R.id.editTextCallee );
        buttonAnswer = (Button) findViewById( R.id.buttonL );
        buttonInvite = (Button) findViewById( R.id.buttonC );
        buttonHangup = (Button) findViewById( R.id.buttonR );

        textViewVersion = (TextView) findViewById( R.id.textViewVersion );
        textViewCallState = (TextView) findViewById( R.id.textViewCallState );
        textViewMediaStats = (TextView) findViewById( R.id.textViewMediaStats );

        Intent intent = getIntent();
        username = intent.getStringExtra( "username" );
        password = intent.getStringExtra( "password" );

        C2JavaAPI.setCallStateListener( this );

        j2c = new Java2CAPI();
        String sdkVersion = j2c.cosdkVersion();
        String localIP = PeerInfo.getLocalIP( this );
        textViewVersion.setText(sdkVersion + "\n\nLocalIP: " + localIP);
        editTextCallee.setText(localIP);
        textViewMediaStats.setVisibility( INVISIBLE );

        j2c.mediaCreate( this,0 );
        j2c.cosdkInit();

        buttonAnswer.setEnabled( false );
        buttonInvite.setEnabled( true );
        buttonHangup.setEnabled( false );

        voicelocal = new PeerInfo( "127.0.0.1", 20000 );
        voiceremote = new PeerInfo( "127.0.0.1", 20000 );

        buttonInvite.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textViewMediaStats.setVisibility( VISIBLE );
                buttonInvite.setEnabled( false );
                buttonHangup.setEnabled( true );
                callee = editTextCallee.getText().toString();
                Sound.Play( VoiceCallActivity.this, CallDir.OUTGOING );
                callInfoList = "";
                j2c.mediaStartRecv( voicelocal.getPort() );
                callid = j2c.cosdkInvite( callee );
                //j2c.mediaStartSend( voiceremote.getPort(), callee, "PCMA");
            }
        } );

        buttonAnswer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAnswer.setEnabled( false );
                //buttonHangup.setEnabled( true );
                Sound.StopPlay();

                j2c.cosdkAnswer( -1 );
                j2c.mediaStartRecv( voicelocal.getPort() );
                j2c.mediaStartSend( voiceremote.getPort(), voiceremote.getAddr(), "PCMA" );
                textViewMediaStats.setVisibility( VISIBLE );
                Log.d( "cosdk", "click annswer remoteaddr:port " + voiceremote.getAddr() + ":" + voiceremote.getPort() );
            }
        } );

        buttonHangup.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHangup.setEnabled( false );
                //buttonInvite.setEnabled( true );
                Sound.StopPlay();

                j2c.cosdkHangup( -1 );
                mediaStopAll();
            }
        } );
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        Sound.StopPlay();
        mediaStopAll();
        j2c.cosdkExit();
        j2c.mediaDelete();
        super.onDestroy();
    }
    public void mediaStopAll(){
        textViewMediaStats.setVisibility( INVISIBLE );
        j2c.mediaStop();
    }
    @Override
    public int SdkCallback(final int s, final int c, final String info, final String jsonData) {
        final SipEvent code = SipEvent.values()[c];

        if(s == 1) {
            Log.d( "cosdk", "java  SdkCallback s=" + s + " c=" + c + " info=" + info );
            callInfoList += (info + "\n");
        }

        try {
            if (s == 1) {
                if (code == SipEvent.SIP_CALL_INVITE || code == SipEvent.SIP_CALL_ANSWERED) {  // SIP_CALL_INVITE
                    JSONObject jsonObject = new JSONObject( jsonData );
                    callid = jsonObject.getInt( "callid" );

                    JSONObject jsonObjectVoice = (JSONObject) jsonObject.get( "voice" );
                    voiceremote.setPort( jsonObjectVoice.getInt( "port" ) );
                    voiceremote.setAddr( jsonObjectVoice.getString( "addr" ) );
                    Log.d( "cosdk", "remotesdp addr:port " + voiceremote.getAddr() + ":" + voiceremote.getPort() );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                if (s == 0) {
                    //textViewVersion.setText( "Register State: " + info );
                } else if (s == 2) {   // mediaStatistics
                    textViewMediaStats.setText( info );
                } else if (s == 1) {
                    SipEvent code = SipEvent.values()[c];
                    Log.d( "cosdk", "java runOnUiThread s=" + s + " c=" + c + " info=" + info );
                    if (code == SipEvent.SIP_CALL_CLOSED || code == SipEvent.SIP_CALL_MESSAGE_ANSWERED ||
                            code == SipEvent.SIP_CALL_CANCELLED || code == SipEvent.SIP_CALL_GLOBALFAILURE) {
                        buttonAnswer.setEnabled( false );
                        buttonHangup.setEnabled( false );
                        buttonInvite.setEnabled( true );
                        Sound.StopPlay();
                    }
                    switch (code) {
                        /*case SIP_CALL_PROCEEDING:
                            break;*/
                        case SIP_CALL_INVITE:
                            buttonAnswer.setEnabled( true );
                            buttonInvite.setEnabled( false );
                            buttonHangup.setEnabled( true );
                            Sound.Play( VoiceCallActivity.this, CallDir.INCOMING );
                            callInfoList = info + "\n";
                            Log.d( "cosdk", "recv incoming call" );

                            editTextCallee.setText(voiceremote.getAddr()); // only for call next time
                            break;
                        case SIP_CALL_ANSWERED:
                            Sound.StopPlay();
                            j2c.mediaStartSend( voiceremote.getPort(), voiceremote.getAddr(), "PCMA" );
                            textViewMediaStats.setVisibility( VISIBLE );
                            Log.d( "cosdk", "recv 200ok" );
                            break;

                        /*case SIP_CALL_ACK:
                            break;*/

                        case SIP_CALL_CLOSED:  // receive bye
                            //Sound.StopPlay();
                            mediaStopAll();
                            break;
                        case SIP_CALL_MESSAGE_ANSWERED: // response of bye, did return to -1, and tid add 1
                            buttonAnswer.setEnabled( false );
                            buttonHangup.setEnabled( false );
                            buttonInvite.setEnabled( true );
                            break;
                        case SIP_CALL_CANCELLED:  // cancel

                            //Sound.StopPlay();
                            break;
                        case SIP_CALL_GLOBALFAILURE: // be refused recv 6xx

                            //Sound.StopPlay();
                            mediaStopAll();
                            break;
                        default:
                            break;
                    }
                    textViewCallState.setText( callInfoList );
                }
            }
        } );

        return 0;
    }
}
