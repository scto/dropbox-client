package com.example.file_dropboxdir;

import com.dropbox.client2.exception.DropboxException;
import com.example.box_client.Explorer;
import com.example.box_client.FileExplorer;
import com.example.box_client.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class OnSelectPath implements OnClickListener {

	private FileDropbox instance;
	private Activity activity;

	public OnSelectPath(FileDropbox instance, Activity activity) {
		this.instance = instance;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String fromPath = instance.getFromPath();
		String toPath = instance.getToPath() + MainActivity.ROOT + instance.getFileName();
		String s = "";
		try {
			instance.getmApi().move(fromPath, toPath);
			s = "Item has moved successfully!";
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			s = "Moving failed!";
		}
		Toast.makeText(activity, s, 3000).show(); // Note user
	}

}
