package com.mini_colombia.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.mini_colombia.values.Modelo;

public class DaoModelo 
{
	public static Modelo darModelo(Dao<Modelo, String> dao, String nombreModelo) throws SQLException
	{
		List<Modelo> l = dao.queryForEq("nombre", nombreModelo);
		return l.get(0);
	}
	
}