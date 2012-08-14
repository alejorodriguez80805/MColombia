package com.mini_colombia.familia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.servicios.MailClient;

public class FamiliaTestDrive extends Activity
{
	private static final String EDICION = "edicion";

	private String nombreCarro;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia_test_drive);
		nombreCarro=getIntent().getStringExtra(EDICION);

		EditText textoCarro= (EditText)findViewById(R.id.testDrive_carro);
		textoCarro.setText(nombreCarro);


		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");

		TextView tituloTest = (TextView) findViewById(R.id.tituloTestDrive);
		tituloTest.setTypeface(tipoMini);

		TextView  textoEnviar = (TextView) findViewById(R.id.textoEnviarTestDrive);
		textoEnviar.setTypeface(tipoMini);
	}

	public void enviarTestDrive(View v)
	{
		EditText textoNombre= (EditText)findViewById(R.id.testDrive_nombre);
		EditText textoApellido= (EditText)findViewById(R.id.testDrive_apellido);
		EditText textoMail= (EditText)findViewById(R.id.testDrive_email);
		EditText textoTelefono= (EditText)findViewById(R.id.testDrive_telefono);
		EditText textoCiudad= (EditText)findViewById(R.id.testDrive_ciudad);
		EditText textoCarro= (EditText)findViewById(R.id.testDrive_carro);

		MailClient sender = new MailClient("alejorodriguez80805","alejo1234");
		try 
		{
			sender.sendMail("Test Drive " , "afadfasdfasdfasdfadf", "alejorodriguez80805@gmail.com", "alejo80805@hotmail.com");
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent i = new Intent(FamiliaTestDrive.this, FamiliaSolicitud.class);
		View v1 = FamiliaInicio.grupoFamilia.getLocalActivityManager().startActivity("", i).getDecorView();
		FamiliaInicio actividadPadre =(FamiliaInicio) getParent(); 
		actividadPadre.reemplazarView(v1);
	}


	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	}

}
