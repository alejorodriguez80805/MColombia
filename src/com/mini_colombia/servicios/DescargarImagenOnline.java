package com.mini_colombia.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DescargarImagenOnline 
{
	public static Bitmap descargarImagen(String url)
	{
		Bitmap respuesta = null;
		URL urlImagen;
		try 
		{
			urlImagen = new URL(url);
			HttpURLConnection conn= (HttpURLConnection)urlImagen.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BitmapFactory.Options options = new BitmapFactory.Options();
			respuesta = BitmapFactory.decodeStream(is,null,options);

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
		
		return respuesta;

	}

}
