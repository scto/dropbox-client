package com.example.file_system;

import java.io.File;
import java.util.ArrayList;

import com.dropbox.client2.DropboxAPI.Entry;
import com.example.box_client.R;
import com.example.model.Basename;
import com.example.model.FileImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileSystemAdapter extends ArrayAdapter<String>{
	Context context;
	ArrayList<File> listExplorer;
	ArrayList<String> listTitles;
	ArrayList<String> listSizes;

	public FileSystemAdapter(Context c, ArrayList<String> listTitles,
			ArrayList<String> listSizes, ArrayList<File> listExplorer) {
		super(c, R.layout.single_row, R.id.txtTittle, listTitles);
		this.listTitles = listTitles;
		this.listSizes = listSizes;
		this.listExplorer = listExplorer;
		this.context = c;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.single_row, parent, false);

		ImageView img = (ImageView) row.findViewById(R.id.imageView1);
		TextView txtTitle = (TextView) row.findViewById(R.id.txtTittle);
		TextView txtDescription = (TextView) row
				.findViewById(R.id.txtDescription);

		txtTitle.setText(listTitles.get(position));
		txtDescription.setText(listSizes.get(position));

		File e = listExplorer.get(position);
		if (e.isDirectory()) {
			img.setImageResource(R.drawable.folder);
		} else {
			Basename basename = new Basename(e.getPath(), '/', '.');
			FileImage fileImage = FileImage.getInstance();
			img.setImageResource(fileImage.getImage(basename.extension()));
		}
		return row;
	}
}
