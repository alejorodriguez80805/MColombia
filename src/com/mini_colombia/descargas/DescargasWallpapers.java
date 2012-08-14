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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

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
	
	private int numWallpapers;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		thumbnails = new ArrayList<Bitmap>();
		imagenes = new ArrayList<Bitmap>();
		setContentView(R.layout.activity_descargas_wallpapers);
		new DescargarThumbnails().execute(NUM_WALLPAPERS);
		
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView)findViewById(R.id.tituloDescargasWallpapers);
		titulo.setTypeface(tipoMini);


	}

	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	}


	private class DescargarThumbnails extends AsyncTask<Integer, Integer, Boolean>
	{

		ProgressDialog progress;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Cargando...",false);
		}

		@Override
		/**
		 * Metodo que descarga tanto 
		 */
		protected Boolean doInBackground(Integer... params) 
		{

			boolean respuesta = false;
			Parser jparser = new Parser();
			JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_DESCARGAS_WALLPAPERS));
			try 
			{
				JSONArray wallpapers = jsonObject.getJSONArray(getString(R.string.TAG_WALLPAPERS));
				numWallpapers = Integer.parseInt(jsonObject.getString(getString(R.string.TAG_WALLPAPERS_NUMERO_TOTAL))); 
				int parametro = params[0];
				int cota;
				int i;
				if(parametro!=0)
				{
					cota = parametro;
					respuesta = true;
					i = 0;
				}
					
				else
				{
					cota = wallpapers.length();
					i= NUM_WALLPAPERS;
				}

				

				while(i<cota)
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
					
					i++;
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return respuesta;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss();
			pintarPantalla(result);
		}

	}

	private void pintarPantalla(boolean numVez)
	{
		LinearLayout layoutPrincipal = (LinearLayout) findViewById(R.id.linearLayoutWallpapers);
		int i;
		int cota;
		boolean pintarVerMas = false;;
		
		if(numVez)
		{
			i = 0;
			cota = NUM_WALLPAPERS;
			pintarVerMas = true;
		}
		
		else
		{
			
			View v = new View(this);
			LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(10,10);
			v.setLayoutParams(vParams);	
			layoutPrincipal.addView(v);
			i = NUM_WALLPAPERS;
			cota = thumbnails.size();
		}
		
		while(i<cota)
		{
			final int posicionActual =i;
			
			RelativeLayout relLayout = new RelativeLayout(this);
			relLayout.setBackgroundColor(Color.TRANSPARENT);
			relLayout.setBackgroundDrawable(new BitmapDrawable(thumbnails.get(i)));
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			
			
			
			Log.d("prueba", "" + relLayout.getWidth());
			
			Button thumbnail = new Button(this);
			RelativeLayout.LayoutParams bParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,292);
			
			final Bitmap imagen = imagenes.get(i);
			
			thumbnail.setLayoutParams(bParams);
			thumbnail.setBackgroundColor(Color.TRANSPARENT);
			relLayout.addView(thumbnail);
			bParams.setMargins(0, 0, 0, 40);
			thumbnail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) 
				{
					Intent i = new Intent(DescargasWallpapers.this, DescargasImagen.class);
					i.putExtra(IMAGEN, imagen);
					i.putExtra("actual", posicionActual+1);
					i.putExtra("total", numWallpapers);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					View v1 = DescargasInicio.grupoDescargas.getLocalActivityManager().startActivity("", i).getDecorView();
					DescargasInicio actividadPadre = (DescargasInicio) getParent();
					actividadPadre.reemplazarView(v1);
				}
			});


			Button bDescargar = new Button(this);
			bDescargar.setBackgroundColor(Color.TRANSPARENT);
			bDescargar.setOnClickListener(new OnClickListener() 
			{
				
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
			bDescargar.setLayoutParams(paramsbutton);
			relLayout.addView(bDescargar);

			layoutPrincipal.addView(relLayout, relParams);
			
			i++;

		}
			
			
		
			if(pintarVerMas)
			{
				final Button verMas = new Button(this);
				verMas.setBackgroundColor(Color.BLACK);
				LinearLayout.LayoutParams paramsVerMas = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				paramsVerMas.setMargins(0, 7, 0, 0);
				verMas.setLayoutParams(paramsVerMas);
				verMas.setText("VER MAS");
				verMas.setTextSize(12);
				verMas.setTextColor(Color.WHITE);
				verMas.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
//				verMas.setPadding(5, 5, 0, 5);
				verMas.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						verMas.setVisibility(View.GONE);
						new DescargarThumbnails().execute(0);
						
					}
				});
				layoutPrincipal.addView(verMas);
			}
		

			
		
	}
	
	public void pintarWallpapersRestantes()
	{
		LinearLayout layoutPrincipal = (LinearLayout) findViewById(R.id.linearLayoutWallpapers);

		for(int i=NUM_WALLPAPERS;i<thumbnails.size();i++)
		{
			RelativeLayout relLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			relParams.setMargins(0, 10, 0, 0);

			ImageButton ib = new ImageButton(this);
			ib.setImageBitmap(thumbnails.get(i));
			ib.setBackgroundColor(Color.TRANSPARENT);
			relLayout.addView(ib);
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
