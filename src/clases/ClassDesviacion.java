package clases;

import java.util.ArrayList;

import Miscelanea.DateTime;
import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;
import eaav.android_v1.FormLoggin;

public class ClassDesviacion {
	private DateTime 	FcnDateTime ;
	private SQLite 		FcnSQL;
	private Context 	_ctxDesviacion;
	private String 		_folderAplicacion;
	
	private ArrayList<ContentValues>	_tempTabla 		= new ArrayList<ContentValues>();
	private ContentValues 				_tempRegistro 	= new ContentValues();
	

	
	public ClassDesviacion(Context _ctx, String _folder){
		this._ctxDesviacion		= _ctx;
		this._folderAplicacion	= _folder;
		this.FcnDateTime		= new DateTime();
		this.FcnSQL				= new SQLite(this._ctxDesviacion, this._folderAplicacion, FormLoggin.NOMBRE_DATABASE);
	}
	
	
	public boolean existeRegistroDesviacion(String _revision){
		return this.FcnSQL.ExistRegistros("db_desviaciones", "revision='"+_revision+"'");
	}
	
	
	public void registrarDesviacion(String _revision){
		this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_solicitudes", "revision,codigo,nombre,direccion,serie,ciclo,promedio,uso", "revision='"+_revision+"'");
		this._tempRegistro.put("visita", this.FcnSQL.IntSelectShieldWhere("db_solicitudes", "visita", "revision='"+_revision+"'")+1);
		this._tempRegistro.put("fecha", this.FcnDateTime.GetFecha());
		this._tempRegistro.put("hora", this.FcnDateTime.GetHora());
		this.FcnSQL.InsertRegistro("db_desviaciones", this._tempRegistro);
	}
	
	
	public void setPrecinto(String _revision, String _precinto){
		this._tempRegistro.clear();
		this._tempRegistro.put("precinto", _precinto);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setActividad(String _revision, String _actividad){
		this._tempRegistro.clear();
		this._tempRegistro.put("actividad", _actividad);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setEstrato(String _revision, String _estrato){
		this._tempRegistro.clear();
		this._tempRegistro.put("estrato", _estrato);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setEstado(String _revision, String _estado){
		this._tempRegistro.clear();
		this._tempRegistro.put("estado", _estado);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	public void setHabitado(String _revision, String _habitado){
		this._tempRegistro.clear();
		this._tempRegistro.put("habitado", _habitado);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setPersonas(String _revision, String _personas){
		this._tempRegistro.clear();
		this._tempRegistro.put("residentes", _personas);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setTipo(String _revision, String _tipo){
		this._tempRegistro.clear();
		this._tempRegistro.put("tipo", _tipo);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setServicioAcueducto(String _revision, String _acueducto){
		this._tempRegistro.clear();
		this._tempRegistro.put("servicioacueducto", _acueducto);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setServicioAlcantarillado(String _revision, String _alcantarrillado){
		this._tempRegistro.clear();
		this._tempRegistro.put("servicioalcantarillado", _alcantarrillado);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setArea(String _revision, String _area){
		this._tempRegistro.clear();
		this._tempRegistro.put("area", _area);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setPisos(String _revision, String _pisos){
		this._tempRegistro.clear();
		this._tempRegistro.put("pisos", _pisos);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setUso(String _revision, String _uso){
		this._tempRegistro.clear();
		this._tempRegistro.put("uso", _uso);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setNombreUsuario(String _revision, String _nombreUsuario){
		this._tempRegistro.clear();
		this._tempRegistro.put("nombreusuario", _nombreUsuario);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setCedulaUsuario(String _revision, String _cedulaUsuario){
		this._tempRegistro.clear();
		this._tempRegistro.put("cedulausuario", _cedulaUsuario);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setNombreTestigo(String _revision, String _nombreTestigo){
		this._tempRegistro.clear();
		this._tempRegistro.put("nombretestigo", _nombreTestigo);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setCedulaTestigo(String _revision, String _cedulaTestigo){
		this._tempRegistro.clear();
		this._tempRegistro.put("cedulatestigo", _cedulaTestigo);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void getPrecinto(String _revision){
		
	}
	
	
	public void getActividad(String _revision){
		
	}
	
	
	public void getEstrato(String _revision){
		
	}
	
	
	public void getEstado(String _revision){
		
	}
	
	
	public void getPersonas(String _revision){
		
	}
	
	
	public void getTipo(String _revision){
		
	}
	
	
	public void getServicioAcueducto(String _revision){
		
	}
	
	
	public void getServicioAlcantarrillado(String _revision){
		
	}
	
	
	public void getArea(String _revision){
		
	}
	
	
	public void getPisos(String _revision){
		
	}
	
	
	public void getUso(String _revision){
		
	}
	
	
	public void getNombreUsuario(String _revision){
		
	}
	
	
	public void getCedulaUsuario(String _revision){
		
	}
	
	
	public void getNombreTestigo(String _revision){
		
	}
	
	
	public void getCedulaTestigo(String _revision){
		
	}
	
}
