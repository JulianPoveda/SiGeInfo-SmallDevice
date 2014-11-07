package clases;

import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;
import eaav.android_v1.FormLoggin;

public class ClassConfiguracion {
	private SQLite 	FcnSQL;
	private Context _ctxConfiguracion;
	private String 	_folderAplicacion;
	
	private ContentValues _tempRegistro = new ContentValues();


	public ClassConfiguracion(Context _ctx, String _folder){
		this._ctxConfiguracion	= _ctx;
		this._folderAplicacion	= _folder;
		this.FcnSQL				= new SQLite(this._ctxConfiguracion, this._folderAplicacion, FormLoggin.NOMBRE_DATABASE);
	}
	
	public String getVersion(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='version'");		
	}
	
	public String getServidor(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='servidor'");		
	}

	public String getPuerto(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='puerto'");	
	}
	
	public String getServicio(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='servicio'");	
	}
	
	public String getWebService(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='web_service'");	
	}
	
	public String getEquipo(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='pda'");	
	}
	
	public String getImpresora(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='impresora'");	
	}
	
	public String getNombreTecnico(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='nombre_tecnico'");	
	}
	
	public String getCedulaTecnico(){
		return this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item='cedula_tecnico'");	
	}
	
	public void setServidor(String _servidor){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _servidor);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='servidor'");
	}
	
	public void setPuerto(String _puerto){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _puerto);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='puerto'");
	}
	
	public void setServicio(String _servicio){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _servicio);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='servicio'");
	}
	
	public void setWebService(String _web_service){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _web_service);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='web_service'");
	}
	
	public void setEquipo(String _equipo){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _equipo);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='pda'");
	}
	
	public void setImpresora(String _impresora){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _impresora);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='impresora'");
	}
	
	public void setNombreTecnico(String _nombre){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _nombre);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='nombre_tecnico'");
	}
	
	public void setCedulaTecnico(String _cedula){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _cedula);
		this.FcnSQL.UpdateRegistro("db_parametros", this._tempRegistro, "item='cedula_tecnico'");
	}
}