package com.mini_colombia.servicios;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mini_colombia.auxiliares.ImagenGaleria;
import com.mini_colombia.comunidad.ComunidadImagen;
import com.mini_colombia.comunidad.ComunidadInicio;

public class ImageAdapter extends BaseAdapter
{

	private ArrayList<ImagenGaleria> arregloImagenes;
	private Context mContext;
	private Context c;
	private Activity padre;
	ProgressBar progress;
	

	public ImageAdapter( Context c, ArrayList<ImagenGaleria> arregloImagenes, Activity padre)
	{
		mContext = c;
		this.arregloImagenes = arregloImagenes;
		this.padre = padre;

	}

	@Override
	public int getCount() {
		return arregloImagenes.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{


		ImageView imageView;
		if (convertView == null) 
		{  // if it's not recycled, initialize some attributes

			TextView t = new TextView(mContext);
			t.setText("prueba");
			t.setVisibility(View.VISIBLE);


			imageView = new ImageView(mContext);
			c=imageView.getContext();
			imageView.setLayoutParams(new GridView.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 20, 8, 8);

		} 
		else 
		{
			imageView = (ImageView) convertView;
		}


		imageView.setImageBitmap(arregloImagenes.get(position).getThumbnail());
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(mContext, ComunidadImagen.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("size", arregloImagenes.size());
				i.putExtra("posActual", position +1);
				i.putExtra("titulo", arregloImagenes.get(position).getNombre());
				i.putExtra("url", arregloImagenes.get(position).getImagen());
				View v1 = ComunidadInicio.grupoComunidad.getLocalActivityManager().startActivity("", i).getDecorView();
				ComunidadInicio actividadPadre = (ComunidadInicio) padre;
				actividadPadre.reemplazarView(v1);

			}
		});


		return imageView;
	}

	private class DescargarImagen extends AsyncTask<String, Void, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(String... params) 
		{
			String url = params[0];
			return DescargarImagenOnline.descargarImagen(url);
		}



	}

	public Context darContexto()
	{
		return mContext.getApplicationContext();
	}

	public ProgressBar darC()
	{
		return progress;
	}

}