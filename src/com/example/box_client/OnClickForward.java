package com.example.box_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI.Entry;

public class OnClickForward extends AsyncTask<String, Void, Void> {

	private Finder explorer;
	private ArrayList<Entry> listExplorer;
	private Activity activity;
	private int index;

	/*
	 * Constructor
	 */
	public OnClickForward(Finder explorer, Activity activity, int index) {
		this.explorer = explorer;
		this.listExplorer = explorer.getListExplorer();
		this.activity = activity;
		this.index = index;
	}

	@SuppressWarnings("deprecation")
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(9);
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		String path = listExplorer.get(index).path;
		explorer.goForward(path);
		return null;
	}

	@SuppressWarnings("deprecation")
	protected void onPostExecute(Void unused) {
		Intent intent = activity.getIntent();
		activity.dismissDialog(9);
		activity.removeDialog(9);
		activity.finish();
		activity.startActivity(intent);
	}
}
