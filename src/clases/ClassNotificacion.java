package clases;

import Miscelanea.DateTime;
import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;
import eaav.android_v1.Loggin;

public class ClassNotificacion {
	private DateTime 		FcnDateTime ;
	private SQLite 			FcnSQL;
	private Context 		_ctxNotificacion;
	private String 			_folderAplicacion;
	private ContentValues 	_tempRegistro = new ContentValues();

	public ClassNotificacion(Context _ctx, String _folder){
		this._ctxNotificacion	= _ctx;
		this._folderAplicacion	= _folder;
		this.FcnDateTime		= new DateTime();
		this.FcnSQL				= new SQLite(this._ctxNotificacion, this._folderAplicacion, Loggin.NOMBRE_DATABASE);
	}
	
	public String getFechaNotificacion(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_notificaciones", "fecha_notificacion", "revision='"+_revision+"'");
	}
	
	public String getJornadaNotificacion(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_notificaciones", "jornada_notificacion", "revision='"+_revision+"'");
	}
	
	public String getLectura(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_notificaciones", "lectura", "revision='"+_revision+"'");
	}
	
	public String getMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_notificaciones", "medidor", "revision='"+_revision+"'");
	}
	
	public String getPrecinto(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_notificaciones", "precinto", "revision='"+_revision+"'");
	}
	
	public String getObservacion(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_notificaciones", "observacion", "revision='"+_revision+"'");
	}
	
	public String getMotivo(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_notificaciones", "motivo", "revision='"+_revision+"'");
	}
	
	public boolean existeRegistroNotificacion(String _revision){
		return this.FcnSQL.ExistRegistros("db_notificaciones", "revision='"+_revision+"'");
	}
	
	public void registrarNotificacion(String _revision){
		this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_solicitudes", "revision,codigo,nombre,direccion,serie,ciclo,promedio,visita", "revision='"+_revision+"'");
		this._tempRegistro.put("fecha_visita", this.FcnDateTime.GetFecha());
		this._tempRegistro.put("hora_visita", this.FcnDateTime.GetHora());
		this._tempRegistro.put("lectura", "");
		this._tempRegistro.put("medidor", "");
		this._tempRegistro.put("precinto", "");
		this._tempRegistro.put("observacion", "");
		this._tempRegistro.put("fecha_notificacion", this.FcnDateTime.GetFecha());
		this._tempRegistro.put("jornada_notificacion", "am");
		this._tempRegistro.put("motivo", "");
		if(this.FcnSQL.InsertRegistro("db_notificaciones", this._tempRegistro)){
			Toast.makeText(this._ctxNotificacion,"Datos guardados correctamente.", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this._ctxNotificacion,"Error crear el registro de notificacion para la revision "+_revision, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setFechaNotificacion(String _revision, String _fecha){
		this._tempRegistro.clear();
		this._tempRegistro.put("fecha_notificacion", _fecha);
		this.FcnSQL.UpdateRegistro("db_notificaciones", this._tempRegistro, "revision='"+_revision+"'");		
	}
	
	public void setJornadaNotificacion(String _revision, String _jornada){
		this._tempRegistro.clear();
		this._tempRegistro.put("jornada_notificacion", _jornada);
		this.FcnSQL.UpdateRegistro("db_notificaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	public void setLectura(String _revision, String _lectura){
		this._tempRegistro.clear();
		this._tempRegistro.put("lectura", _lectura);
		this.FcnSQL.UpdateRegistro("db_notificaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	public void setMedidor(String _revision, String _medidor){
		this._tempRegistro.clear();
		this._tempRegistro.put("medidor", _medidor);
		this.FcnSQL.UpdateRegistro("db_notificaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	public void setPrecinto(String _revision, String _precinto){
		this._tempRegistro.clear();
		this._tempRegistro.put("precinto", _precinto);
		this.FcnSQL.UpdateRegistro("db_notificaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	public void setObservacion(String _revision, String _observacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("observacion", _observacion);
		this.FcnSQL.UpdateRegistro("db_notificaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	public void setMotivo(String _revision, String _motivo){
		this._tempRegistro.clear();
		this._tempRegistro.put("motivo", _motivo);
		this.FcnSQL.UpdateRegistro("db_notificaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
}
