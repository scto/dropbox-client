package com.example.file_dropboxdir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.example.box_client.Explorer;
import com.example.box_client.MainActivity;

public class FileDropbox {
	private ArrayList<Entry> listExplorer;
	private ArrayList<String> listTitles;
	private ArrayList<String> listDescriptions;
	private static FileDropbox instance;
	private String toPath;
	private String fromPath;
	private String path = "";
	private String fileName;
	private DropboxAPI<AndroidAuthSession>mApi = Explorer.getInstance().getMDBApi();
	
	public static FileDropbox getInstance () {
		if (instance == null) {
			instance = new FileDropbox();
			instance.init();
			instance.initTitlesAndDescription();
		} 
		return instance;
	}
	
	private void init() {
		listExplorer = new ArrayList<Entry>();
		getFolderList("/");
	}
	
	private void initTitlesAndDescription () {
		listTitles = new ArrayList<String>();
		listDescriptions = new ArrayList<String>();
		for (int i = 0; i < listExplorer.size(); i++) {
			Entry e = listExplorer.get(i);
			listTitles.add(e.fileName());
			if (e.isDir) {
				listDescriptions.add("Folder");
			} else {
				listDescriptions.add("File: " + e.size);
			}
		}
	}

	public ArrayList<Entry> getListExplorer() {
		return listExplorer;
	}

	public void setListExplorer(ArrayList<Entry> listExplorer) {
		this.listExplorer = listExplorer;
	}

	public ArrayList<String> getListTitles() {
		return listTitles;
	}

	public void setListTitles(ArrayList<String> listTitles) {
		this.listTitles = listTitles;
	}

	public ArrayList<String> getListDescriptions() {
		return listDescriptions;
	}

	public void setListDescriptions(ArrayList<String> listDescriptions) {
		this.listDescriptions = listDescriptions;
	}

	public String getToPath() {
		return toPath;
	}

	public void setToPath(String toPath) {
		this.toPath = toPath;
	}

	public String getFromPath() {
		return fromPath;
	}

	public void setFromPath(String fromPath) {
		this.fromPath = fromPath;
	}
	
	public void setPath (String path) {
		this.path = path;
	}
	
	public String getPath () {
		return path;
	}
	
	
	public DropboxAPI<AndroidAuthSession> getmApi() {
		return mApi;
	}

	public void setmApi(DropboxAPI<AndroidAuthSession> mApi) {
		this.mApi = mApi;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * Go Foward and Go Backward
	 */
	public void goForward(String path) {
		setPath(path);
		getFolderList(FileDropbox.getInstance().getPath());
		setToPath(path);
	}
	
	public void goBackward() {
		path = splitString(path);
		setPath(path);
		getFolderList(path);
		setToPath(path);
	}

	/*
	 * Retrieve Folder List
	 */
	private void getFolderList(String path) {
		ArrayList<Entry> listExplorer = this.getListExplorer();
		listExplorer.clear();
		try {
			Entry entries = mApi.metadata(path, 100, null, true, null);
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
		initTitlesAndDescription();
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
	
	
	
	
	
	
	
}
