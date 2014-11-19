package eaav.android_v1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import clases.ClassConfiguracion;
import clases.ClassRevision;

import Miscelanea.Archivos;
import Miscelanea.Dialogos;
import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ConnectServer {
	//Instancias a clases
	private Context ConnectServerContext;
	private String 	DirectorioConexionServer;
	
	private 		ClassConfiguracion FcnConfiguracion;
		
	//nusoap
	public String 		URL;			
	private String 		NAMESPACE; 	
	private String 		_servidor;
	private String 		_puerto;
	private String 		_pagina;
	private String 		_web_service;
	private String 		_pda;
		
		
	public ConnectServer(Context context, String Directorio){
		this.ConnectServerContext 		= context;
		this.DirectorioConexionServer 	= Directorio;
		this.FcnConfiguracion			= new ClassConfiguracion(this.ConnectServerContext, FormLoggin.CARPETA_RAIZ);
		_servidor 	= this.FcnConfiguracion.getServidor();
		_puerto 	= this.FcnConfiguracion.getPuerto();
		_pagina 	= this.FcnConfiguracion.getServicio();
		_web_service= this.FcnConfiguracion.getWebService();
		_pda 		= this.FcnConfiguracion.getEquipo();		
		this.URL 		= this._servidor+":"+this._puerto+"/"+this._pagina+"/"+this._web_service;
		this.NAMESPACE 	= this._servidor+":"+this._puerto+"/"+this._pagina;
	}
	
	
	public void UpLoadTrabajoRealizado(){
		new UpLoadTrabajoRealizado(this.ConnectServerContext,this.DirectorioConexionServer).execute();
	}
	
	
	public void UpLoadTrabajoSinRealizar(){
		new UpLoadTrabajoSinRealizar(this.ConnectServerContext, this.DirectorioConexionServer).execute();
	}
	
	
	public void DownLoadTrabajoProgramado(){
		new DownLoadTrabajoProgramado(this.ConnectServerContext, this.DirectorioConexionServer).execute();
	}
	
		
	
	//clase para enviar de forma asincrona las desviaciones
	private class UpLoadTrabajoRealizado extends AsyncTask<Void,Void,SoapPrimitive>{
		//Variables para contexto de mensajes a visualizar y conexion a la base de datos
		private Context 	CtxTrabajoRealizado;
		private SQLite 		RealizadoSQL;
		private Archivos 	RealizadoArch;
		private Dialogos 	MensajeDialog; 	
		SoapPrimitive 		response = null;
		private ArrayList<ContentValues> 	_tempTabla = new ArrayList<ContentValues>();
		private ContentValues 				_tempRegistro= new ContentValues();
		    	
		//Variables con la informacion del web service
		private static final String METHOD_NAME	= "RevisionesRealizadas";
		private static final String SOAP_ACTION	= "RevisionesRealizadas";
		    	
		//Variables de ruta y archivo a crear para cargar en el servidor
		private String 	DirectorioCarga = null;
		static final String ArchivoCarga = "TrabajoRealizado.txt";
		 
		private UpLoadTrabajoRealizado(Context context, String DirectorioArchivo){
    		this.CtxTrabajoRealizado = context;
    		this.DirectorioCarga 	= DirectorioArchivo;
    		this.RealizadoSQL 		= new SQLite(this.CtxTrabajoRealizado, FormLoggin.CARPETA_RAIZ, FormLoggin.NOMBRE_DATABASE);
    		this.RealizadoArch 		= new Archivos(this.CtxTrabajoRealizado, this.DirectorioCarga,10);
    		MensajeDialog 			= new Dialogos(this.CtxTrabajoRealizado);
		}
		
    	protected void onPreExecute(){
    		int NumNotificadas= 0;
    		int NumTerminadas= 0;
    		String CadenaArchivo = "";    		
    		//ArrayList<ArrayList<String>> InfSendRevision;
    		//InfSendRevision = new ArrayList<ArrayList<String>>();
    		
    		this._tempTabla = this.RealizadoSQL.SelectData(	"db_notificaciones", 
    														"fecha_notificacion,revision,codigo,nombre,direccion,serie,ciclo,promedio,visita,lectura,medidor,precinto,observacion,fecha_visita,motivo,jornada_notificacion,hora_visita",
															"revision IS NOT NULL");
    		
    		NumNotificadas = this._tempTabla.size();
    		
			for(int i=0;i<this._tempTabla.size();i++){
				this._tempRegistro = this._tempTabla.get(i);
				CadenaArchivo+="N|"+this._tempRegistro.getAsString("fecha_notificacion")+"|"+
									this._tempRegistro.getAsString("revision")+"|"+
									this._tempRegistro.getAsString("codigo")+"|"+
									this._tempRegistro.getAsString("nombre")+"|"+
									this._tempRegistro.getAsString("direccion")+"|"+
									this._tempRegistro.getAsString("serie")+"|"+
									this._tempRegistro.getAsString("ciclo")+"|"+
									this._tempRegistro.getAsString("promedio")+"|"+
									this._tempRegistro.getAsString("visita")+"|"+
									this._tempRegistro.getAsString("lectura")+"|"+
									this._tempRegistro.getAsString("medidor")+"|"+
									this._tempRegistro.getAsString("precinto")+"|"+
									this._tempRegistro.getAsString("observacion")+"|"+
									this._tempRegistro.getAsString("fecha_visita")+"|"+
									this._tempRegistro.getAsString("motivo")+"|"+
									this._tempRegistro.getAsString("jornada_notificacion")+"|"+
									this._tempRegistro.getAsString("hora_visita")+"|&";
			}
    		    		
			this._tempTabla = this.RealizadoSQL.SelectData(	"db_desviaciones", 
															"revision, codigo, nombre," +
															"direccion, serie, ciclo," +
															"promedio, fecha, hora," +
															"tipo, area, pisos, " +
															"actividad, uso, residentes, " +
															"habitado, estado, acueducto, " +
															"camaramedidor, estadocamara, serieindividual," +
															"marcaindividual, diametroindividual, lecturaindividual, " +
															"serietotalizador, marcatotalizador,diametrototalizador, " +
															"lecturatotalizador, subterraneos, itemsubterraneos, " +
															"estadosubterraneos, lavaplatos, itemlavaplatos, " +
															"estadolavaplatos, lavaderos,itemlavadero, " +
															"estadolavadero, elevados, itemelevado, " +
															"estadoelevado, iteminternas, estadointernas, " +
															"piscinas, itempiscina, estadopiscina, "+
															"medidorregpaso, medidorregantifraude, medidordestruido, " +
															"medidorinvertido, medidorilegible, medidorprecintoroto, " +
															"hermeticidadreginternos, hermeticidadequipomedida, estanqueidadreselevado," +
															"estanqueidadreslavadero,estanqueidadressubterraneo,estanqueidadcapelevado," +
															"estanqueidadcaplavadero,estanqueidadcapsubterraneo,estanqueidadfugaelevado," +
															"estanqueidadfugalavadero,estanqueidadfugasubterrano,hermeticidadfugaimperceptible, " +
															"hermeticidadfugas,hermeticidadfugavisible, diagnostico, " +
															"estrato, precinto, serviciodirecto, " +
															"bypass, horaapertura, nombreusuario, " +
															"cedulausuario, nombretestigo,cedulatestigo, " +
															"cisterna, itemcisterna, estadocisterna, " +
															"ducha, itemducha, estadoducha, " +
															"lavamanos, itemlavamanos,estadolavamanos, " +
															"servicioacueducto, servicioalcantarillado, horacierre, " +
															"segundoconcepto, respuestadesviacion",
															"revision IS NOT NULL");
			
			for(int i=0;i<this._tempTabla.size();i++){
				this._tempRegistro = this._tempTabla.get(i);
				CadenaArchivo +="T|"+this._tempRegistro.getAsString("revision")+"|"
									+this._tempRegistro.getAsString("codigo")+"|"
									+this._tempRegistro.getAsString("nombre")+"|"
									+this._tempRegistro.getAsString("direccion")+"|"
									+this._tempRegistro.getAsString("serie")+"|"
									+this._tempRegistro.getAsString("ciclo")+"|"
									+this._tempRegistro.getAsString("promedio")+"|"
									+this._tempRegistro.getAsString("fecha")+"|"
									+this._tempRegistro.getAsString("hora")+"|"
									+this._tempRegistro.getAsString("tipo")+"|"
									+this._tempRegistro.getAsString("area")+"|"
									+this._tempRegistro.getAsString("pisos")+"|"
									+this._tempRegistro.getAsString("actividad")+"|"
									+this._tempRegistro.getAsString("uso")+"|"
									+this._tempRegistro.getAsString("residentes")+"|"
									+this._tempRegistro.getAsString("habitado")+"|"
									+this._tempRegistro.getAsString("estado")+"|"
									+this._tempRegistro.getAsString("acueducto")+"|"
									+this._tempRegistro.getAsString("camaramedidor")+"|"
									+this._tempRegistro.getAsString("estadocamara")+"|"
									+this._tempRegistro.getAsString("serieindividual")+"|"
									+this._tempRegistro.getAsString("marcaindividual")+"|"
									+this._tempRegistro.getAsString("diametroindividual")+"|"
									+this._tempRegistro.getAsString("lecturaindividual")+"|"
									+this._tempRegistro.getAsString("serietotalizador")+"|"
									+this._tempRegistro.getAsString("marcatotalizador")+"|"
									+this._tempRegistro.getAsString("diametrototalizador")+"|"
									+this._tempRegistro.getAsString("lecturatotalizador")+"|"
									+this._tempRegistro.getAsString("subterraneos")+"|"
									+this._tempRegistro.getAsString("itemsubterraneos")+"|"
									+this._tempRegistro.getAsString("estadosubterraneos")+"|"
									+this._tempRegistro.getAsString("lavaplatos")+"|"
									+this._tempRegistro.getAsString("itemlavaplatos")+"|"
									+this._tempRegistro.getAsString("estadolavaplatos")+"|"
									+this._tempRegistro.getAsString("lavaderos")+"|"
									+this._tempRegistro.getAsString("itemlavadero")+"|"
									+this._tempRegistro.getAsString("estadolavadero")+"|"
									+this._tempRegistro.getAsString("elevados")+"|"
									+this._tempRegistro.getAsString("itemelevado")+"|"
									+this._tempRegistro.getAsString("estadoelevado")+"|"
									+this._tempRegistro.getAsString("iteminternas")+"|"
									+this._tempRegistro.getAsString("estadointernas")+"|"
									+this._tempRegistro.getAsString("piscinas")+"|"
									+this._tempRegistro.getAsString("itempiscina")+"|"
									+this._tempRegistro.getAsString("estadopiscina")+"|"
									+this._tempRegistro.getAsString("medidorregpaso")+"|"
									+this._tempRegistro.getAsString("medidorregantifraude")+"|"
									+this._tempRegistro.getAsString("medidordestruido")+"|"
									+this._tempRegistro.getAsString("medidorinvertido")+"|"
									+this._tempRegistro.getAsString("medidorilegible")+"|"
									+this._tempRegistro.getAsString("medidorprecintoroto")+"|"
									+this._tempRegistro.getAsString("hermeticidadreginternos")+"|"
									+this._tempRegistro.getAsString("hermeticidadequipomedida")+"|"
									+this._tempRegistro.getAsString("estanqueidadreselevado")+"-"+this._tempRegistro.getAsString("estanqueidadreslavadero")+"-"+this._tempRegistro.getAsString("estanqueidadressubterraneo")+"|"
									+this._tempRegistro.getAsString("estanqueidadcapelevado")+"-"+this._tempRegistro.getAsString("estanqueidadcaplavadero")+"-"+this._tempRegistro.getAsString("estanqueidadcapsubterraneo")+"|"
									+this._tempRegistro.getAsString("estanqueidadfugaelevado")+"-"+this._tempRegistro.getAsString("estanqueidadfugalavadero")+"-"+this._tempRegistro.getAsString("estanqueidadfugasubterrano")+"|"
									+this._tempRegistro.getAsString("hermeticidadfugaimperceptible")+"|"
									+this._tempRegistro.getAsString("hermeticidadfugas")+"|"
									+this._tempRegistro.getAsString("hermeticidadfugavisible")+"|"
									+this._tempRegistro.getAsString("diagnostico")+"|"
									+this._tempRegistro.getAsString("estrato")+"|"
									+this._tempRegistro.getAsString("precinto")+"|"
									+this._tempRegistro.getAsString("serviciodirecto")+"|"
									+this._tempRegistro.getAsString("bypass")+"|"
									+this._tempRegistro.getAsString("horaapertura")+"|"
									+this._tempRegistro.getAsString("nombreusuario")+"|"
									+this._tempRegistro.getAsString("cedulausuario")+"|"
									+this._tempRegistro.getAsString("nombretestigo")+"|"
									+this._tempRegistro.getAsString("cedulatestigo")+"|"
									+this._tempRegistro.getAsString("cisterna")+"|"
									+this._tempRegistro.getAsString("itemcisterna")+"|"
									+this._tempRegistro.getAsString("estadocisterna")+"|"
									+this._tempRegistro.getAsString("ducha")+"|"
									+this._tempRegistro.getAsString("itemducha")+"|"
									+this._tempRegistro.getAsString("estadoducha")+"|"
									+this._tempRegistro.getAsString("lavamanos")+"|"
									+this._tempRegistro.getAsString("itemlavamanos")+"|"
									+this._tempRegistro.getAsString("estadolavamanos")+"|"
									+this._tempRegistro.getAsString("servicioacueducto, , ")+"|"
									+this._tempRegistro.getAsString("servicioalcantarillado")+"|"
									+this._tempRegistro.getAsString("horacierre")+"|"
									+this._tempRegistro.getAsString("segundoconcepto")+"|"
									+this._tempRegistro.getAsString("respuestadesviacion")+"|&";
				NumTerminadas += 1;
			}
			
    		CadenaArchivo = CadenaArchivo.replace("\n", ". "); 	//Se elimina los saltos de linea dentro de la informacion digitada por el tecnico
			CadenaArchivo = CadenaArchivo.replace("&", "\n");	//Se hace salto de linea por cada revision notificada o terminada que exista
			
			if(!this.RealizadoArch.DoFile("Descargas",UpLoadTrabajoRealizado.ArchivoCarga, CadenaArchivo)){
				Toast.makeText(this.CtxTrabajoRealizado,"Imposible crear el archivo de carga.", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this.CtxTrabajoRealizado,"Enviando \n"+NumNotificadas+" revisiones notificaciones. \n"+NumTerminadas+" revisiones terminadas, por favor espere...", Toast.LENGTH_SHORT).show();	
		    }		
    	}

    	
    	
    	
    	@Override
    	protected SoapPrimitive doInBackground(Void... params) {
    		try{
    			SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
    			so.addProperty("Informacion", this.RealizadoArch.FileToArrayBytes(FormLoggin.CARPETA_RAIZ+"/Descargas/"+ArchivoCarga));
    			so.addProperty("PDA",  RealizadoSQL.StrSelectShieldWhere("db_parametros", "valor", "item='pda'"));
    			SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			new MarshalBase64().register(sse);
    			sse.dotNet=true;
    			sse.setOutputSoapObject(so);
    			HttpTransportSE htse=new HttpTransportSE(URL);
    			htse.call(SOAP_ACTION, sse);
    			response=(SoapPrimitive) sse.getResponse();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return response;
    	}

    	@Override
    	protected void onPostExecute(SoapPrimitive rta) {
    		this.RealizadoArch.DeleteFile(FormLoggin.CARPETA_RAIZ+File.pathSeparator+UpLoadTrabajoRealizado.ArchivoCarga);
    		if(rta==null) {
    			MensajeDialog.DialogoInformativo("ESTADO DE LA CONEXION","Error, no se ha obtenido respuesta del servidor.");
    		}else{
    			String resultado = rta.toString();
				String ListaRetorno[] = resultado.split("\\|");
				resultado = resultado.replace("|", "\n");
				
				for(int i=0;i<ListaRetorno.length;i++){
					String OrdenesOk[] = ListaRetorno[i].split("--");
					if(OrdenesOk[0].equals("OK")){
						if(OrdenesOk[2].equals("Notificada")){
							this.RealizadoSQL.DeleteRegistro("db_notificaciones", "revision = '" + OrdenesOk[1] + "'");
						}else if(OrdenesOk[2].equals("Terminada")){
							this.RealizadoSQL.DeleteRegistro("db_desviaciones", "revision = '" + OrdenesOk[1] + "'");
						}
						this.RealizadoSQL.DeleteRegistro("db_solicitudes", "revision = '" + OrdenesOk[1] + "'");
					}
				}
				int NotificadaPendientes = this.RealizadoSQL.IntSelectShieldWhere("db_notificaciones", "count(revision) as cantidad", "revision IS NOT NULL");	
				int TerminadaPendientes = this.RealizadoSQL.IntSelectShieldWhere("db_desviaciones", "count(revision) as cantidad", "revision IS NOT NULL");	
				MensajeDialog.DialogoInformativo("ESTADO DEL ENVIO","Respuesta Del Servidor \n"+resultado+" \n"+NotificadaPendientes + " revisiones notificadas pendientes por enviar.\n"+TerminadaPendientes + " revisiones terminadas pendientes por enviar.");
    		}
    	}	
    }

	
	private class UpLoadTrabajoSinRealizar extends AsyncTask<Void,Void,SoapPrimitive>{
    	//Variables para contexto de mensajes a visualizar y conexion a la base de datos
		private Context 	CtxTrabajoSinRealizar;
    	private SQLite 		SinRealizarSQL;
    	private ArrayList<ContentValues> _tempTabla = new ArrayList<ContentValues>();
    	private Archivos 	SinRealizarArch;
    	private Dialogos 	MensajeDialog; 	
    	SoapPrimitive 		response = null;
    	
    	
    	//Variables con la informacion del web service
    	private static final String METHOD_NAME	= "RevisionesDevueltas";
    	private static final String SOAP_ACTION	= "RevisionesDevueltas";
    	
    	//Variables de ruta y archivo a crear para cargar en el servidor
    	private String 	DirectorioCarga = null;
    	static final String ArchivoCarga = "TrabajoSinRealizar.txt";
    	
    	private UpLoadTrabajoSinRealizar(Context context, String DirectorioArchivo){
    		this.CtxTrabajoSinRealizar 	= context;
    		this.DirectorioCarga 		= DirectorioArchivo;
    		this.SinRealizarSQL 		= new SQLite(this.CtxTrabajoSinRealizar, this.DirectorioCarga, FormLoggin.NOMBRE_DATABASE);
    		this.SinRealizarArch 		= new Archivos(this.CtxTrabajoSinRealizar, this.DirectorioCarga, 10);
    		MensajeDialog 				= new Dialogos(this.CtxTrabajoSinRealizar);    		
    	}
    	 
    	protected void onPreExecute(){
    		int NumRegistros = 0;
    		String CadenaArchivo = "";    		
    		   		
    		this._tempTabla = this.SinRealizarSQL.SelectData("db_solicitudes", "revision", "estado = 0");
    		for(int i=0;i<this._tempTabla.size();i++){
    			CadenaArchivo += this._tempTabla.get(i).getAsString("revision")+"|\n";
    		}
    		NumRegistros = this._tempTabla.size();
			
			if(!this.SinRealizarArch.DoFile("Descargas", UpLoadTrabajoSinRealizar.ArchivoCarga, CadenaArchivo)){
				Toast.makeText(this.CtxTrabajoSinRealizar,"Imposible crear el archivo de carga.", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this.CtxTrabajoSinRealizar,"Enviando "+NumRegistros+" revisiones sin realizar, por favor espere...", Toast.LENGTH_SHORT).show();	
			}
		}

    	@Override
    	protected SoapPrimitive doInBackground(Void... params) {
    		try{
    			SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
    			so.addProperty("Informacion", this.SinRealizarArch.FileToArrayBytes(FormLoggin.CARPETA_RAIZ+"/Descargas/"+ArchivoCarga));
    			so.addProperty("PDA", SinRealizarSQL.StrSelectShieldWhere("db_parametros", "valor", "item='pda'"));	
    			SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			new MarshalBase64().register(sse);
    			sse.dotNet=true;
    			sse.setOutputSoapObject(so);
    			HttpTransportSE htse=new HttpTransportSE(URL);
    			htse.call(SOAP_ACTION, sse);
    			response=(SoapPrimitive) sse.getResponse();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return response;
    	}

    	@Override
    	protected void onPostExecute(SoapPrimitive rta) {
    		this.SinRealizarArch.DeleteFile(FormLoggin.CARPETA_RAIZ+File.pathSeparator+UpLoadTrabajoSinRealizar.ArchivoCarga);
    		if(rta==null) {
    			MensajeDialog.DialogoInformativo("ESTADO DE LA CONEXION","Error, no se ha obtenido respuesta del servidor.");
    		}else{
    			String resultado = rta.toString();
				String ListaRetorno[] = resultado.split("\\|");
				resultado = resultado.replace("|", "\n");
				
				for(int i=0;i<ListaRetorno.length;i++){
					String OrdenesOk[] = ListaRetorno[i].split("--");
					if(OrdenesOk[0].equals("OK")){
						this.SinRealizarSQL.DeleteRegistro("db_solicitudes", "revision = '" + OrdenesOk[1] + "' AND estado = 0");
					}
				}
				int pendientes = this.SinRealizarSQL.IntSelectShieldWhere("db_solicitudes", "count(revision) as cantidad", "estado = 0");				
				MensajeDialog.DialogoInformativo("ESTADO DEL ENVIO","Respuesta Del Servidor \n"+resultado+" \n"+pendientes + " registros pendientes por enviar.");
    		}
    	}	
    }
	

	//Clase privada para descargar el trabajo asignado
    private class DownLoadTrabajoProgramado extends AsyncTask<Void,Void,Void>{
    	static final String ArchivoDescarga = "TrabajoProgramado.txt";
    	private ClassRevision 		FcnRevision;
    	private Archivos 			FcnArchivos;
    	
    	private Dialogos 	MensajeDialog;
    	private Context 	CtxTrabajoProgramado;
    	private String 		DirectorioDescarga = null;
    	
    	
    	private DownLoadTrabajoProgramado(Context context, String DirectorioArchivo){
    		this.CtxTrabajoProgramado 	= context;
    		this.DirectorioDescarga		= DirectorioArchivo;
    		MensajeDialog 			= new Dialogos(this.CtxTrabajoProgramado);
    		this.FcnRevision		= new ClassRevision(this.CtxTrabajoProgramado, this.DirectorioDescarga);
    		this.FcnArchivos 		= new Archivos(this.CtxTrabajoProgramado,this.DirectorioDescarga,10);
    	}
    	
    	protected void onPreExecute(){
    		Toast.makeText(this.CtxTrabajoProgramado,"Conectando con el servidor.", Toast.LENGTH_SHORT).show();
    	}
    	
    	@Override
    	protected Void doInBackground(Void... params) {
    		URL url= null;
    	    HttpURLConnection urlConnection = null;
    	    FileOutputStream fileOutput = null;
    	    InputStream inputStream = null;
    	    byte[] buffer = new byte[4096];
    	    int bufferLength = 0; 
    	    
    		try {    			
    			url = new URL(_servidor+":"+_puerto+"/"+_pagina+"/ServerToPDA.php?Proceso=Descargue&PDA="+_pda);
				urlConnection = (HttpURLConnection) url.openConnection();
    			urlConnection.setRequestMethod("GET");
    			urlConnection.setDoOutput(true);
    			urlConnection.connect();
    			inputStream = urlConnection.getInputStream();
        		
    			if(inputStream.available()>0){					//Si en el buffer de recepcion hay datos se crea el archivo y se escribe el contenido
    				File file = new File(this.DirectorioDescarga, ArchivoDescarga);
            		fileOutput = new FileOutputStream(file);
            		while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
            			fileOutput.write(buffer, 0, bufferLength); 
            		}
            		fileOutput.close();
    			}
    			
    		} catch (MalformedURLException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Void unused) {
    		Toast.makeText(this.CtxTrabajoProgramado,"Conexion terminada.", Toast.LENGTH_SHORT).show();
    		if(this.FcnArchivos.ExistFolderOrFile(ArchivoDescarga, true)){
    			ArrayList<String> OrdenesTrabajo = new ArrayList<String>();
        		OrdenesTrabajo = this.FcnArchivos.FileToArrayString(ArchivoDescarga, false);
        		this.FcnArchivos.DeleteFile(ArchivoDescarga);
        		MensajeDialog.DialogoInformativo("ESTADO DE LA RECEPCION","Revisiones Recepcionadas \n"+this.FcnRevision.registrarRevisiones(OrdenesTrabajo)+" \n");
        	}else{
    			MensajeDialog.DialogoInformativo("ESTADO DE LA RECEPCION", "No tiene revisiones programadas.");
        	}		
        }	
    }
}
