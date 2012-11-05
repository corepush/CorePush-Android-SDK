package com.coreasp.corepushsample;

import com.coreasp.CorePushManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

/**
 * 通知設定画面のアクティビティ
 */
public class SettingActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);      
        
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        
        //デバイストークンを取得する
        String token = CorePushManager.getInstance().getToken(this);
     
        //プッシュ通知の通知設定の初期化
        if (token.equals("")) {
       	   //デバイストークンが空の場合は、通知設定をOFF
        	checkBox.setChecked(false);     
        } else {
        	 //デバイストークンが空でない場合は、通知設定をOFF
        	checkBox.setChecked(true);
        }
        
        // チェックボックスがクリックされた場合の動作を定義
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
               
                //プッシュ通知の状態を取得
                boolean checked = checkBox.isChecked();
                
                CorePushManager manager = CorePushManager.getInstance();
                
                //プッシュ通知がONの場合
                if (checked) {
                    //CORE PUSHにデバイストークンを登録
                    manager.registToken(SettingActivity.this);
                    
                //プッシュ通知がOFFの場合
                } else {
                    ////CORE PUSHからデバイストークンを削除
                    manager.removeToken(SettingActivity.this);
                }
            }
        });

    }
 
}