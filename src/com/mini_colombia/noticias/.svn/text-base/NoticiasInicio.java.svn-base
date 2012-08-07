package com.mini_colombia.noticias;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.mini_colombia.ManejadorTabs;
import com.mini_colombia.R;
import com.mini_colombia.db.DataBaseHelper;
import com.mini_colombia.servicios.ObtenerImagen;
import com.mini_colombia.servicios.Resize;
import com.mini_colombia.values.Noticia;

public class NoticiasInicio extends ActivityGroup implements OnClickListener
{
	private static final String NOMBRE_CATEGORIA="categoria";

	private static final String CATEGORIA_INTERNACIONAL = "Noticia Internacional.";

	private static final String CATEGORIA_NACIONAL = "Noticia Nacional.";
	
	private static final String PROMOCION = "Promocion.";
	
	private static final String NOVEDAD = "Novedad.";
	
	private DataBaseHelper databaseHelper;

	private Dao<Noticia, Integer> daoNoticia;

	public static NoticiasInicio grupoNoticias;

	public ArrayList<View> historialViews;
	
	private static NoticiasInicio instance;
	
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noticias_inicio);
		grupoNoticias = this;
		historialViews = new ArrayList<View>();


		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView tituloTexto = (TextView)findViewById(R.id.tituloInicioNoticias);
		tituloTexto.setTypeface(tipoMini);

		LinearLayout layoutPrincipal = (LinearLayout)findViewById(R.id.linearLayoutInicioNoticias);

		daoNoticia = darDaoNoticia();

		try 
		{
			List<Noticia> noticias = daoNoticia.queryForAll();

			LinearLayout layout;
			Bitmap bitmap;
			Bitmap bitmap1;
			ImageView imagen;
			RelativeLayout rel;
			TextView categoriaNoticia;
			TextView fechaNoticia;
			TextView titulo;
			TextView resumen;
			TextView verMas;

			for(int i = 0;i<noticias.size(); i ++)
			{


				//Layout contenedor tanto de la parte izquierda como de la parte derecha
				layout = new LinearLayout(this);
				layout.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams parametros = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				parametros.setMargins(0, 15, 0, 0);
				layout.setLayoutParams(parametros);
				layout.setId(1);

				Noticia n = noticias.get(i);

				//Seccion de carga de la imagen
				bitmap = ObtenerImagen.darImagen(n.getThumbnail(), getApplicationContext());
				bitmap1 =Resize.resizeBitmap(bitmap, 198, 192);
				imagen = new ImageView(this);
				imagen.setImageBitmap(bitmap1);

				//Adicion de la parte izquierda
				layout.addView(imagen);



				rel = new RelativeLayout(this);
				rel.setBackgroundResource(R.drawable.fondo_noticias);
				RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				rel.setLayoutParams(relParams);



				categoriaNoticia = new TextView(this);
				categoriaNoticia.setGravity(Gravity.LEFT);
				categoriaNoticia.setTextColor(Color.parseColor("#D0D0D0"));
				categoriaNoticia.setText(n.getCategoria());
				categoriaNoticia.setTextSize(12);
				categoriaNoticia.setTypeface(null, Typeface.BOLD);
				categoriaNoticia.setPadding(5, 0, 0, 0);
				categoriaNoticia.setId(i+1);
				RelativeLayout.LayoutParams paramsCategoria = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsCategoria.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				categoriaNoticia.setLayoutParams(paramsCategoria);
				rel.addView(categoriaNoticia);


				fechaNoticia = new TextView(this);
				fechaNoticia.setGravity(Gravity.LEFT);
				fechaNoticia.setTextColor(Color.parseColor("#D0D0D0"));
				fechaNoticia.setText(n.getFechaCreacion());
				fechaNoticia.setTextSize(12);
				fechaNoticia.setPadding(5, 0, 0, 0);
				fechaNoticia.setId(i+2);
				RelativeLayout.LayoutParams paramsFecha = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsFecha.addRule(RelativeLayout.BELOW, categoriaNoticia.getId());
				paramsFecha.setMargins(0, 8, 0, 0);
				fechaNoticia.setLayoutParams(paramsFecha);
				rel.addView(fechaNoticia);

				Button b = new Button(this);
				b.setBackgroundColor(Color.TRANSPARENT);
				b.setId(i);
				b.setOnClickListener(this);
				RelativeLayout.LayoutParams paramsbutton = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,80);
				paramsbutton.addRule(RelativeLayout.BELOW,fechaNoticia.getId());
				b.setLayoutParams(paramsbutton);
				rel.addView(b);

				titulo = new TextView(this);
				titulo.setGravity(Gravity.LEFT);
				titulo.setTextColor(Color.parseColor("#D0D0D0"));
				titulo.setTypeface(null, Typeface.BOLD);
				titulo.setPadding(5, 0, 0, 0);
				titulo.setTextSize(12);
				titulo.setText(n.getTitulo());
				titulo.setId(i+3);
				RelativeLayout.LayoutParams paramsTitulo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsTitulo.addRule(RelativeLayout.BELOW, fechaNoticia.getId());
				titulo.setLayoutParams(paramsTitulo);
				rel.addView(titulo);


				resumen = new TextView(this);
				resumen.setGravity(Gravity.LEFT);
				resumen.setTextColor(Color.parseColor("#D0D0D0"));
				resumen.setTextSize(12);
				resumen.setPadding(5, 0, 15, 0);
				resumen.setText(n.getResumen());
				resumen.setId(i+4);
				RelativeLayout.LayoutParams paramsResumen = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsResumen.addRule(RelativeLayout.BELOW, titulo.getId());
				resumen.setLayoutParams(paramsResumen);
				rel.addView(resumen);




				verMas = new TextView(this);
				verMas.setGravity(Gravity.LEFT);
				verMas.setTextColor(Color.parseColor("#D0D0D0"));
				String base = "Ver m‡s ";
				String terminacion = null;
				switch (i) 
				{
				case 0:terminacion="noticias internaciones.";
				verMas.setTextSize(11);
				break;
				case 1:terminacion="noticias nacionales.";
				verMas.setTextSize(12);
				break;
				case 2:terminacion="promociones.";
				verMas.setTextSize(12);
				break;
				case 3:terminacion="novedades.";
				verMas.setTextSize(12);
				break;
				default:
					break;
				}
				verMas.setText(base + terminacion);
				RelativeLayout.LayoutParams paramsVer = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsVer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				verMas.setPadding(4, 0, 7, 7);
				verMas.setLayoutParams(paramsVer);
				verMas.setId((i+1)*10);
				verMas.setOnClickListener(this);

				rel.addView(verMas);
				layout.addView(rel,relParams);
				layoutPrincipal.addView(layout);
			}

		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 




	}

	protected void onDestroy() 
	{
		super.onDestroy();
		Log.d("Tutorial", "hizo el ondestroy de noticias inicio");
		//Manejo del objeto DataBaseHelper
		if(databaseHelper != null)
			OpenHelperManager.releaseHelper();
		databaseHelper = null;
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

	@Override
	public void onBackPressed() 
	{
		NoticiasInicio.grupoNoticias.back();
		return;
	}

	public void back()
	{


		if(historialViews.size()>0)
		{
			historialViews.remove(historialViews.size()-1);
			if(historialViews.size()>0)
				setContentView(historialViews.get(historialViews.size()-1));
			else
				onCreate(null);

		}
		else
		{
			finish();
		}

	}



	public void reemplazarView(View v)
	{
		historialViews.add(v);
		setContentView(v);
	}

	@Override
	public void onClick(View v) 
	{
		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo red  = conMgr.getActiveNetworkInfo();
		boolean conexionInternet = red!=null && red.getState() == NetworkInfo.State.CONNECTED;
		
		if(conexionInternet)
		{
			Intent iNoticia = new Intent(NoticiasInicio.this, NoticiasNoticia.class);
			Intent iCategoria = new Intent(NoticiasInicio.this, NoticiasCategorias.class);
			View v1;
			int id = v.getId();
			switch (id) 
			{
			case 0:

				iNoticia.putExtra(NOMBRE_CATEGORIA, CATEGORIA_INTERNACIONAL);
				iNoticia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iNoticia).getDecorView();
				reemplazarView(v1);
				break;
			case 1:
				iNoticia.putExtra(NOMBRE_CATEGORIA, CATEGORIA_NACIONAL);
				iNoticia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iNoticia).getDecorView();
				reemplazarView(v1);
				break;
			case 2:
				iNoticia.putExtra(NOMBRE_CATEGORIA, PROMOCION);
				iNoticia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iNoticia).getDecorView();
				reemplazarView(v1);
				break;
			case 3:
				iNoticia.putExtra(NOMBRE_CATEGORIA,NOVEDAD);
				iNoticia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iNoticia).getDecorView();
				reemplazarView(v1);
				break;
			case 10:
				iCategoria.putExtra(NOMBRE_CATEGORIA, 1);
				iCategoria.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iCategoria).getDecorView();
				reemplazarView(v1);
				break;
			case 20:
				iCategoria.putExtra(NOMBRE_CATEGORIA, 2);
				iCategoria.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iCategoria).getDecorView();
				reemplazarView(v1);
				break;
			case 30:
				iCategoria.putExtra(NOMBRE_CATEGORIA, 3);
				iCategoria.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iCategoria).getDecorView();
				reemplazarView(v1);
				break;
			case 40:
				iCategoria.putExtra(NOMBRE_CATEGORIA, 40);
				iCategoria.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				v1 = getLocalActivityManager().startActivity("", iCategoria).getDecorView();
				reemplazarView(v1);
				break;
			}
		}
		else
		{
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setMessage("Debes tener accesso a internet para esta secci—n de la aplicacion");
			alertBuilder.setCancelable(false);
			alertBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() 
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					onCreate(null);
				}
			});
			AlertDialog alerta = alertBuilder.create();
			alerta.show();
		}
		
		


	}
	
	public Context darContexto()
	{
		return getApplicationContext();
	}
	


	


}
