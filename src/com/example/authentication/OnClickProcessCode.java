package com.example.authentication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.box_client.MainActivity;
import com.example.mail_sender.GmailSender;

public class OnClickProcessCode implements OnClickListener {

	private Activity activity;
	private String input;
	private String content;
	private int idCommand;
	
	private EditText txtTextTo;
	private String emailCli = "dropbox.client1234@gmail.com";
	private String passCli = "Dropbox_pass1234";

	public OnClickProcessCode(Activity activity, String email, String content,
			int id) {
		this.activity = activity;
		this.input = email;
		this.content = content;
		this.idCommand = id;
	}
	
	public EditText getTxtTextTo() {
		return txtTextTo;
	}

	public void setTxtTextTo(EditText txtTextTo) {
		this.txtTextTo = txtTextTo;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		input = txtTextTo.getText().toString();
		switch (idCommand) {
		case 0:
			GmailSender sender = new GmailSender(emailCli, passCli);
			new SendingEmailAsync(sender, content, emailCli, input, activity).execute();
			break;
		case 1:
			if (input.equals(content)) {
				activity.finish();
				Intent authen = new Intent(activity, MainActivity.class);
				authen.putExtra("IS_VERIFY", true);
				activity.startActivity(authen);
			} else {
				showMess("Code is invalid!");
			}
			break;
		default:
			break;
		}
	}

	private void showMess(String str) {
		Toast toast = Toast.makeText(activity.getApplicationContext(), str,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
