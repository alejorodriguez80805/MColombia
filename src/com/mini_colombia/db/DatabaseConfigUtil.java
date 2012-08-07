package com.mini_colombia.db;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.mini_colombia.values.Edicion;
import com.mini_colombia.values.Modelo;
import com.mini_colombia.values.Noticia;
import com.mini_colombia.values.Persistencia;
import com.mini_colombia.values.Timestamp;


public class DatabaseConfigUtil extends OrmLiteConfigUtil
{
	
	private static final Class<?>[] classes = new Class[] {
	    Modelo.class, Edicion.class, Persistencia.class, Timestamp.class, Noticia.class
	  };
	
	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt",classes);
	}
}