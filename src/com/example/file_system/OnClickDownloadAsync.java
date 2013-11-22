package com.example.file_system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.example.box_client.Explorer;
import com.example.box_client.FileExplorer;
import com.example.box_client.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

public class OnClickDownloadAsync extends AsyncTask<String, Void, Void> {
	
	private Activity activity;
	private String path;
	private String fileName;
	
	public OnClickDownloadAsync (Activity activity, String path, String fileName) {
		this.activity = activity;
		this.path = path;
		this.fileName = fileName;
	}
	
	
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(0);
	}


	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		FileOutputStream outputStream = null;
		try {
			String curPath = FileSystem.getInstance().getCurPath() + "/" + fileName;
			File file = new File(curPath);
			outputStream = new FileOutputStream(file);
			DropboxFileInfo info = Explorer.getInstance().getMDBApi()
					.getFile(path, null, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	protected void onPostExecute(Void unused) {
		showMess("Downloading successfully!");
		FileSystem.getInstance().goForward(MainActivity.ROOT);
		activity.dismissDialog(0);
		activity.removeDialog(0);
		activity.finish();
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
