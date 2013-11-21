package com.example.box_client;

import java.util.ArrayList;

import com.dropbox.client2.DropboxAPI.Entry;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnClickItemForwardExplorer implements OnItemClickListener {
	
	private Explorer explorer;
	private ArrayList<Entry> listExplorer;
	private Activity fileExplorer;
	
	/*
	 * Constructor
	 */
	public OnClickItemForwardExplorer (Explorer explorer, ArrayList<Entry> listExplorer, Activity fileExplorer) {
		this.explorer = explorer;
		this.listExplorer = listExplorer;
		this.fileExplorer = fileExplorer;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		// TODO Auto-generated method stub
		String path = listExplorer.get(index).path;
		explorer.goForward(path);
		Intent intent = fileExplorer.getIntent();
		fileExplorer.finish();
		fileExplorer.startActivity(intent);
	}

}
