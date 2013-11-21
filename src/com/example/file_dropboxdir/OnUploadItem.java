package com.example.file_dropboxdir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.dropbox.client2.DropboxAPI.Entry;
import com.example.box_client.Explorer;
import com.example.box_client.FileExplorer;
import com.example.box_client.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class OnUploadItem extends AsyncTask<String, Void, Void>{

	private Activity activity;
	private String path;
	private FileDropbox instance;
	
	public OnUploadItem (Activity activity, String path, FileDropbox instance) {
		this.activity = activity;
		this.path = path;
		this.instance = instance;
	}
	
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(0);
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		File file = new File(path);
		FileInputStream inputStream = null;
		Log.e("FILE NAME", file.getName());
		try {
			String path = instance.getPath() + "/" + file.getName();
			Log.e("TARGET PATH", path);
			inputStream = new FileInputStream(file);
			Entry newEntry = Explorer
					.getInstance()
					.getMDBApi()
					.putFile(path, inputStream, file.length(),
							null, null);
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	protected void onPostExecute(Void unused) {
		showMess("Uploading successfully!");
		instance.goForward(MainActivity.ROOT);
		activity.dismissDialog(0);
		activity.removeDialog(0);
		activity.finish(); // It will go back to FileSystemExplorer Activity
		// Jumping to Dropbox directory
		Intent fileExplorer = new Intent(activity, FileExplorer.class);
		activity.startActivity(fileExplorer);
	}
	
	private void showMess(String str) {
		Toast toast = Toast.makeText(activity.getApplicationContext(), str,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
