package com.example.file_system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.example.box_client.Explorer;
import com.example.box_client.ExplorerActivity;
import com.example.box_client.MainActivity;
import com.example.box_client.R;
import com.example.file_dropboxdir.FileDropboxActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FileSystemActivity extends Activity {

	/*
	 * Properties for main Activity
	 */
	private ListView list;
	private ArrayList<File> listExplorer;
	private ArrayList<String> listTitles;
	private ArrayList<String> listDescriptions;
	ArrayAdapter<String> listAdapter;
	private FileSystem instance;
	private Button bttSelect;
	private Button bttCancel;
	private String path = "";
	private String fileName = "";
	private int idCommand = 0;
	public static int ITEM_ID_LONGCLICKED;
	private ProgressDialog mProgressDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_filesystem_explorer);
		bttSelect = (Button) findViewById(R.id.bttSelectFiSys);
		bttCancel = (Button) findViewById(R.id.bttCancelFiSys);
		instance = FileSystem.getInstance();
		setTitle(instance.getCurPath());
		Log.e("TITLE", instance.getCurPath());

		// Get Old Path from FileExplorer Activity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			path = extras.getString("OLD_PATH");
			instance.setPath(path);
			fileName = extras.getString("FILE_NAME");
			instance.setFileName(fileName);
			idCommand = extras.getInt("ID");
		}

		// Initialization
		listExplorer = instance.getListExplorer();
		listTitles = instance.getListTitles();
		listDescriptions = instance.getListDescriptions();

		// Update Adapter
		list = (ListView) findViewById(R.id.listView1);
		FileSystemAdapter adapter = new FileSystemAdapter(this, listTitles,
				listDescriptions, listExplorer);
		list.setAdapter(adapter);

		// Adding Listener
		list.setOnItemClickListener(new OnClickForwardSytem(instance, this));

		switch (idCommand) {
		case 3:
			// OnClickDownload will be called
			bttSelect.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new OnDownloadItem(FileSystemActivity.this, path, fileName)
							.execute();
				}
			});
			break;
		case 4:
			// OnClickUpload will be called
			Log.e("ON_CLICK", "Upload is called");
			bttSelect.setVisibility(View.GONE);
			bttCancel.setWidth(730);
			list.setOnItemLongClickListener(new OnSystemLongClicked(this)); // Moving
																			// to
																			// Context
																			// menu
																			// below
			break;
		default:
			break;
		}

		bttCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				instance.goForward(MainActivity.ROOT);
				finish();
				Intent fileExplorer = new Intent(FileSystemActivity.this,
						ExplorerActivity.class);
				FileSystemActivity.this.startActivity(fileExplorer);
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
		if (!instance.getCurPath().equals(MainActivity.ROOT)) {
			if (instance.goBackward()) {
				updateFileExplorer();
			}
		} else {
			finish();
			Intent fileExplorer = new Intent(FileSystemActivity.this,
					ExplorerActivity.class);
			FileSystemActivity.this.startActivity(fileExplorer);
		}
	}

	/******************************************************
	 * Context Menu * Implement Upload Function *
	 ******************************************************/

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("Options");
		// menu.add (.., ID, .., "Name of Option")
		menu.add(0, 0, 0, "Upload");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Get ID of Option
		int id = item.getItemId();
		File file = listExplorer.get(ITEM_ID_LONGCLICKED);

		// If item is directory, return false
		if (file.isDirectory()) {
			return false;
		}

		switch (id) {
		case 0:
			FileSystem.getInstance().setPath(MainActivity.ROOT);
			this.finish();
			Intent fileDropboxExplorer = new Intent(this,
					FileDropboxActivity.class);
			fileDropboxExplorer.putExtra("FILE_PATH", file.getPath());
			fileDropboxExplorer.putExtra("ID", 4);
			this.startActivity(fileDropboxExplorer);
			break;
		default:
			break;
		}
		return true;
	}

	/******************************************************
	 * Dialog to process OnClickDownloadAsync *
	 ******************************************************/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Downloading...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
}
