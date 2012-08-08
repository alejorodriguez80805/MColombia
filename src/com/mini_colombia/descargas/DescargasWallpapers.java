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
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
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

public class DescargasWallpapers extends Activity 
{

	private static final int NUM_WALLPAPERS=1;
	
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
			JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_DESCARGAS_WALLPAPERS));
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

		for(int i=0;i<NUM_WALLPAPERS;i++)
		{
			RelativeLayout relLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

			ImageButton ib = new ImageButton(this);
			ib.setImageBitmap(thumbnails.get(i));
			ib.setBackgroundColor(Color.TRANSPARENT);
			ib.setPadding(0, 0, 0, 45);
//			RelativeLayout.LayoutParams paramsIb = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//			paramsIb.setMargins(0, 0, 0, 5);
//			ib.setLayoutParams(paramsIb);
			relLayout.addView(ib);
			final Bitmap imagen = imagenes.get(i);
			ib.setId(i);
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
			
			if(i == NUM_WALLPAPERS -1)
			{
				final Button verMas = new Button(this);
				verMas.setBackgroundColor(Color.BLACK);
				verMas.setText("VER MAS");
				verMas.setTextSize(12);
				verMas.setTextColor(Color.WHITE);
				verMas.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
				verMas.setPadding(5, 5, 0, 5);
				verMas.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						verMas.setVisibility(View.GONE);
						pintarWallpapersRestantes();
						
					}
				});
				RelativeLayout.LayoutParams paramsVerMas = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				paramsVerMas.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				verMas.setLayoutParams(paramsVerMas);
				relLayout.addView(verMas);
			}

			layoutPrincipal.addView(relLayout, relParams);
		}
	}
	
	public void pintarWallpapersRestantes()
	{
		LinearLayout layoutPrincipal = (LinearLayout) findViewById(R.id.linearLayoutWallpapers);

		for(int i=NUM_WALLPAPERS;i<thumbnails.size();i++)
		{
			RelativeLayout relLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

			ImageButton ib = new ImageButton(this);
			ib.setImageBitmap(thumbnails.get(i));
			ib.setBackgroundColor(Color.TRANSPARENT);
			relLayout.addView(ib);
			final Bitmap imagen = imagenes.get(i);
			ib.setId(i);
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
		}
	}

	public Context darContexto()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}

}
