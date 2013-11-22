package com.example.box_client;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private ProgressDialog mProgressDialog;

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
		setTitle(explorer.getPath());
		// Update Adapter
		list = (ListView) findViewById(R.id.listView1);
		FileExplorerAdapter adapter = new FileExplorerAdapter(this, listTitles,
				listSizes, listExplorer);
		list.setAdapter(adapter);

		// Go Forward
		// list.setOnItemClickListener(new
		// OnClickItemForwardExplorer(explorer,listExplorer, this));
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if (listExplorer.get(index).isDir) {
					new OnClickForward(explorer, FileExplorer.this, index)
							.execute();
				}
			}
		});

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

	// Implement Listener for Back button
	@Override
	public void onBackPressed() {
		new OnClickBackward(explorer, FileExplorer.this).execute();
	}

	/******************************************************
	 * Context Menu *
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
		menu.add(0, 5, 0, "Modify");
		menu.add(0, 6, 0, "Exit");
	}

	// Rename Directory Listener
	private void renameListener(String fileName, String dir) {
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
					new ContextItemSelector(explorer, FileExplorer.this, 1,
							name, directory).execute();
					s = "Rename Successfully!";
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
			// Delete
			new ContextItemSelector(explorer, FileExplorer.this, id, fileName,
					directory).execute();
			break;
		case 1:
			// Rename
			renameListener(fileName, directory);
			break;
		case 2:
			// Moving Directory
			new ContextItemSelector(explorer, FileExplorer.this, id, fileName,
					directory).execute();
			break;
		case 3:
			// Download
			showMess("Select Folder in SD_CARD to Save Your Item!");
			new ContextItemSelector(explorer, FileExplorer.this, id, fileName,
					directory).execute();
			break;
		case 4:
			// Upload
			showMess("Select specific File to Upload!");
			new ContextItemSelector(explorer, FileExplorer.this, id, fileName,
					directory).execute();
			break;
		case 5:
			// Modifying
			Entry e = listExplorer.get(ITEM_LONGCLICKED);
			if (!e.isDir && e.mimeType.equals("text/plain")) {
				new ContextItemSelector(explorer, FileExplorer.this, id,
						fileName, directory).execute();
			} else {
				showMess("Text/Plain only!");
			}
			break;
		case 6:
			// Exit
			finish();
			Intent authen = new Intent(this, MainActivity.class);
			authen.putExtra("IS_VERIFY", true);
			this.startActivity(authen);
			break;
		default:
			break;

		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Path is deleting!");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 1:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Item is renaming!");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 2:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Select a new Dropbox's location!");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 3:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Go to Filesytem to Save Item!");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 4:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Go to Filesytem to Upload New Item!");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 5:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Preapring to modify...");
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
		Toast toast = Toast.makeText(getApplicationContext(), str, 5000);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}