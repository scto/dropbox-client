package com.example.model;

import java.util.Arrays;

import com.example.box_client.R;

public class FileImage {
	private int [] fileImage = {R.drawable.docx, R.drawable.music, R.drawable.pdf, 
			R.drawable.text, R.drawable.code,  R.drawable.zipicon, R.drawable.raricon};
	private String ext;
	private static FileImage instance;
	
	public static FileImage getInstance () {
		if (instance == null) {
			instance = new FileImage();
		}
		return instance;
	}

	public int[] getFileImage() {
		return fileImage;
	}

	public void setFileImage(int[] fileImage) {
		this.fileImage = fileImage;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public int getImage (String ext) {
		String [] musicFile = {"mp3", "wma", "flac", "midi", "m4a"};
		String [] textFile = {"txt", "rtf", "sql", "xml"};
		String [] codeFile = {"css", "html", "c", "cpp", "hpp", "h", "java"};
		String [] zipFile = {"zip", "apk", "tar"};
		if (ext.equals("docx") || ext.equals("doc")) {
			return fileImage[0];
		} else if (Arrays.asList(musicFile).contains(ext)) {
			return fileImage[1];
		} else if (ext.equals("pdf")) {
			return fileImage[2];
		} else if (Arrays.asList(textFile).contains(ext)) {
			return fileImage[3];
		} else if (Arrays.asList(codeFile).contains(ext)) {
			return fileImage[4];
		} else if (Arrays.asList(zipFile).contains(ext)) {
			return fileImage[5];
		} else if (ext.equals("rar")) {
			return fileImage[6];
		}
		return R.drawable.file;
	}
}
