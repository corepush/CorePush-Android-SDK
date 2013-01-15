# Core Push Android SDK

##概要

Core Push Android SDK は、プッシュ通知ASPサービス「CORE PUSH」の Android用のSDKになります。ドキュメントは CORE PUSH Developer サイトに掲載しております。

 
■公式サイト

CORE PUSH：<a href="http://core-asp.com">http://core-asp.com</a>

CORE PUSH Developer（開発者向け）：<a href="http://developer.core-asp.com">http://developer.core-asp.com</a>##動作条件
* GCM方式による通知はAndroid2.2以上が動作対象になります。* SDK/Eternal/gcm.jar をプロジェクトのlibsフォルダにコピーしてください。* SDK/corepush.jar を プロジェクトの libsフォルダにコピーしてください。
* リッチ通知の動作は、サンプルのCorePushRichSampleプロジェクトでご確認できます。##アプリの通知設定

Core Push Android SDKを利用するための設定を行います。

###ApplicationContext.xmlの設定

* GCM方式による通知は Android 2.2以上で動作するため、minSdkVersion に8以上の値を指定してください。

```
	<!-- SDKの最小のAPIレベルを指定。C2DMはAPIレベル8以上で動作可能 -->
	<uses-sdk android:minSdkVersion="8" android:targetSdkVersion="16"/>
```

* 実行するアプリケーションのみが通知を受信するために、以下のパーミッションを指定してください。android:name のcom.coreasp.corepushsample の部分は実行するアプリケーションのパッケージ名に置き換えてください。

```	 <permission android:name="com.coreasp.corepushsample.permission.C2D_MESSAGE" android:protectionLevel="signature" />    <uses-permission android:name="com.coreasp.corepushsample.permission.C2D_MESSAGE" />
```

* その他、通知の利用の際に必要な以下のパーミッションを指定してください。

```
    <!-- インターネット接続のパーミッション設定 -->    <uses-permission android:name="android.permission.INTERNET"></uses-permission>    <!-- GCM requires a Google account. -->    <uses-permission android:name="android.permission.GET_ACCOUNTS" />    <!-- Keeps the processor from sleeping when a message is received. -->    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />    
```


* ブロードキャストレシーバを指定してください。 category のandroid:name の com.coreasp.corepushsmapleの部分は実行するアプリケーションのパッケージ名に置き換えてください。また、receive の android:name には SDK内の com.coreasp.CorePushBroadcastReceiver を指定してください。

```
    <!-- GCM用のブロードキャストレシーバーを設定 -->
    <receive
        android:name="com.coreasp.CorePushBroadcastReceiver"
        android:permission="com.google.android.c2dm.permission.SEND" >
        <intent-filter>
            <!-- Receives the actual messages. -->
            <action android:name="com.google.android.c2dm.intent.RECEIVE" />
            <!-- Receives the registration id. -->
            <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
            <category android:name="com.coreasp.corepushsample" />
        </intent-filter>
	</receiver>

```
* インテントサービスを指定してください。service の andorid:name には SDK内の com.coreasp.CorePushIntentService を指定してください。

```
  	<!-- GCM用のインテントサービスを設定 -->    <service android:name="com.coreasp.CorePushIntentService" />```

以下に、サンプルプロジェクトのCorePushSample で利用している ApplicationManifest.xml の内容を記載します。

