package com.mini_colombia.descargas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.mini_colombia.R;
import com.mini_colombia.servicios.Resize;

public class DescargasImagen extends Activity
{
	private static final String IMAGEN="imagen";

	private Bitmap bitmapPreliminar;
	private File f;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		bitmapPreliminar = (Bitmap) b.get(IMAGEN);
		Bitmap bitmapFinal = Resize.resizeBitmap(bitmapPreliminar, 297, 445);
		setContentView(R.layout.activity_descargas_imagen);

		ImageView iv = (ImageView) findViewById(R.id.imagenWallpaper);
		iv.setImageBitmap(bitmapFinal);

		//Almacenamiento del bitmap en la tarjeta SD
		String path = Environment.getExternalStorageDirectory().toString();
		f = new File(path, "MINI.jpg");
		OutputStream fos;
		try 
		{
			fos = new FileOutputStream(f);
			bitmapPreliminar.compress(Bitmap.CompressFormat.JPEG, 85, fos);
			fos.flush();
			fos.close();
			MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), f.getName(), f.getName());

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



	}

	public void abrirFacebook(View v)
	{
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/MINI"));
		startActivity(i);
	}

	public void abrirTwitter(View v)
	{
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MINI"));
		startActivity(i);
	}

	public void enviarCorreo(View v)
	{

		Uri uri = Uri.fromFile(f); 

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("image/jpg");
		emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
		startActivity(emailIntent);


	}

	public void compartirImagen(View v)
	{
		Uri uri = Uri.fromFile(f);

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/jpg");
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(shareIntent, "Compartir via"));

	}

	public void descargarImagen(View v)
	{
		MediaStore.Images.Media.insertImage(getContentResolver(), bitmapPreliminar, "Mini","");
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


	@Override
	protected void onDestroy() 
	{
		f.delete();
		super.onDestroy();
	}

	public Context darContexto()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}


}
