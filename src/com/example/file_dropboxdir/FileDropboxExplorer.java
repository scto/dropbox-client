package com.example.file_dropboxdir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.example.file_dropboxdir.OnClickItemForwardExplorer;
import com.example.box_client.Explorer;
import com.example.box_client.FileExplorer;
import com.example.box_client.MainActivity;
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
	private String pathFile;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_dropboxdir);
		instance = FileDropbox.getInstance();

		// Get Old Path from FileExplorer Activity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String path = extras.getString("OLD_PATH");
			instance.setFromPath(path);
			String fileName = extras.getString("FILE_NAME");
			instance.setFileName(fileName);
			idCommand = extras.getInt("ID");
			pathFile = extras.getString("FILE_PATH");
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
		list.setOnItemClickListener(new OnClickItemForwardExplorer(instance,
				FileDropboxExplorer.this));

		// Adding Listener for buttons
		bttSelect = (Button) findViewById(R.id.bttSelectDir);
		switch (idCommand) {
		
		// Moving item
		case 2:
			bttSelect.setOnClickListener(new OnSelectPath(instance, this));
			break;
		// Uploading item
		case 4:
			bttSelect.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					File file = new File(pathFile);
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
						showMess("Uploading successfully!");
						FileDropboxExplorer.this.finish();
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
				FileDropboxExplorer.this.finish();
			}
		});
	}

	// Reload Activity
	private void updateFileExplorer() {
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	// Implement Listener for Back button
	@Override
	public void onBackPressed() {
		instance.goBackward();
		updateFileExplorer();
	}

	private void showMess(String str) {
		Toast toast = Toast.makeText(getApplicationContext(), str,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
