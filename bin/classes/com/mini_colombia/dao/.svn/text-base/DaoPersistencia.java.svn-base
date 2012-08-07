package com.mini_colombia.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.mini_colombia.values.Persistencia;




/**
 * Clase que se encarga de las transacciones de la tabla Persistencia.
 * @author Usuario
 *
 */
public class DaoPersistencia
{
	
	
	public static boolean hayPersistencia(Dao<Persistencia, Integer> dao) throws SQLException
	{
		List<Persistencia> lista = dao.queryForAll();
		return lista.size()==0?false:true;
	}
	
	public static void primeraVez(Dao<Persistencia, Integer> dao) throws SQLException
	{
		Persistencia persistencia = new Persistencia(true);
		dao.create(persistencia);
	}
	
}