package com.mini_colombia.familia;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.mini_colombia.R;
import com.mini_colombia.db.DataBaseHelper;
import com.mini_colombia.servicios.ObtenerImagen;
import com.mini_colombia.servicios.Resize;
import com.mini_colombia.values.Modelo;

public class FamiliaInicio  extends ActivityGroup implements OnClickListener
{
	private static final String MODELO = "modelo";

	private DataBaseHelper databaseHelper = null;

	private Dao<Modelo, String> daoModelo;
	
	public static FamiliaInicio grupoFamilia;
	
	public ArrayList<View> historialViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia_inicio);
		this.grupoFamilia = this;
		this.historialViews = new ArrayList<View>();


		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView textoTitulo = (TextView)findViewById(R.id.TextViewInicioFamilia);
		textoTitulo.setTypeface(tipoMini);
		textoTitulo.setText(R.string.Mini);


		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutFamilia);
		daoModelo = darDaoModelo();

		try 
		{
			List<Modelo> modelos = daoModelo.queryForAll();
			Modelo m;
			for(int i = 0; i<modelos.size(); i ++)
			{
				m = modelos.get(i);
				ImageButton ib = new ImageButton(this);
				LayoutParams parametros = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				String nombreImagen = m.getThumbnail();


					Bitmap bitmap = ObtenerImagen.darImagen(nombreImagen, getApplicationContext());
					Bitmap bitmapEscala= Resize.resizeBitmap(bitmap, 195, 451);
					ib.setLayoutParams(parametros);
					ib.setImageBitmap(bitmapEscala);
					ib.setBackgroundColor(Color.BLACK);
					ib.setOnClickListener(this);
					ib.setId(i);
					layout.addView(ib);


			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}


	

	
	@Override
	public void onBackPressed() 
	{
		FamiliaInicio.grupoFamilia.back();
		return;
	}
	


	@Override
	public void onClick(View v) 
	{
		String id = String.valueOf(v.getId());
		Intent i = new Intent(FamiliaInicio.this, FamiliaModelos.class);
		i.putExtra(MODELO, id);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		reemplazarView("1", i);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
	
	public void reemplazarView(View v)
	{
		historialViews.add(v);
		setContentView(v);
	}
	
	public void back()
	{
		if(historialViews.size()>0)
		{
			
			historialViews.remove(historialViews.size() -1);
			if(historialViews.size()>0)
			{
				setContentView(historialViews.get(historialViews.size()-1));
			}
			else
				onCreate(null);
		}

		else
		{

			finish();
		}

	}


	@Override
	protected void onDestroy() 
	{
		super.onDestroy();

		//Manejo del objeto DataBaseHelper
		if(databaseHelper != null)
			OpenHelperManager.releaseHelper();
		databaseHelper = null;
	}


	///////////////////////////////
	//Metodos de la base de datos
	///////////////////////////////

	/**
	 * Metodo necesario para la creacion de un objeto DAO para acceder a cada uno de las tablas
	 * @return
	 */
	public DataBaseHelper getHelper()
	{
		if(databaseHelper == null)
			databaseHelper = OpenHelperManager.getHelper(this,DataBaseHelper.class);
		return databaseHelper;
	}


	public Dao<Modelo,String> darDaoModelo()
	{
		Dao<Modelo, String> daoModelo = null;

		try 
		{
			daoModelo = getHelper().darDaoModelo();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return daoModelo;

	}
	
	public Context darContexto()
	{
		return this;
	}
	


}
