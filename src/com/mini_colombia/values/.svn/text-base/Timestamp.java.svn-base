package com.mini_colombia.values;

import java.math.BigDecimal;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "timestamp")
public class Timestamp 
{
	@DatabaseField(dataType = DataType.BIG_DECIMAL)
	private BigDecimal timestamp;
	
	@DatabaseField
	private String identificador;
	
	public Timestamp()
	{
		
	}
	
	public Timestamp(BigDecimal timestamp, String identificador)
	{
		this.timestamp = timestamp;
		this.identificador = identificador;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public BigDecimal getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BigDecimal timestamp) {
		this.timestamp = timestamp;
	}


	
	
}
