package com.mini_colombia.descargas;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.auxiliares.Ringtone;
import com.mini_colombia.parser.Parser;

public class DescargasRingtones extends Activity 
{

	//Arreglo utilizado almacenar cada ringtone y manejar los eventos de los botones
	private ArrayList<Ringtone> arregloRingtones;

	//Arreglo utilizado para almacenar la url y el nombre de cada ringtone
	private ArrayList<String> arreglo;




	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descargas_ringtones);

		arregloRingtones = new ArrayList<Ringtone>();
		arreglo = new ArrayList<String>();
		new DescargarJsonRingtones().execute(getString(R.string.CONSTANTE_DESCARGAS_RINGTONES));

		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView) findViewById(R.id.tituloDescargasRingtones);
		titulo.setTypeface(tipoMini);

	}

	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
	}


	public Context darContextoTabs()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}

	//////////////////////////////////////////////////////////////////	
	//  Metodos que maneja los eventos de los botones de cada ringtone
	//////////////////////////////////////////////////////////////////

	public void clickPlay(int id)
	{
		for(int i = 0; i<arregloRingtones.size(); i ++)
		{
			if(id!=i)
				arregloRingtones.get(i).deshabilitarBotones();
		}
	}

	public void clickStop(int id)
	{
		for(int i =0; i<arregloRingtones.size();i ++)
		{
			arregloRingtones.get(i).habilitarBotones();
		}
	}



	/**
	 * Metodo utilizado por la clase ringtone para obtener la direccion de la tarjeta SD
	 * @return
	 */
	public static String darDireccionSDcard()
	{
		return Environment.getExternalStorageDirectory().toString();
	}



	/**
	 * 	Metodo asincronico que se encarga de descargar y almacenar en el arreglo 
	 * 	arreglo tanto el nombre como la url de cada ringtone
	 * @author Usuario
	 *
	 */
	private class DescargarJsonRingtones extends AsyncTask<String, Void, Void>
	{

		ProgressDialog progress;
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContextoTabs(),"","Cargando...",false);
		}
		
		@Override
		protected Void doInBackground(String... params) 
		{
			String url = params[0];
			Parser jparser = new Parser();
			JSONObject jsonObject = jparser.getJSONFromUrl(url);
			Ringtone r = null;

			try 
			{
				JSONArray ringtones = jsonObject.getJSONArray(getString(R.string.TAG_RINGTONES));

				for(int i = 0;i<ringtones.length();i++)
				{
					JSONObject ringtone = ringtones.getJSONObject(i);

					String nombre = ringtone.getString(getString(R.string.TAG_NOMBRE_RINGTONES));
					String urlRingtone = ringtone.getString(getString(R.string.TAG_URL_RINGTONES));

					arreglo.add(nombre + ";" + urlRingtone);
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			if(progress.isShowing())
				progress.dismiss();
			crearRingtones();
		}

	}


	/**
	 * Metodo utilizado para m=enviar a la clas Ringtone el contexto de esta clase.
	 * @return
	 */
	private DescargasRingtones darContexto()
	{
		return this;
	}


	/**
	 * Metodo que anade ringtones al arreglo arregloringtones
	 */
	private void crearRingtones()
	{
		for(int i=0;i<arreglo.size();i++)
		{
			boolean ultimo;

			if(i != arreglo.size()-1)
				ultimo = false;
			else
				ultimo = true;
			String [] parametrosJson = arreglo.get(i).split(";");
			String nombre = parametrosJson[0];
			String url = parametrosJson[1];

			Ringtone r = new Ringtone((LinearLayout)findViewById(R.id.linearLayoutRingtones), url, darContexto(), i, getResources().getDrawable(R.drawable.seek_thumb), getResources().getDrawable(R.drawable.seekbar_progress_bg), darContextoTabs(), nombre, ultimo);
			arregloRingtones.add(r);
		}
	}

}
