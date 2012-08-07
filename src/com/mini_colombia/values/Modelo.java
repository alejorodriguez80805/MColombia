package com.mini_colombia.values;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "modelo")
public class Modelo 
{
	
	//Atributos
	

	
	@DatabaseField
	private String imagen;
	
	@DatabaseField(id = true,index = true)
	private String nombre;
	
	@DatabaseField
	private String thumbnail;

	
	public Modelo()
	{
		//Metodo utilizado por el ORM
	}
	
	



	/**
	 * Constructor
	 * @param imagen
	 * @param nombre
	 * @param descripcion
	 */
	public Modelo(String imagen, String nombre, String thumbnail)
	{
		this.imagen = imagen;
		this.nombre = nombre;
		this.thumbnail = thumbnail;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getImagen() 
	{
		return imagen;
	}


	public void setImagen(String imagen) 
	{
		this.imagen = imagen;
	}


	public String getNombre() 
	{
		return nombre;
	}


	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getImagen());
		sb.append(getNombre());
		sb.append(getThumbnail());
		
		return sb.toString();
		
		
	}
	
	
	
}
