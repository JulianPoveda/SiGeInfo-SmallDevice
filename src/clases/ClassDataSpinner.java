package clases;

import java.util.ArrayList;

import eaav.android_v1.FormLoggin;

import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;

public class ClassDataSpinner {

	private static Context context;
    private SQLite  FcnSQL;
    //private String DirectorioConexion;
    private static ClassDataSpinner ourInstance;
    private ArrayList<ContentValues> _tempTabla = new ArrayList<ContentValues>();
    
    public static ClassDataSpinner getInstance(Context _ctx){
        if(ourInstance==null){
            ourInstance = new ClassDataSpinner(_ctx);
        }
        else{
            context = _ctx;
        }
        return ourInstance;
    }

    public ClassDataSpinner(Context _ctx){
        this.context = _ctx;        
        //this.DirectorioConexion = Directorio;
        this.FcnSQL         = new SQLite(this.context, FormLoggin.CARPETA_RAIZ, FormLoggin.NOMBRE_DATABASE);
    }

    public ArrayList<String> getDataSpinner(String _spinner){
        _tempTabla.clear();
        ContentValues _tempRegistro = new ContentValues();
        ArrayList<String> tipologia = new ArrayList<String>();
        tipologia.clear();

        _tempTabla = FcnSQL.SelectData("db_parametros_valores","valor","item='"+_spinner+"'");
        for(int i=0;i<_tempTabla.size();i++){
            _tempRegistro = _tempTabla.get(i);
            tipologia.add(_tempRegistro.getAsString("valor"));
        }
        return tipologia;
    }

   /* public ArrayList<String> getDataSpinnerSubTipologia(String _spinner, String _tipologia){
        _tempTabla.clear();
        ContentValues _tempRegistro = new ContentValues();
        ArrayList<String> subTipologia = new ArrayList<String>();

        _tempTabla = FcnSQL.SelectData("valores_spinner","subtipologia","nombre_spinner='"+_spinner+"' AND tipologia='"+_tipologia+"'");
        for(int i=0;i<_tempTabla.size();i++){
            _tempRegistro = _tempTabla.get(i);
            subTipologia.add(_tempRegistro.getAsString("subtipologia"));
        }
        return subTipologia;
    }*/


}