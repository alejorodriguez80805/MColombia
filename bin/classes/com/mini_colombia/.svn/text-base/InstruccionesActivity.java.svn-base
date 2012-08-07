package com.mini_colombia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;


public class InstruccionesActivity extends Activity
{
	GridView grid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		
		
		setContentView(R.layout.activity_instrucciones);
		
		
		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		Typeface tipoHelvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");
		
		
		//Agregar fuentes y textos a TextViews
		TextView textoTitulo = (TextView) findViewById(R.id.TextoTituloInstrucciones);
		textoTitulo.setTypeface(tipoMini);
		textoTitulo.setText(R.string.Instrucciones);
		
		TextView textoInstrucciones1 = (TextView) findViewById(R.id.TextoInstrucciones1);
		textoInstrucciones1.setTypeface(tipoHelvetica);
		textoInstrucciones1.setText(R.string.Instruccion1);
		
		TextView textoInstrucciones2 = (TextView) findViewById(R.id.TextoInstrucciones2);
		textoInstrucciones2.setTypeface(tipoHelvetica);
		textoInstrucciones2.setText(R.string.Instruccion1);
		
		
		

	}
	

}
