package com.example.box_client;

import java.util.ArrayList;

import com.dropbox.client2.DropboxAPI.Entry;

public interface Finder {
	public ArrayList<Entry> getListExplorer();
	public void goForward(String path);
	public void goBackward();
	public void delPath(String directory);
	public void renameDirectory(String fromPath, String newName);
}
