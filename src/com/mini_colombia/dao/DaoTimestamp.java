package com.mini_colombia.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.mini_colombia.values.Timestamp;

public class DaoTimestamp 
{
	public static void primeraVez(Dao<Timestamp, Integer> dao, BigDecimal timeStamp,String identificador) throws SQLException
	{
		Timestamp t = new Timestamp(timeStamp, identificador);
		dao.create(t);
	}
	
	public static BigDecimal darTimestamp(Dao<Timestamp, Integer> dao, String identificador) throws SQLException
	{
		List<Timestamp> t = dao.queryForEq("identificador", identificador);
		return t.get(0).getTimestamp();
	}
	
	public static void updateTimestamp(Dao<Timestamp, Integer> dao, BigDecimal timestamp)
	{
		UpdateBuilder<Timestamp, Integer> updateBuilder = dao.updateBuilder();
		try 
		{
			updateBuilder.updateColumnValue("timestamp", timestamp);
			updateBuilder.where().eq("identificador", "nombre");
			dao.update(updateBuilder.prepare());
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
