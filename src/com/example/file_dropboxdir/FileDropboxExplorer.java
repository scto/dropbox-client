package com.example.file_dropboxdir;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI.Entry;
import com.example.box_client.FileExplorer;
import com.example.box_client.MainActivity;
import com.example.box_client.OnClickBackward;
import com.example.box_client.OnClickForward;
import com.example.box_client.R;

public class FileDropboxExplorer extends Activity {

	/*
	 * Properties for main Activity
	 */
	private ListView list;
	private ArrayList<Entry> listExplorer;
	private ArrayList<String> listTitles;
	private ArrayList<String> listDescriptions;
	ArrayAdapter<String> listAdapter;
	private FileDropbox instance;
	private Button bttSelect;
	private Button bttCancel;
	private int idCommand;
	private String pathFile; // Path to Upload file from sd_card
	private ProgressDialog mProgressDialog;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_dropboxdir);
		instance = FileDropbox.getInstance();

		// Get Old Path from FileExplorer Activity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String path = extras.getString("OLD_PATH"); // Get the from_path to move item
			instance.setFromPath(path);
			String fileName = extras.getString("FILE_NAME"); // Get the file name to move item
			instance.setFileName(fileName);
			idCommand = extras.getInt("ID");
			pathFile = extras.getString("FILE_PATH"); // Path to Upload file from sd_card
		}

		// Initialization
		listExplorer = instance.getListExplorer();
		listTitles = instance.getListTitles();
		listDescriptions = instance.getListDescriptions();

		// Update Adapter
		list = (ListView) findViewById(R.id.listView1);
		FileDropboxAdapter adapter = new FileDropboxAdapter(this, listTitles,
				listDescriptions, listExplorer);
		list.setAdapter(adapter);
		// Go Forward
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				// TODO Auto-generated method stub
				new OnClickForward(instance, FileDropboxExplorer.this, index).execute();
			}
		});

		// Adding Listener for buttons
		bttSelect = (Button) findViewById(R.id.bttSelectDir);
		
		/* If idDommand is 2, Moving function will be called,
		 * else it is 4, Uploading function will be called instead.
		 */
		switch (idCommand) {
		
		// Moving item
		case 2:
			bttSelect.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new OnMovingItemAsync(instance, FileDropboxExplorer.this).execute();
				}
			});
			break;
		// Uploading item
		case 4:
			showMess("Select Dropbox's Directory to Upload Your Item!");
			bttSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new OnUploadItem(FileDropboxExplorer.this, pathFile, instance).execute();
					// Dialog is defined below
				}
			});
			break;
		default:
			break;

		}

		bttCancel = (Button) findViewById(R.id.bttCancelDir);
		bttCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				instance.goForward(MainActivity.ROOT);
				finish();
				Intent fileExplorer = new Intent(FileDropboxExplorer.this, FileExplorer.class);
				FileDropboxExplorer.this.startActivity(fileExplorer);
			}
		});
	}

	// Implement Listener for Back button
	@Override
	public void onBackPressed() {
		new OnClickBackward(instance, FileDropboxExplorer.this).execute();
	}
	
	
	/******************************************************
	 * 		Dialog to process OnClickUploadItem			  *
	 ******************************************************/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Uploading...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 2:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Moving...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 9:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Processing");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	private void showMess(String str) {
		Toast toast = Toast.makeText(getApplicationContext(), str,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
