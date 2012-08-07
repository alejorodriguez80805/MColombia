package com.mini_colombia.servicios;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Resize 
{
	
	public static Bitmap resizeBitmap(Bitmap bitOriginal, int nuevaAltura, int nuevaAnchura)
	{
		int anchoOriginal = bitOriginal.getWidth();
		
		int altoOriginal = bitOriginal.getHeight();
		
		float factorEscalaAltura = (float)nuevaAltura/altoOriginal;
		
		float factorEscalaAncho = (float)nuevaAnchura /anchoOriginal;
		
		Matrix matriz = new Matrix();
		
		matriz.postScale(factorEscalaAncho, factorEscalaAltura);
		
		Bitmap nuevoBitmap = Bitmap.createScaledBitmap(bitOriginal, nuevaAnchura, nuevaAltura, true);
		return nuevoBitmap;
	}
	

}
