package com.example.grid_view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dropbox.client2.DropboxAPI.Entry;
import com.example.box_client.Explorer;
import com.example.box_client.ExplorerActivity;
import com.example.box_client.MainActivity;
import com.example.box_client.OnClickBackward;
import com.example.box_client.OnClickForward;
import com.example.box_client.R;
 
public class GridviewActivity extends Activity {
 
	GridView gridView;
	
	private ArrayList<Entry> listExplorer;
	private Explorer explorer;
	private ProgressDialog mProgressDialog;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);
		init();
		setTitle(explorer.getPath());
 
		gridView = (GridView) findViewById(R.id.gridView1);
 
		gridView.setAdapter(new GridviewAdapter(this, listExplorer));
 
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				new OnClickForward(explorer, GridviewActivity.this, position)
				.execute();
			}
		});
 
	}
	
	// Init Array List
	private void init() {
		this.explorer = Explorer.getInstance();
		this.listExplorer = explorer.getListExplorer();
	}
 
	// Implement Listener for Back button
	@Override
	public void onBackPressed() {
		if (!Explorer.getInstance().getPath().equals(MainActivity.ROOT)) {
			new OnClickBackward(explorer, GridviewActivity.this).execute();
		} else {
			this.finish();
		}
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.grid, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.btt_list:
	    		finish();
	    		Intent fileExplorer = new Intent(this, ExplorerActivity.class);
	    		startActivity(fileExplorer);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}

