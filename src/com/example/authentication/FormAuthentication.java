package com.example.authentication;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.box_client.R;

public class FormAuthentication extends Activity {

	private TextView txtEmail;
	private EditText ediTxtEmail;
	private Button bttSendCode;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_form);

		// Init Components
		txtEmail = (TextView) findViewById(R.id.txt_email_code);
		ediTxtEmail = (EditText) findViewById(R.id.edit_email_code);
		bttSendCode = (Button) findViewById(R.id.verify_button);

		// Collection email address
		txtEmail.setText("Enter your email: ");
		bttSendCode.setText("Send Code");

		// Generating code
		String content = generateRandomString();

		// Processing ...
		OnClickProcessCode onClick = new OnClickProcessCode(this, null,
				content, 0);
		onClick.setTxtTextTo(ediTxtEmail);
		bttSendCode.setOnClickListener(onClick);
	}

	// Generating Random string of 7 characters
	private String generateRandomString() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCEFGHIJKLMNOPQRSTUVWXYZ"
				.toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 7; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Sending Email...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
}
