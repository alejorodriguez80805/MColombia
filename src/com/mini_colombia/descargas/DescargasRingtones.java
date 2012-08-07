package com.mini_colombia.descargas;

import java.util.ArrayList;
import java.util.Dictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;

import com.mini_colombia.R;
import com.mini_colombia.auxiliares.Ringtone;
import com.mini_colombia.parser.Parser;

public class DescargasRingtones extends Activity 
{
	ArrayList<String> canciones = new ArrayList<String>();

	private ArrayList<Ringtone> arregloRingtones;


	private ArrayList<String> arreglo;




	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descargas_ringtones);

		arregloRingtones = new ArrayList<Ringtone>();
		arreglo = new ArrayList<String>();
		new DescargarJsonRingtones().execute(getString(R.string.CONSTANTE_DESCARGAS_RINGTONES));


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

	public static String darDireccionSDcard()
	{
		return Environment.getExternalStorageDirectory().toString();
	}

	private class DescargarJsonRingtones extends AsyncTask<String, Void, Void>
	{

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

					//					LinearLayout l = darLayoutPrincipal();
					//					DescargasRingtones d = darContexto();
					//					Drawable p = darProgress();
					//					Drawable t = darThumb();
					//					Context c = darContextoTabs();

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
			crearRingtones();
		}

	}

	//	private LinearLayout darLayoutPrincipal()
	//	{
	//		return (LinearLayout)findViewById(R.id.linearLayoutRingtones);
	//	}
	//
	//	private Drawable darThumb()
	//	{
	//		return getResources().getDrawable(R.drawable.seek_thumb);
	//	}
	//
	//	private Drawable darProgress()
	//	{
	//		return getResources().getDrawable(R.drawable.seekbar_progress_bg);
	//	}
	//
	private DescargasRingtones darContexto()
	{
		return this;
	}
	//
	//	private Ringtone crearRingtone(LinearLayout l, DescargasRingtones d, Drawable p, Drawable t, Context c, String nombre, String urlRingtone, int id)
	//	{
	//		return new Ringtone(l, urlRingtone, d, id, t, p, c, nombre);
	//	}

	private void crearRingtones()
	{
		for(int i=0;i<arreglo.size();i++)
		{
			String [] parametrosJson = arreglo.get(i).split(";");
			String nombre = parametrosJson[0];
			String url = parametrosJson[1];

			Ringtone r = new Ringtone((LinearLayout)findViewById(R.id.linearLayoutRingtones), url, darContexto(), i, getResources().getDrawable(R.drawable.seek_thumb), getResources().getDrawable(R.drawable.seekbar_progress_bg), darContextoTabs(), nombre);
			arregloRingtones.add(r);
		}
	}

}
