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
	
	public DescargarAudioOnline()
	{
		super("");
	}
	
	public static void descargarAudio(String sUrl)
	{
		URL url;
		String path = Environment.getExternalStorageDirectory().toString();
		try 
		{
			url = new URL(sUrl);
			HttpURLConnection conn= (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(path + "/ringtoneMini.mp3");
			
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
		

	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		String sUrl = intent.getStringExtra("url");
		boolean ringtone = intent.getBooleanExtra("ringtone", false);
		ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
		String path = Environment.getExternalStorageDirectory().toString();
		
		try 
		{
			URL url = new URL(sUrl);
			HttpURLConnection conn= (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			String s = path + getString(R.string.TAG_NOMBRE_ARCHIVO_RINGTONE);
			OutputStream os = new FileOutputStream(path + getString(R.string.TAG_NOMBRE_ARCHIVO_RINGTONE));
			
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
		adjuntos.putString("nombre", path + getString(R.string.TAG_NOMBRE_ARCHIVO_RINGTONE));
		receiver.send(ACABO, adjuntos);
		
	}
}
