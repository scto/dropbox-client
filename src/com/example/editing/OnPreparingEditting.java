package com.example.editing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dropbox.client2.exception.DropboxException;
import com.example.box_client.Explorer;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

public class OnPreparingEditting extends AsyncTask<String, Void, Void>{

	private Activity activity;
	private String path;
	private EditText txtEdit;
	
	public OnPreparingEditting (Activity activity, String path, EditText txtEdit) {
		this.activity = activity;
		this.path = path;
		this.txtEdit = txtEdit;
	}
	
	
	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(0);
	}

	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			FileOutputStream fOut = activity.openFileOutput("temp.txt", activity.MODE_PRIVATE);
			Explorer.getInstance().getMDBApi().getFile(path, null, fOut, null);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	protected void onPostExecute(Void unused) {
		activity.dismissDialog(0);
		activity.removeDialog(0);
		
		String content = readFromFile("temp.txt");
		txtEdit.setText(content);
		activity.deleteFile("temp.txt");
	}
	


	private String readFromFile(String filename) {

	    String ret = "";

	    try {
	        FileInputStream inputStream = activity.openFileInput(filename);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	                stringBuilder.append("\n");
	            }
	            if (stringBuilder.length()>0) {
	            	stringBuilder.deleteCharAt(stringBuilder.length()-1);
	            }
	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
}
