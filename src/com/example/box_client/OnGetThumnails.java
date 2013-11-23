package com.example.box_client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.DropboxAPI.ThumbFormat;
import com.dropbox.client2.DropboxAPI.ThumbSize;
import com.dropbox.client2.exception.DropboxException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class OnGetThumnails extends AsyncTask<String, Void, Void> {

	private boolean thumbExist;
	private String cachePath;
	private ImageView img;
	private Entry e;
	private Bitmap mBitmap;
	
	public OnGetThumnails (boolean thumbExist, String cachePath, ImageView img, Entry e) {
		this.thumbExist = thumbExist;
		this.cachePath = cachePath;
		this.img = img;
		this.e = e;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		FileOutputStream mFos;
		if (thumbExist) {
			try {
				mFos = new FileOutputStream(cachePath);
				Explorer.getInstance()
						.getMDBApi()
						.getThumbnail(e.path, mFos, ThumbSize.ICON_128x128,
								ThumbFormat.PNG, null);
			} catch (DropboxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.e("IS THUMNAILS", String.valueOf(thumbExist));
			mBitmap = BitmapFactory.decodeFile(cachePath);
			
		} else {
			Log.e("IS THUMNAILS", String.valueOf(thumbExist));
		}
		return null;
	}

    @Override
    protected void onPostExecute(Void unsed) {
    	Bitmap resizedbitmap=Bitmap.createScaledBitmap(mBitmap, 100, 100, true);
    	img.setImageBitmap(resizedbitmap);
    }
}
