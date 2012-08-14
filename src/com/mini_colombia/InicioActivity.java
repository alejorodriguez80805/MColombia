package com.mini_colombia;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.mini_colombia.dao.DaoModelo;
import com.mini_colombia.dao.DaoPersistencia;
import com.mini_colombia.dao.DaoTimestamp;
import com.mini_colombia.db.DataBaseHelper;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.values.Edicion;
import com.mini_colombia.values.Modelo;
import com.mini_colombia.values.Noticia;
import com.mini_colombia.values.Persistencia;
import com.mini_colombia.values.Timestamp;


public class InicioActivity extends Activity 
{
	private DataBaseHelper databaseHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);


		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo red  = conMgr.getActiveNetworkInfo();
		boolean conexionInternet = red!=null && red.getState() == NetworkInfo.State.CONNECTED;


		try 
		{
			Dao<Persistencia, Integer> dao = getHelper().darDaoPersistencia();
			if(!DaoPersistencia.hayPersistencia(dao) && conexionInternet)
			{
				DaoPersistencia.primeraVez(dao);
				new FetchInfoInicialFamilia().execute("");

			}
			else if(!DaoPersistencia.hayPersistencia(dao) && !conexionInternet)
			{
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
				alertBuilder.setMessage("Debes tener accesso a internet la primera vez que ejecutas la aplicacion");
				alertBuilder.setCancelable(false);
				alertBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() 
				{

					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						finish();
						
						
					}
				});
				AlertDialog alerta = alertBuilder.create();
				alerta.show();

			}
			else if(conexionInternet)
			{
//				new ActualizarFamilia().execute("");
//				new ActualizarNoticias().execute("");
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//////////////////////////////////////////////////////////////////
	//Metodos de carga inicial de datos
	//////////////////////////////////////////////////////////////////
	
	private class FetchInfoInicialFamilia extends AsyncTask<String, Integer, Boolean>
	{

		private JSONArray modelos=null;

		private JSONArray ediciones=null;

		private Dao<Modelo, String> daoModelo;

		private Dao<Edicion, String>  daoEdicion;

		private Dao<Timestamp, Integer> daoTimestamp;


		ProgressDialog progress;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Carga inicial de familia...",false);
		}



		@Override
		protected Boolean doInBackground(String... params) 
		{



			daoEdicion = darDaoEdicion();
			daoModelo = darDaoModelo();
			daoTimestamp = darDaoTimestamp();

			Parser jparser = new Parser();
			JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_CARGAR_MODELO_EDICION_URL));




			Modelo modelo;

			try 
			{
				String timeStamp = jsonObject.getString(getString(R.string.TAG_TIMESTAMP));
				BigDecimal fTimestamp =new BigDecimal(timeStamp);

				DaoTimestamp.primeraVez(daoTimestamp, fTimestamp,"familia");

				modelos = jsonObject.getJSONArray(getString(R.string.TAG_MODELO));

				for(int i = 0 ; i < modelos.length(); i++)
				{
					JSONObject object_modelo = modelos.getJSONObject(i);

					String imagen_modelo = object_modelo.getString(getString(R.string.TAG_IMAGEN_MODELO));
					String nombre_modelo = object_modelo.getString(getString(R.string.TAG_NOMBRE_MODELO));
					String thumbnail_modelo = object_modelo.getString(getString(R.string.TAG_THUMBNAIL_MODELO));

					//Descarga de imagenes
					String nombre_imagen =getString(R.string.CONSTANTE_NOMBRE_IMAGEN_MODELO) + nombre_modelo;
					descargarImagen(imagen_modelo, nombre_imagen);

					String nombreImagenThumbnail = getString(R.string.CONSTANTE_THUMBNAIL_MODELO) +nombre_modelo;
					descargarImagen(thumbnail_modelo, nombreImagenThumbnail);

					//Creacion y almacenamiento del objeto modelo
					modelo = new Modelo(nombre_imagen,nombre_modelo,nombreImagenThumbnail);
					daoModelo.create(modelo);

					Edicion edicion;

					ediciones = object_modelo.getJSONArray(getString(R.string.TAG_EDICIONES));

					for(int j = 0; j< ediciones.length(); j ++)
					{
						JSONObject object_edicion= ediciones.getJSONObject(j);

						String nombre_edicion = object_edicion.getString(getString(R.string.TAG_NOMBRE_EDICION));

						//Manejo Imagen
						String imagen_edicion =  object_edicion.getString(getString(R.string.TAG_IMAGEN_EDICION));
						String nombreImagen = getString(R.string.CONSTANTE_NOMBRE_IMAGEN_EDICION) + nombre_edicion;
						descargarImagen(imagen_edicion, nombreImagen);

						String descripcion_edicion = object_edicion.getString(getString(R.string.TAG_DESCRIPCION_EDICION));

						//Manejo imagen thumbnail
						String imagen_thumbnail_edicion = object_edicion.getString(getString(R.string.TAG_IMAGEN_THUMBNAIL_EDICION));
						String nombre_thumbnail = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_EDICION) + nombre_edicion;
						descargarImagen(imagen_thumbnail_edicion, nombre_thumbnail);
						boolean test_drive_edicion = Boolean.parseBoolean(object_edicion.getString(getString(R.string.TAG_TEST_DRIVE_EDICION)));
						String templateColor = object_edicion.getString(getString(R.string.TAG_TEMPLATE_EDICION));

						//Creacion y almacenamiento del objeto edicion
						edicion = new Edicion(nombreImagen,nombre_edicion,descripcion_edicion,nombre_thumbnail,test_drive_edicion,modelo,templateColor);
						daoEdicion.create(edicion);
					}


				}
			} 
			catch (JSONException e) 
			{
				Toast t = Toast.makeText(darContexto(), "Se perdio la conexion a internet. Reinicia la aplicaci—n", Toast.LENGTH_LONG);
				t.show();
				finish();
				e.printStackTrace();
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss();

			new FetchInicialNoticias().execute("");
		}
	}
	
	private class FetchInicialNoticias extends AsyncTask<String, Integer, Boolean>
	{
		private Dao<Noticia, Integer> daoNoticia;
		
		private Dao<Timestamp, Integer> daoTimestamp;

		ProgressDialog progress;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Carga inicial de noticias...",false);
		}

		@Override
		protected Boolean doInBackground(String... params) 
		{
			daoNoticia = darDaoNoticia();
			daoTimestamp = darDaoTimestamp();
			Parser jparser = new Parser();
			String s = (getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL))+"/0";
			JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL)+"0");


			try 
			{
				String timeStamp = jsonObject.getString(getString(R.string.TAG_TIMESTAMP));
				BigDecimal fTimestamp =new BigDecimal(timeStamp);
				DaoTimestamp.primeraVez(daoTimestamp, fTimestamp,"noticias");
				
				JSONArray noticias = jsonObject.getJSONArray(getString(R.string.TAG_NOTICIAS));

				for(int i = 0; i<noticias.length();i++)
				{
					JSONObject noticia = noticias.getJSONObject(i);
					String categoria = noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA));
					String resumen = noticia.getString(getString(R.string.TAG_NOTICIAS_RESUMEN));
					String pagina = noticia.getString(getString(R.string.TAG_NOTICIAS_PAGINA));
					String titulo = noticia.getString(getString(R.string.TAG_NOTICIAS_TITULO));

					//Manejo imagen thumbnail
					String thumbnail = noticia.getString(getString(R.string.TAG_NOTICIAS_THUMBNAIL));
					String nombreImagenThumbnail = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_NOTICIA)+categoria;
					descargarImagen(thumbnail, nombreImagenThumbnail);
					String fechaCreacion = noticia.getString(getString(R.string.TAG_NOTICIAS_FECHA_CREACION));
					String url = noticia.getString(getString(R.string.TAG_NOTICIAS_URL));

					Noticia tNoticia = new Noticia(categoria,resumen,pagina,titulo,nombreImagenThumbnail,fechaCreacion,url);

					daoNoticia.create(tNoticia);

				}

				List<Noticia> n= daoNoticia.queryForAll();
				for(Noticia n1: n)
				{
					Log.d("asfasdfadf", n1.getTitulo());
				}

			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss(); 

		}

	}
	
	//////////////////////////////////////////////////////////////////
	//Metodos de actualizacion
	//////////////////////////////////////////////////////////////////
	
	private class ActualizarFamilia extends AsyncTask<String,Integer , Boolean>
	{
		private JSONObject actualizacion = null;

		private Dao<Modelo, String> daoModelo;

		private Dao<Edicion, String>  daoEdicion;

		private Dao<Timestamp, Integer> daoTimestamp;

		private boolean hayActualizaciones;

		private String timestamp;

		ProgressDialog progress;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Actualizando familia",false);
		}



		@Override
		protected Boolean doInBackground(String... params) 
		{
			daoEdicion = darDaoEdicion();
			daoModelo = darDaoModelo();
			daoTimestamp = darDaoTimestamp();


			Parser jparser = new Parser();
			try 
			{
				JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_ACTUALIZAR_MODELO_EDICION_URL)+DaoTimestamp.darTimestamp(daoTimestamp,"familia")+"/");

				hayActualizaciones = jsonObject.getBoolean(getString(R.string.TAG_HAY_ACTUALIZACIONES));
				if(hayActualizaciones)
				{
					timestamp = jsonObject.getString(getString(R.string.TAG_TIMESTAMP));
					BigDecimal bTimestamp = new BigDecimal(timestamp);
					DaoTimestamp.updateTimestamp(daoTimestamp, bTimestamp);


					JSONArray actualizaciones = jsonObject.getJSONArray(getString(R.string.TAG_ACTUALIZACIONES));

					JSONObject modelo;

					JSONObject edicion;

					for(int i=0; i<actualizaciones.length(); i ++)
					{
						actualizacion = actualizaciones.getJSONObject(i);

						//Operaciones para actualizar los modelos y las ediciones
						if(actualizacion.get(getString(R.string.TAG_ACCION)).equals(getString(R.string.TAG_ACTUALIZAR)))
						{
							if(actualizacion.get(getString(R.string.TAG_TIPO)).equals("modelo"))
							{



								modelo = actualizacion.getJSONObject("modelo");

								UpdateBuilder<Modelo, String> updateBuilder = daoModelo.updateBuilder();
								updateBuilder.updateColumnValue("nombre", modelo.getString(getString(R.string.TAG_NOMBRE_MODELO)));

								//Manejo de la imagen
								String nombreImagenAEliminar = getString(R.string.CONSTANTE_NOMBRE_IMAGEN_MODELO) + actualizacion.getString(getString(R.string.TAG_NOMBRE_MODELO));
								eliminarImagen(nombreImagenAEliminar);
								String url = modelo.getString(getString(R.string.TAG_IMAGEN_MODELO));
								String nombreImagen = getString(R.string.CONSTANTE_NOMBRE_IMAGEN_MODELO)+ modelo.getString(getString(R.string.TAG_NOMBRE_MODELO));
								descargarImagen(url, nombreImagen);

								//Manejo thumbnail
								String nombreThumbnailAEliminar = getString(R.string.CONSTANTE_THUMBNAIL_MODELO)+ actualizacion.getString(getString(R.string.TAG_NOMBRE_MODELO));
								eliminarImagen(nombreThumbnailAEliminar);
								String urlThumbnail = modelo.getString(getString(R.string.TAG_THUMBNAIL_MODELO));
								String nombreThumbnail = getString(R.string.CONSTANTE_THUMBNAIL_MODELO) + modelo.getString(getString(R.string.TAG_NOMBRE_MODELO));
								descargarImagen(urlThumbnail, nombreThumbnail);

								//Continuacion del update
								updateBuilder.updateColumnValue("imagen", nombreImagen);
								updateBuilder.updateColumnValue("thumbnail", nombreThumbnail);
								updateBuilder.where().eq("nombre", actualizacion.getString(getString(R.string.TAG_NOMBRE_MODELO)));
								daoModelo.update(updateBuilder.prepare());
							}
							else 
							{
								edicion = actualizacion.getJSONObject("edicion");

								UpdateBuilder<Edicion, String> updateBuilder = daoEdicion.updateBuilder();

								//Manejo de la imagen
								String nombreImagenAeliminar = getString(R.string.CONSTANTE_NOMBRE_IMAGEN_EDICION) + actualizacion.getString(getString(R.string.TAG_NOMBRE_EDICION));
								eliminarImagen(nombreImagenAeliminar);
								String urlImagen = edicion.getString(getString(R.string.TAG_IMAGEN_EDICION));
								String nombreImagen = getString(R.string.CONSTANTE_NOMBRE_IMAGEN_EDICION) + edicion.getString(getString(R.string.TAG_NOMBRE_EDICION));
								descargarImagen(urlImagen, nombreImagen);

								updateBuilder.updateColumnValue("imagen", nombreImagen);
								updateBuilder.updateColumnValue("test_drive",Boolean.parseBoolean(edicion.getString(getString(R.string.TAG_TEST_DRIVE_EDICION))));
								updateBuilder.updateColumnValue("descripcion", edicion.getString(getString(R.string.TAG_DESCRIPCION_EDICION)));
								updateBuilder.updateColumnValue("nombre", edicion.getString(getString(R.string.TAG_NOMBRE_EDICION)));
								updateBuilder.updateColumnValue("templateColor", edicion.getString(getString(R.string.TAG_TEMPLATE_EDICION)));

								//Manejo de la imagen del thumbnial
								String nombreThumbEliminar = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_EDICION) + actualizacion.getString(getString(R.string.TAG_NOMBRE_EDICION));
								eliminarImagen(nombreThumbEliminar);
								String urlThumbnail = edicion.getString(getString(R.string.TAG_IMAGEN_THUMBNAIL_EDICION));
								String nombreThumbnail = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_EDICION) + edicion.getString(getString(R.string.TAG_NOMBRE_EDICION));
								descargarImagen(urlThumbnail, nombreThumbnail);

								updateBuilder.updateColumnValue("thumbnail", nombreThumbnail);
								updateBuilder.where().eq("nombre", actualizacion.getString(getString(R.string.TAG_NOMBRE_EDICION)));
								daoEdicion.update(updateBuilder.prepare());


							}
						}

						//Operaciones para borrar modelos y ediciones
						else if(actualizacion.get(getString(R.string.TAG_ACCION)).equals(getString(R.string.TAG_ELIMINAR)))
						{
							if(actualizacion.get(getString(R.string.TAG_TIPO)).equals("modelo"))
							{
								DeleteBuilder<Modelo, String> deleteBuilder = daoModelo.deleteBuilder();
								deleteBuilder.where().eq("nombre", actualizacion.get(getString(R.string.TAG_NOMBRE_MODELO)));

								daoModelo.delete(deleteBuilder.prepare());
							}
							else
							{
								DeleteBuilder<Edicion, String> deleteBuilder = daoEdicion.deleteBuilder();
								deleteBuilder.where().eq("nombre",actualizacion.get(getString(R.string.TAG_NOMBRE_EDICION)));
								daoEdicion.delete(deleteBuilder.prepare());
							}

						}
						else if(actualizacion.get(getString(R.string.TAG_ACCION)).equals(getString(R.string.TAG_CREAR)))
						{
							if(actualizacion.get(getString(R.string.TAG_TIPO)).equals("modelo"))
							{
								modelo = actualizacion.getJSONObject("modelo");
								//								String descripcion = modelo.getString(getString(R.string.TAG_DESCRIPCION_MODELO));
								String nombre = modelo.getString(getString(R.string.TAG_NOMBRE_MODELO));
								String urlImagen = modelo.getString(getString(R.string.TAG_IMAGEN_MODELO));


								String nombreImagen = getString(R.string.CONSTANTE_NOMBRE_IMAGEN_MODELO) + nombre;
								descargarImagen(urlImagen, nombreImagen);
								//								Modelo m = new Modelo(nombreImagen,nombre,descripcion);
								//								daoModelo.create(m);
							}
							else
							{
								edicion = actualizacion.getJSONObject("edicion");


								String nombre = edicion.getString(getString(R.string.TAG_NOMBRE_EDICION));

								//Manejo imagen
								String urlImagen = edicion.getString(getString(R.string.TAG_IMAGEN_EDICION));
								String nombreImagen = getString(R.string.CONSTANTE_NOMBRE_IMAGEN_EDICION) + nombre;
								descargarImagen(urlImagen, nombreImagen);
								boolean test_drive = Boolean.parseBoolean(edicion.getString(getString(R.string.TAG_TEST_DRIVE_EDICION)));
								String descripcion = edicion.getString(getString(R.string.TAG_DESCRIPCION_EDICION));
								String templateColor= edicion.getString(getString(R.string.TAG_TEMPLATE_EDICION));

								//Manejo thumbnail
								String urlImagenThumbnail = edicion.getString(getString(R.string.TAG_IMAGEN_THUMBNAIL_EDICION));
								String nombreThumbnail = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_EDICION);
								descargarImagen(urlImagenThumbnail, nombreThumbnail);


								String nombreModelo = edicion.getString(getString(R.string.TAG_NOMBRE_MODELO_EDICION));
								Modelo m = DaoModelo.darModelo(daoModelo, nombreModelo);

								Edicion e = new Edicion(nombreImagen,nombre,descripcion,nombreThumbnail,test_drive,m,templateColor);
								daoEdicion.create(e);


							}
						}


					}

				}
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss();
			new ActualizarNoticias().execute("");

		}


	}
	
	private class ActualizarNoticias extends AsyncTask<String, Integer, Boolean>
	{

		private Dao<Timestamp, Integer> daoTimestamp;
		private Dao<Noticia, Integer> daoNoticia;
		private boolean hayActualizaciones;

		ProgressDialog progress;
		
		
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Actualizando noticias...",false);
		}

		@Override
		protected Boolean doInBackground(String... params) 
		{
			daoNoticia = darDaoNoticia();
			daoTimestamp = darDaoTimestamp();
			Parser jparser = new Parser();
			



			JSONArray noticias;
			try 
			{
				BigDecimal d = DaoTimestamp.darTimestamp(daoTimestamp,"noticias");
				String s = getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL)+DaoTimestamp.darTimestamp(daoTimestamp,"noticias")+"/";
				JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL)+DaoTimestamp.darTimestamp(daoTimestamp,"noticias")+"/");
				
				hayActualizaciones = jsonObject.getBoolean(getString(R.string.TAG_HAY_ACTUALIZACIONES));
				if(hayActualizaciones)
				{
					
					String timeStamp = jsonObject.getString(getString(R.string.TAG_TIMESTAMP));
					BigDecimal fTimestamp =new BigDecimal(timeStamp);
					DaoTimestamp.primeraVez(daoTimestamp, fTimestamp,"noticias");
					
					
					
					noticias = jsonObject.getJSONArray(getString(R.string.TAG_NOTICIAS));
					for(int i = 0; i<noticias.length();i++)
					{
						UpdateBuilder<Noticia, Integer> updateBuilder = daoNoticia.updateBuilder();
						JSONObject noticia = noticias.getJSONObject(i);
						updateBuilder.updateColumnValue("categoria", noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA)));
						updateBuilder.updateColumnValue("resumen", noticia.getString(getString(R.string.TAG_NOTICIAS_RESUMEN)));
