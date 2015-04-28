package clases;

import java.text.SimpleDateFormat;

import eaav.android_v1.FormLoggin;

import Miscelanea.SQLite;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;

public class ClassFlujoInformacion {
    private Context         context;
    private String			DiretorioConexion;
    private String          _campos[];
    private ContentValues   _tempRegistro;

    private SQLite 			FcnSQL;

    private String currentDateandTime;
    ProgressDialog _pDialog;


    public ClassFlujoInformacion(Context _ctx,String Directorio){
        this.context        = _ctx;
        this.DiretorioConexion = Directorio;
        this._tempRegistro  = new ContentValues();
        this.FcnSQL				= new SQLite(this.context, this.DiretorioConexion, FormLoggin.NOMBRE_DATABASE);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        currentDateandTime = sdf.format(date);

    }


    public void EliminarParametros(){
        /**Eliminar los parametros previamente cargados**/
        this.FcnSQL.DeleteRegistro("db_parametros_valores","item is not null");       
    }



    public void CargarParametros(String _informacion, String _delimitador){
        /**Se inicia el registro de los nuevos parametros**/
        this._campos = _informacion.split(_delimitador);
        this._tempRegistro.clear();
        this._tempRegistro.put("item",this._campos[0]);
        this._tempRegistro.put("valor",this._campos[1]);
        this._tempRegistro.put("proceso",this._campos[2]);
        this.FcnSQL.InsertRegistro("db_parametros_valores",this._tempRegistro);
        }
  
}
