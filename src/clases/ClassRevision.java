package clases;

import java.util.ArrayList;
import java.util.ListIterator;

import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;
import eaav.android_v1.FormLoggin;

public class ClassRevision {
	private SQLite 	FcnSQL;
	private Context _ctxRevision;
	private String _folderAplicacion;
	
	private ArrayList<ContentValues>	_tempTabla 		= new ArrayList<ContentValues>();
	private ContentValues 				_tempRegistro 	= new ContentValues();
	

	public ClassRevision(Context _ctx, String _folder){
		this._ctxRevision		= _ctx;
		this._folderAplicacion	= _folder;
		this.FcnSQL				= new SQLite(this._ctxRevision, this._folderAplicacion, FormLoggin.NOMBRE_DATABASE);
	}
	
	public String registrarRevisiones(ArrayList<String> _revisiones){
		String[] Campos={"Id_serial","Revision","Codigo","Nombre","Direccion","Ciclo","Ruta","Uso","Marca","Serie","Novedad1","Lectura1","Novedad2","Lectura2","Promedio","Visita","Estado","Jornada","Factura","Periodo_ini","Periodo_fin","Codigo_apertura"};
		String Registro[];
		String _retorno = "";
		
		ListIterator<String> iCiclo = _revisiones.listIterator();
		while(iCiclo.hasNext()){
			Registro = iCiclo.next().split("\\;",22);
			this._tempRegistro.clear();
			for(int i=0;i<Campos.length;i++){
				this._tempRegistro.put(Campos[i],Registro[i]);
			}
			
			if(this.FcnSQL.InsertRegistro("db_solicitudes", this._tempRegistro)){
				_retorno += this._tempRegistro.getAsString("Revision")+"\n";
    		}
		}
		return _retorno;
	}
	
	
	public ArrayList<ContentValues> getRevisiones(){
		this._tempTabla = this.FcnSQL.SelectData("db_solicitudes", "revision,marca,direccion,estado", "estado IS NOT NULL ORDER BY revision");
		return this._tempTabla;
	}
	
	public String getCodigo(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "codigo", "revision='"+_revision+"'");
	}

	public String getSerie(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "serie", "revision='"+_revision+"'");
	}
	
	public String getNombre(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "nombre", "revision='"+_revision+"'");
	}
	
	public String getDireccion(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "direccion", "revision='"+_revision+"'");
	}
	
	public String getMarca(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "marca", "revision='"+_revision+"'");
	}
	
	public String getCiclo(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "ciclo", "revision='"+_revision+"'");
	}
	
	public String getRuta(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "ruta", "revision='"+_revision+"'");
	}
	
	public String getLectura1(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "lectura1", "revision='"+_revision+"'");
	}
	
	public String getNovedad1(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "novedad1", "revision='"+_revision+"'");
	}
	
	
	public void setEstadoRevision(String _revision, int _estado){
		this._tempRegistro.clear();
		this._tempRegistro.put("estado",_estado);
		this.FcnSQL.UpdateRegistro("db_solicitudes", this._tempRegistro, "revision='"+_revision+"'");		
	}
	
	
	public boolean iniciarNotificacion(String _revision){
		boolean _retorno = false;
		if(this.FcnSQL.ExistRegistros("db_solicitudes", "estado IN (1,2) AND revision <>'"+_revision+"'")){
			_retorno = false;
		}else if(this.FcnSQL.ExistRegistros("db_solicitudes", "estado = '3' AND revision ='"+_revision+"'")){
			_retorno = false;
		}else if(this.FcnSQL.ExistRegistros("db_solicitudes", "estado IN (0,1) AND revision ='"+_revision+"'")){
			_retorno = true;
		}
		return _retorno;
	}
	
	public boolean iniciarDesviacion(String _revision){
		boolean _retorno = false;
		if(this.FcnSQL.ExistRegistros("db_solicitudes", "estado IN (1,2) AND revision <>'"+_revision+"'")){
			_retorno = false;
		}else if(this.FcnSQL.ExistRegistros("db_solicitudes", "estado = '3' AND revision ='"+_revision+"'")){
			_retorno = false;
		}else if(this.FcnSQL.ExistRegistros("db_solicitudes", "estado IN (0,2) AND revision ='"+_revision+"'")){
			_retorno = true;
		}
		return _retorno;
	}
	
	
}