//						String pagina = noticia.getString(getString(R.string.TAG_NOTICIAS_PAGINA));
//						updateBuilder.updateColumnValue("pagina", pagina);
						updateBuilder.updateColumnValue("titulo", noticia.getString(getString(R.string.TAG_NOTICIAS_TITULO)));

						//Manejo del thumbnail
						String nombreImagenEliminar = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_NOTICIA)+noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA));
						eliminarImagen( nombreImagenEliminar);
						String thumbnail = noticia.getString(getString(R.string.TAG_NOTICIAS_THUMBNAIL));
						String nombreThumbnail = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_NOTICIA) + noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA));
						descargarImagen(thumbnail, nombreThumbnail);
						updateBuilder.updateColumnValue("thumbnail", nombreThumbnail);

						updateBuilder.updateColumnValue("fechaCreacion",noticia.getString(getString(R.string.TAG_NOTICIAS_FECHA_CREACION)));
						updateBuilder.updateColumnValue("url",noticia.getString(getString(R.string.TAG_NOTICIAS_URL)));
						updateBuilder.where().eq("categoria", noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA)));
						daoNoticia.update(updateBuilder.prepare());



					}
				}
				

				
				List<Noticia> n = daoNoticia.queryForAll();
				n.size();
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SQLException e) 
			{
				Log.d("Tutorial", e.getMessage());
				e.printStackTrace();
			}
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss();
		}
	}
	

	//////////////////////////////////////////////////////////////////
	//Metodo para manejo de las imagenes dentro del dispositivo
	//////////////////////////////////////////////////////////////////

	/**
	 * Metodo que descarga una imagen de una url y la guarda en el dispostivo
	 * @param bitmap
	 * @param nombre
	 */
	public void descargarImagen(String dirUrl, String nombre)
	{
		URL url;
		Bitmap bitmap = null;
		try 
		{
			url = new URL(dirUrl);

			//Seccion de descarga de la imagen
			HttpURLConnection connection= (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream is = connection.getInputStream();
			bitmap=BitmapFactory.decodeStream(is);


			//Seccion de almacenamiento de la imagen
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			FileOutputStream fos = openFileOutput(nombre, Context.MODE_WORLD_WRITEABLE);
			fos.write(bytes.toByteArray());
			fos.close();



		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			Toast toast = Toast.makeText(this, "Se perdio el accesso a Internet. Reinicia la aplicacion", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
	}

	/**
	 * Metodo utilizado para eliminar una imagen. Este se usa para los updates y los deletes
	 * @param nombre
	 */
	public void eliminarImagen(String nombre)
	{
		this.deleteFile(nombre);
	}

	//////////////////////////////////////////////////////////////////
	//Metodos para obtener los objetos DAO de cada una de las tablas
	//////////////////////////////////////////////////////////////////

	/**
	 * Metodo necesario para la creacion de un objeto DAO para acceder a cada uno de las tablas
	 * @return
	 */
	public DataBaseHelper getHelper()
	{
		if(databaseHelper == null)
			databaseHelper = OpenHelperManager.getHelper(this,DataBaseHelper.class);
		return databaseHelper;
	}


	public Dao<Edicion, String> darDaoEdicion()
	{

		Dao<Edicion, String> daoEdicion = null;
		try 
		{
			daoEdicion = getHelper().darDaoEdicion();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return daoEdicion;
	}

	public Dao<Modelo,String> darDaoModelo()
	{
		Dao<Modelo, String> daoModelo = null;

		try 
		{
			daoModelo = getHelper().darDaoModelo();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return daoModelo;

	}

	public Dao<Persistencia, Integer> darDaoPersistencia()
	{
		Dao<Persistencia, Integer> daoPErsistencia = null;

		try
		{
			daoPErsistencia = getHelper().darDaoPersistencia();
		}
		catch (SQLException e) 
		{

		}
		return daoPErsistencia;
	}

	public Dao<Timestamp, Integer> darDaoTimestamp()
	{
		Dao<Timestamp, Integer> daoTimestamp = null;

		try 
		{
			daoTimestamp = getHelper().darDaoTimestamp();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return daoTimestamp;
	}

	public Dao<Noticia, Integer> darDaoNoticia()
	{
		Dao<Noticia, Integer> daoNoticia = null;
		try 
		{
			daoNoticia = getHelper().darDaoNoticia();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return daoNoticia;
	}


	//////////////////////////////////////////////////////////////////
	//Metodos de la clase activity
	//////////////////////////////////////////////////////////////////


	/**
	 * Metodo que retorna el contexto de esta actividad
	 * @return
	 */
	public Context darContexto()
	{
		return this;
	}


	////////////////////////////////////////////////
	// Metodos para el manejo de los botones
	////////////////////////////////////////////////


	public void inicioInstrucciones(View v)
	{
		Intent i = new Intent(InicioActivity.this, InstruccionesActivity.class);
		startActivity(i);
	}

	public void inicioTabs(View v)
	{
		Intent i = new Intent(InicioActivity.this, ManejadorTabs.class);
		i.putExtra("tab", 0);
		startActivity(i);
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		if(databaseHelper != null)
			OpenHelperManager.releaseHelper();
		databaseHelper = null;
	}



}
