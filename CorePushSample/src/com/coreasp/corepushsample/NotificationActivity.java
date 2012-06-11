//
//  NotificationActivity.java
//  CorePushSample
//
//  Copyright (c) 2012 株式会社ブレスサービス. All rights reserved.
//

package com.coreasp.corepushsample;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NotificationActivity extends Activity {
	
	private boolean pushFlg = false;
	
	private Context mContext;
	
	protected final NotificationSettingReceiver receiver = new NotificationSettingReceiver();

	/**
	 * 通知設定のレシーバの設定。
	 * デバイストークンの登録•解除後にNotificationReceiverから呼び出される。
	 */
	protected class NotificationSettingReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			notifyService(intent);
		}
	}
	
	protected void notifyService(Intent intent) {
		//プッシュ通知のスイッチのON•OFFのフラグを設定から取得
       	SharedPreferences sharedPreferences = mContext.getSharedPreferences("CorePushSample",Context.MODE_PRIVATE);
       	pushFlg = sharedPreferences.getBoolean("enablePushNotification", false);
       	
    	ImageView pushSwitch = (ImageView)findViewById(R.id.push_check);
       	if (pushFlg) {
       		pushSwitch.setImageResource(R.drawable.switch_on);
       	} else {
       		pushSwitch.setImageResource(R.drawable.switch_off);
       	}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext = this;
        
		String deviceId = android.provider.Settings.System.getString(this.getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		SharedPreferences.Editor editor = this.getSharedPreferences("CorePushSample", Context.MODE_PRIVATE).edit();
		
		// 値を消去
		if (deviceId != null)
			editor.putString("DEVICE_ID", deviceId);

		editor.commit();
        
        deviceEntry();
    }
    
	public void onResume() {
		super.onResume();      
		
		//通知設定のレシーバの登録
		IntentFilter filter = new IntentFilter("NOTIFICATION_SETTING_CHANGE");
		registerReceiver(receiver, filter);

		//プッシュ通知のスイッチの設定
		ImageView pushSwitch = (ImageView)findViewById(R.id.push_check);
       	SharedPreferences sharedPreferences = mContext.getSharedPreferences("CorePushSample",Context.MODE_PRIVATE);
       	pushFlg = sharedPreferences.getBoolean("enablePushNotification", false);

       	if (pushFlg) {
       		pushSwitch.setImageResource(R.drawable.switch_on);
       	} else {
       		pushSwitch.setImageResource(R.drawable.switch_off);
       	}
		
		pushSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView img = (ImageView)v;
				pushFlg = !pushFlg;
				if(pushFlg) {
					img.setImageResource(R.drawable.switch_on);
					
					//デバイスの登録
					deviceEntry();
				} else {		
					img.setImageResource(R.drawable.switch_off);
				
			    	//デバイスの削除
					deviceRemove();
				} 
			}
		});
	}
	
	public void onPause() {
		super.onPause();      
		//通知設定のレシーバの登録解除
		unregisterReceiver(receiver);
	}
    
	/**
	 * 通知サービスの登録
	 */
    public void deviceEntry(){
    	 Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
	     intent.putExtra("app",PendingIntent.getBroadcast(this,0,new Intent(),0));
	     intent.putExtra("sender","sample@core-asp.com"); //C2DMのメールアドレスを指定
	     startService(intent);
    }
    
    /**
     * 通知サービスの解除
     */
	public void deviceRemove(){
		Intent intent2 = new Intent("com.google.android.c2dm.intent.UNREGISTER");
	    intent2.putExtra("app",PendingIntent.getBroadcast(this,0,new Intent(),0));
	    startService(intent2);
	}
}