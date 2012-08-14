package com.mini_colombia.comunidad;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.mini_colombia.R;
import com.mini_colombia.auxiliares.ImagenGaleria;
import com.mini_colombia.descargas.DescargasImagen;
import com.mini_colombia.descargas.DescargasInicio;
import com.mini_colombia.descargas.DescargasWallpapers;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.AsyncTaskListener;
import com.mini_colombia.servicios.DescargarImagenOnline;
import com.mini_colombia.servicios.ImageAdapter;

public class ComunidadGaleria extends Activity implements AsyncTaskListener<ArrayList<ImagenGaleria>>
{

	private ArrayList<ImagenGaleria> arregloImagenes;
	private ArrayList<String> imagenes;
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comunidad_galeria);

		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView) findViewById(R.id.textoGaleria);
		titulo.setText("GALERIA.");
		titulo.setTypeface(tipoMini);
		String url = getString(R.string.CONSTANTE_COMUNIDAD_GALERIA);
		inicializarTareaAsincrona(url);

	}

	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	} 


	private class DescargarInformacion extends AsyncTask<String, Void, ArrayList<ImagenGaleria>>
	{	
		private ArrayList<ImagenGaleria> arreglo;

		private Context context;

		private AsyncTaskListener<ArrayList<ImagenGaleria>> callback;

		private ProgressDialog progress;

		public DescargarInformacion(Context context, AsyncTaskListener<ArrayList<ImagenGaleria>> callback)
		{
			this.context = context;

			this.callback = callback;
		}

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(darContexto(),"","Cargando imagenes...",false);
		}

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
//					Bitmap bImagen = DescargarImagenOnline.descargarImagen(imagenURL);

					ImagenGaleria j = new ImagenGaleria(nombre, imagenThumbnail, imagenURL);
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
			if(progress.isShowing())
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
	public void onTaskComplete(ArrayList<ImagenGaleria> result) 
	{
		arregloImagenes = result;
		ArrayList<String> imagenes = new ArrayList<String>();
//		for(int i=0; i<arregloImagenes.size(); i ++)
//		{
//			imagenes.add(arregloImagenes.get(i).getThumbnail());
//		}

		final GridView grid = (GridView) findViewById(R.id.gridGaleria);

		grid.setAdapter(new ImageAdapter(this, arregloImagenes, darPadre()));
		grid.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
			{

//				ImagenGaleria imagen = arregloImagenes.get(position); 
//
//				Intent i = new Intent(ComunidadGaleria.this, ComunidadImagen.class);
//				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				i.putExtra("size", arregloImagenes.size());
//				i.putExtra("posActual", position +1);
//				i.putExtra("titulo", imagen.getNombre());
//				i.putExtra("url", imagen.getImagen());
//				View v1 = ComunidadInicio.grupoComunidad.getLocalActivityManager().startActivity("", i).getDecorView();
//				ComunidadInicio actividadPadre = (ComunidadInicio) getParent();
//				actividadPadre.reemplazarView(v1);

			}

		});
	}

	public void inicializarTareaAsincrona (String url)
	{
		DescargarInformacion tarea = new DescargarInformacion(darContexto(),this);
		tarea.execute(url);
	}
	
	public Activity darPadre()
	{
		return getParent();
	}




}
