package com.example.box_client;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI.Entry;
import com.example.file_dropboxdir.FileDropboxExplorer;
import com.example.file_system.FileSystemExplorer;

@SuppressLint("ShowToast")
public class FileExplorer extends Activity {

	/*	
	 * Properties for main Activity
	 */
	private ListView list;
	private ArrayList<Entry> listExplorer;
	private ArrayList<String> listTitles;
	private ArrayList<String> listSizes;
	ArrayAdapter<String> listAdapter;
	private Explorer explorer;
	public static int ITEM_LONGCLICKED;
	
	/*
	 * Dialog Custom Properties
	 */
	private Dialog custom;
	private Button bttSave;
	private Button bttCancel;
	private TextView txtName;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_explorer);

		// Initialization
		init();

		// Update Adapter
		list = (ListView) findViewById(R.id.listView1);
		FileExplorerAdapter adapter = new FileExplorerAdapter(this, listTitles,
				listSizes, listExplorer);
		list.setAdapter(adapter);

		// Go Foward
		list.setOnItemClickListener(new OnClickItemForwardExplorer(explorer,
				listExplorer, this));

		list.setOnItemLongClickListener(new OnItemLongClickOptions(this));
	}

	// Init Array List
	private void init() {
		this.explorer = Explorer.getInstance();
		this.listExplorer = explorer.getListExplorer();
		listTitles = new ArrayList<String>();
		insertTitle();
		listSizes = new ArrayList<String>();
		insertSize();
	}

	private void insertTitle() {
		for (int i = 0; i < listExplorer.size(); i++) {
			Entry e = listExplorer.get(i);
			listTitles.add(e.fileName());
		}
	}

	private void insertSize() {
		for (int i = 0; i < listExplorer.size(); i++) {
			Entry e = listExplorer.get(i);
			if (e.isDir) {
				listSizes.add("Folder");
			} else {
				listSizes.add("File: " + e.size);
			}
		}
	}

	// Reload Activity
	private void updateFileExplorer () {
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	
	// Implement Listener for Back button
	@Override
	public void onBackPressed() {
		explorer.goBackward();
		updateFileExplorer();
	}

	
	/******************************************************
	 * 					Context Menu 				  	  * 
	 ******************************************************/
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("Options");
		// menu.add (.., ID, .., "Name of Option")
		menu.add(0, 0, 0, "Remove");
		menu.add(0, 1, 0, "Rename");
		menu.add(0, 2, 0, "Move");
		menu.add(0, 3, 0, "Download");
		menu.add(0, 4, 0, "Upload");
		menu.add(0, 5, 0, "Exit");
	}

	// Rename Directory Listener
	private void renameListener (String fileName, String dir) {
		final String directory = dir;
		// Init
		custom = new Dialog(FileExplorer.this);
		custom.setContentView(R.layout.dialog_message);
		custom.setTitle(fileName);
		bttSave = (Button) custom.findViewById(R.id.bttSave);
		bttCancel = (Button) custom.findViewById(R.id.bttCan);
		txtName = (TextView) custom.findViewById(R.id.txtNewName);
		
		// Define listeners for save and cancel button
		bttSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = txtName.getText().toString();
				String s = null;
				if (name.equals("")) {
					s = "Invalid Input!"; // Validate blank input
				} else {
					explorer.renameDirectory(directory, name);
					s = "Rename Successfully!";		                
					updateFileExplorer();
				}
				Toast.makeText(FileExplorer.this, s, 3000).show(); // Note user
				custom.dismiss();
			}
		});
		bttCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				custom.dismiss();
			}
		});
		
		// Run Custom
		custom.show();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Get ID of Option
		int id = item.getItemId();
		String directory = listExplorer.get(ITEM_LONGCLICKED).path;
		String fileName = listExplorer.get(ITEM_LONGCLICKED).fileName();
		switch (id) {
		case 0:
			explorer.delPath(directory);
			updateFileExplorer();
			break;
		case 1:
			// Rename
			renameListener(fileName, directory);
			break;
		case 2:
			// Moving Directory
			Intent fileDropboxExplorer = new Intent(FileExplorer.this, FileDropboxExplorer.class);
			fileDropboxExplorer.putExtra("OLD_PATH", directory);
			fileDropboxExplorer.putExtra("FILE_NAME", fileName);
			fileDropboxExplorer.putExtra("ID", 2);
			FileExplorer.this.startActivity(fileDropboxExplorer);
			break;
		case 3:
			// Download
			Intent fileSystemExplorer = new Intent(FileExplorer.this, FileSystemExplorer.class);
			fileSystemExplorer.putExtra("OLD_PATH", directory);
			fileSystemExplorer.putExtra("FILE_NAME", fileName);
			fileSystemExplorer.putExtra("ID", 3);
			FileExplorer.this.startActivity(fileSystemExplorer);
			break;
		case 4:
			// Upload
			Intent fileSysExplorer = new Intent(FileExplorer.this, FileSystemExplorer.class);
			fileSysExplorer.putExtra("ID", 4);
			FileExplorer.this.startActivity(fileSysExplorer);
			break;
		case 5:
			// Exit
			this.finish();
			break;
		default:
			break;

		}
		return true;
	}
}