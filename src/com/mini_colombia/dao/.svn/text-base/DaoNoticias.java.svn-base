package com.mini_colombia.dao;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.mini_colombia.values.Noticia;

public class DaoNoticias 
{
	
	private static final String CATEGORIA = "categoria";
	
	private static final String RESUMEN = "resumen";
	
	private static final String PAGINA = "pagina";
	
	private static final String TITULO = "titulo";
	
	private static final String THUMBNAIL = "thumbnail";
	
	private static final String FECHA_CREACION = "fechaCreacion";
	
	private static final String URL = "url";
	
	public static Noticia darNoticia(String categoria, Dao<Noticia, Integer> dao) throws SQLException
	{
		List<Noticia> noticias = dao.queryForEq("categoria", categoria);
		return noticias.get(0);
	}
	
	public static void actualizarNoticia(String categoria, Dao<Noticia, Integer> dao, JSONObject noticia)
	{
		UpdateBuilder<Noticia, Integer> updateBuilder = dao.updateBuilder();
	}
}