```	<?xml version="1.0" encoding="utf-8"?>	<manifest xmlns:android="http://schemas.android.com/apk/res/android"    package="com.coreasp.corepushsample"    android:versionCode="1"    android:versionName="1.0" >    <!-- SDKの最小のAPIレベルを指定。C2DMはAPIレベル8以上で動作可能 -->    <uses-sdk android:minSdkVersion="8" android:targetSdkVersion="16"/>        <!-- インターネット接続のパーミッション設定 -->    <uses-permission android:name="android.permission.INTERNET"></uses-permission>    <!-- GCM requires a Google account. -->    <uses-permission android:name="android.permission.GET_ACCOUNTS" />    <!-- Keeps the processor from sleeping when a message is received. -->    <uses-permission android:name="android.permission.WAKE_LOCK" />        <!-- 通知時のバイブレーションのパーミッション設定  -->	<uses-permission android:name="android.permission.VIBRATE" />        	<!-- C2DMのパーミッション設定 -->    <permission android:name="com.coreasp.corepushsample.permission.C2D_MESSAGE" android:protectionLevel="signature" />    <uses-permission android:name="com.coreasp.corepushsample.permission.C2D_MESSAGE" />    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />        <application        android:icon="@drawable/ic_launcher"        android:label="@string/app_name"  android:debuggable="true">                <!-- 初回起動のアクティビティを設定 -->        <activity            android:label="@string/app_name"            android:name=".NotificationActivity" >            <intent-filter >                <action android:name="android.intent.action.MAIN" />                <category android:name="android.intent.category.LAUNCHER" />            </intent-filter>        </activity>                <!-- GCM用のブロードキャストレシーバーを設定 -->        <receiver            android:name="com.coreasp.CorePushBroadcastReceiver"            android:permission="com.google.android.c2dm.permission.SEND" >            <intent-filter>                <!-- Receives the actual messages. -->                <action android:name="com.google.android.c2dm.intent.RECEIVE" />                <!-- Receives the registration id. -->                <action android:name="com.google.android.c2dm.intent.REGISTRATION" />                <category android:name="com.coreasp.corepushsample" />            </intent-filter>        </receiver>
	    	<!-- GCM用のインテントサービスを設定 -->        <service android:name="com.coreasp.CorePushIntentService" />            </application></manifest>```###CORE PUSHの設定キーの指定
Core Push管理画面 にログインし、ホーム画面からAndroidアプリの設定キーを確認してください。 この設定キーをCorePushManager#setConfigKey で指定します。	
	CorePushManager.getInsance().setConfigKey("XXXXXXXXXX");###SenderId(ProjectId)の指定
<a href="http://developer.core-asp.com/gcm.php">Project ID（アプリ）とAPI Key（管理画面）の作成方法</a> を参考に プロジェクトIDを取得し、CorePushManager#setSenderIdで指定します。	CorePushManager.getInstance().setSenderId("1234567890");	###通知から起動するアクティビティの指定
ステータスバーの通知をタップした後に起動するアクティビティのクラスを CorePushManager#setActivity で指定します。
	CorePushManager.getInstance().setActivity(NotificationActivity.class);
###通知アイコンの指定
通知時のステータスバーに表示されるアイコンのリソースIDを指定します。
	CorePushManager.getInstance().setIconResourceId(R.drawable.ic_launcher);
##デバイスの通知登録解除
デバイスが通知を受信できるようにするには、CORE PUSH にデバイストークンを送信します。またデバイスが通知を受信できないようにするには、CORE PUSH からデバイストークンを削除します。

###デバイストークンの登録
CORE PUSH にデバイストークンを登録するには、CorePushManager#registToken を呼び出します。
	
	CorePushManager.getInstance().registToken(this);
	
本メソッドはアプリの初回起動時かON/OFFスイッチなどで通知をONにする場合に使用してください。		###デバイストークンの削除
CORE PUSH からデバイストークンを削除するには、CorePushManager#removeToken を呼び出します。
	CorePushManager.getInstance().removeToken(this);		
本メソッドはON/OFFスイッチなどで通知をOFFにする場合に使用してください。

##通知履歴の表示


###通知履歴の取得
CorePushNotificationHistoryManager#execute を呼び出すことで通知履歴を最大100件取得できます。

		CorePushNotificationHistoryManager manager = new CorePushNotificationHistoryManager(this);
    	
    	//コールバックリスナーを設定
    	manager.setListener(this);
    	
    	//プログレスダイアログを表示
    	manager.setProgressDialog(true);
    	
    	//通知履歴取得リクエストの実行
    	manager.execute();

取得した通知履歴のオブジェクトの配列は、CorePushNotificationHistoryManager#notificationHistoryModelArray に格納されます。

		ArrayList<CorePushNotificationHistoryModel> historyModelList = (ArrayList<CorePushNotificationHistoryModel>) CorePushNotificationHistoryManager.getNotificationHistoryModelList();
	
  
