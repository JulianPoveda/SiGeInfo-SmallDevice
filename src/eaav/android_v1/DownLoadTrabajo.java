package eaav.android_v1;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.kobjects.base64.Base64;
import android.widget.Toast;
import clases.ClassConfiguracion;
import clases.ClassFlujoInformacion;
import clases.ClassRevision;
import Miscelanea.Archivos;
import Miscelanea.Dialogos;

public class DownLoadTrabajo extends AsyncTask<String, Integer, Integer>{ //doInBakGround, Progress, onPostExecute

    private Archivos 			    FcnArch;    
    private ClassFlujoInformacion   FcnInformacion;
    private ClassConfiguracion      FcnConfiguracion;
    private ClassRevision 			FcnRevision;
    private Dialogos 				MensajeDialog;
    
	private Context ConnectServerContext;
	private String 	DirectorioConexionServer;
	
	private ArrayList<String> OrdenesTrabajo = new ArrayList<String>();
    

	//nusoap
	public String 		URL;			
	private String 		NAMESPACE; 	
	private String 		_servidor;
	private String 		_puerto;
	private String 		_pagina;
	private String 		_web_service;
	private String 		_pda;

    private static final String METHOD_NAME	= "DownLoadRevisiones";
    private static final String SOAP_ACTION	= "DownLoadRevisiones";
    SoapPrimitive 	response = null;
    ProgressDialog 	_pDialog;


    //Contructor de la clase
    public DownLoadTrabajo(Context context, String Directorio){
    	this.ConnectServerContext 		= context;
		this.DirectorioConexionServer 	= Directorio;
		this.FcnConfiguracion			= new ClassConfiguracion(this.ConnectServerContext, FormLoggin.CARPETA_RAIZ);
		this.FcnInformacion				= new ClassFlujoInformacion(this.ConnectServerContext,this.DirectorioConexionServer);
		this.FcnRevision				= new ClassRevision(this.ConnectServerContext, this.DirectorioConexionServer);
		this.MensajeDialog 				= new Dialogos(this.ConnectServerContext);
		
		_servidor 	= this.FcnConfiguracion.getServidor();
		_puerto 	= this.FcnConfiguracion.getPuerto();
		//_pagina 	= this.FcnConfiguracion.getServicio();
		_pagina		= "EAAV-Desviaciones/ServerPDA";
		_web_service= this.FcnConfiguracion.getWebService();
		_pda 		= this.FcnConfiguracion.getEquipo();				
        
    }


    //Operaciones antes de realizar la conexion con el servidor
    protected void onPreExecute(){
    	
    	this.URL 		= this._servidor+":"+this._puerto+"/"+this._pagina+"/"+this._web_service;
		this.NAMESPACE 	= this._servidor+":"+this._puerto+"/"+this._pagina;
    	
        Toast.makeText(this.ConnectServerContext,"Iniciando conexion con el servidor, por favor espere...", Toast.LENGTH_SHORT).show();
        _pDialog = new ProgressDialog(this.ConnectServerContext);
        _pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        _pDialog.setMessage("Ejecutando operaciones...");
        _pDialog.setCancelable(false);
        _pDialog.setProgress(0);
        _pDialog.setMax(100);
        _pDialog.show();
    }



    //Conexion con el servidor en busca de actualizaciones de trabajo programado
    @Override
    protected Integer doInBackground(String... params) {
        int _retorno = 0;
        try{
            SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
            so.addProperty("id_interno", params[0]);
            SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            sse.dotNet=true;
            sse.setOutputSoapObject(so);
            HttpTransportSE htse=new HttpTransportSE(URL);
            htse.call(SOAP_ACTION, sse);
            response=(SoapPrimitive) sse.getResponse();

            /**Inicio de tratamiento de la informacion recibida**/
            if(response.toString()==null) {
                _retorno = -1;
            }else if(response.toString().isEmpty()){
                _retorno = -2;
            }else{
                try {
                    //this.FcnInformacion.EliminarParametros();
                    String informacion[] = new String(Base64.decode(response.toString()), "ISO-8859-1").split("\\n");
                    this.OrdenesTrabajo.clear();
                    for(int i=0;i<informacion.length;i++){
                        //this.FcnInformacion.CargarParametros(informacion[i],"\\|");
                    	this.OrdenesTrabajo.add(informacion[i]);
                    	onProgressUpdate(i*100/informacion.length);
                    }
                    //this.FcnRevision.registrarRevisiones(OrdenesTrabajo);
                    _retorno = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    _retorno = -3;
                }
            }
        } catch (Exception e) {
            e.toString();
            _retorno = -4;
        }
        return _retorno;
    }



    //Operaciones despues de finalizar la conexion con el servidor
    @Override
    protected void onPostExecute(Integer rta) {
        if(rta==1){
            MensajeDialog.DialogoInformativo("ESTADO DE LA RECEPCION","Revisiones Recepcionadas \n"+this.FcnRevision.registrarRevisiones(this.OrdenesTrabajo)+" \n");
        }else if(rta==-1){
        	MensajeDialog.DialogoInformativo("ERROR","Intento fallido, el servidor no ha respondido.");
        }else if(rta==-2){
        	MensajeDialog.DialogoInformativo("ERROR","No hay nuevas ordenes pendientes para cargar.");
        }else{
        	MensajeDialog.DialogoInformativo("ERROR","Error desconocido.");
        }
        _pDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }
}



