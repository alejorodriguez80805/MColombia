package com.mini_colombia.servicios;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ObtenerImagen 
{
	public static Bitmap  darImagen(String nombreImagen, Context context)
	{
		Bitmap respuesta = null;
		
		try 
		{
			FileInputStream fis = context.openFileInput(nombreImagen);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] bMapArray = new byte[bis.available()];
			bis.read(bMapArray);
			respuesta = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
			bis.close();
			fis.close();
		} 
		catch (FileNotFoundException e) 
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