上記の配列により、個々の通知履歴の CorePushNotificationHistoryModel オブジェクトを取得できます。CorePushNotificationHistoryModelオブジェクトには、履歴ID、通知メッセージ、通知日時、リッチ通知URLが格納されます。

		// 例) 451
		NSString historyId = historyMode.getHistoryId();
	
		// 例) CORE PUSH からのお知らせ!
		NSString message = historyMode.getMessage();

		// 例) http://core-asp.com
		NSString* url = historyModel.getUrl();

		// 例) 2012-08-18 17:48:30
		NSString* regDate = historyModel.getRegDate();


##リッチ通知画面(ポップアップウインドウ)の表示

リッチ通知を受信した場合は、通知から起動したアクティビティ内のIntentオブジェクトにリッチ通知用のURLが含まれます。リッチ通知用のURLは、以下の方法でIntentオブジェクトから取得できます。

	    	//リッチ通知のURLを取得
    		String url = CorePushManager.getInstance().getUrl(intent);
	
上記のURLをリッチ通知画面で表示するには、レイアウトファイルの rich_windows を使用します。
以下は CorePushRichSampleプロジェクトからの抜粋になります。

			//ポップアウトウインドウをレイアウトファイルから取得
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


##現在位置情報の送信

CorePushManager#reportCurrentLocation 、現在の位置情報(緯度、経度)をパラメータに付加し、CORE PUSHサーバにデバイストークンの送信を行います。

    //現在地の位置情報を送信する。
    CorePushManager.getInstance().reportCurrentLocation(this);
    
位置情報を取得するには、ApplicationManifest.xml に 以下のパーミッションを設定する必要があります。

    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
	

##カテゴリの設定
###１次元カテゴリ設定
デバイストークン登録APIの category_id パラメータの設定を行うことができます。パラメータの設定を行うには、
CorePushManager#setCategoryIds で カテゴリID(文字列型)のリストを指定します。以下はカテゴリIDのリストの作成例になります。<br/>
※例は事前に管理画面で1から4までのカテゴリを設定しておいたものとする。

    List<String> categoryIds = new ArrayList<String>();
    
    //1:北海道、2:東北 3:関東、4:近畿
    categoryIds.add("1");
    categoryIds.add("2");
    categoryIds.add("3");
    categoryIds.add("4");
    
    CorePushManager.getInstance().setCategoryIds(categoryIds);
    
上記カテゴリの設定後にデバイストークンを送信した場合、設定したcategory_id パラメータの値をCORE PUSHサーバにPOSTします。
(category_idパラメータを設定しない場合のデフォルト値は 1 になります。)

###２次元カテゴリ設定
デバイストークン登録APIの category_id パラメータの設定を行うことができます。パラメータの設定を行うには、
CorePushManager#setMultiCategoryIds で カテゴリIDのマップを指定します。以下はカテゴリIDのマップの作成例になります。<br/>
※例は事前に管理画面で1から4までのカテゴリを設定しておいたものとする。

	Map<String, List<String>> multiCategoryIds = new HashMap<String, List<String>>();
	multiCategoryIds.put("1", Arrays.asList(new String[]{"神奈川"})); //地域が「神奈川」の場合
	multiCategoryIds.put("2", Arrays.asList(new String[]{"男性"}));   //性別が「男性」の場合
	multiCategoryIds.put("3", Arrays.asList(new String[]{"20代"}));   //年代が「20代」の場合
	multiCategoryIds.put("4", Arrays.asList(new String[]{"音楽", "読書"})); //好きなジャンルが「音楽」と「読書」の場合
	CorePushManager.getInstance().setMultiCategoryIds(multiCategoryIds);
    
上記カテゴリの設定後にデバイストークンを送信した場合、設定したcategory_id パラメータの値をCORE PUSHサーバにPOSTします。
(1次元カテゴリと2次元カテゴリの両方が設定されている場合、category_id パラメータには２次元カテゴリの設定が優先されます。category_idパラメータを設定しない場合のデフォルト値は 1 になります。)
    
    
##ユーザー間プッシュ通知
ユーザー間のプッシュ通知を実現するには、事前にアプリ側でユーザーのデバイストークンのCORE PUSHへの登録とユーザー属性の御社サーバへの登録を行う必要があります。全体のイメージ図につきましては、<a href="http://developer.core-asp.com/api_image.php">http://developer.core-asp.com/api_image.php</a> をご参照ください。
### CORE PUSHへのデバイストークンの登録

