package com.mini_colombia.noticias;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini_colombia.InicioActivity;
import com.mini_colombia.R;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.DescargarImagenOnline;
import com.mini_colombia.servicios.Resize;
import com.mini_colombia.values.Noticia;

public class NoticiasCategorias extends Activity implements OnClickListener
{
	private static final String NOMBRE_CATEGORIA="categoria";

	private static final String NOTICIAS_INTERNACIONALES="NOTICIAS INTERNACIONALES.";

	private static final String NOTICIAS_NACIONALES="NOTICIAS NACIONALES.";

	private static final String PROMOCIONES="PROMOCIONES.";

	private static final String NOVEDADES="NOVEDADES.";

	private JSONObject objetoJson;

	private ArrayList<Bitmap> imagenes;
	
	private ArrayList<String> paginas;
	
	private static final String URL_PAGINA ="url";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noticias_categorias);
		int numCategoria = getIntent().getIntExtra(NOMBRE_CATEGORIA, 0);
		imagenes = new ArrayList<Bitmap>();
		paginas = new ArrayList<String>();
		objetoJson = null;

		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView textoTitulo = (TextView)findViewById(R.id.tituloInicioCategorias);
		textoTitulo.setTypeface(tipoMini);

		switch (numCategoria) 
		{
		case 1: textoTitulo.setText(NOTICIAS_INTERNACIONALES);
		break;
		case 2:textoTitulo.setText(NOTICIAS_NACIONALES);
		break;
		case 3:textoTitulo.setText(PROMOCIONES);
		break;
		case 4:textoTitulo.setText(NOVEDADES);
		break;
		}

		new DescargasJson().execute(numCategoria);



	}



	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	public Context darContexto()
	{
		Context context = null;
	    if (getParent() != null) 
	    	context = getParent();
	    return context;
	}

	private class DescargasJson extends AsyncTask<Integer, Integer, Boolean>
	{

		ProgressDialog progress;



		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Cargando...",false);
		}


		@Override
		protected Boolean doInBackground(Integer... params) 
		{
			Parser jparser = new Parser();
			JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_NOTICIAS_CATEGORIAS)+params[0]+"/");
			setObjetoJson(jsonObject);
			try 
			{

				JSONArray noticias = objetoJson.getJSONArray(getString(R.string.TAG_NOTICIAS));

				for(int i = 0; i< noticias.length(); i++)
				{
					JSONObject noticia = noticias.getJSONObject(i);

					String urlImagen = noticia.getString(getString(R.string.TAG_NOTICIAS_THUMBNAIL));
					Bitmap imagen1= DescargarImagenOnline.descargarImagen(urlImagen);
					Bitmap imagen2 = Resize.resizeBitmap(imagen1, 198, 192);
					imagenes.add(imagen2);
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}


			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss();
			pintarPantalla();
		}
	}

	private void setObjetoJson(JSONObject objeto)
	{
		objetoJson= objeto;
	}

	private void pintarPantalla()
	{
		LinearLayout layoutPrincipal = (LinearLayout)findViewById(R.id.linearLayoutNoticiasCategorias);


		JSONArray noticias;
		try 
		{
			noticias = objetoJson.getJSONArray(getString(R.string.TAG_NOTICIAS));
			int j = noticias.length();
			for(int i = 0; i<noticias.length();i++)
			{
				JSONObject noticia = noticias.getJSONObject(i);
				String resumen = noticia.getString(getString(R.string.TAG_NOTICIAS_RESUMEN));
				String titulo = noticia.getString(getString(R.string.TAG_NOTICIAS_TITULO));

				ImageView iv = new ImageView(darContexto());
				iv.setImageBitmap(imagenes.get(i));
				String fechaCreacion = noticia.getString(getString(R.string.TAG_NOTICIAS_FECHA_CREACION));
				String url = noticia.getString(getString(R.string.TAG_NOTICIAS_URL));
				paginas.add(url);

				//Layout contenedor tanto de la parte izquierda como de la parte derecha
				LinearLayout layout = new LinearLayout(darContexto());
				layout.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams parametros = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				parametros.setMargins(0, 15, 0, 0);
				layout.setLayoutParams(parametros);

				layout.addView(iv);

				RelativeLayout rel = new RelativeLayout(darContexto());
				rel.setBackgroundResource(R.drawable.fondo_noticias_categoria);
				RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				rel.setLayoutParams(relParams);

				TextView fecha = new TextView(darContexto());
				fecha.setText(fechaCreacion);
				fecha.setId(i+j+3);
				fecha.setGravity(Gravity.LEFT);
				fecha.setTextColor(Color.parseColor("#D0D0D0"));
				fecha.setTextSize(12);
				fecha.setPadding(7, 0, 0, 0);
				RelativeLayout.LayoutParams paramsFecha = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsFecha.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				paramsFecha.setMargins(0, 7, 0, 0);
				fecha.setLayoutParams(paramsFecha);
				rel.addView(fecha);
				
				Button b = new Button(this);
				b.setBackgroundColor(Color.TRANSPARENT);
				b.setId(i);
				b.setOnClickListener(this);
				RelativeLayout.LayoutParams paramsbutton = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,120);
				paramsbutton.addRule(RelativeLayout.BELOW,fecha.getId());
				paramsbutton.setMargins(0, 25, 0, 0);
				b.setLayoutParams(paramsbutton);
				rel.addView(b);

				TextView tituloNoticia = new TextView(darContexto());
				tituloNoticia.setText(titulo);
				tituloNoticia.setId(i+j+4);
				tituloNoticia.setGravity(Gravity.LEFT);
				tituloNoticia.setTextColor(Color.parseColor("#D0D0D0"));
				tituloNoticia.setTextSize(12);
				tituloNoticia.setTypeface(null,Typeface.BOLD);
				tituloNoticia.setPadding(7, 0, 0, 0);
				RelativeLayout.LayoutParams paramsTituloNoticia = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsTituloNoticia.addRule(RelativeLayout.BELOW, fecha.getId());
				paramsTituloNoticia.setMargins(0, 17, 0, 0);
				tituloNoticia.setLayoutParams(paramsTituloNoticia);
				rel.addView(tituloNoticia);

				TextView tResumen = new TextView(darContexto());
				tResumen.setText(resumen);
				tResumen.setId(i+5+j);
				tResumen.setGravity(Gravity.LEFT);
				tResumen.setTextColor(Color.parseColor("#D0D0D0"));
				tResumen.setTextSize(12);
				tResumen.setPadding(5, 0, 15, 0);
				RelativeLayout.LayoutParams paramsResumen = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				paramsResumen.addRule(RelativeLayout.BELOW, tituloNoticia.getId());
				tResumen.setLayoutParams(paramsResumen);
				rel.addView(tResumen);

				layout.addView(rel,relParams);
				layoutPrincipal.addView(layout);


			}
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public void onClick(View v) 
	{
		int i = v.getId();
		String pagina= paginas.get(i);
		Intent j = new Intent(NoticiasCategorias.this,NoticiasNoticia.class);
		j.putExtra(URL_PAGINA, pagina);
		j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = NoticiasInicio.grupoNoticias.getLocalActivityManager().startActivity("",j).getDecorView();
		NoticiasInicio actividadPadre = (NoticiasInicio)getParent();
		actividadPadre.reemplazarView(v1);
		
		
		
	}

}

