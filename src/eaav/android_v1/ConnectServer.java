package eaav.android_v1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;

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
	
	//nusoap
	private static String URL		= "http://190.93.133.127:8080/EAAV-Desviaciones/ServerPDA/WS_EAAV_Desviaciones.php?wsdl";
	private static String NAMESPACE = "http://190.93.133.127:8080/EAAV-Desviaciones/ServerPDA";
	
		
		
	public ConnectServer(Context context, String Directorio){
		this.ConnectServerContext 		= context;
		this.DirectorioConexionServer 	= Directorio;
		//this.FcnSQL 		= new SQLite(this.ConnectServerContext, this.DirectorioConexionServer, Loggin.NOMBRE_DATABASE);
		//this.FcnArchivos 	= new Archivos(this.ConnectServerContext,this.DirectorioConexionServer,10);
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
	
	/*public void DescargarNotificaciones(){
		new UpLoadAllNotificaciones(this.ConnectServerContext).execute();
	}*/
	
		
	
	//clase para enviar de forma asincrona las desviaciones
	private class UpLoadTrabajoRealizado extends AsyncTask<Void,Void,SoapPrimitive>{
		//Variables para contexto de mensajes a visualizar y conexion a la base de datos
		private Context CtxTrabajoRealizado;
		private SQLite RealizadoSQL;
		private Archivos RealizadoArch;
		private Dialogos MensajeDialog; 	
		SoapPrimitive response = null;
		    	
		//Variables con la informacion del web service
		private static final String METHOD_NAME	= "RevisionesRealizadas";
		private static final String SOAP_ACTION	= "RevisionesRealizadas";
		    	
		//Variables de ruta y archivo a crear para cargar en el servidor
		private String 	DirectorioCarga = null;
		static final String ArchivoCarga = "TrabajoRealizado.txt";
		 
		private UpLoadTrabajoRealizado(Context context, String DirectorioArchivo){
    		this.CtxTrabajoRealizado = context;
    		this.DirectorioCarga 	= DirectorioArchivo;
    		this.RealizadoSQL 		= new SQLite(this.CtxTrabajoRealizado, this.DirectorioCarga, "BdEAAV_Android");
    		//this.RealizadoArch 		= new Archivos(this.CtxTrabajoRealizado, this.DirectorioCarga);
    		MensajeDialog 			= new Dialogos(this.CtxTrabajoRealizado);
		}
		
    	protected void onPreExecute(){
    		int NumNotificadas= 0;
    		int NumTerminadas= 0;
    		String CadenaArchivo = "";    		
    		ArrayList<ArrayList<String>> InfSendRevision;
    		InfSendRevision = new ArrayList<ArrayList<String>>();
    		
    		/*InfSendRevision = RealizadoSQL.SelectDataKeyValue(	"db_notificaciones",
																"fecha_notificacion,revision,codigo,nombre,direccion,serie,ciclo,promedio,visita,lectura,medidor,precinto,observacion,fecha_visita,motivo,jornada_notificacion,hora_visita",
																"revision IS NOT NULL");*/
			
			ListIterator<ArrayList<String>> iCiclo = InfSendRevision.listIterator();
			while(iCiclo.hasNext()){
				ArrayList<String> Registro = iCiclo.next();				
				CadenaArchivo +="N|"+Registro.get(0)+"|"+Registro.get(1)+"|"+Registro.get(2)+"|"+
								Registro.get(3)+"|"+Registro.get(4)+"|"+Registro.get(5)+"|"+
								Registro.get(6)+"|"+Registro.get(7)+"|"+Registro.get(8)+"|"+
								Registro.get(9)+"|"+Registro.get(10)+"|"+Registro.get(11)+"|"+
								Registro.get(12)+"|"+Registro.get(13)+"|"+Registro.get(14)+"|"+
								Registro.get(15)+"|"+Registro.get(16)+"|&";
				NumNotificadas += 1;
			}
    		
    		/*InfSendRevision = RealizadoSQL.SelectDataKeyValue(	"db_desviaciones", 
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
																"revision IS NOT NULL");*/		
    		
    		iCiclo = InfSendRevision.listIterator();
			while(iCiclo.hasNext()){
				ArrayList<String> Registro = iCiclo.next();		
				CadenaArchivo +="T|"+Registro.get(0)+"|"+Registro.get(1)+"|"+Registro.get(2)+"|"+
								Registro.get(3)+"|"+Registro.get(4)+"|"+Registro.get(5)+"|"+
								Registro.get(6)+"|"+Registro.get(7)+"|"+Registro.get(8)+"|"+
								Registro.get(9)+"|"+Registro.get(10)+"|"+Registro.get(11)+"|"+
								Registro.get(12)+"|"+Registro.get(13)+"|"+Registro.get(14)+"|"+
								Registro.get(15)+"|"+Registro.get(16)+"|"+Registro.get(17)+"|"+
								Registro.get(18)+"|"+Registro.get(19)+"|"+Registro.get(20)+"|"+
								Registro.get(21)+"|"+Registro.get(22)+"|"+Registro.get(23)+"|"+
								Registro.get(24)+"|"+Registro.get(25)+"|"+Registro.get(26)+"|"+
								Registro.get(27)+"|"+Registro.get(28)+"|"+Registro.get(29)+"|"+
					 			Registro.get(30)+"|"+Registro.get(31)+"|"+Registro.get(32)+"|"+
					 			Registro.get(33)+"|"+Registro.get(34)+"|"+Registro.get(35)+"|"+
					 			Registro.get(36)+"|"+Registro.get(37)+"|"+Registro.get(38)+"|"+
					 			Registro.get(39)+"|"+Registro.get(40)+"|"+Registro.get(41)+"|"+
					 			Registro.get(42)+"|"+Registro.get(43)+"|"+Registro.get(44)+"|"+
					 			Registro.get(45)+"|"+Registro.get(46)+"|"+Registro.get(47)+"|"+
					 			Registro.get(48)+"|"+Registro.get(49)+"|"+Registro.get(50)+"|"+
					 			Registro.get(51)+"|"+Registro.get(52)+"|"+Registro.get(53)+"-"+	Registro.get(54)+"-"+Registro.get(55)+"|"+
					 			Registro.get(56)+"-"+Registro.get(57)+"-"+Registro.get(58)+"|"+	Registro.get(59)+"-"+Registro.get(60)+"-"+Registro.get(61)+"|"+	Registro.get(62)+"|"+
					 			Registro.get(63)+"|"+Registro.get(64)+"|"+Registro.get(65)+"|"+
					 			Registro.get(66)+"|"+Registro.get(67)+"|"+Registro.get(68)+"|"+
					 			Registro.get(69)+"|"+Registro.get(70)+"|"+Registro.get(71)+"|"+
					 			Registro.get(72)+"|"+Registro.get(73)+"|"+Registro.get(74)+"|"+
					 			Registro.get(75)+"|"+Registro.get(76)+"|"+Registro.get(77)+"|"+
					 			Registro.get(78)+"|"+Registro.get(79)+"|"+Registro.get(80)+"|"+
					 			Registro.get(81)+"|"+Registro.get(82)+"|"+Registro.get(83)+"|"+
					 			Registro.get(84)+"|"+Registro.get(85)+"|"+Registro.get(86)+"|"+
					 			Registro.get(87)+"|"+Registro.get(88)+"|&";
				NumTerminadas += 1;
			}
			
			CadenaArchivo = CadenaArchivo.replace("\n", ". "); 	//Se elimina los saltos de linea dentro de la informacion digitada por el tecnico
			CadenaArchivo = CadenaArchivo.replace("&", "\n");	//Se hace salto de linea por cada revision notificada o terminada que exista
			
			/*if(!this.RealizadoArch.CrearArchivo(UpLoadTrabajoRealizado.ArchivoCarga, CadenaArchivo)){
				Toast.makeText(this.CtxTrabajoRealizado,"Imposible crear el archivo de carga.", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this.CtxTrabajoRealizado,"Enviando \n"+NumNotificadas+" revisiones notificaciones. \n"+NumTerminadas+" revisiones terminadas, por favor espere...", Toast.LENGTH_SHORT).show();	
		    }*/  		
    	}

    	
    	
    	
    	@Override
    	protected SoapPrimitive doInBackground(Void... params) {
    		try{
    			/*String pda = RealizadoSQL.SelectShieldWhere("db_parametros", "valor", "item='pda'");			
        		SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
    			so.addProperty("Informacion", this.RealizadoArch.FileToBytes(ArchivoCarga));
    			so.addProperty("PDA", pda);	
    			SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			new MarshalBase64().register(sse);
    			sse.dotNet=true;
    			sse.setOutputSoapObject(so);
    			HttpTransportSE htse=new HttpTransportSE(URL);
    			htse.call(SOAP_ACTION, sse);
    			response=(SoapPrimitive) sse.getResponse();*/
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return response;
    	}

    	@Override
    	protected void onPostExecute(SoapPrimitive rta) {
    		/*this.RealizadoArch.DeleteFolderOrFile(UpLoadTrabajoRealizado.ArchivoCarga);
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
							this.RealizadoSQL.BorraRegistro("db_notificaciones", "revision = '" + OrdenesOk[1] + "'");
						}else if(OrdenesOk[2].equals("Terminada")){
							this.RealizadoSQL.BorraRegistro("db_desviaciones", "revision = '" + OrdenesOk[1] + "'");
						}
						this.RealizadoSQL.BorraRegistro("db_solicitudes", "revision = '" + OrdenesOk[1] + "'");
					}
				}
				int NotificadaPendientes = this.RealizadoSQL.SelectCountWhere("db_notificaciones", "revision IS NOT NULL");	
				int TerminadaPendientes = this.RealizadoSQL.SelectCountWhere("db_desviaciones", "revision IS NOT NULL");	
				MensajeDialog.DialogoInformativo("ESTADO DEL ENVIO","Respuesta Del Servidor \n"+resultado+" \n"+NotificadaPendientes + " revisiones notificadas pendientes por enviar.\n"+TerminadaPendientes + " revisiones terminadas pendientes por enviar.");
    		}*/
    	}	
    }

	
	private class UpLoadTrabajoSinRealizar extends AsyncTask<Void,Void,SoapPrimitive>{
    	//Variables para contexto de mensajes a visualizar y conexion a la base de datos
		private Context CtxTrabajoSinRealizar;
    	private SQLite SinRealizarSQL;
    	private Archivos SinRealizarArch;
    	private Dialogos MensajeDialog; 	
    	SoapPrimitive response = null;
    	
    	//Variables con la informacion del web service
    	private static final String METHOD_NAME	= "RevisionesDevueltas";
    	private static final String SOAP_ACTION	= "RevisionesDevueltas";
    	
    	//Variables de ruta y archivo a crear para cargar en el servidor
    	private String 	DirectorioCarga = null;
    	static final String ArchivoCarga = "TrabajoSinRealizar.txt";
    	
    	private UpLoadTrabajoSinRealizar(Context context, String DirectorioArchivo){
    		this.CtxTrabajoSinRealizar 	= context;
    		this.DirectorioCarga 		= DirectorioArchivo;
    		this.SinRealizarSQL 		= new SQLite(this.CtxTrabajoSinRealizar, this.DirectorioCarga, "BdEAAV_Android");
    		//this.SinRealizarArch 		= new Archivos(this.CtxTrabajoSinRealizar, this.DirectorioCarga);
    		MensajeDialog 				= new Dialogos(this.CtxTrabajoSinRealizar);    		
    	}
    	 
    	protected void onPreExecute(){
    		int NumRegistros = 0;
    		String CadenaArchivo = "";    		
    		ArrayList<ArrayList<String>> InfSendRevision;
    		InfSendRevision = new ArrayList<ArrayList<String>>();
    		
    		//InfSendRevision = SinRealizarSQL.SelectDataKeyValue("db_solicitudes", "revision", "estado = 0");			
			ListIterator<ArrayList<String>> iCiclo = InfSendRevision.listIterator();
			while(iCiclo.hasNext()){
				ArrayList<String> Registro = iCiclo.next();				
				CadenaArchivo +=Registro.get(0)+"|\n";
				NumRegistros += 1;
			}
			
			
			/*if(!this.SinRealizarArch.CrearArchivo(UpLoadTrabajoSinRealizar.ArchivoCarga, CadenaArchivo)){
				Toast.makeText(this.CtxTrabajoSinRealizar,"Imposible crear el archivo de carga.", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this.CtxTrabajoSinRealizar,"Enviando "+NumRegistros+" revisiones sin realizar, por favor espere...", Toast.LENGTH_SHORT).show();	
			}*/
		}

    	@Override
    	protected SoapPrimitive doInBackground(Void... params) {
    		try{
    			//String pda = SinRealizarSQL.SelectShieldWhere("db_parametros", "valor", "item='pda'");			
        		SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
    			//so.addProperty("Informacion", this.SinRealizarArch.FileToBytes(ArchivoCarga));
    			//so.addProperty("PDA", pda);	
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
    		/*this.SinRealizarArch.DeleteFolderOrFile(UpLoadTrabajoSinRealizar.ArchivoCarga);
    		if(rta==null) {
    			MensajeDialog.DialogoInformativo("ESTADO DE LA CONEXION","Error, no se ha obtenido respuesta del servidor.");
    		}else{
    			String resultado = rta.toString();
				String ListaRetorno[] = resultado.split("\\|");
				resultado = resultado.replace("|", "\n");
				
				for(int i=0;i<ListaRetorno.length;i++){
					String OrdenesOk[] = ListaRetorno[i].split("--");
					if(OrdenesOk[0].equals("OK")){
						this.SinRealizarSQL.BorraRegistro("db_solicitudes", "revision = '" + OrdenesOk[1] + "' AND estado = 0");
					}
				}
				int pendientes = this.SinRealizarSQL.SelectCountWhere("db_solicitudes", "estado = 0");				
				MensajeDialog.DialogoInformativo("ESTADO DEL ENVIO","Respuesta Del Servidor \n"+resultado+" \n"+pendientes + " registros pendientes por enviar.");
    		}*/
    	}	
    }
	
	
	

	//Clase privada para descargar el trabajo asignado
    private class DownLoadTrabajoProgramado extends AsyncTask<Void,Void,Void>{
    	static final String ArchivoDescarga = "TrabajoProgramado.txt";
    	private ClassRevision 		FcnRevision;
    	private ClassConfiguracion 	FcnConfiguracion;
    	private Archivos 			FcnArchivos;
    	
    	private Dialogos 	MensajeDialog;
    	private Context 	CtxTrabajoProgramado;
    	private String 		DirectorioDescarga = null;
    	private String 		_servidor = null;
    	private String 		_puerto = null;
    	private String 		_pagina = null;
    	private String 		_pda = null;
    	
    	private DownLoadTrabajoProgramado(Context context, String DirectorioArchivo){
    		this.CtxTrabajoProgramado 	= context;
    		this.DirectorioDescarga		= DirectorioArchivo;
    		MensajeDialog 			= new Dialogos(this.CtxTrabajoProgramado);
    		this.FcnRevision		= new ClassRevision(this.CtxTrabajoProgramado, this.DirectorioDescarga);
    		this.FcnConfiguracion 	= new ClassConfiguracion(this.CtxTrabajoProgramado, this.DirectorioDescarga);
    		this.FcnArchivos 		= new Archivos(this.CtxTrabajoProgramado,this.DirectorioDescarga,10);
    	}
    	
    	protected void onPreExecute(){
    		Toast.makeText(this.CtxTrabajoProgramado,"Conectando con el servidor.", Toast.LENGTH_SHORT).show();
    		_servidor 	= this.FcnConfiguracion.getServidor();
			_puerto 	= this.FcnConfiguracion.getPuerto();
			_pagina 	= this.FcnConfiguracion.getServicio();
			_pda 		= this.FcnConfiguracion.getEquipo();
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
    		if(this.FcnArchivos.ExistFolderOrFile(this.DirectorioDescarga+File.separator +ArchivoDescarga)){
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
