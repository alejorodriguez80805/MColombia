package com.mini_colombia.values;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "edicion")
public class Edicion 
{
	
	//Atributos
	
	
	@DatabaseField
	private String imagen;
	
	@DatabaseField(id = true,index = true)
	private String nombre;
	
	@DatabaseField(dataType = DataType.LONG_STRING)
	private String descripcion;
	
	@DatabaseField
	private String thumbnail;
	
	@DatabaseField
	private boolean test_drive;
	
	@DatabaseField(foreign = true)
	private Modelo modelo;
	
	@DatabaseField
	private String templateColor;
	
	public Edicion()
	{
		//Metodo utilizado por el ORM
	}
	
	public Edicion(String imagen, String nombre, String descripcion, String thumbnail, boolean test_drive, Modelo modelo, String templateColor)
	{
		this.imagen = imagen;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.thumbnail = thumbnail;
		this.test_drive = test_drive;
		this.modelo = modelo;
		this.templateColor = templateColor;
	}

	public String getTemplateColor() {
		return templateColor;
	}

	public void setTemplateColor(String templateColor) {
		this.templateColor = templateColor;
	}

	public Modelo getModelo() 
	{
		return modelo;
	}

	public void setModelo(Modelo modelo) 
	{
		this.modelo = modelo;
	}

	public String getImagen() 
	{
		return imagen;
	}

	public void setImagen(String imagen) 
	{
		this.imagen = imagen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

	public String getDescripcion() 
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion) 
	{
		this.descripcion = descripcion;
	}

	public String getThumbnail() 
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) 
	{
		this.thumbnail = thumbnail;
	}

	public boolean isTest_drive() 
	{
		return test_drive;
	}

	public void setTest_drive(boolean test_drive) 
	{
		this.test_drive = test_drive;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getImagen());
		sb.append(getNombre());
		sb.append(getDescripcion());
		sb.append(getThumbnail());
		sb.append(isTest_drive());
		sb.append(getModelo().getNombre());
		sb.append(getTemplateColor());
		return sb.toString();
	}
	
	
}
