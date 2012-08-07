package com.mini_colombia.descargas;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.mini_colombia.R;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.DescargarImagenOnline;
import com.mini_colombia.servicios.Resize;

public class DescargasWallpapers extends Activity implements OnClickListener
{

	private static final int NUM_WALLPAPERS=5;
	
	private static final String IMAGEN="imagen";

	private ArrayList<Bitmap> thumbnails;
	private ArrayList<Bitmap> imagenes;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		thumbnails = new ArrayList<Bitmap>();
		imagenes = new ArrayList<Bitmap>();
		setContentView(R.layout.activity_descargas_wallpapers);
		new DescargarThumbnails().execute("");


	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}


	private class DescargarThumbnails extends AsyncTask<String, Integer, Boolean>
	{

		ProgressDialog progress;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Cargando...",false);
		}

		@Override
		protected Boolean doInBackground(String... params) 
		{
			Parser jparser = new Parser();
			JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_DESCARGAS_WALLPAPERS)+NUM_WALLPAPERS+"/");
			try 
			{
				JSONArray wallpapers = jsonObject.getJSONArray(getString(R.string.TAG_WALLPAPERS));

				for(int i = 0;i<wallpapers.length();i++)
				{
					JSONObject wallpaper = wallpapers.getJSONObject(i);

					String nombre = wallpaper.getString(getString(R.string.TAG_WALLPAPERS_NOMBRE));

					//Manejo thumbnail
					String urlThumbnail = wallpaper.getString(getString(R.string.TAG_WALLPAPERS_THUMBNAIL));
					Bitmap thumbnailPreliminar = DescargarImagenOnline.descargarImagen(urlThumbnail);
					Bitmap thumbnailPostResize= Resize.resizeBitmap(thumbnailPreliminar, 292,440);
					thumbnails.add(thumbnailPostResize);

					//Manejo imagen
					String urlImagen = wallpaper.getString(getString(R.string.TAG_WALLPAPERS_IMAGEN));
					imagenes.add(DescargarImagenOnline.descargarImagen(urlImagen));
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss();
			pintarPantalla();
		}

	}

	private void pintarPantalla()
	{
		LinearLayout layoutPrincipal = (LinearLayout) findViewById(R.id.linearLayoutWallpapers);

		int j=0;
		for(int i=0;i<thumbnails.size();i++)
		{
			RelativeLayout relLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

			ImageButton ib = new ImageButton(this);
			ib.setImageBitmap(thumbnails.get(i));
			ib.setBackgroundColor(Color.TRANSPARENT);
			relLayout.addView(ib);
			ib.setId(j);
			final Bitmap thumbnail = thumbnails.get(i);
			final Bitmap imagen = imagenes.get(i);
			ib.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i = new Intent(DescargasWallpapers.this, DescargasImagen.class);
					i.putExtra(IMAGEN, imagen);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					View v1 = DescargasInicio.grupoDescargas.getLocalActivityManager().startActivity("", i).getDecorView();
					DescargasInicio actividadPadre = (DescargasInicio) getParent();
					actividadPadre.reemplazarView(v1);
				}
			});

			Button b = new Button(this);
			b.setBackgroundColor(Color.TRANSPARENT);
			b.setId(j+1);
			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					MediaStore.Images.Media.insertImage(getContentResolver(), imagen, "Mini","");
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
					alertBuilder.setMessage("La imagen ha sido descargada a la galeria del telefono");
					alertBuilder.setCancelable(false);
					alertBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() 
					{

						@Override
						public void onClick(DialogInterface dialog, int which) 
						{

						}
					});
					AlertDialog alerta = alertBuilder.create();
					alerta.show();
					
				}
			});
			RelativeLayout.LayoutParams paramsbutton = new RelativeLayout.LayoutParams(115,50);
			paramsbutton.setMargins(322, 245, 0, 0);
			b.setLayoutParams(paramsbutton);
			relLayout.addView(b);

			layoutPrincipal.addView(relLayout, relParams);
			j+=2;
		}
	}

	public Context darContexto()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}

	public void onClick(View v)
	{
		int id = v.getId();
		if(id%2==0)
		{
			Intent i = new Intent(DescargasWallpapers.this, DescargarImagenOnline.class);
			//HAcer algo con las imagenes
			if(id==0)
				i.putExtra("imagen", imagenes.get(0));
			else
				i.putExtra("imagen", imagenes.get(id-1));
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View v1= DescargasInicio.grupoDescargas.getLocalActivityManager().startActivity("", i).getDecorView();
			DescargasInicio actividadPadre = (DescargasInicio) getParent();
			actividadPadre.reemplazarView(v1);
		}
		else
		{


			Bitmap b;
			if(id==1)
				b = imagenes.get(id-1);
			else
				b = imagenes.get(id-2);
			MediaStore.Images.Media.insertImage(getContentResolver(), b, "Mini","");
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
			alertBuilder.setMessage("La imagen ha sido descargada a la galeria del telefono");
			alertBuilder.setCancelable(false);
			alertBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() 
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{

				}
			});
			AlertDialog alerta = alertBuilder.create();
			alerta.show();
		}
	}

}
