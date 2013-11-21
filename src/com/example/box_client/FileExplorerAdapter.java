package com.example.box_client;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI.Entry;

public class FileExplorerAdapter extends ArrayAdapter<String> {

	Context context;
	ArrayList<Entry> listExplorer;
	ArrayList<String> listTitles;
	ArrayList<String> listSizes;

	public FileExplorerAdapter(Context c, ArrayList<String> listTitles,
			ArrayList<String> listSizes, ArrayList<Entry> listExplorer) {
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

		Entry e = listExplorer.get(position);
		if (e.isDir) {
			img.setImageResource(R.drawable.folder);
		} else {
			img.setImageResource(R.drawable.file);
		}
		return row;
	}
}

