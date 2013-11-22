package com.example.box_client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

public class OnClickBackward extends AsyncTask<String, Void, Void>{
	private Finder explorer;
	private Activity activity;
	
	/*
	 * Constructor
	 */
	public OnClickBackward (Finder explorer, Activity activity) {
		this.explorer = explorer;
		this.activity = activity;
	}
	
	@SuppressWarnings("deprecation")
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(9);
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		explorer.goBackward();
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
