package com.example.file_dropboxdir;

import com.dropbox.client2.exception.DropboxException;
import com.example.box_client.ExplorerActivity;
import com.example.box_client.MainActivity;
import com.example.file_system.FileSystem;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

public class OnMovingItem extends AsyncTask<String, Void, Void> {
	
	private FileDropbox instance;
	private Activity activity;

	public OnMovingItem(FileDropbox instance, Activity activity) {
		this.instance = instance;
		this.activity = activity;
	}
	
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(2);
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String fromPath = instance.getFromPath();
		String toPath = instance.getToPath() + MainActivity.ROOT + instance.getFileName();
		try {
			instance.getmApi().move(fromPath, toPath);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Void unused) {
		showMess("Item has moved successfully!");
		instance.goForward(MainActivity.ROOT);
		activity.dismissDialog(2);
		activity.removeDialog(2);
		activity.finish();
		Intent fileExplorer = new Intent(activity, ExplorerActivity.class);
		activity.startActivity(fileExplorer);
	}
	
	private void showMess(String str) {
		Toast toast = Toast.makeText(activity.getApplicationContext(), str,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
