package com.example.editing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.example.box_client.Explorer;
import com.example.box_client.FileExplorer;
import com.example.box_client.MainActivity;
import com.example.file_system.FileSystem;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class OnClickSaving extends AsyncTask<Void, Void, Void>{
	
	private Activity activity;
	private String path;
	private EditText txtEdit;
	
	public OnClickSaving (Activity activity, String path, EditText txtEdit) {
		this.activity = activity;
		this.path = path;
		this.txtEdit = txtEdit;
	}
	
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(1);
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		saveToFile("temp.txt",txtEdit.getText().toString());
		try {
			FileInputStream inputStream = activity.openFileInput("temp.txt");
			Entry response = Explorer.getInstance().getMDBApi().putFileOverwrite(path, inputStream, txtEdit.getText().toString().length(), null);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			activity.deleteFile("temp.txt");
		}
		return null;
	}

	
	protected void onPostExecute(Void unused) {
		showMess("Editting successfully!");
		Explorer.getInstance().goForward(MainActivity.ROOT);
		activity.dismissDialog(1);
		activity.removeDialog(1);
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
	
	public void saveToFile(String filename, String input) {
		try {
			FileOutputStream fOut = activity.openFileOutput(filename,activity.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);		 
            
            osw.write(input);
          
            osw.flush();
            osw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
