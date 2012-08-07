package com.mini_colombia.familia;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.mini_colombia.R;
import com.mini_colombia.db.DataBaseHelper;
import com.mini_colombia.servicios.ObtenerImagen;
import com.mini_colombia.servicios.Resize;
import com.mini_colombia.values.Edicion;
import com.mini_colombia.values.Modelo;

public class FamiliaModelos extends Activity implements OnClickListener
{
	private static final String MODELO = "modelo";
	
	private static final String EDICION = "edicion";

	private DataBaseHelper databaseHelper;

	private String modelo;

	private Dao<Modelo, String> daoModelo;

	private Dao<Edicion, String> daoEdicion;
	
	private String nombreModelo;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia_modelos);

		modelo = getIntent().getStringExtra(MODELO);
		daoModelo = darDaoModelo();
		daoEdicion = darDaoEdicion();

		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");

		try 
		{
			List<Modelo> modelos = daoModelo.queryForAll();
			Modelo m = modelos.get(Integer.parseInt(modelo));
			nombreModelo=m.getNombre();

			//Creacion del titulo
			TextView textoTitulo = (TextView)findViewById(R.id.TituloModelo);
			textoTitulo.setTypeface(tipoMini);
			textoTitulo.setText(m.getNombre());

			ImageView iv = new ImageView(this);
			HorizontalScrollView horScroll = (HorizontalScrollView)findViewById(R.id.horizontalScrollModelos);
			horScroll.setHorizontalFadingEdgeEnabled(false);
			horScroll.setHorizontalScrollBarEnabled(false);
			
			//Seccion Carga Imagen
			String nombreImagen = m.getImagen();
			Bitmap bitmap = ObtenerImagen.darImagen(nombreImagen, getApplicationContext());
			//Bitmap bitmapEscala= Resize.resizeBitmap(bitmap, 600,899 );
			iv.setImageBitmap(bitmap);
			horScroll.addView(iv);


			//Metodos para hacer la consulta de las ediciones asociadas al modelos actual
			QueryBuilder<Edicion,String> queryBuilder = daoEdicion.queryBuilder();
			Where<Edicion, String> where = queryBuilder.where();
			SelectArg argumentosSeleccion = new SelectArg();
			where.eq("modelo_id", argumentosSeleccion);
			PreparedQuery<Edicion> query = queryBuilder.prepare();
			argumentosSeleccion.setValue(m);
			List<Edicion> ediciones = daoEdicion.query(query);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layoutDerModelos);
			
			ImageButton ib;
			for(int i = 0; i<ediciones.size(); i++)
			{
				ib = new ImageButton(this);
				Edicion e = ediciones.get(i);
				String nombreImagenThumbnail = e.getThumbnail();

				
				bitmap = ObtenerImagen.darImagen(nombreImagenThumbnail, getApplicationContext());
				Bitmap bitmapEscala= Resize.resizeBitmap(bitmap, 147, 220);
				ib.setImageBitmap(bitmapEscala);
				ib.setBackgroundColor(Color.BLACK);
				ib.setOnClickListener(this);
				ib.setId(i);
				
				layout.addView(ib);
			}





		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 










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

	public Dao<Edicion, String> darDaoEdicion()
	{

		Dao<Edicion, String> daoEdicion = null;
		try 
		{
			daoEdicion = getHelper().darDaoEdicion();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return daoEdicion;
	}


	///////////////////////////////
	//Metodos de la actividad
	///////////////////////////////
	
	
	@Override
	public void onClick(View v) 
	{
		String id = String.valueOf(v.getId());
		Intent i = new Intent(FamiliaModelos.this, FamiliaEdicion.class);
		i.putExtra(EDICION, id);
		i.putExtra(MODELO, nombreModelo);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = FamiliaInicio.grupoFamilia.getLocalActivityManager().startActivity("", i).getDecorView();
		FamiliaInicio actividadPadre =(FamiliaInicio) getParent(); 
		actividadPadre.reemplazarView(v1);
		
	}
	
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
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
	

}
