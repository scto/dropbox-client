package com.example.file_system;

import java.io.File;

import com.example.box_client.FileExplorer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OnClickItemForwardExplorer implements OnItemClickListener {

	private FileSystem fileSystem;
	private Activity activity;

	public OnClickItemForwardExplorer(FileSystem fileSystem, Activity activity) {
		this.fileSystem = fileSystem;
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		// TODO Auto-generated method stub
		File tmp = fileSystem.getListExplorer().get(index);
		String path = tmp.getPath();

		if (fileSystem.goForward(path)) {
			Intent intent = activity.getIntent();
			activity.finish();
			activity.startActivity(intent);
		} else {
			showMess("Permission denied!");
		}
	}
	private void showMess(String str) {
		Toast toast = Toast.makeText(activity.getApplicationContext(), str,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}

}
