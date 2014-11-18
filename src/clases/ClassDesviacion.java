package clases;

import java.util.ArrayList;

import Miscelanea.DateTime;
import Miscelanea.SQLite;
import Miscelanea.Utilidades;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;
import eaav.android_v1.FormLoggin;

public class ClassDesviacion {
	private Utilidades	FcnUtilidades;
	private DateTime 	FcnDateTime ;
	private SQLite 		FcnSQL;
	private Context 	_ctxDesviacion;
	private String 		_folderAplicacion;
	
	private ContentValues 				_tempRegistro 	= new ContentValues();
	private ContentValues 				_tempRegistro1 	= new ContentValues();

	
	public ClassDesviacion(Context _ctx, String _folder){
		this._ctxDesviacion		= _ctx;
		this._folderAplicacion	= _folder;
		this.FcnDateTime		= new DateTime();
		this.FcnUtilidades		= new Utilidades();
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
	
	public void eliminarDesviacion(String _revision){
		this.FcnSQL.DeleteRegistro("db_desviaciones", "revision='"+_revision+"'");
	}
	
	public void setFechaCierre(String _revision){
		this._tempRegistro.clear();
		this._tempRegistro.put("horacierre", FcnDateTime.GetHora());
		this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'");
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
	
	
	public String getActividad(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "actividad", "revision='"+_revision+"'");
	}
	
	public String getAcueducto(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "acueducto", "revision='"+_revision+"'");
	}
	
	public void getEstrato(String _revision){
		
	}
	
