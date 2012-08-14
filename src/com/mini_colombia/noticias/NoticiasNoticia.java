package com.mini_colombia.noticias;

import java.sql.SQLException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.mini_colombia.R;
import com.mini_colombia.dao.DaoNoticias;
import com.mini_colombia.db.DataBaseHelper;
import com.mini_colombia.values.Noticia;

public class NoticiasNoticia extends Activity 
{

	private static final String NOMBRE_CATEGORIA="categoria";
	
	private static final String URL_PAGINA ="url";

	private String nombreCategoria;

	private DataBaseHelper databaseHelper;

	private Dao<Noticia, Integer> daoNoticia;

	private WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_noticias_noticia);
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView textoTitulo = (TextView)findViewById(R.id.tituloNoticiasNoticia);
		textoTitulo.setTypeface(tipoMini);
		
		nombreCategoria = getIntent().getStringExtra(NOMBRE_CATEGORIA);
		wv = (WebView) findViewById(R.id.webViewNoticia);
		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo red  = conMgr.getActiveNetworkInfo();
		boolean conexionInternet = red!=null && red.getState() == NetworkInfo.State.CONNECTED;
		if(nombreCategoria !=null)
		{
			daoNoticia = darDaoNoticia();
			try 
			{
				Noticia n = DaoNoticias.darNoticia(nombreCategoria, daoNoticia);

				if(conexionInternet)
					wv.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);

				else
					wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);

				wv.loadUrl(n.getUrl());
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			String url = getIntent().getStringExtra(URL_PAGINA);
			if(conexionInternet)
				wv.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);

			else
				wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			
			wv.loadUrl(url);
			
		}

		



	}




		@Override
		public void onBackPressed() 
		{
			getParent().onBackPressed();
		}

	public DataBaseHelper getHelper()
	{
		if(databaseHelper == null)
			databaseHelper = OpenHelperManager.getHelper(this,DataBaseHelper.class);
		return databaseHelper;
	}

	public Dao<Noticia, Integer> darDaoNoticia()
	{
		Dao<Noticia, Integer> daoNoticia = null;
		try 
		{
			daoNoticia = getHelper().darDaoNoticia();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return daoNoticia;
	}



	protected void onDestroy() 
	{
		super.onDestroy();
		Log.d("Tutorial", "onDestroy noticia");
		//Manejo del objeto DataBaseHelper
		if(databaseHelper != null)
			OpenHelperManager.releaseHelper();
		databaseHelper = null;
	}

	private WebView darWebView()
	{
		return wv;
	}
	
	public Context darContexto()
	{
		Context context = null;
	    if (getParent() != null) 
	    	context = getParent();
	    return context;
	}


}
