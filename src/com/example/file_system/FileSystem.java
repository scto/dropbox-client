package com.example.file_system;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.box_client.FileExplorer;

import android.util.Log;
import android.widget.Toast;

public class FileSystem {

	private ArrayList<File> listExplorer;
	private ArrayList<String> listTitles;
	private ArrayList<String> listDescriptions;
	private static FileSystem instance;
	private String curPath;
	private String path;
	private String fileName;

	public static FileSystem getInstance() {
		if (instance == null) {
			instance = new FileSystem();
			instance.init();
		}
		return instance;
	}

	public void setCurPath(String path) {
		this.curPath = path;
	}

	public String getCurPath() {
		return curPath;
	}

	public ArrayList<File> getListExplorer() {
		return listExplorer;
	}

	public void setListExplorer(ArrayList<File> listExplorer) {
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private void init() {
		listExplorer = new ArrayList<File>();
		File curDir = new File("/");
		File[] dir = curDir.listFiles();
		for (int i = 0; i < dir.length; i++) {
			File tmp = dir[i];
			listExplorer.add(tmp);
		}
		insertTitles();
		insertDescription();
	}

	private void insertTitles() {
		listTitles = new ArrayList<String>();
		for (int i = 0; i < listExplorer.size(); i++) {
			listTitles.add(listExplorer.get(i).getName());
		}
	}

	private void insertDescription() {
		listDescriptions = new ArrayList<String>();
		for (int i = 0; i < listExplorer.size(); i++) {
			File tmp = listExplorer.get(i);
			if (tmp.isDirectory()) {
				listDescriptions.add("Folder");
			} else {
				String size = String.valueOf(tmp.length());
				listDescriptions.add("File: " + size + " Bytes");
			}
		}
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

	private boolean retrieveDirectory(String path) {
		File curDir = new File(path);
		File[] dir = curDir.listFiles();

		// Without if-condition, dir will return Null Pointer Exception
		if (dir != null && dir.length > 0) {
			listExplorer.clear();
			for (int i = 0; i < dir.length; i++) {
				File tmp = dir[i];
				listExplorer.add(tmp);
				insertTitles();
				insertDescription();
			}
			setCurPath(path);
			return true;
		}
		return false;
	}

	public boolean goForward(String path) {
		setCurPath(path);
		return retrieveDirectory(path);
	}

	public boolean goBackward() {
		setCurPath(splitString(curPath));
		String path = curPath;
		return retrieveDirectory(path);
	}
}
