package com.mini_colombia.auxiliares;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.descargas.DescargasRingtones;
import com.mini_colombia.servicios.DescargarAudioOnline;

public class Ringtone extends Activity implements MediaPlayer.OnPreparedListener
{
	private static final String NOMBRE_CARPETA = "Mini";

	private static final String SEPARADOR = "/";

	private static final String EXTENSION ="mp3";

	private ImageButton botonPlay;

	private ImageButton botonPause;

	private ImageButton botonPonerRingtone;

	private ImageButton botonDescargar;

	private SeekBar seekBar=null;

	private MediaPlayer player;

	private Context contexto;

	private int id;

	private ProgressDialog progress;

	private Context padreTabs;

	private String nombreRingtone;

	private boolean ultimo;

	public Ringtone(LinearLayout layoutPrincipal, final String url, final DescargasRingtones contexto, final int id, Drawable thumb,Drawable progressDrawable,final Context padreTabs, final String nombreRingtone, boolean ultimo)
	{
		this.contexto = contexto;
		this.id = id;
		this.padreTabs = padreTabs;
		this.nombreRingtone = nombreRingtone;
		this.ultimo = ultimo;



		LinearLayout layoutModulo;
		LinearLayout layoutMargenes;



		layoutModulo = new LinearLayout(contexto);
		if(ultimo)
			layoutModulo.setBackgroundResource(R.drawable.fondo_ringtone_ultimo);
		else
			layoutModulo.setBackgroundResource(R.drawable.fondo_ringtones);
		layoutModulo.setOrientation(LinearLayout.VERTICAL);

		layoutMargenes = new LinearLayout(contexto);
		LinearLayout.LayoutParams paramsLayoutMargenes = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		paramsLayoutMargenes.setMargins(20, 10, 20, 0);
		layoutMargenes.setLayoutParams(paramsLayoutMargenes);
		layoutMargenes.setOrientation(LinearLayout.VERTICAL);



		RelativeLayout relSuperior = new RelativeLayout(contexto);
		RelativeLayout.LayoutParams relSupParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		relSuperior.setLayoutParams(relSupParams);

		TextView textoRingtone = new TextView(contexto);
		textoRingtone.setText(nombreRingtone);
		textoRingtone.setTextColor(Color.WHITE);
		RelativeLayout.LayoutParams paramsTRingtone = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		paramsTRingtone.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		relSuperior.addView(textoRingtone,paramsTRingtone);


		botonPlay = new ImageButton(contexto);
		botonPlay.setImageResource(R.drawable.play);
		botonPlay.setBackgroundColor(Color.TRANSPARENT);
		RelativeLayout.LayoutParams paramsBPlay = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		paramsBPlay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		botonPlay.setId(1);
		botonPlay.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{

				contexto.clickPlay(id);
				deshabilitarDescargasYRingtone();
				player = new MediaPlayer();
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				try 
				{
					player.setDataSource(url);
					player.setOnPreparedListener((OnPreparedListener) darContexto());

				} 
				catch (IllegalArgumentException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IllegalStateException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				player.prepareAsync();
				progress = ProgressDialog.show(padreTabs,"","Cargando ringtone",false);
			}
		});
		relSuperior.addView(botonPlay,paramsBPlay);			

