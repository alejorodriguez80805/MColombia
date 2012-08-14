package com.mini_colombia.auxiliares;

import android.graphics.Bitmap;

public class ImagenGaleria 
{
	String nombre;

	Bitmap thumbnail;

	String imagen;

	public ImagenGaleria(String nombre, Bitmap thumbnail, String imagen)
	{
		this.nombre = nombre;
		this.thumbnail = thumbnail;
		this.imagen = imagen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}