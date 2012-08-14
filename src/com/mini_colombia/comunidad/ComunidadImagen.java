package com.mini_colombia.comunidad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.servicios.AsyncTaskListener;
import com.mini_colombia.servicios.DescargarImagenOnline;
import com.mini_colombia.servicios.Resize;

public class ComunidadImagen extends Activity implements AsyncTaskListener<Bitmap>
{
	private static final String NOMBRE_CARPETA = "MINI";

	private static final String SEPARADOR = "/";
	
	private Bitmap bImagen;

	private Uri uri;

	private static final String EXTENSION = ".jpg";

	private String nombreImagen;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comunidad_imagen);

		int posActual = getIntent().getIntExtra("posActual", 0);
		int size = getIntent().getIntExtra("size", 0);

		TextView titulo = (TextView) findViewById(R.id.comunidad_imagen_titulo);
		titulo.setText(posActual + " de " + size);

		nombreImagen = getIntent().getStringExtra("titulo");
		TextView tituloImagen = (TextView) findViewById(R.id.comunidad_nombre_imagen);
		tituloImagen.setText(nombreImagen);

		String url = getIntent().getStringExtra("url");
		DescargarImagen tarea = new DescargarImagen(darContexto(), this);
		tarea.execute(url);

	}


	private class DescargarImagen extends AsyncTask<String, Void, Bitmap>
	{

		private AsyncTaskListener<Bitmap> callback;

		private Context context;

		private ProgressDialog progress;

		public DescargarImagen(Context context, AsyncTaskListener<Bitmap> callback)
		{
			this.context = context;
			this.callback = callback;
		}

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(darContexto(),"","Cargando imagen...",false);
		}

		@Override
		protected Bitmap doInBackground(String... params) 
		{
			Bitmap b = DescargarImagenOnline.descargarImagen(params[0]);
			Bitmap bFinal = Resize.resizeBitmap(b, 292, 440);
			return bFinal;
		}

		@Override
		protected void onPostExecute(Bitmap result) 
		{
			progress.dismiss();
			callback.onTaskComplete(result);
		}

	}

	public Context darContexto()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}


	@Override
	public void onTaskComplete(Bitmap result) 
	{
		ImageView imagen = (ImageView) findViewById(R.id.imagenComunidadGaleria);
		imagen.setImageBitmap(result);
		bImagen = result;



	}

	public boolean guardarImagen(Bitmap result)
	{
		boolean respuesta;
		

		
		
		String path = Environment.getExternalStorageDirectory().toString() + SEPARADOR + NOMBRE_CARPETA;
		File directorio = new File(path);
		if(!directorio.exists())
			directorio.mkdir();
		
		File f = new File(path,darNombreImagen()+ EXTENSION);
		if(!f.exists())
		{
			Bitmap b = result;
			OutputStream fos;
			try 
			{
				fos = new FileOutputStream(f);
				b.compress(Bitmap.CompressFormat.JPEG, 85, fos);
				fos.flush();
				fos.close();
				MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), darNombreImagen()+ EXTENSION, f.getName());

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
			
					
					respuesta = true;
		}
		else
		{
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
			alertBuilder.setMessage("La imagen ya ha sido descargada.");
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
			respuesta = false;
		}
		uri = Uri.fromFile(f);
		return respuesta;

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
		String path = Environment.getExternalStorageDirectory().toString();
		File f = new File(path + SEPARADOR + NOMBRE_CARPETA + SEPARADOR+ nombreImagen+ EXTENSION);
		if(!f.exists())
		{
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
			alertBuilder.setMessage("Debe descargar primero la imagen y volver a intentar de nuevo.");
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
		else
		{
			Uri uri = darUri();

			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("image/jpg");
			emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
			startActivity(emailIntent);
		}




	}

	public void compartirImagen(View v)
	{
		String path = Environment.getExternalStorageDirectory().toString();
		File f = new File(path + SEPARADOR + NOMBRE_CARPETA + SEPARADOR+ nombreImagen+ EXTENSION);
		if(!f.exists())
		{
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
			alertBuilder.setMessage("Debe descargar primero la imagen y volver a intentar de nuevo.");
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
		
		else
		{
			Uri uri = darUri();

			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("image/jpg");
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(shareIntent, "Compartir via"));
		}


	}

	public void descargarImagen(View v)
	{
		if(guardarImagen(darBitmap()))
		{
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
		
//		MediaStore.Images.Media.insertImage(getContentResolver(), darBitmap(), nombreImagen+ EXTENSION,"");
		
	}



	public Uri darUri()
	{
		return uri;
	}

	public Bitmap darBitmap()
	{
		return bImagen;
	}

	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	}

	@Override
	protected void onDestroy() 
	{
		Log.d("prueba", "paseo");
		super.onDestroy();
	}


	public String darNombreImagen()
	{
		return nombreImagen;
	}

}
