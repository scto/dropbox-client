package com.example.file_dropboxdir;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dropbox.client2.DropboxAPI.Entry;

public class OnClickItemForwardExplorer implements OnItemClickListener {
	
	private FileDropbox explorer;
	private ArrayList<Entry> listExplorer;
	private Activity activity;
	
	/*
	 * Constructor
	 */
	public OnClickItemForwardExplorer (FileDropbox explorer, Activity activity) {
		this.explorer = explorer;
		this.listExplorer = explorer.getListExplorer();
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		// TODO Auto-generated method stub
		String path = listExplorer.get(index).path;
		explorer.goForward(path);
		Intent intent = activity.getIntent();
		activity.finish();
		activity.startActivity(intent);
	}

}