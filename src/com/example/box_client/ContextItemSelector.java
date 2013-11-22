package com.example.box_client;

import com.example.editing.FileEditing;
import com.example.file_dropboxdir.FileDropboxExplorer;
import com.example.file_system.FileSystemExplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

public class ContextItemSelector extends AsyncTask<String, Void, Void>{
	
	private Activity activity;
	private int idItem;
	private Finder finder;
	private String fileName;
	private String directory;
	
	public ContextItemSelector (Finder finder, Activity activity, int idItem, String fileName, String directory) {
		this.finder = finder;
		this.activity = activity;
		this.idItem = idItem;
		this.fileName = fileName;
		this.directory = directory;
	}
	
	@SuppressWarnings("deprecation")
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(idItem);
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		switch (idItem) {
		case 0:
			finder.delPath(directory);
			Intent intent = activity.getIntent();
			activity.finish();
			activity.startActivity(intent);
			break;
		case 1:
			finder.renameDirectory(directory, fileName);
			Intent intent_ = activity.getIntent();
			activity.finish();
			activity.startActivity(intent_);
			break;
		case 2:
			// Moving Directory
			finder.goForward(MainActivity.ROOT);
			activity.finish();
			Intent fileDropboxExplorer = new Intent(activity, FileDropboxExplorer.class);
			fileDropboxExplorer.putExtra("OLD_PATH", directory);
			fileDropboxExplorer.putExtra("FILE_NAME", fileName);
			fileDropboxExplorer.putExtra("ID", 2);
			activity.startActivity(fileDropboxExplorer);
			break;
		case 3:
			// Download
			finder.goForward(MainActivity.ROOT);
			activity.finish();
			Intent fileSystemExplorer = new Intent(activity, FileSystemExplorer.class);
			fileSystemExplorer.putExtra("OLD_PATH", directory);
			fileSystemExplorer.putExtra("FILE_NAME", fileName);
			fileSystemExplorer.putExtra("ID", 3);
			activity.startActivity(fileSystemExplorer);
			break;
		case 4:
			// Upload
			finder.goForward(MainActivity.ROOT);
			Intent fileSysExplorer = new Intent(activity, FileSystemExplorer.class);
			fileSysExplorer.putExtra("ID", 4);
			activity.startActivity(fileSysExplorer);
			Explorer.getInstance().setPath(MainActivity.ROOT);
			activity.finish();
			break;
		case 5:
			// Modify
			finder.goForward(MainActivity.ROOT);
			Intent fileEditting = new Intent(activity, FileEditing.class);
			fileEditting.putExtra("OLD_PATH", directory);
			activity.startActivity(fileEditting);
			Explorer.getInstance().setPath(MainActivity.ROOT);
			activity.finish();
			break;
		default:
			break;

		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	protected void onPostExecute(Void unused) {
		activity.dismissDialog(idItem);
		activity.removeDialog(idItem);
	}
}
