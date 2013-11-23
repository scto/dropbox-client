package com.example.editing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.box_client.ExplorerActivity;
import com.example.box_client.R;

public class FileEditing extends Activity {

	private EditText txtEdit;
	private Button bttSave;
	private Button bttCancel;
	private String path;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_editor);

		// Get Old Path from FileExplorer Activity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			path = extras.getString("OLD_PATH"); // Get the from_path to
														// download file
		}

		txtEdit = (EditText) findViewById(R.id.txt_edit_file);
		new OnPreparingEditting(this, path, txtEdit).execute();

		bttSave = (Button) findViewById(R.id.btn_ted_save);
		bttCancel = (Button) findViewById(R.id.btn_ted_cancel);
		
		bttSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new OnClickSaving(FileEditing.this, path, txtEdit).execute();
			}
		});
		
		bttCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent(FileEditing.this, ExplorerActivity.class);
				FileEditing.this.finish();
				FileEditing.this.startActivity(returnIntent);
			}
		});
	}
	
	/******************************************************
	 * Dialog to process OnClickUploadItem *
	 ******************************************************/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		case 1:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Saving...");
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
