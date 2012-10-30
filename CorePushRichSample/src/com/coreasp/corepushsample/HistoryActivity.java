package com.coreasp.corepushsample;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coreasp.CorePushNotificationHistoryManager;
import com.coreasp.CorePushNotificationHistoryManagerListener;
import com.coreasp.CorePushNotificationHistoryModel;

/**
 *　通知履歴を表示するアクティビティ
 */
public class HistoryActivity extends ListActivity implements CorePushNotificationHistoryManagerListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  new ArrayList<String>()));
    }
 
    public void onResume() {
    	super.onResume();
    	
    	CorePushNotificationHistoryManager manager = new CorePushNotificationHistoryManager(this);
    	
    	//コールバックリスナーを設定
    	manager.setListener(this);
    	
    	//プログレスダイアログを表示
    	manager.setProgressDialog(true);
    	
    	//通知履歴取得リクエストの実行
    	manager.execute();
    }
    
    public void onPause() {
    	super.onPause();
    	
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	//リストアイテムをタップされた時の動作を定義
        TextView t = (TextView)v;
        super.onListItemClick(l, v, position, id);
    }
    
	/**
	 * 通知履歴の取得に成功した場合
	 */
	@Override
	public void notificationHistoryManagerSuccess() {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListView().getAdapter();
		adapter.clear();
		
		ArrayList<CorePushNotificationHistoryModel> historyModelList = (ArrayList<CorePushNotificationHistoryModel>) CorePushNotificationHistoryManager.getNotificationHistoryModelList();
		for (int i = 0; i < historyModelList.size(); i++) {
			CorePushNotificationHistoryModel historyModel = (CorePushNotificationHistoryModel) historyModelList.get(i);
			String message = historyModel.getMessage();
			String regDate = historyModel.getRegDate();
			
			// 「通知メッセージ : 通知日時」の形式のテキストを設定
			String text = message + " : " + regDate;
			adapter.add(text);
;		}
		
		adapter.notifyDataSetChanged();
	}

    /**
     * 通知履歴の取得に失敗した場合
     */
	@Override
	public void notificationHistoryManagerFail() {
		
	}


}