package com.mini_colombia.auxiliares;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
	private ImageButton botonPlay;

	private ImageButton botonPause;

	private SeekBar seekBar=null;;

	private MediaPlayer player;

	private Context contexto;

	private int id;

	private ProgressDialog progress;

	private Context padreTabs;
	
	private String nombreRingtone;

	public Ringtone(LinearLayout layoutPrincipal, final String url, final DescargasRingtones contexto, final int id, Drawable thumb,Drawable progressDrawable,final Context padreTabs, final String nombreRingtone)
	{
		this.contexto = contexto;
		this.id = id;
		this.padreTabs = padreTabs;
		this.nombreRingtone = nombreRingtone;



		LinearLayout layoutModulo;
		LinearLayout layoutMargenes;



		layoutModulo = new LinearLayout(contexto);
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
				progress = ProgressDialog.show(padreTabs,"","Cargando Ringtone",false);
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

		ImageButton botonPonerRingtone = new ImageButton(contexto);
		botonPonerRingtone.setBackgroundResource(R.drawable.poner_ringtone);
		botonPonerRingtone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				String path = darDireccionSDcard();
				File directorio = new File(path + "/Mini");
				if(!directorio.exists())
					directorio.mkdir();
				

				File file  = new File(path + contexto.getString(R.string.TAG_NOMBRE_ARCHIVO_RINGTONE));
				if(file.exists())
				{
					ContentValues values = new ContentValues();
					values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
					values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
					values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
					values.put(MediaStore.MediaColumns.TITLE, "hola");

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
					contexto.startService(intent);


				}


			}
		});
		RelativeLayout.LayoutParams paramsBPRingtone = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsBPRingtone.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsBPRingtone.setMargins(0, 20, 0, 0);
		relInferior.addView(botonPonerRingtone, paramsBPRingtone);

		ImageButton botonDescargar = new ImageButton(contexto);
		botonDescargar.setBackgroundResource(R.drawable.descargar_ringtone);
		botonDescargar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				String path = darDireccionSDcard();
				File directorio = new File(path + "/Mini");
				if(!directorio.exists())
					directorio.mkdir();
				progress = ProgressDialog.show(padreTabs,"","Descargando ringtone",false);
				Intent intent = new Intent(contexto, DescargarAudioOnline.class);
				intent.putExtra("url", url);
				intent.putExtra("receiver", new DescargaAudioReceiver(new Handler()));
				contexto.startService(intent);
				//				new DescargarRingtone().execute(url);
			}
		});
		RelativeLayout.LayoutParams paramsBDescargar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsBDescargar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsBDescargar.setMargins(0, 20, 0, 0);
		relInferior.addView(botonDescargar, paramsBDescargar);

		layoutMargenes.addView(relInferior);

		layoutModulo.addView(layoutMargenes);
		layoutPrincipal.addView(layoutModulo);
	}

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

	public SeekBar darSeekBarActual()
	{
		return this.seekBar;
	}

	public MediaPlayer darPlayerActual()
	{
		return this.player;
	}

	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		progress.dismiss();
		player.start();
		final int duracion = player.getDuration();
		darSeekBarActual().setProgress(0);
		darSeekBarActual().setMax(duracion);

		new Player().execute(duracion);



	}



	public Context darContexto()
	{
		return this;
	}

	public void deshabilitarBotones()
	{
		botonPlay.setClickable(false);
		botonPause.setClickable(false);
	}

	public void habilitarBotones()
	{
		botonPlay.setClickable(true);
		botonPause.setClickable(true);
	}

	public DescargasRingtones darClasePadre()
	{
		return (DescargasRingtones) contexto;
	}

	public Window darWindow()
	{
		return getWindow();
	}


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

				if(resultData.getBoolean("ringtone"))
				{
					
					String path = resultData.getString("nombre");
					ContentValues values = new ContentValues();
					values.put(MediaStore.MediaColumns.DATA, path);
					values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
					values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
					values.put(MediaStore.MediaColumns.TITLE, "hola");

					Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
					Uri newUri = contexto.getContentResolver().insert(uri, values);

					RingtoneManager.setActualDefaultRingtoneUri(contexto, RingtoneManager.TYPE_RINGTONE, newUri);
				}
			}

		}

	}

	public String darDireccionSDcard()
	{
		return  DescargasRingtones.darDireccionSDcard();
	}







}
