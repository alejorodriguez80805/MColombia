package com.mini_colombia.auxiliares;

import android.graphics.Bitmap;

public class ImagenGaleria 
{
	String nombre;

	Bitmap thumbnail;

	Bitmap imagen;

	public ImagenGaleria(String nombre, Bitmap thumbnail, Bitmap imagen)
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

	public Bitmap getImagen() {
		return imagen;
	}

	public void setImagen(Bitmap imagen) {
		this.imagen = imagen;
	}

}