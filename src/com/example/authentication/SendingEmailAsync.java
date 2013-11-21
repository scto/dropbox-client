package com.example.authentication;

import com.example.mail_sender.GmailSender;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class SendingEmailAsync extends AsyncTask<String, Void, Void> {

	private GmailSender sender;
	private String content;
	private String email;
	private String input;
	private Activity activity;

	public SendingEmailAsync(GmailSender sender, String content, String email,
			String input, Activity activity) {
		this.sender = sender;
		this.content = content;
		this.email = email;
		this.input = input;
		this.activity = activity;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		activity.showDialog(0);
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			sender.sendMail("Email Verification", content, email, input);
		} catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
		}
		return null;
	}

	protected void onPostExecute(Void unused) {
		Intent intent = new Intent(activity, VerifyAuthentication.class);
		intent.putExtra("CODE_GENERATE", content);
		activity.startActivity(intent);
		activity.dismissDialog(0);
		activity.removeDialog(0);
	}

}
