package com.example.file_system;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OnItemSysLongClicked implements OnItemLongClickListener{
	
	/*
	 * Properties
	 */
	private Activity activity;
	
	public OnItemSysLongClicked (Activity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int index, long arg3) {
		// TODO Auto-generated method stub
		activity.registerForContextMenu(adapterView);
		FileSystemExplorer.ITEM_ID_LONGCLICKED = index;
		return false;
	}
}