		botonPause = new ImageButton(contexto);
		botonPause.setImageResource(R.drawable.pause);
		botonPause.setBackgroundColor(Color.TRANSPARENT);
		RelativeLayout.LayoutParams paramsBPause = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		paramsBPause.addRule(RelativeLayout.LEFT_OF, botonPlay.getId());
		paramsBPause.setMargins(0, 0, 25, 0);
		botonPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				contexto.clickStop(id);
				habilitarDescargasYRingtone();
				player.pause();
			}
		});
		relSuperior.addView(botonPause,paramsBPause);


		layoutMargenes.addView(relSuperior);

		seekBar = new SeekBar(contexto);


		seekBar.setThumb(thumb);
		seekBar.setProgressDrawable(progressDrawable);
		RelativeLayout.LayoutParams paramss = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,13);
		seekBar.setLayoutParams(paramss);
		//		seekBar.setBackgroundColor(Color.TRANSPARENT);
		layoutMargenes.addView(seekBar);

		RelativeLayout relInferior = new RelativeLayout(contexto);
		RelativeLayout.LayoutParams r = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		relInferior.setLayoutParams(r);

		botonPonerRingtone = new ImageButton(contexto);
		botonPonerRingtone.setBackgroundResource(R.drawable.poner_ringtone);
		botonPonerRingtone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				String path = darDireccionSDcard();
				File directorio = new File(path + SEPARADOR + NOMBRE_CARPETA);
				if(!directorio.exists())
					directorio.mkdir();


				File file  = new File(path + SEPARADOR + NOMBRE_CARPETA + SEPARADOR+ nombreRingtone + EXTENSION);
				if(file.exists())
				{
					ContentValues values = new ContentValues();
					values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
					values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
					values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
					values.put(MediaStore.MediaColumns.TITLE, nombreRingtone);

					Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
					Uri newUri = contexto.getContentResolver().insert(uri, values);

					RingtoneManager.setActualDefaultRingtoneUri(contexto, RingtoneManager.TYPE_RINGTONE, newUri);
				}
				else
				{
					progress = ProgressDialog.show(padreTabs,"","Descargando ringtone",false);
					Intent intent = new Intent(contexto, DescargarAudioOnline.class);
					intent.putExtra("url", url);
					intent.putExtra("ringtone", true);
					intent.putExtra("receiver", new DescargaAudioReceiver(new Handler()));
					intent.putExtra("nombre", nombreRingtone);
					contexto.startService(intent);


				}


			}
		});
		RelativeLayout.LayoutParams paramsBPRingtone = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsBPRingtone.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsBPRingtone.setMargins(0, 30, 0, 0);
		relInferior.addView(botonPonerRingtone, paramsBPRingtone);

		botonDescargar = new ImageButton(contexto);
		botonDescargar.setBackgroundResource(R.drawable.descargar_ringtone);
		botonDescargar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{


				String path = darDireccionSDcard();
				File directorio = new File(path + SEPARADOR+ NOMBRE_CARPETA);
				if(!directorio.exists())
					directorio.mkdir();

				File file  = new File(path + SEPARADOR + NOMBRE_CARPETA + SEPARADOR + nombreRingtone + EXTENSION);

				if(!file.exists())
				{
					progress = ProgressDialog.show(padreTabs,"","Descargando ringtone",false);
					Intent intent = new Intent(contexto, DescargarAudioOnline.class);
					intent.putExtra("url", url);
					intent.putExtra("receiver", new DescargaAudioReceiver(new Handler()));
					intent.putExtra("nombre", nombreRingtone);
					intent.putExtra("ringtone", false);
					contexto.startService(intent);
				}
				else
				{
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(padreTabs);
					alertBuilder.setMessage("Ya has descargado este ringtone.");
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
		});
		RelativeLayout.LayoutParams paramsBDescargar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsBDescargar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsBDescargar.setMargins(0, 30, 0, 0);
		relInferior.addView(botonDescargar, paramsBDescargar);

		layoutMargenes.addView(relInferior);

		layoutModulo.addView(layoutMargenes);
		layoutPrincipal.addView(layoutModulo);
	}

	/**
	 * Clase que se encarga de la implementacion des seekbar.
	 * @author Usuario
	 *
	 */
	private class Player extends AsyncTask<Integer, Integer, Void>
	{
		SeekBar seekBar;

		MediaPlayer playerActual;

		int progreso;

		@Override
		protected void onPreExecute() 
		{
			seekBar= darSeekBarActual();
			playerActual = darPlayerActual();
			progreso=0;
		}


		@Override
		protected Void doInBackground(Integer... params) 
		{
			int duracion = params[0];
			seekBar.setMax(duracion);
			while(progreso<duracion && playerActual.isPlaying())
			{
				progreso = playerActual.getCurrentPosition();
				SystemClock.sleep(150);
				publishProgress(progreso);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) 
		{
			seekBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			player.stop();
			player.release();
			seekBar.setProgress(0);
			darClasePadre().clickStop(id);
		}

	}



	/**
	 * Metodo que se lanza despues de que el metodo prepareAsync() haya concluido.
	 */
	public void onPrepared(MediaPlayer mp) 
	{
		progress.dismiss();
		player.start();
		final int duracion = player.getDuration();
		darSeekBarActual().setProgress(0);
		darSeekBarActual().setMax(duracion);

		new Player().execute(duracion);



	}


	//////////////////////////////////////////////////////////////////	
	//  getters 
	//////////////////////////////////////////////////////////////////

	public Context darContexto()
	{
		return this;
	}
	
	public SeekBar darSeekBarActual()
	{
		return this.seekBar;
	}

	public MediaPlayer darPlayerActual()
	{
		return this.player;
	}

	public DescargasRingtones darClasePadre()
	{
		return (DescargasRingtones) contexto;
	}
	
	public String darDireccionSDcard()
	{
		return  DescargasRingtones.darDireccionSDcard();
	}
	
	//////////////////////////////////////////////////////////////////	
	//  Metodos para bloquear y desbloquear los botones
	//////////////////////////////////////////////////////////////////
	
	public void deshabilitarBotones()
	{
		botonPlay.setClickable(false);
		botonPause.setClickable(false);
		botonPonerRingtone.setClickable(false);
		botonDescargar.setClickable(false);
	}

	public void habilitarBotones()
	{
		botonPlay.setClickable(true);
		botonPause.setClickable(true);
		botonPonerRingtone.setClickable(true);
		botonDescargar.setClickable(true);
	}

	public void deshabilitarDescargasYRingtone()
	{
		botonPonerRingtone.setClickable(false);
		botonDescargar.setClickable(false);
	}

	public void habilitarDescargasYRingtone()
	{
		botonPonerRingtone.setClickable(true);
		botonDescargar.setClickable(true);
	}

	/**
	 * Clase receiver encargada de recibir los resultados del servicio lanzado cuando
	 * se descarga el ringtone.
	 * @author Usuario
	 *
	 */
	private class DescargaAudioReceiver extends ResultReceiver
	{

		public DescargaAudioReceiver(Handler handler)
		{
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) 
		{
			// TODO Auto-generated method stub
			super.onReceiveResult(resultCode, resultData);
			if(resultCode == DescargarAudioOnline.ACABO)
			{
				progress.dismiss();

				//Condicional que determina si se quiere establecer el archivo como ringtone
				//o si solo se queria escuchar
				if(resultData.getBoolean("ringtone"))
				{

					String rutaCompleta = resultData.getString("ruta");
					String nombre = resultData.getString("nombre");
					ContentValues values = new ContentValues();
					values.put(MediaStore.MediaColumns.DATA, rutaCompleta);
					values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
					values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
					values.put(MediaStore.MediaColumns.TITLE, nombre);

					Uri uri = MediaStore.Audio.Media.getContentUriForPath(rutaCompleta);
					Uri newUri = contexto.getContentResolver().insert(uri, values);

					RingtoneManager.setActualDefaultRingtoneUri(contexto, RingtoneManager.TYPE_RINGTONE, newUri);
				}
			}

		}

	}









}
