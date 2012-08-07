package com.mini_colombia.servicios;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;

import com.mini_colombia.R;

public class DescargarAudioOnline extends IntentService
{
	public static final int ACABO = 1;
	
	private static final String SEPARADOR = "/";
	
	private static final String MINI = "Mini";
	
	private static final String EXTENSION = "mp3";
	
	public DescargarAudioOnline()
	{
		super("");
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		String sUrl = intent.getStringExtra("url");
		boolean ringtone = intent.getBooleanExtra("ringtone", false);
		ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
		String nombre = intent.getStringExtra("nombre");
		String path = Environment.getExternalStorageDirectory().toString();
		String rutaCompleta = path + SEPARADOR + MINI+ SEPARADOR + nombre + EXTENSION;
		
		try 
		{
			URL url = new URL(sUrl);
			HttpURLConnection conn= (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(rutaCompleta);
			
			byte data[] = new byte[1024];
            int count;
            
            while((count = is.read(data)) != -1)
            {
            	os.write(data, 0, count);
            }
            
            os.flush();
            os.close();
            is.close();
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Bundle adjuntos = new Bundle();
		adjuntos.putBoolean("ringtone", ringtone);
		adjuntos.putString("ruta", rutaCompleta);
		adjuntos.putString("nombre", nombre);
		receiver.send(ACABO, adjuntos);
		
	}
}
