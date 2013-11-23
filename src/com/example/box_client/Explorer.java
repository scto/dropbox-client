package com.example.box_client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Account;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.example.grid_view.GridviewActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class Explorer extends AsyncTask<Void, Long, Boolean>  implements Finder{

	private Context ctx;
	private DropboxAPI<AndroidAuthSession> mDBApi;
	private String path;
	private Activity mainActivity;
	private ArrayList<Entry> listExplorer;
	private static Explorer instance;

	public Explorer(Context context, DropboxAPI<AndroidAuthSession> mDBApi,
			String path, Activity mActivity) {
		this.ctx = context.getApplicationContext();
		this.mDBApi = mDBApi;
		this.path = path;
		this.mainActivity = mActivity;
		listExplorer = new ArrayList<Entry>();
	}

	
	@SuppressWarnings("deprecation")
	protected void onPreExecute() {
		super.onPreExecute();
		mainActivity.showDialog(0);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		// Get the metadata for a directory

		try {
			Entry entries = mDBApi.metadata(MainActivity.ROOT, 100, null, true, null);
			if (!entries.isDir || entries.contents == null) {
				// It's not a directory, or there's nothing in it
				String mErrorMsg = "File or empty directory";
				Log.e("Error in doInBG", mErrorMsg);
				return false;
			}
			for (Entry e : entries.contents) {
				if (!e.isDeleted) {
					listExplorer.add(e);
				}
			}

		} catch (DropboxUnlinkedException e) {
			// TODO Auto-generated catch block
			Log.d("DbAuthLog", "Unlink Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.d("DbAuthLog", "DropboxException NUll Pointer");
		} catch (DropboxException e1) {
			// TODO Auto-generated catch block
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mainActivity.dismissDialog(0);
		mainActivity.removeDialog(0);
		mainActivity.finish();
		Intent fileExplorer = new Intent(mainActivity, ExplorerActivity.class);
		mainActivity.startActivity(fileExplorer);
	}
	
	/*
	 * Explorer APIs
	 */

	/*
	 * Getter & Setter
	 */
	public static void setInstance(Explorer explorer) {
		Explorer.instance = explorer;
	}
	public static Explorer getInstance() {
		return Explorer.instance;
	}
	public ArrayList<Entry> getListExplorer() {
		return listExplorer;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	public DropboxAPI<AndroidAuthSession> getMDBApi() {
		return mDBApi;
	}

	/*
	 * Go Foward and Go Backward
	 */
	public void goForward(String path) {
		setPath(path);
		getFolderList(Explorer.getInstance().getPath());
	}
	public void goBackward() {
		path = splitString(path);
		setPath(path);
		getFolderList(path);

	}

	
	// Splitting Path String to get Parent Path
	private String splitString(String path) {
		List<String> list = new ArrayList<String>(
				Arrays.asList(path.split("/")));
		String result = "";
		for (int i = 0; i < list.size() - 1; i++) {
			if (i == (list.size() - 1)) {
				result += list.get(i);
			} else {
				result += list.get(i) + "/";
			}
		}
		return result;
	}

	/*
	 * Retrieve Folder List
	 */
	private void getFolderList(String path) {
		ArrayList<Entry> listExplorer = this.getListExplorer();
		listExplorer.clear();
		try {
			Entry entries = mDBApi.metadata(path, 100, null, true, null);
			if (!entries.isDir || entries.contents == null) {
				// It's not a directory, or there's nothing in it
				String mErrorMsg = "File or empty directory";
				Log.e("Error in doInBG", mErrorMsg);
			} else {
				for (Entry e : entries.contents) {
					if (!e.isDeleted) {
						listExplorer.add(e);
					}
				}
			}
		} catch (DropboxUnlinkedException e) {
			// TODO Auto-generated catch block
			Log.d("DbAuthLog", "Unlink Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.d("DbAuthLog", "DropboxException NUll Pointer");
		} catch (DropboxException e1) {
			// TODO Auto-generated catch block
		}
	}

	/*
	 * Delete Directories
	 */
	public void delPath(String directory) {
		try {
			this.getMDBApi().delete(directory);
			getFolderList(path); // Reload list directories
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Rename Directories
	 */
	public void renameDirectory(String fromPath, String newName) {
		try {
			String toPath = splitString(fromPath) + "/" + newName; // mkdir new directory
			this.getMDBApi().move(fromPath, toPath); // move directory
			delPath(fromPath); // remove old directory
			getFolderList(this.path); // Reload list directories
		} catch (DropboxException e) {
			e.printStackTrace();
		}
	}

}
