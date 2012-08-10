package com.mini_colombia.vitrinas;

import com.mini_colombia.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class VitrinasInicio extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		TextView t = new TextView(this);
		t.setText("hola familia");
		setContentView(R.layout.activity_vitrinas_inicio);
	}
}
