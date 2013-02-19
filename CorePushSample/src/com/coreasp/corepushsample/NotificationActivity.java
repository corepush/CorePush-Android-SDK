package com.coreasp.corepushsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.coreasp.CorePushAppLaunchAnalyticsManager;
import com.coreasp.CorePushManager;

public class NotificationActivity extends Activity {
	
    /**
     * 画面(ビュー)を設定
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        CorePushManager manager = CorePushManager.getInstance();
        
        //設定キーを指定
        manager.setConfigKey("XXXXXXXXXXXXXXXXX");
        
        //senderId(projectId)を指定
        manager.setSenderId("1234567890");
        
        //通知から起動するアクティビティを指定
        manager.setActivity(NotificationActivity.class);
        
        //通知アイコンを指定
        manager.setIconResourceId(R.drawable.ic_launcher);
        
        //通知スタイルの設定 (デフォルトのスタイルは通知ステータスバー)
        //ステータスバーに通知する場合は、CorePushManager.NOTIFICATION_STYLE_STATUS_BAR を指定
        //ダイアログで通知する場合は、CorePushManager.NOTIFICATION_STYLE_DIALOG を指定
        manager.setNotificationStyle(CorePushManager.NOTIFICATION_STYLE_STATUS_BAR);
        
        //CORE PUSHにデバイストークンを登録
        manager.registToken(this);
        
        ////CORE PUSHからデバイストークンを削除
        //manager.removeToken(this);
        
        // 通知センターから起動時に通知パラメータを取得
        Intent intent = getIntent();
        
        String date = manager.getDate(intent);
        String title = manager.getTitle(intent);
        String message = manager.getMessage(intent);
        String url = manager.getUrl(intent);
        
        //intentオブジェクトから通知IDを取得する
        String pushId = manager.getPushId(intent);

        if (pushId != null) {
	    CorePushAppLaunchAnalyticsManager analyticsManager = new CorePushAppLaunchAnalyticsManager(this);
            analyticsManager.execute(manager.getToken(this), "XXXXXXXXXXXXXXXXX" ,pushId, "0", "0");
        }
    }
    
}
