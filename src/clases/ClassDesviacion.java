package clases;

import java.util.ArrayList;

import Miscelanea.DateTime;
import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;
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
	
	
	public void setDatosMedidor(String _revision, String _medidor, String _numero, String _marca, String _lectura, String _diametro){
		if(_medidor.equals("Individual")){
			this._tempRegistro.clear();
			this._tempRegistro.put("serieindividual", _numero);
			this._tempRegistro.put("marcaindividual", _marca);
			this._tempRegistro.put("lecturaindividual", _lectura);
			this._tempRegistro.put("diametroindividual", _diametro);
		}else if(_medidor.equals("Totalizador")){
			this._tempRegistro.clear();
			this._tempRegistro.put("serietotalizador", _numero);
			this._tempRegistro.put("marcatotalizador", _marca);
			this._tempRegistro.put("lecturatotalizador", _lectura);
			this._tempRegistro.put("diametrototalizador", _diametro);
		}
		
		if(this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'")){
			Toast.makeText(this._ctxDesviacion,"Datos del medidor "+_medidor+" guardados correctamente.", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this._ctxDesviacion,"Error al guardar los datos del medidor "+_medidor+".", Toast.LENGTH_LONG).show();
		}
	}
	
	
	public void setAcueducto(String _revision, String _acueducto){
		this._tempRegistro.clear();
		this._tempRegistro.put("acueducto", _acueducto);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");		
	}
	
	
	public void setEscapeCamaraMedidor(String _revision, String _escape){
		this._tempRegistro.clear();
		this._tempRegistro.put("escapecamara", _escape);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");		
	}
	
	
	public void setServicioDirecto(String _revision, String _servicioDirecto){
		this._tempRegistro.clear();
		this._tempRegistro.put("serviciodirecto", _servicioDirecto);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");		
	}
	
	
	public void setBypass(String _revision, String _bypass){
		this._tempRegistro.clear();
		this._tempRegistro.put("bypass", _bypass);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");	
	}
	
	
	public void setCamaraMedidor(String _revision, String _camaraMedidor){
		this._tempRegistro.clear();
		this._tempRegistro.put("camaramedidor", _camaraMedidor);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setEstadoCamaraMedidor(String _revision, String _estadoCamaraMedidor){
		this._tempRegistro.clear();
		this._tempRegistro.put("estadocamara", _estadoCamaraMedidor);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setSegundoConcepto(String _revision, String _concepto){
		this._tempRegistro.clear();
		this._tempRegistro.put("segundoconcepto", _concepto);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setRespuestaDesviacion(String _revision, String _respuesta){
		this._tempRegistro.clear();
		this._tempRegistro.put("respuestadesviacion", _respuesta);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	public void setDiagnostico(String _revision, String _diagnostico){
		this._tempRegistro.clear();
		this._tempRegistro.put("diagnostico", _diagnostico);
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
	}
	
	
	/*Inicio de funciones para consulta de datos*/
	public void getPrecinto(String _revision){
		
	}
	
	
	public void getActividad(String _revision){
		
	}
	
	
	public void getEstrato(String _revision){
		
	}
	
	
	public void getEstado(String _revision){
		
	}
	
	public String getHabitado(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "habitado", "revision='"+_revision+"'");
	}
	
	public int getPersonas(String _revision){
		return this.FcnSQL.IntSelectShieldWhere("db_desviaciones", "residentes", "revision='"+_revision+"'");
	}
	
	
	public void getTipo(String _revision){
		
	}
	
	
	public void getServicioAcueducto(String _revision){
		
	}
	
	
	public void getServicioAlcantarrillado(String _revision){
		
	}
	
	
	public void getArea(String _revision){
		
	}
	
	
	public int getPisos(String _revision){
		return this.FcnSQL.IntSelectShieldWhere("db_desviaciones", "pisos", "revision='"+_revision+"'");	
	}
	
	
	public String getUso(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "uso", "revision='"+_revision+"'");	
	}
	
	
	public String getNombreUsuario(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "nombreusuario", "revision='"+_revision+"'");		
	}
	
	
	public String getCedulaUsuario(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "cedulausuario", "revision='"+_revision+"'");
	}
	
	
	public void getNombreTestigo(String _revision){
		
	}
	
	
	public void getCedulaTestigo(String _revision){
		
	}
	
	
	public String getCamaraMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "camaramedidor", "revision='"+_revision+"'");
	}
	
	public String getEstadoCamaraMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "estadocamara", "revision='"+_revision+"'");
	}
	
	public String getObservacion(String _revision){
		String _observacion = "";
		_observacion = 	"Se realiza visita tecnica en presencia de " + getNombreUsuario(_revision) + ",identificado (a) con cedula " + getCedulaUsuario(_revision) + ", quien no " +
    					"hace uso del derecho que le otorga el art. 12 de la resolucion CRA 413 de 2006 de estar asesorado de un tecnico particular a su costo, predio de " + getPisos(_revision) + " pisos, habitado por " + getHabitado(_revision) + ", numero de habitantes " + getPersonas(_revision) + ", de uso " + getUso(_revision);
    	
    	if(getCamaraMedidor(_revision).equals("Grande")||getCamaraMedidor(_revision).equals("Pequena")){
    		if(getEstadoCamaraMedidor(_revision).equals("Buena")){
    			_observacion += ", cajilla en buen estado ";
    		}else if(getEstadoCamaraMedidor(_revision).equals("Regular")){
    			_observacion += ", cajilla en estado regular ";
        	}else{
        		_observacion += ", cajilla en mal estado ";
        	}
    	}else{
    		_observacion += ", predio sin cajilla ";
    	}
    	
    	/*if(_RtaDestruido.getSelectedItem().toString().equals("No")&&(_RtaVidrioIlegible.getSelectedItem().toString().equals("No"))){
    		ObservacionSistema += " y medidor visualmente en buen estado.";
    	}else{
    		ObservacionSistema += " y medidor visualmente en mal estado.";
    	}
    	
    	//SQL.abrir();
		//SQL.SelectData(	RtaCamposDesviacion, 												//Se consulta la informacion basica
		//				"db_desviaciones", 
		//				"hermeticidadreginternos,hermeticidadequipomedida,hermeticidadfugas,hermeticidadfugaimperceptible,hermeticidadfugavisible,estadointernas",
		//				"revision = '" + Solicitud + "'");
		//SQL.cerrar();
    	
		if(RtaCamposDesviacion.get(0).equals("Si")&&(RtaCamposDesviacion.get(1).equals("Si"))){
			ObservacionSistema += " Se realizaron pruebas de hermeticidad encontrandose ";
			if((RtaCamposDesviacion.get(3).equals("No"))&&(RtaCamposDesviacion.get(4).equals("No"))){
				ObservacionSistema += "conforme ";
			}else{
				ObservacionSistema += "no conforme, ";
			}	
		}
    	
    	ObservacionSistema += "instalaciones hidraulicas en ";
    	if(RtaCamposDesviacion.get(5).indexOf("Malo")==-1){
    		ObservacionSistema += "buen estado.";
    	}else{
    		ObservacionSistema += "mal estado.";
    	}*/
    	//Verificar el estado de las instalaciones internas
		return _observacion;
	}
	
	
	
	
	public ArrayList<String> getClaseItems(){
		ArrayList<String> _items = new ArrayList<String>();
		_items.clear();
		_items.add("");
		_items.add("Elementos");
		_items.add("Estanqueidad");
		_items.add("Instalaciones");
		_items.add("Medidor");		
		return _items;
	}
	
	
	public ArrayList<String> getSubClaseItems(String _itemClase){
		ArrayList<String> _subItems = new ArrayList<String>();
		_subItems.clear();
		_subItems.add("");
		if(_itemClase.equals("Elementos")){
			_subItems.add("Cisterna");
			_subItems.add("Ducha");
			_subItems.add("Instalaciones Internas");
			_subItems.add("Lavadero");
			_subItems.add("Lavamanos");
			_subItems.add("Lavaplatos");
			_subItems.add("Piscina");
			_subItems.add("Subterraneo");
			_subItems.add("Tanque Elevado");
		}else if(_itemClase.equals("Estanqueidad")){
			_subItems.add("Tanque Elevado");
			_subItems.add("Tanque Lavadero");
			_subItems.add("Tanque Subterraneo");
		}else if(_itemClase.equals("Instalaciones")){
			_subItems.add("Instalaciones");
		}else if(_itemClase.equals("Medidor")){
			_subItems.add("Medidor");
		}				
		return _subItems;
	}
}
