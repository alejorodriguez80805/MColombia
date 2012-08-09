package com.mini_colombia.descargas;


import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mini_colombia.R;

public class DescargasInicio  extends ActivityGroup
{
	public static DescargasInicio grupoDescargas;
	
	public ArrayList<View> historialViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descargas_inicio);
		this.grupoDescargas = this;
		
		historialViews = new ArrayList<View>();
		
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView)findViewById(R.id.tituloDescargasInicio);
		titulo.setTypeface(tipoMini);
	}
	
	
	public void inicioWallpapers(View v)
	{
		Intent i = new Intent(DescargasInicio.this, DescargasWallpapers.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
	
	
	public void inicioRingtones(View v)
	{
		Intent i = new Intent(DescargasInicio.this, DescargasRingtones.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
	
	
	public void reemplazarView(View v)
	{
		historialViews.add(v);
		setContentView(v);
	}
	
	public void back()
	{
		if(historialViews.size()>0)
		{
			
			historialViews.remove(historialViews.size() -1);
			if(historialViews.size()>0)
			{
				setContentView(historialViews.get(historialViews.size()-1));
			}
			else
				onCreate(null);
		}

		else
		{

			finish();
		}

	}
	
	@Override
	public void onBackPressed() 
	{
		DescargasInicio.grupoDescargas.back();
		return;
	}
}
