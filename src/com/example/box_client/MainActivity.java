package com.example.box_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Account;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.example.file_dropboxdir.FileDropboxExplorer;
import com.example.file_system.FileSystemExplorer;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {

	final static private String APP_KEY = "viium65hjspul3v";
	final static private String APP_SECRET = "t3lq5vf1myeuqpy";

	final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;

	final static private String ACCOUNT_PREFS_NAME = "prefs";
	final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";

	final static public String ROOT = "/";
	
	DropboxAPI<AndroidAuthSession> mDBApi;

	private boolean isLogin;
	private Button btt_explorer;
	private Button btt_authentication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidAuthSession session = buildSession();
		mDBApi = new DropboxAPI<AndroidAuthSession>(session);

		setContentView(R.layout.activity_main);
		
		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		btt_authentication = (Button) findViewById(R.id.btt_link);
		btt_explorer = (Button) findViewById(R.id.btt_explorer);
		btt_explorer.setVisibility(View.GONE);
		btt_authentication.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isLogin) {
					mDBApi.getSession().unlink();
					clearKeys();
					setLoggedIn(false);
				} else {
					mDBApi.getSession().startAuthentication(MainActivity.this);
				}
			}
		});
		
		btt_explorer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Explorer explo = new Explorer(MainActivity.this.getApplicationContext(), mDBApi, ROOT, MainActivity.this);
				Explorer.setInstance(explo);
				explo.execute();
				
				/*
				Intent fileExplorer = new Intent(MainActivity.this, FileDropboxExplorer.class);
				MainActivity.this.startActivity(fileExplorer);*/
			}
		});
		
		setLoggedIn(mDBApi.getSession().isLinked());
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mDBApi.getSession().authenticationSuccessful()) {
			try {
				mDBApi.getSession().finishAuthentication();
				TokenPair tokens = mDBApi.getSession().getAccessTokenPair();
				storeKeys(tokens.key, tokens.secret);
				setLoggedIn(true);

			} catch (IllegalStateException e) {
				Log.i("DbAuthLog", "Error authenticating", e);
			}
		}
	}

	private void setLoggedIn(boolean loggedIn) {
		isLogin = loggedIn;
		if (loggedIn) {
			btt_authentication.setText("Unlink from Dropbox");
			btt_explorer.setVisibility(View.VISIBLE);
			try {
				Account c = mDBApi.accountInfo();
				Log.e("NAME", c.displayName);
			} catch (DropboxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			btt_authentication.setText("Authentication");
			btt_explorer.setVisibility(View.GONE);
		}
	}

	private void storeKeys(String key, String secret) {
		// Save the access key for later
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.putString(ACCESS_KEY_NAME, key);
		edit.putString(ACCESS_SECRET_NAME, secret);
		edit.commit();
	}

	private void clearKeys() {
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	private String[] getKeys() {

		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(ACCESS_KEY_NAME, null);
		String secret = prefs.getString(ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		} else {
			return null;
		}
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session;

		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE,
					accessToken);
		} else {
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
		}

		return session;
	}
}
