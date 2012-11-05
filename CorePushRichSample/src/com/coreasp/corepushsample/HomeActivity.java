package com.coreasp.corepushsample;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.coreasp.CorePushManager;

/**
 * 設定画面と通知履歴画面のタブを持つアクティビティ
 */
public class HomeActivity extends TabActivity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        CorePushManager manager = CorePushManager.getInstance();
        
        //設定キーを指定
        manager.setConfigKey("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

        //senderId(projectId)を指定
        manager.setSenderId("0123456789");

        //通知から起動するアクティビティを指定
        manager.setActivity(HomeActivity.class);
        
        //通知アイコンを指定
        manager.setIconResourceId(R.drawable.sample_0);
        
//        //アプリ内のユーザーIDを指定
//        CorePushManager.getInstance().setAppUserId("userid");
        
        // 通知センターから起動時に通知パラメータを取得
        Intent intent = getIntent();
//        String date = manager.getDate(intent);
//        String title = manager.getTitle(intent);
//        String message = manager.getMessage(intent);
//        String url = manager.getUrl(intent);   
       
        //タブ画面を初期化
        this.initTabs();
        
        //リッツ通知のポップアップウインドウを表示
        this.showPopupView(intent);
    }
    
	protected void initTabs() {
		Resources res = getResources();
		TabHost tabHost = getTabHost();

		TabHost.TabSpec spec;
		Intent intent;

		// 設定画面
		intent = new Intent().setClass(this, SettingActivity.class);
		spec = tabHost.newTabSpec("設定")
				.setIndicator("設定", res.getDrawable(R.drawable.ic_launcher))
				.setContent(intent);
		tabHost.addTab(spec);

		// 履歴画面
		intent = new Intent().setClass(this, HistoryActivity.class);
		spec = tabHost.newTabSpec("通知履歴")
				.setIndicator("通知履歴", res.getDrawable(R.drawable.ic_launcher))
				.setContent(intent);

		tabHost.addTab(spec);
		tabHost.setCurrentTab(0);
	}
    
	/**
	 * リッチ通知のポップアップウインドウを表示
	 */
    public void showPopupView(Intent intent) {
    
    	//リッチ通知のURLを取得
    	final String url = CorePushManager.getInstance().getUrl(intent);

    	// URLの文字列が空ではない場合、リッチ通知の画面を表示
		if (url != null && !"".equals(url.trim())) {
			LinearLayout window = (LinearLayout) findViewById(R.id.rich_windows);
			window.setVisibility(View.VISIBLE);
			window.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
					R.anim.fadein));
			
			// 閉じるボタンタップ時の動作を定義
			Button clouse = (Button) findViewById(R.id.clouse);
			clouse.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					LinearLayout window = (LinearLayout) findViewById(R.id.rich_windows);
					window.setVisibility(View.GONE);
				}
			});

			// タイトルを設定
			TextView title = (TextView) findViewById(R.id.title);
			title.setText("CorePushSample からのお知らせ");

			// WebViewを作成
			final WebView webview = (WebView) findViewById(R.id.web);
			webview.getSettings().setUseWideViewPort(true);
			webview.getSettings().setLoadWithOverviewMode(true);
			webview.setVerticalScrollbarOverlay(true); 
			webview.getSettings().setJavaScriptEnabled(true);
			webview.loadUrl(url);
			webview.setInitialScale(50);
			webview.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								HomeActivity.this);
						// ダイアログの設定
						alertDialog.setTitle("確認"); 
						alertDialog.setMessage("ブラウザを起動します。よろしいでしょうか。"); 
						// いいえボタンの設定
						alertDialog.setPositiveButton("いいえ",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								});
						// はいボタンの設定
						alertDialog.setNeutralButton("はい",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										Uri uri = Uri.parse(url);
										Intent i = new Intent(
												Intent.ACTION_VIEW, uri);
										startActivity(i);

									}
								});
						// ダイアログの表示
						alertDialog.setCancelable(true);
						alertDialog.show();
					}
					return true;
				}
			});
			webview.setWebViewClient(new WebViewClient() {
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					
					super.onReceivedError(view, errorCode, description,
							failingUrl);
				}
			});
		}
    }
    
}