	public String getSegundoConcepto(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "segundoconcepto", "revision='"+_revision+"'");
	}
	
	public String getRespuestaDesviacion(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "respuestadesviacion", "revision='"+_revision+"'");
	}
	
	public String getEstado(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "estado", "revision='"+_revision+"'");
	}
	
	public String getHabitado(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "habitado", "revision='"+_revision+"'");
	}
	
	public String getPersonas(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "residentes", "revision='"+_revision+"'");
	}
	
	public String getObservacion(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "diagnostico", "revision='"+_revision+"'");
	}
	
	
	public String getTipo(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "tipo", "revision='"+_revision+"'");
	}
	
	public String getServicioDirecto(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "serviciodirecto", "revision='"+_revision+"'");
	}
	
	public String getBypass(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "bypass", "revision='"+_revision+"'");
	}
	
	public String getSerieMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "serieindividual", "revision='"+_revision+"'");
	}
	
	public String getMarcaMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "marcaindividual", "revision='"+_revision+"'");
	}
	
	public String getLecturaMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "lecturaindividual", "revision='"+_revision+"'");
	}
	
	public String getDiametroMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "diametroindividual", "revision='"+_revision+"'");
	}
	
	public void getServicioAcueducto(String _revision){
		
	}
	
	
	public void getServicioAlcantarrillado(String _revision){
		
	}
	
	
	public String getArea(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "area", "revision='"+_revision+"'");
	}
	
	
	public String getPisos(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "pisos", "revision='"+_revision+"'");	
	}
	
	
	public String getUso(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "uso", "revision='"+_revision+"'");	
	}
	
	public String getRegistroPaso(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorregpaso", "revision='"+_revision+"'");	
	}
	
	public String getRegistroAntifraude(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorregantifraude", "revision='"+_revision+"'");	
	}
	
	/*public String getMedidorDestruido(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidordestruido", "revision='"+_revision+"'");	
	}*/
	
	public String getNombreUsuario(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "nombreusuario", "revision='"+_revision+"'");		
	}
	
	
	public String getCedulaUsuario(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "cedulausuario", "revision='"+_revision+"'");
	}
	
	
	public String getNombreTestigo(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "nombretestigo", "revision='"+_revision+"'");	
	}
	
	
	public String getCedulaTestigo(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "cedulatestigo", "revision='"+_revision+"'");
	}
	
	
	public String getCamaraMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "camaramedidor", "revision='"+_revision+"'");
	}
		
	public String getEstadoCamaraMedidor(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "estadocamara", "revision='"+_revision+"'");
	}
	
	public String getEscapeCamara(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "escapecamara", "revision='"+_revision+"'");
	}
	
	public String getMedidorDestruido(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorinvertido", "revision='"+_revision+"'");
	}
	
	public String getMedidorInvertido(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidordestruido", "revision='"+_revision+"'");
	}
	
	public String getMedidorIlegible(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorilegible", "revision='"+_revision+"'");
	}
	
	public String getMedidorPrecintoRoto(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorprecintoroto", "revision='"+_revision+"'");
	}
	
	public String getHermeticidadRegInternos(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadreginternos", "revision='"+_revision+"'");
	}
	
	public String getHermeticidadEquipoMedida(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadequipomedida", "revision='"+_revision+"'");
	}
	
	public String getHermeticidadFugas(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadfugas", "revision='"+_revision+"'");
	}
	
	public String getHermeticidadFugaImperceptible(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadfugaimperceptible", "revision='"+_revision+"'");
	}
	
	public String getHermeticidadFugaVisible(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadfugavisible", "revision='"+_revision+"'");
	}
	
	public String getInstalacionesHidraulicas(String _revision){
		return this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "estadointernas", "revision='"+_revision+"'");
	}
	
	public String generarObservacion(String _revision){
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
    	
    	if(getMedidorDestruido(_revision).toString().equals("No")&&(getMedidorIlegible(_revision).equals("No"))){
    		_observacion += " y medidor visualmente en buen estado.";
    	}else{
    		_observacion += " y medidor visualmente en mal estado.";
    	}
    	
		if(getHermeticidadRegInternos(_revision).equals("Si")&&(getHermeticidadEquipoMedida(_revision).equals("Si"))){
			_observacion += " Se realizaron pruebas de hermeticidad encontrandose ";
			if(getHermeticidadFugaImperceptible(_revision).equals("No") && getHermeticidadFugaVisible(_revision).equals("No")){
				_observacion += "conforme ";
			}else{
				_observacion += "no conforme, ";
			}	
		}
    	
		_observacion += "instalaciones hidraulicas en ";
    	if(getInstalacionesHidraulicas(_revision).equals("Bueno")){
    		_observacion += "buen estado.";
    	}else{
    		_observacion += "mal estado.";
    	}
    	return _observacion;
	}
	
	public void registrarDatosMedidor(String _revision, String _registroPaso, String _registroAntifraude, String _destruido, String _invertido, String _ilegible, String _precintoRoto){
		this._tempRegistro.clear();
		this._tempRegistro.put("medidorregpaso", _registroPaso);
		this._tempRegistro.put("medidorregantifraude", _registroAntifraude);
		this._tempRegistro.put("medidordestruido", _destruido);
		this._tempRegistro.put("medidorinvertido", _invertido);
		this._tempRegistro.put("medidorilegible", _ilegible);
		this._tempRegistro.put("medidorprecintoroto", _precintoRoto);	
		if(this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'")){
			Toast.makeText(this._ctxDesviacion,"Datos de inspeccion al medidor guardados correctamente.", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this._ctxDesviacion,"Error al registrar los datos de inspeccion al medidor.", Toast.LENGTH_LONG).show();
		}
	}
	
	
	//funcion para validar las instalaciones
	private boolean validarVisitaTecnicaInstalaciones(String _regInternos, String _equipoMedida, String _revisionFugas, String _fugaImperceptible, String _fugaVisible){
		boolean ValorRetorno = false;
		if(_regInternos.isEmpty()||_equipoMedida.isEmpty()||_revisionFugas.isEmpty()||_fugaImperceptible.isEmpty()||_fugaVisible.isEmpty()){
			ValorRetorno = false;
		}else{
			ValorRetorno = true;
		}
		return ValorRetorno;
	}
		
	public void registrarInstalaciones(String _revision, String _regInternos, String _equipoMedida, String _revisionFugas, String _fugaImperceptible, String _fugaVisible){
		if(this.validarVisitaTecnicaInstalaciones(_regInternos, _equipoMedida, _revisionFugas, _fugaImperceptible, _fugaVisible)){
			this._tempRegistro.clear();
			this._tempRegistro.put("hermeticidadreginternos", _regInternos);
			this._tempRegistro.put("hermeticidadequipomedida", _equipoMedida);
			this._tempRegistro.put("hermeticidadfugas", _revisionFugas);
			this._tempRegistro.put("hermeticidadfugaimperceptible", _fugaImperceptible);
			this._tempRegistro.put("hermeticidadfugavisible", _fugaVisible);
			if(this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'")){
				Toast.makeText(this._ctxDesviacion,"Datos de instalaciones guardados correctamente.", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this._ctxDesviacion,"Error al registrar los datos de instalaciones.", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this._ctxDesviacion,"No ha seleccionado datos validos.", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	public ContentValues getDatosDesviacion(String _revision){
		this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_desviaciones", 
															"tipo,nombreusuario,nombretestigo,cedulausuario,cedulatestigo,estrato,area,pisos,uso,actividad,residentes,estado,habitado," +
															"acueducto,camaramedidor,escapecamara,estadocamara,serviciodirecto,bypass,serieindividual,marcaindividual,lecturaindividual," +
															"diametroindividual,serietotalizador,marcatotalizador,lecturatotalizador,diametrototalizador,segundoconcepto,respuestadesviacion," +
															"diagnostico,precinto,medidorregpaso,medidorregantifraude,medidordestruido,medidorinvertido,medidorilegible,medidorprecintoroto",
															"revision = '" + _revision + "'");
		return this._tempRegistro;
	}
	
	public ContentValues getDatosMedidor(String _revision,String _tipoMedidor){
		this._tempRegistro.clear();
		if(_tipoMedidor.equals("Individual")){
			this._tempRegistro.put("Numero", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "serieindividual", "revision = '" + _revision + "'"));
			this._tempRegistro.put("Marca", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "marcaindividual", "revision = '" + _revision + "'"));
			this._tempRegistro.put("Lectura", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "lecturaindividual", "revision = '" + _revision + "'"));
			this._tempRegistro.put("Diametro", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "diametroindividual", "revision = '" + _revision + "'"));	
		}else if(_tipoMedidor.equals("Totalizador")){
			this._tempRegistro.put("Numero", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "serietotalizador", "revision = '" + _revision + "'"));
			this._tempRegistro.put("Marca", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "marcatotalizador", "revision = '" + _revision + "'"));
			this._tempRegistro.put("Lectura", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "lecturatotalizador", "revision = '" + _revision + "'"));
			this._tempRegistro.put("Diametro", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "diametrototalizador", "revision = '" + _revision + "'"));
		}		
		return this._tempRegistro;
	}
	
	
	public ContentValues getDatosInstalaciones(String _revision){
		this._tempRegistro.clear();
		this._tempRegistro.put("RegistrosInternos", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadreginternos", "revision = '" + _revision + "'"));
		this._tempRegistro.put("VerificacionEquipo", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadequipomedida", "revision = '" + _revision + "'"));
		this._tempRegistro.put("RevisionFugas", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadfugas", "revision = '" + _revision + "'"));
		this._tempRegistro.put("FugaImperceptible", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadfugaimperceptible", "revision = '" + _revision + "'"));	
		this._tempRegistro.put("FugaVisible", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "hermeticidadfugavisible", "revision = '" + _revision + "'"));	
		return this._tempRegistro;
	}
	
	public ContentValues getDatosMedidor(String _revision){
		this._tempRegistro.clear();
		this._tempRegistro.put("RegistroPaso", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorregpaso", "revision = '" + _revision + "'"));
		this._tempRegistro.put("RegistroAntifraude", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorregantifraude", "revision = '" + _revision + "'"));
		this._tempRegistro.put("Destruido", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidordestruido", "revision = '" + _revision + "'"));
		this._tempRegistro.put("Invertido", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorinvertido", "revision = '" + _revision + "'"));	
		this._tempRegistro.put("Ilegible", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorilegible", "revision = '" + _revision + "'"));	
		this._tempRegistro.put("Precinto", this.FcnSQL.StrSelectShieldWhere("db_desviaciones", "medidorprecintoroto", "revision = '" + _revision + "'"));	
		return this._tempRegistro;
	}
	
	
	public ContentValues getDatosPruebasEstanqueidad(String _revision, String _tanque){
		this._tempRegistro1.clear();
		if(_tanque.equals("Tanque Subterraneo")){
			this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_desviaciones", 
																"estanqueidadressubterraneo as res, estanqueidadcapsubterraneo as cap,estanqueidadfugasubterrano as fug", 
																"revision = '" + _revision + "'");
		}else if(_tanque.equals("Tanque Elevado")){
			this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_desviaciones", 
																"estanqueidadreselevado as res, estanqueidadcapelevado as cap, estanqueidadfugaelevado as fug", 
																"revision = '" + _revision + "'");
		}else if(_tanque.equals("Tanque Lavadero")){
			this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_desviaciones", 
																"estanqueidadreslavadero as res, estanqueidadcaplavadero as cap, estanqueidadfugalavadero as fug", 
																"revision = '" + _revision + "'");
		}
		
		String[] myString = this._tempRegistro.getAsString("cap").split(" ");
		this._tempRegistro1.put("Resultado",this._tempRegistro.getAsString("res"));
		if(myString.length>1){
			this._tempRegistro1.put("Capacidad",myString[0]);
			this._tempRegistro1.put("UnidadesCapacidad",myString[1]);
		}else{
			this._tempRegistro1.put("Capacidad","");
			this._tempRegistro1.put("UnidadesCapacidad","");
		}
		myString = this._tempRegistro.getAsString("fug").split(" ");
		if(myString.length>1){
			this._tempRegistro1.put("Fuga",myString[0]);
			this._tempRegistro1.put("UnidadesFuga",myString[1]);
		}else{
			this._tempRegistro1.put("Fuga","");
			this._tempRegistro1.put("UnidadesFuga","");
		}
		
		return this._tempRegistro1;
	}
	
	private boolean validarVisitaTecnicaElementos(String _cantidad){
		boolean ValorRetorno= false;
		if(_cantidad.length()==0){
			ValorRetorno = false;
		}else{
			ValorRetorno = true;
		}
		return ValorRetorno;
	}
	
	public void registrarElementos(String _revision, String _elemento, String _cantidad, String _item1, String _item2, String _item3, String _data1, String _data2, String _data3){
		this._tempRegistro.clear();
		if(this.validarVisitaTecnicaElementos(_cantidad)){
			if(_elemento.equals("Cisterna")){
				this._tempRegistro.put("cisterna", _cantidad);
				this._tempRegistro.put("itemcisterna", _item1+"-"+_item2+"-"+_item3);
				this._tempRegistro.put("estadocisterna", _data1+"-"+_data2+"-"+_data3);
			}else if(_elemento.equals("Ducha")){
				this._tempRegistro.put("ducha", _cantidad);
				this._tempRegistro.put("itemducha", _item1);
				this._tempRegistro.put("estadoducha", _data1);
			}else if(_elemento.equals("Instalaciones Internas")){
				this._tempRegistro.put("internas", _cantidad);
				this._tempRegistro.put("iteminternas", _item1+"-"+_item2);
				this._tempRegistro.put("estadointernas", _data1+"-"+_data2);
			}else if(_elemento.equals("Lavadero")){
				this._tempRegistro.put("lavaderos", _cantidad);
				this._tempRegistro.put("itemlavadero", _item1);
				this._tempRegistro.put("estadolavadero", _data1);
			}else if(_elemento.equals("Lavamanos")){
				this._tempRegistro.put("lavamanos", _cantidad);
				this._tempRegistro.put("itemlavamanos", _item1);
				this._tempRegistro.put("estadolavamanos", _data1);
			}else if(_elemento.equals("Lavaplatos")){
				this._tempRegistro.put("lavaplatos", _cantidad);
				this._tempRegistro.put("itemlavaplatos", _item1);
				this._tempRegistro.put("estadolavaplatos", _data1);
			}else if(_elemento.equals("Piscina")){
				this._tempRegistro.put("piscina", _cantidad);
				this._tempRegistro.put("itempiscina", _item1+"-"+_item2+"-"+_item3);
				this._tempRegistro.put("estadopiscina", _data1+"-"+_data2+"-"+_data3);			
			}else if(_elemento.equals("Subterraneo")){
				this._tempRegistro.put("subterraneos", _cantidad);
				this._tempRegistro.put("itemsubterraneos", _item1);
				this._tempRegistro.put("estadosubterraneos", _data1);
			}else if(_elemento.equals("Tanque Elevado")){
				this._tempRegistro.put("elevados", _cantidad);
				this._tempRegistro.put("itemelevado", _item1);
				this._tempRegistro.put("estadoelevado", _data1);
			}
			if(this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'")){
				Toast.makeText(this._ctxDesviacion,"Datos de elemento "+_elemento+" guardados correctamente.", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this._ctxDesviacion,"Error al registrar del elemento "+_elemento+".", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this._ctxDesviacion,"No ha ingresado una cantidad valida.", Toast.LENGTH_LONG).show();
		}
	}
	
	
	public void registrarPruebasEstanqueidad(String _revision, String _tanque, String _resultado, String _capacidad, String _uniCapacidad, String _fuga, String _uniFuga){
		this._tempRegistro.clear();
		if(_tanque.equals("Tanque Subterraneo")){
			this._tempRegistro.put("estanqueidadressubterraneo", _resultado);
			this._tempRegistro.put("estanqueidadcapsubterraneo", _capacidad+" "+_uniCapacidad);
			this._tempRegistro.put("estanqueidadfugasubterrano", _fuga+" "+_uniFuga);
		}else if(_tanque.equals("Tanque Elevado")){
			this._tempRegistro.put("estanqueidadreselevado", _resultado);
			this._tempRegistro.put("estanqueidadcapelevado", _capacidad+" "+_uniCapacidad);
			this._tempRegistro.put("estanqueidadfugaelevado", _fuga+" "+_uniFuga);
		}else if(_tanque.equals("Tanque Lavadero")){
			this._tempRegistro.put("estanqueidadreslavadero", _resultado);
			this._tempRegistro.put("estanqueidadcaplavadero", _capacidad+" "+_uniCapacidad);
			this._tempRegistro.put("estanqueidadfugalavadero", _fuga+" "+_uniFuga);
		}
		if(this.FcnSQL.UpdateRegistro("db_desviaciones", this._tempRegistro, "revision='"+_revision+"'")){
			Toast.makeText(this._ctxDesviacion,"Datos de pruebas de hermeticidad al "+_tanque+" guardados correctamente.", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this._ctxDesviacion,"Error al registrar las pruebas de hermiticidad al "+_tanque+".", Toast.LENGTH_LONG).show();
		}
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
	
	public ArrayList<String> getOpciones(String _itemSubclase){
		ArrayList<String> _opciones = new ArrayList<String>();
		_opciones.clear();
		if(_itemSubclase.equals("BuenoMaloNoAplica")){
			_opciones.add("Bueno");
			_opciones.add("Malo");
			_opciones.add("N/A");
		}if(_itemSubclase.equals("BuenoMaloNoTiene")){
			_opciones.add("Bueno");
			_opciones.add("Malo");
			_opciones.add("No Tiene");
		}else if(_itemSubclase.equals("SiNo")){
			_opciones.add("");
			_opciones.add("Si");
			_opciones.add("No");
		}else if(_itemSubclase.equals("SiNoNoAplica")){
			_opciones.add("");
			_opciones.add("Si");
			_opciones.add("No");
			_opciones.add("N/A");
		}else if(_itemSubclase.equals("SiNoNoTiene")){
			_opciones.add("");
			_opciones.add("Si");
			_opciones.add("No");
			_opciones.add("No Tiene");
		}else if(_itemSubclase.equals("BuenaMalaNoSeRealizo")){
			_opciones.add("");
			_opciones.add("No Se Realizo");
			_opciones.add("Buena");
			_opciones.add("Mala");
		}else if(_itemSubclase.equals("Capacidad")){
			_opciones = this.FcnUtilidades.getRangeAdapter(0, 5000, 25);
		}else if(_itemSubclase.equals("Fuga")){
			_opciones = this.FcnUtilidades.getRangeAdapter(0, 10, 0.10);
		}else if(_itemSubclase.equals("UnidadesCapacidad")){
			_opciones.add("Lts");
			_opciones.add("Mts3");
		}else if(_itemSubclase.equals("UnidadesFuga")){
			_opciones.add("Lts/min");
			_opciones.add("Mts3/min");
		}else if(_itemSubclase.equals("Vacio")){
			_opciones.add("");
		}				
		return _opciones;
	}
}
