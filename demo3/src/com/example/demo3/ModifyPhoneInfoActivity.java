package com.example.demo3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModifyPhoneInfoActivity extends Activity {
	
	CommMethod	commMethod =null;
	public EditText m_eModelNumber;
	Button m_bSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_phone_info);
		commMethod = new CommMethod(ModifyPhoneInfoActivity.this);
		m_eModelNumber = (EditText)findViewById(R.id.eModelNumber);
		
		m_bSubmit = (Button)findViewById(R.id.bSubmit);
		m_bSubmit.setOnClickListener(new SubmitButtonlistener());
	}
	
	class SubmitButtonlistener implements OnClickListener{
		
		public void onClick(View v){
			
			if( m_eModelNumber.getText().toString().isEmpty() == false)
        	{
        		commMethod.setOfficialName(m_eModelNumber.getText().toString());
        	}
			
		}
		
	}

}
