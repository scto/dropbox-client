package com.example.file_system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.example.box_client.Explorer;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class OnClickDownload implements OnClickListener {

	private Activity activity;
	private String path;
	private String fileName;
	
	public OnClickDownload (Activity activity, String path, String fileName) {
		this.activity = activity;
		this.path = path;
		this.fileName = fileName;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		downloadDropboxFile(path, fileName);
		Toast.makeText(activity, "Downloading successfully!", 3000).show();
		activity.finish();
	}

	private void downloadDropboxFile(String directory, String fileName) {
		FileOutputStream outputStream = null;
		try {
			String curPath = FileSystem.getInstance().getCurPath() + "/" + fileName;
			File file = new File(curPath);
			outputStream = new FileOutputStream(file);
			DropboxFileInfo info = Explorer.getInstance().getMDBApi()
					.getFile(directory, null, outputStream, null);
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
	}
	
}
