package com.mini_colombia.db;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mini_colombia.R;
import com.mini_colombia.values.Edicion;
import com.mini_colombia.values.Modelo;
import com.mini_colombia.values.Noticia;
import com.mini_colombia.values.Persistencia;
import com.mini_colombia.values.Timestamp;


public class DataBaseHelper extends OrmLiteSqliteOpenHelper 
{

	private static final String DATABASE_NAME = "android.db";
	
	private static final int DATABASE_VERSION = 1;
	
	private Dao<Modelo, String> modeloDao = null;
	
	private Dao<Edicion, String> edicionDao = null;
	
	
	private Dao<Persistencia, Integer> persistenciaDao = null;
	
	private Dao<Timestamp, Integer> timestampDao = null;
	
	private Dao<Noticia, Integer> noticiasDao = null;
	
	
	

	
	public DataBaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION,R.raw.ormlite_config);
	}
	
	
	////////////////////////////////////////////////////
	//    Metodos necesarios de la clase DatabaseHelper
	///////////////////////////////////////////////////
	
	
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) 
	{
		try 
		{
			Log.i(DataBaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Modelo.class);
			TableUtils.createTable(connectionSource, Edicion.class);
			TableUtils.createTable(connectionSource, Persistencia.class);
			TableUtils.createTable(connectionSource, Timestamp.class);
			TableUtils.createTable(connectionSource, Noticia.class);
		} 
		catch (SQLException e) 
		{
			Log.e(DataBaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
		
	}
	
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try 
		{
			Log.i(DataBaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Modelo.class, true);
			TableUtils.dropTable(connectionSource, Edicion.class, true);
			TableUtils.dropTable(connectionSource, Persistencia.class, true);
			TableUtils.dropTable(connectionSource, Timestamp.class, true);
			TableUtils.dropTable(connectionSource, Noticia.class, true);
			onCreate(db, connectionSource);
		} 
		catch (SQLException e) 
		{
			Log.e(DataBaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////
	// Creacion de los elemento para acceder a cada uno de los objetos
	////////////////////////////////////////////////////////////////////
	
	public Dao<Persistencia, Integer> darDaoPersistencia() throws SQLException
	{
		if(persistenciaDao == null)
			persistenciaDao = getDao(Persistencia.class);
		return persistenciaDao;
	}
	
	public Dao<Modelo, String> darDaoModelo() throws SQLException
	{
		if(modeloDao == null)
			modeloDao = getDao(Modelo.class);
		return modeloDao;
	}
	
	public Dao<Edicion, String> darDaoEdicion() throws SQLException
	{
		if(edicionDao == null)
			edicionDao = getDao(Edicion.class);
		return edicionDao;
	}
	
	public Dao<Timestamp,Integer> darDaoTimestamp() throws SQLException 
	{
		if(timestampDao == null)
			timestampDao = getDao(Timestamp.class);
		return timestampDao;
	}
	
	public Dao<Noticia, Integer> darDaoNoticia() throws SQLException
	{
		if(noticiasDao == null)
			noticiasDao = getDao(Noticia.class);
		return noticiasDao;
	}
	

	
	
	
	public void close()
	{
		super.close();
		modeloDao = null;
		edicionDao = null;
		persistenciaDao = null;
		timestampDao = null;
		noticiasDao = null;
	}
	

	
	
	
	
	
	
	
}
