package com.mini_colombia;
import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;


public class ManejadorTabs extends TabActivity
{
	private static TabHost tabHost;
	
	private int tabInicial;

	public static ArrayList<View> tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tabs = new ArrayList<View>();
		tabInicial = getIntent().getIntExtra("tab", 0);


		//Objetos necesarios para crear cada uno de los tabs
		Resources res = getResources(); 
		tabHost = getTabHost();  
		TabHost.TabSpec spec;  
		Intent intent; 


		intent = new Intent().setClass(this, com.mini_colombia.familia.FamiliaInicio.class);
		spec = tabHost.newTabSpec("").setIndicator("Familia",res.getDrawable(R.drawable.icono_familia_tab)).setContent(intent);
		tabHost.addTab(spec);


		intent = new Intent().setClass(this, com.mini_colombia.comunidad.ComunidadInicio.class);
		spec = tabHost.newTabSpec("").setIndicator("Comunidad",res.getDrawable(R.drawable.icono_comunidad_tab)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, com.mini_colombia.noticias.NoticiasInicio.class);
		spec = tabHost.newTabSpec("").setIndicator("Noticias",res.getDrawable(R.drawable.icono_noticias_tab)).setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, com.mini_colombia.vitrinas.VitrinasInicio.class);
		spec = tabHost.newTabSpec("").setIndicator("Vitrinas",res.getDrawable(R.drawable.icono_vitrinas_tab)).setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, com.mini_colombia.descargas.DescargasInicio.class);
		spec = tabHost.newTabSpec("").setIndicator("Descargas",res.getDrawable(R.drawable.icono_descargas_tab)).setContent(intent);
		tabHost.addTab(spec);
		


		for(int i = 0; i < tabHost.getChildCount(); i++)
		{
			tabs.add(tabHost.getTabWidget().getChildAt(i));

		}


		tabHost.setOnTabChangedListener(new OnTabChangeListener()
		{
			@Override
			public void onTabChanged(String tabId) 
			{
				setSelectedTabColor(tabHost);

			}
		});
		setSelectedTabColor(tabHost);
		tabHost.setCurrentTab(tabInicial);

	}




	private void setSelectedTabColor(TabHost tabHost) 
	{
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)  
		{  
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.BLACK); 
		}  
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.DKGRAY); 
	}

	public static void cambiarEstadoTab(boolean estado,int indice)
	{


		tabHost.getTabWidget().getChildAt(indice).setEnabled(estado);

	}


}
