//
//  C2DMReceiver.java
//  CorePushSample
//
//  Copyright (c) 2012 株式会社ブレスサービス. All rights reserved.
//

package com.coreasp.corepushsample;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 *　通知のブロードキャストレシーバー
 */
public class C2DMReceiver extends BroadcastReceiver {
	
	private static String CONFIG_KEY = "9b8cdedbfa669cf03c31c4f1807ddcce"; //config_keyパタメータの値。設定キー
	
    @Override
    public void onReceive(Context context,Intent intent) {
    	
    	//デバイス登録IDの受信
        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            
        	String regId = intent.getStringExtra("registration_id");
            
            if (intent.getStringExtra("error")!=null) {
            	toast(context, "設定に失敗しました。");
    			SharedPreferences sharedPreferences = context.getSharedPreferences("CorePushSample", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putBoolean("enablePushNotification", false);
				editor.commit();
    			
    			Intent settingChangeIntent = new Intent("NOTIFICATION_SETTING_CHANGE");
				context.sendBroadcast(settingChangeIntent);
				
            }
            //デバイス登録解除
            else if (intent.getStringExtra("unregistered")!=null) {
            	
      			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            	SharedPreferences sharedPreferences = context.getSharedPreferences("CorePushSample",Context.MODE_PRIVATE);
    			nameValuePair.add(new BasicNameValuePair("config_key", CONFIG_KEY)); //config_keyパラメータ(必須)。設定キー。
    			nameValuePair.add(new BasicNameValuePair("device_token", sharedPreferences.getString("DEVICE_TOKEN_STRING",""))); //device_tokenパラメータ(必須)。デバイストークン(通知送信用のID)。
    			nameValuePair.add(new BasicNameValuePair("device_id", "1")); //device_idパラメータ。UDID(初期値:1)。
    			nameValuePair.add(new BasicNameValuePair("category_id", "1")); //category_idパラメータ。カテゴリID。2桁の整数の配列(初期値:1)。
    			nameValuePair.add(new BasicNameValuePair("mode", "2")); //modeパラメータ。デバイストークン(登録:1/削除:2)
            	JSONObject data;
            	URI uri;
				try {
					String jsonText = "";
					uri = new URI("http://api.core-asp.com/android_token_regist.php");
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(uri);
                    
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
					HttpResponse response = httpclient.execute(httpPost);
					
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					response.getEntity().writeTo(byteArrayOutputStream);
                    
					jsonText += byteArrayOutputStream.toString();
			        data = new JSONObject(jsonText);
				} catch (Exception e) {
					return;
				}
				
            	int status;
				try {
					status = data.getInt("status");
				} catch (JSONException e) {
					return;
				}
				
				SharedPreferences.Editor editor = sharedPreferences.edit();
            	if(status == 0){ 
                    toast(context,"通知をOFFにしました");
	   				editor.putBoolean("enablePushNotification", false);
	   			}
	   			else{
	   				toast(context,"通知のOFF設定に失敗しました");
	   				editor.putBoolean("enablePushNotification", true);
	   			}
            	editor.commit();
            	
    			Intent settingChangeIntent = new Intent("NOTIFICATION_SETTING_CHANGE");
				context.sendBroadcast(settingChangeIntent);
            	
            }
            //デバイス登録完了
            else if (regId!=null) {
            	toast(context, "regId:" + regId);
				SharedPreferences sharedPreferences = context.getSharedPreferences("CorePushSample", Context.MODE_PRIVATE);
				
       			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    			nameValuePair.add(new BasicNameValuePair("config_key", CONFIG_KEY)); //config_keyパラメータ(必須)。設定キー。
    			nameValuePair.add(new BasicNameValuePair("device_token", regId)); //device_tokenパラメータ(必須)。デバイストークン(通知送信用のID)。
    			nameValuePair.add(new BasicNameValuePair("device_id", "1")); //device_idパラメータ。UDID(初期値:1)。
    			nameValuePair.add(new BasicNameValuePair("category_id", "1")); //category_idパラメータ。カテゴリID。2桁の整数の配列(初期値:1)。
    			nameValuePair.add(new BasicNameValuePair("mode", "1")); //modeパラメータ。デバイストークン(登録:1/削除:2)
    			
    			sharedPreferences.edit().putString("DEVICE_TOKEN_STRING", regId);
    			sharedPreferences.edit().commit();
    			
    			
            	JSONObject data;
            	URI uri;
				try {
					String jsonText = "";
					uri = new URI("http://api.core-asp.com/android_token_regist.php");
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(uri);
                    
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
					HttpResponse response = httpclient.execute(httpPost);
					
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					response.getEntity().writeTo(byteArrayOutputStream);
                    
					jsonText += byteArrayOutputStream.toString();
					Log.i("jsonText", jsonText);
			        data = new JSONObject(jsonText);
				} catch (Exception e) {
					return;
				}
				
            	int status;
				try {
					status = data.getInt("status");
				} catch (JSONException e) {
					return;
				}
				
				SharedPreferences.Editor editor = sharedPreferences.edit();
            	if(status == 0){ 
	   				toast(context,"通知をONにしました");
	   				editor.putBoolean("enablePushNotification", true);
	   			}
	   			else{
	   				toast(context,"通知のON設定に失敗しました");
	   				editor.putBoolean("enablePushNotification", false);
	   			}
            	editor.commit();
            	
    			Intent settingChangeIntent = new Intent("NOTIFICATION_SETTING_CHANGE");
				context.sendBroadcast(settingChangeIntent);
            }
        }
        //メッセージの受信
        else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
        	// メッセージ
        	String message = intent.getStringExtra("message");
            
        	// タイトル
        	String title = intent.getStringExtra("title");
            
        	// アイコン
        	String icon = intent.getStringExtra("icon");
        	
        	// バッジ
        	String badge = intent.getStringExtra("badge");
        	int badgeNum = 0;
        	
        	if (badge != null) {
        		try {
        			badgeNum = Integer.parseInt(badge);
        		} catch (NumberFormatException e) {
        			
        		}
        	}
            
        	//メッセージ、タイトルのURLデコード
        	try {
        		if (message != null)
        			message = URLDecoder.decode(message,"utf-8");
        		
        		if (title != null)
        			title = URLDecoder.decode(title,"utf-8");
			} catch (UnsupportedEncodingException e) {
			} catch (Exception e) {
			}
            
        	//アイコンの設定（10種類まで）
        	int iconId = R.drawable.ic_launcher;
			try {
				if (icon != null) {
					switch (Integer.parseInt(icon)) {
                        case 0: // iconIdが0の場合
                            // resフォルダの画像を指定
                            iconId = R.drawable.sample_0;
                            break;
                            
                        case 1: // iconIdが1の場合
                            iconId = R.drawable.sample_1;
                            break;
                            
                        case 2: // iconIDが2の場合
                            iconId = R.drawable.sample_2;
                            break;
                        default:
                            break;
					}
				}
			} catch (Exception e) {
			}
            
			// ステータスバーの通知設定
        	Notification notification = new Notification(iconId ,message,System.currentTimeMillis());
        	Intent intentN = new Intent(context,NotificationActivity.class);
        	intentN.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intentN, PendingIntent.FLAG_CANCEL_CURRENT);
    		notification.setLatestEventInfo(context,title, message,contentIntent);
    		notification.flags = Notification.FLAG_AUTO_CANCEL;
            
    		//デフォルトのサウンド設定
    		notification.sound = Settings.System.DEFAULT_NOTIFICATION_URI;
    		
    		//デフォルトのバイブレーション設定
    		//バイブレーションの間隔を指定
   			notification.vibrate = new long[]{0, 200, 100, 200, 100, 200};
    		
   			//バッジの追加
    		if (badgeNum != 0) {
    			notification.number = badgeNum;
    		}
            
    		//通知の送信
        	NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    		notificationManager.notify(R.string.app_name, notification);
        }
    }
    
    //トーストの表示
    public static void toast(Context context,String text) {
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
