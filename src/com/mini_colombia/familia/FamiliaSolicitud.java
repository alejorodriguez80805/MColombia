package com.mini_colombia.familia;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.mini_colombia.R;

public class FamiliaSolicitud extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia_solicitud);


		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		
		TextView titutloSolicitud = (TextView)findViewById(R.id.tituloSolicitud);
		titutloSolicitud.setTypeface(tipoMini);
	}
}
