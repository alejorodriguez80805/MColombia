package com.mini_colombia.values;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "noticia")
public class Noticia 
{
	
	@DatabaseField(generatedId=true)
	private int id;
	
	@DatabaseField
	private String categoria;
	
	@DatabaseField
	private String resumen;
	
	@DatabaseField(dataType = DataType.LONG_STRING)
	private String pagina;
	
	@DatabaseField(index=true)
	private String titulo;
	
	@DatabaseField(dataType = DataType.LONG_STRING)
	private String thumbnail;
	
	@DatabaseField
	private String fechaCreacion;
	
	@DatabaseField(dataType = DataType.LONG_STRING)
	private String url;
	
	public Noticia()
	{
		
	}
	
	public Noticia(String categoria, String resumen, String pagina, String titulo, String thumbnail, String fechaCreacion,String url)
	{
		this.categoria = categoria;
		this.resumen = resumen;
		this.pagina = pagina;
		this.titulo = titulo;
		this.thumbnail = thumbnail;
		this.fechaCreacion = fechaCreacion;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	
}