デバイストークンの登録を行う前に、CorePushManager#setAppUserIdでアプリ内のユーザーIDを指定します。
	//アプリ内でのユーザーの識別IDを登録
	CorePushManager.getInstance().setAppUserId("userid");
	//デバイストークンの登録	CorePushManager manager = CorePushManager.getInstance();
	manager.registToken(SettingActivity.this);
  
上記により、api.core-asp.com/android_token_regist.php のトークン登録APIに
対して、app_user_id のパラメータが送信され、アプリ内でのユーザーの識別IDとデバイストークンが
紐づいた形でDBに保存されます。
  ### 御社サーバへのユーザー属性の登録
CorePushRegisterUserAttributeManager#execute: で御社サーバにユーザー属性の登録を行う前に
、CorePushManager#setAppUserIdでアプリ内でのユーザーの識別IDを指定します。	//アプリ内でのユーザーの識別IDを登録
	CorePushManager.getInstance().setAppUserId("userid");


ユーザー属性を定義したリストを作成します。
   
    //ユーザー属性の配列を作成。例) 1:いいね時の通知許可、3:コメント時の通知許可、7:フォロー時の通知許可
   
    List attributes = new ArrayList();
    attributes.add("1");
    attributes.add("3");
    attributes.add("7");

ユーザー属性を送信する御社サーバ上の任意のURLを指定します。

	//ユーザー属性を送信する御社の任意のURLを指定
    String userAttributeApi = @"ユーザ属性を送信する御社の任意のURL";

作成したユーザー属性を定義した配列とユーザー属性を送信するAPIのURLを引数として、CorePushRegisterUserAttributeManagerオブジェクトを生成します。

	CorePushRegisterUserAttributeManager attributeManager = new 	CorePushRegisterUserAttributeManager(this, attributes, userAttributeApi);

上記で作成したCorePushRegisterUserAttributeManagerオブジェクトのexecuteメソッドを呼び出すことで、アプリ内でのユーザーの識別IDとユーザー属性を御社サーバに送信します。

	//アプリ内でのユーザーの識別IDとユーザー属性の送信
    attributeManager.execute();

特定のユーザーに対してプッシュ通知を行うには、通知送信リクエストAPIに対して、御社サーバから通知の送信依頼
を行います。詳細につきましては、<a href="http://developer.core-asp.com/api_request.php">http://developer.core-asp.com/api_request.php</a> をご参照ください。


## アクセス解析

### 通知からの起動数の把握

通知からのアプリの起動時にアクセス解析用のパラメータをCORE PUSHサーバに対して送信することで、管理画面の通知履歴から通知からの起動数を把握することができます。

アクセス解析用のパラメータを CORE PUSHサーバに対して送信するには、通知から起動される Intentオブジェクトから CorePushManager#getPushIdで通知IDを取得し、CorePushAppLaunchAnalyticsManager#executeで
通知IDを送信します。executeメソッドの引数には デバイストークン、設定キー、通知ID、緯度、経度の値を順に指定します。アプリ起動時の緯度・経度を送信しない場合は、0を指定します。

		//intentオブジェクトから通知IDを取得する
        String pushId = manager.getPushId(intent);
        
        if (pushId != null) {
        	CorePushAppLaunchAnalyticsManager analyticsManager = new CorePushAppLaunchAnalyticsManager(this);
        	analyticsManager.execute(manager.getToken(this), CONFIG_KEY ,pushId, "0", "0");
        }
        
## プッシュ通知の送信エラー

###エラー内容の把握

プッシュ通知の送信に失敗した場合、管理画面の送信履歴のエラー数のリンク先からエラー画面を確認できます。
エラー区分としては下記に分類されます。

1. アプリ削除でトークンが無効となった場合や、形式不正なトークンなどによるエラー
2. 上記以外のエラー（通信失敗、その他）


