package eaav.android_v1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import clases.ClassConfiguracion;
import Miscelanea.Archivos;
import Miscelanea.SQLite;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore.Files;

public class UpLoadFoto extends AsyncTask<String, Integer, Integer> {
    private Intent              new_form;
    private ClassConfiguracion  FcnCfg;
    private Archivos            FcnArch;
    private SQLite              FcnSQL;

    private Context 			context;

    
    private Files				listaFotos[];
    
    private String URL;
    private String NAMESPACE;
    private String Respuesta   = "";
    
    private ArrayList<String>    _orden			= new ArrayList<String>();
    private ArrayList<String>    _fotos			= new ArrayList<String>();

    private static final String METHOD_NAME	= "UpLoadFotos";
    private static final String SOAP_ACTION	= "UpLoadFotos";

    SoapPrimitive 				response = null;
    ProgressDialog 				_pDialog;
    SoapObject 					so;
    SoapSerializationEnvelope 	sse;
    HttpTransportSE 			htse;

    public UpLoadFoto(Context _ctx){
        this.context    = _ctx;
        this.FcnCfg     = new ClassConfiguracion(this.context, FormLoggin.CARPETA_RAIZ);
        this.FcnSQL     = new SQLite(this.context, FormLoggin.CARPETA_RAIZ, FormLoggin.NOMBRE_DATABASE);
        this.FcnArch	= new Archivos(this.context, FormLoggin.CARPETA_RAIZ, 10);
    }

    
    protected void onPreExecute() {
        this.URL        = this.FcnCfg.getServidor() + ":" + this.FcnCfg.getPuerto() + "/" + this.FcnCfg.getServicio() + "/" + this.FcnCfg.getWebService();
        this.NAMESPACE  = this.FcnCfg.getServidor() + ":" + this.FcnCfg.getPuerto() + "/" + this.FcnCfg.getServicio();
        
        //this.listaFotos = this.FcnArch.ListaFotos(FormLoggin.CARPETA_FOTOS, true);
        
        
        File f = new File(FormLoggin.CARPETA_FOTOS);
		File[] fotos = this.FcnArch.ListaFotos(FormLoggin.CARPETA_FOTOS, true);
		for (int i=0;i<fotos.length;i++){
			if(!fotos[i].isDirectory()){
				String extension = getFileExtension(fotos[i]);
				if(extension.equals("jpeg")){
					String[] _foto = fotos[i].getName().split("_");
					//_tempRegistro.put(_foto[0], fotos[i].toString());
					_orden.add(_foto[0]);
					_fotos.add(fotos[i].toString());
					//new UpLoadFoto(this.TemporizadorCtx, this.FolderAplicacion).execute(_foto[0],fotos[i].toString());					
				}				
			}
		}
		
		_pDialog = new ProgressDialog(this.context);
        _pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        _pDialog.setMessage("Ejecutando operaciones...");
        _pDialog.setCancelable(false);
        _pDialog.setProgress(0);
        _pDialog.setMax(_fotos.size());
        _pDialog.show();
    }

    
    @Override
    protected Integer doInBackground(String... params) {
        int _retorno    = 0;
        for(int i=0;i<_fotos.size();i++){
        	 try{
	            this.so=new SoapObject(NAMESPACE, this.METHOD_NAME);                        
	            this.so.addProperty("usuario", params[0]);
	            this.so.addProperty("orden",_orden.get(i));
	            this.so.addProperty("informacion",this.FcnArch.FileToArrayBytes(_fotos.get(i)));
	            
	            this.sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            new MarshalBase64().register(sse);
	            this.sse.dotNet=true;
	            this.sse.setOutputSoapObject(so);
	            
	            this.htse=new HttpTransportSE(URL);
	            this.htse.call(SOAP_ACTION, sse);
	            response=(SoapPrimitive) sse.getResponse();
	
	            if(response==null) {
	                this.Respuesta = "-1";
	            }else if(response.toString().isEmpty()){
	                this.Respuesta = "-2";
	            }else {
	                this.FcnArch.DeleteFile(_fotos.get(i));
	                onProgressUpdate(i);
	            }
	        } catch (Exception e) {
	        	this.Respuesta = e.toString();
	        	_retorno = -4;
	        }finally{
	        	if(this.htse != null){
	        		this.htse.reset();
	                try {
	                	this.htse.getServiceConnection().disconnect();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }        
        }
        return _retorno;
    }

    @Override
    protected void onPostExecute(Integer rta) {
    	_pDialog.dismiss();

    }
    
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }
    
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
