package com.example.grid_view;

import java.util.ArrayList;

import com.dropbox.client2.DropboxAPI.Entry;
import com.example.box_client.OnGetThumnails;
import com.example.box_client.R;
import com.example.model.Basename;
import com.example.model.FileImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridviewAdapter extends BaseAdapter {
	private Context context;

	private final ArrayList<Entry> listExplorer;

	public GridviewAdapter(Context context, ArrayList<Entry> listExplorer) {
		this.context = context;
		this.listExplorer = listExplorer;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		Log.e("TITLE:", listExplorer.get(position).fileName());

		Entry e = listExplorer.get(position);
		boolean thumbExist = e.thumbExists;
		
		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.grid_view_item, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(listExplorer.get(position).fileName());

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			if (e.isDir) {
				imageView.setImageResource(R.drawable.folder);
			} else {
				if (thumbExist) {
					String cachePath = context.getCacheDir().getAbsolutePath() + "/"
							+ e.fileName();
					new OnGetThumnails(thumbExist, cachePath, imageView, e).execute();
				} else {
					Log.e("PATH", e.path);
					Log.e("FILE NAME", e.fileName());
					Basename basename = new Basename(e.path, '/', '.');
					FileImage fileImage = FileImage.getInstance();
					imageView.setImageResource(fileImage.getImage(basename.extension()));
				}
			}

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return listExplorer.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
