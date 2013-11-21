package com.example.authentication;

import com.example.box_client.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerifyAuthentication extends Activity {
	
	private TextView txtCode;
	private EditText ediTxtCode;
	private Button bttVerifyCode;
	private String content;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_form);
		
		// Init Components
		txtCode = (TextView) findViewById(R.id.txt_email_code);
		ediTxtCode = (EditText) findViewById(R.id.edit_email_code);
		bttVerifyCode = (Button) findViewById(R.id.verify_button);
		
		// Starting input code
		txtCode.setText("Enter your Code: ");
		bttVerifyCode.setText("Verify Code");
		
		// Getting code generator
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			content = extras.getString("CODE_GENERATE");
			Log.e("CODE_GENE", content);
		}
		
		// Processing...
		OnClickProcessCode onClick = new OnClickProcessCode(this, null, content, 1);
		onClick.setTxtTextTo(ediTxtCode);
		bttVerifyCode.setOnClickListener(onClick);
	}

}
