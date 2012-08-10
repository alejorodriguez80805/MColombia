package com.mini_colombia.comunidad;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.auxiliares.ImagenGaleria;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.DescargarImagenOnline;

public class ComunidadInicio extends ActivityGroup
{
	public ArrayList<ImagenGaleria> g;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comunidad_inicio);
		
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView) findViewById(R.id.textoInicioComunidad);
		titulo.setText("COMUNIDAD.");
		titulo.setTypeface(tipoMini);
		titulo.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
	}
	
	public void iniciarEventos(View v)
	{
		
	}
	
	public void iniciarGaleria(View v)
	{
		String url = getString(R.string.CONSTANTE_COMUNIDAD_GALERIA);
		try {
			 g = new DescargarInformacion().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private class DescargarInformacion extends AsyncTask<String, Void, ArrayList<ImagenGaleria>>
	{

		private ArrayList<ImagenGaleria> arreglo;
		@Override
		protected ArrayList<ImagenGaleria> doInBackground(String... params) 
		{
			arreglo = new ArrayList<ImagenGaleria>();
			
			
			String url = params[0];
			Parser jparser = new Parser();
			JSONObject jsonObject = jparser.getJSONFromUrl(url);
			try 
			{
				JSONArray imagenes = jsonObject.getJSONArray(getString(R.string.TAG_COMUNIDAD_GALERIA_IMAGENES));
				
				for(int i = 0; i<imagenes.length(); i++)
				{
					JSONObject imagen = imagenes.getJSONObject(i);
					
					String nombre = imagen.getString(getString(R.string.TAG_COMUNIDAD_GALERIA_NOMBRE));
					String thumbnailURL = imagen.getString(getString(R.string.TAG_COMUNIDAD_GALERIA_THUMBNAIL));
					Bitmap imagenThumbnail = DescargarImagenOnline.descargarImagen(thumbnailURL);
					String imagenURL = imagen.getString(getString(R.string.TAG_COMUNIDAD_GALERIA_IMAGEN));
					Bitmap bImagen = DescargarImagenOnline.descargarImagen(imagenURL);
					
					ImagenGaleria j = new ImagenGaleria(nombre, imagenThumbnail, bImagen);
					arreglo.add(j);
					
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return arreglo;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ImagenGaleria> result) 
		{
			super.onPostExecute(result);
			darArreglo();
		}
		
	}
	
	public ArrayList<ImagenGaleria> darArreglo()
	{
		ArrayList<ImagenGaleria> o = g;
		return o;
	}
}
