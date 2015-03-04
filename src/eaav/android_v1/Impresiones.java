package eaav.android_v1;

import java.text.DecimalFormat;

import Miscelanea.SQLite;
import Miscelanea.Zebra_QL420plus;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class Impresiones {
	DecimalFormat 	FormatoSinDecima 	= new DecimalFormat("0");
	
	private Context 		context;
	private SQLite 			FcnSQL;
	private Zebra_QL420plus	FcnZebra;
	
	private ContentValues _tempRegistro = new ContentValues();	
	private String Impresora = null;
	
	
	public Impresiones(Context context){
		this.context = context;
	}
	
	
	public void FormatoNotificacion(String TipoImpresion, String Solicitud, String NombreUsuario, String CedulaUsuario, String TipoUsuario){
		this.FcnSQL		= new SQLite(this.context, FormLoggin.CARPETA_RAIZ, FormLoggin.NOMBRE_DATABASE);
		this.Impresora 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'impresora'");
		
		String periodo_ini 	= this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_ini", "revision = '" + Solicitud + "'");
		String periodo_fin 	= this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_fin", "revision = '" + Solicitud + "'");
		Double factura 		= this.FcnSQL.DoubleSelectShieldWhere("db_solicitudes", "factura", "revision = '" + Solicitud + "'");
		String NumVisita = "";
		String CamposGeneral = "";
		String Nombre= "";
		String Direccion = "";
				
		CamposGeneral 	= this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "id_serial", "revision = '" + Solicitud + "'");
		
		this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_notificaciones", 
															"revision,codigo,nombre,direccion,serie,ciclo,promedio,visita,lectura,medidor,precinto,observacion,fecha_visita,hora_visita,motivo,fecha_notificacion,jornada_notificacion", 
															"revision = '" + Solicitud + "'");
		   
	    if (this._tempRegistro.size()>0){
	    	Nombre = this._tempRegistro.getAsString("nombre");
	        Direccion = this._tempRegistro.getAsString("direccion");
	        
	        if (Nombre.length() > 35){
	            Nombre = Nombre.substring(0, 35);
	        }
	
	        if (Direccion.length() > 35){
	            Direccion = Direccion.substring(0, 35);
	        }
	
	        this.FcnZebra = new Zebra_QL420plus(this.context,570, 10, 3, 15, 100, false);	
	        this.FcnZebra.clearInformacion();
	        
	        this.FcnZebra.DrawImage("AGUAS.PCX", 0, 5);
	        this.FcnZebra.DrawImage("EAAV.PCX", 410, 5);
	        this.FcnZebra.WrTitulo("UNION TEMPORAL AGUAS DE LOS",1.5,1); 
	        this.FcnZebra.WrTitulo("LLANOS", 0, 1.2);
	        this.FcnZebra.WrTitulo("VISITA TECNICA", 1, 1.2);
	        this.FcnZebra.WrTitulo("PROCESO DE FACTURACION", 0, 1.2);
	        this.FcnZebra.WrTitulo("NOVEDAD DE VISITA TECNICA DE CRITICA", 0, 2);
	        this.FcnZebra.WrLabel("Acta No:", "N" + this._tempRegistro.getAsString("revision") + "-" + CamposGeneral + "-" + this._tempRegistro.getAsString("visita"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Factura Investigacion: ", FormatoSinDecima.format(factura), 3, 0, 1);
	        this.FcnZebra.WrLabel("Periodo Investigacion:", periodo_ini, 3, 0, 0);
	        this.FcnZebra.WrLabel(" a ", periodo_fin, 365, 0, 1.5);
	        
	        this.FcnZebra.WrLabel("Fecha:  ", this._tempRegistro.getAsString("fecha_visita"), 3, 0, 0);
	        this.FcnZebra.WrLabel("Hora: ", this._tempRegistro.getAsString("hora_visita"), 250, 0, 1);
	        this.FcnZebra.WrLabel("Codigo: ", this._tempRegistro.getAsString("codigo"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Senor:     ", Nombre, 3, 0, 1);
	        this.FcnZebra.WrLabel("Direccion: ", "", 3, 0, 1);
	        this.FcnZebra.JustInformation(Direccion, 3, 0, 2);
	
	        if (this._tempRegistro.getAsString("visita").equals("1")){
	        	NumVisita = "por primera vez";
	        }else if(this._tempRegistro.getAsString("visita").equals("2")){
	        	NumVisita = "por segunda vez";
		    }else{
		    	NumVisita = "por segunda vez";
		    }
	        
	        this.FcnZebra.JustInformation("En el dia de hoy estuvimos visitando su predio "+NumVisita+", con el fin de realizar revision y practicar las pruebas tecnicas con motivo a la investigacion del incremento del consumo de agua (Art. 149 Ley 142 de 1994). Las cuales no se realizaron por motivo: "+this._tempRegistro.getAsString("motivo").toUpperCase(), 3, 0, 1);
	        
	        if (this._tempRegistro.getAsString("jornada_notificacion").equals("am")){
	        	this.FcnZebra.WrTitulo("TENIENDO EN CUENTA LO ANTERIOR, SE REQUIERE ESTAR PRESENTE EL DIA " + this._tempRegistro.getAsString("fecha_notificacion") + " EN HORAS DE LA MANANA..", 1, 1);
	        }else{
	        	this.FcnZebra.WrTitulo("TENIENDO EN CUENTA LO ANTERIOR, SE REQUIERE ESTAR PRESENTE EL DIA " + this._tempRegistro.getAsString("fecha_notificacion") + " EN HORAS DE LA TARDE.", 1, 1);
		    }
	
	        this.FcnZebra.JustInformation("Recuerde que es obligatorio que atiendan la visita en la fecha y jornada indicada anteriormente, de no ser asi, la empresa podra suspender el servicio de acueducto a lo establecido en el articulo 26 numeral 26.13 del decreto 302 del 2000.", 3, 1, 1);
	        this.FcnZebra.JustInformation("Si presenta dificultad, dentro de las 24 horas siguientes confirmar al numero telefonico 6724308 para concertar una nueva visita.", 3, 1, 1);	
	        this.FcnZebra.JustInformation("De conformidad con el articulo 12 de la resolucion CRA 413 de 2006, usted puede solicitar a su costo, la asesoria de un tecnico particular para que la acompane durante la visita, en la revision de medidor y de las instalaciones internas. No obstante, puede renunciar a esta asesoria, de lo cual se dejara constancia. Esta visita debe ser atendida por un mayor de edad.", 3, 1, 1);	
	        this.FcnZebra.WrSubTitulo("Observacion.", 3, 1, 1);
	        this.FcnZebra.JustInformation(this._tempRegistro.getAsString("observacion"), 3, 0, 2);
	        this.FcnZebra.WrLabel("Lectura Actual:", this._tempRegistro.getAsString("lectura"), 3, 0, 1);
	        this.FcnZebra.WrLabel("No del Medidor:", this._tempRegistro.getAsString("medidor"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Precinto:", this._tempRegistro.getAsString("precinto"), 3, 0, 2);
	        
	        
	        this.FcnZebra.WrRectangle(3, this.FcnZebra.getCurrentLine(), 550, this.FcnZebra.getCurrentLine() + 100, 1, 0);
	        this.FcnZebra.WrLabel("Firma "+TipoUsuario, NombreUsuario, 3, 0, 1);
	        this.FcnZebra.WrLabel("Cedula ", CedulaUsuario, 3, 0, 3);
            
	        	        
	        String Tecnico 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'nombre_tecnico'");
	        String Cedula 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'cedula_tecnico'");
	        this.FcnZebra.JustInformation("Declaro bajo la gravedad de juramento que los datos aqui consignados son veraces, fueron verificados y obdecen al estado actual del predio.", 3, 0, 1);        
	        
	        this.FcnZebra.WrRectangle(3, this.FcnZebra.getCurrentLine(), 550, this.FcnZebra.getCurrentLine() + 100, 1, 0);
	        this.FcnZebra.WrLabel("Nombre del Operario:", Tecnico, 3, 0, 1);
	        this.FcnZebra.WrLabel("CC: ", Cedula, 3, 0, 2);
	        this.FcnZebra.WrLabel(TipoImpresion, "", 3, 0, 1);
	        this.FcnZebra.WrTitulo("UNION TEMPORAL AGUAS DE LOS LLANOS.", 0, 1);
	        this.FcnZebra.WrTitulo("Contratista de la Empresa de Acueducto y Alcantarillado de Villavicencio E.S.P.", 0, 1);
	        this.FcnZebra.printLabel(this.Impresora);   
	        this.FcnZebra.resetEtiqueta();
	    }else{
	    	Toast.makeText(this.context, "No existen registros de la revision, no olvide guardar los datos antes de imprimir.", Toast.LENGTH_LONG).show();   		
        }
	}
	
	
	public void FormatoDesviacion(String TipoImpresion, String Solicitud){
		this.FcnSQL		= new SQLite(this.context, FormLoggin.CARPETA_RAIZ, FormLoggin.NOMBRE_DATABASE);
		this.Impresora 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'impresora'");
		
		String periodo_ini = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_ini", "revision = '" + Solicitud + "'");
		String periodo_fin = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_fin", "revision = '" + Solicitud + "'");
		Double factura = this.FcnSQL.DoubleSelectShieldWhere("db_solicitudes", "factura", "revision = '" + Solicitud + "'");
		
		String CamposGeneral = "";
		String Direccion = "";
		String tempEstrato = "sin identificar";
		
		CamposGeneral = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "id_serial", "revision = '" + Solicitud + "'");
	   
		this._tempRegistro = this.FcnSQL.SelectDataRegistro("db_desviaciones", 
															"revision,			codigo,				nombre,				direccion,			serie," +
															"ciclo,				promedio,			visita,				fecha,				hora," +
															"tipo,				area,				pisos,				actividad,			uso," +
															"residentes,		habitado,			estado,				acueducto,			camaramedidor," +
															"estadocamara,		escapecamara,		serieindividual,	marcaindividual,	diametroindividual," +
															"lecturaindividual,	serietotalizador,	marcatotalizador,	diametrototalizador,lecturatotalizador," +
															"subterraneos,		itemsubterraneos,	estadosubterraneos,	lavaplatos,			itemlavaplatos," +
															"estadolavaplatos,	lavaderos,			itemlavadero,		estadolavadero,		elevados," +
															"itemelevado,		estadoelevado,		internas,			iteminternas,		estadointernas," +
															"piscinas,			itempiscina,		estadopiscina,		medidorregpaso,		medidorregantifraude," +
															"medidordestruido,	medidorinvertido,	medidorilegible,	medidorprecintoroto,hermeticidadreginternos," +
															"hermeticidadequipomedida,		estanqueidadreselevado,		estanqueidadcapelevado,		estanqueidadfugaelevado,	estanqueidadreslavadero," +
															"estanqueidadcaplavadero,		estanqueidadfugalavadero,	estanqueidadressubterraneo,	estanqueidadcapsubterraneo,	estanqueidadfugasubterrano," +
															"hermeticidadfugaimperceptible,	hermeticidadfugas,			hermeticidadfugavisible,	diagnostico,				estrato," +
															"precinto,			serviciodirecto,	bypass,				nombreusuario,		cedulausuario," +
															"nombretestigo,		cedulatestigo,		cisterna,			itemcisterna,		estadocisterna," +
															"ducha,				itemducha,			estadoducha,		lavamanos,			itemlavamanos," +
															"estadolavamanos,	servicioacueducto,	servicioalcantarillado,segundoconcepto,respuestadesviacion",
															"revision = '" + Solicitud + "'");
	    String AllItems[];
	    String AllEstado[];

	    if (this._tempRegistro.size()>0){
	    	Direccion = this._tempRegistro.getAsString("direccion");
	    	String FechaVisita[] = this._tempRegistro.getAsString("fecha").split("-");
	        String Precinto = "No tiene";
	        double dobleColumna = 0;
	    	
	        if (!this._tempRegistro.getAsString("precinto").isEmpty()){
	        	Precinto = this._tempRegistro.getAsString("precinto");
	        }
	        
	        this.FcnZebra = new Zebra_QL420plus(this.context, 570, 10, 3, 15, 100, false);
	        this.FcnZebra.clearInformacion();
	        this.FcnZebra.DrawImage("AGUAS.PCX", 0, 5);
	        this.FcnZebra.DrawImage("EAAV.PCX", 410, 5);
	        this.FcnZebra.WrTitulo("UNION TEMPORAL AGUAS DE LOS",1.5,1); 
	        this.FcnZebra.WrTitulo("LLANOS", 0, 1.2);
	        this.FcnZebra.WrTitulo("VISITA TECNICA", 1, 1.2);
	        this.FcnZebra.WrTitulo("PROCESO DE FACTURACION", 0, 1.2);
	        this.FcnZebra.WrTitulo("INFORME DE VISITA TECNICA DE CRITICA", 0, 2);
		
	        this.FcnZebra.WrLabel("No Revision: ", this._tempRegistro.getAsString("revision"), 3, 0, 0);
	        this.FcnZebra.WrLabel("Codigo: ", this._tempRegistro.getAsString("codigo"), 270, 0, 1);
	        this.FcnZebra.WrLabel("Ciclo: ", this._tempRegistro.getAsString("ciclo"), 3, 0, 0);
	        this.FcnZebra.WrLabel("Acta No: ", "D" + this._tempRegistro.getAsString("revision") + "-" + CamposGeneral, 270, 0, 2);
	        this.FcnZebra.WrLabel("Factura Investigacion: ", FormatoSinDecima.format(factura), 3, 0, 1);
	        this.FcnZebra.WrLabel("Periodo Investigacion:", periodo_ini, 3, 0, 0);
	        this.FcnZebra.WrLabel(" a ", periodo_fin, 365, 0, 1.5);
	
	        this.FcnZebra.WrLabel("Fecha:    ", this._tempRegistro.getAsString("fecha"), 3, 0, 0);
	        this.FcnZebra.WrLabel("Hora: ", this._tempRegistro.getAsString("hora"), 270, 0, 1);
	        this.FcnZebra.WrLabel("Precinto: ", Precinto, 3, 0, 1.5);
	
	        if(!this._tempRegistro.getAsString("estrato").isEmpty()){
	        	tempEstrato = this._tempRegistro.getAsString("estrato");
	        }
	        
	        if (Precinto.equals("No tiene")){
	        	this.FcnZebra.JustInformation("Hoy dia " + FechaVisita[0] + " del mes " + FechaVisita[1] + " de " + FechaVisita[2] + " a las " + this._tempRegistro.getAsString("hora") + " se realizo la visita al inmueble ubicado en la direccion " + Direccion + ", de estrato " + tempEstrato + ", nombre del suscriptor " + this._tempRegistro.getAsString("nombre") + "." + Precinto, 3, 0, 1);
	        }else{
	        	this.FcnZebra.JustInformation("Hoy dia " + FechaVisita[0] + " del mes " + FechaVisita[1] + " de " + FechaVisita[2] + " a las " + this._tempRegistro.getAsString("hora") + " se realizo la visita al inmueble ubicado en la direccion " + Direccion + ", de estrato " + tempEstrato + ", nombre del suscriptor " + this._tempRegistro.getAsString("nombre") + ", en cuyo medidor se encuentra instalado el precinto " + Precinto, 3, 0, 1);
	        }
	        this.FcnZebra.WrLabel("Telefono:", "____________________", 3, 0, 1.5);
	    	
	
	        if (!this._tempRegistro.getAsString("nombreusuario").isEmpty()){
	        	this.FcnZebra.JustInformation("El senor(a) " + this._tempRegistro.getAsString("nombreusuario") + ", identificado con CC. " + this._tempRegistro.getAsString("cedulausuario") + ", manifiesta no hacer uso del derecho que le otorga la resolucion CRA 413 de 2006. Esta visita debe ser atendida por un mayor de edad.", 3, 1, 1);
	        }
	        
	        this.FcnZebra.WrSubTitulo("CARACTERISTICAS DEL INMUEBLE", 3, 1, 1);
	        this.FcnZebra.WrLabel("Tipo:", this._tempRegistro.getAsString("tipo"), 3, 0, 0);
	
	        this.FcnZebra.WrLabel("Pisos:    ", this._tempRegistro.getAsString("pisos"), 273, 0, 1);
	
	        this.FcnZebra.WrLabel("Area:",this._tempRegistro.getAsString("area") + " Mts2", 3, 0, 0);
	
	        this.FcnZebra.WrLabel("Actividad:", this._tempRegistro.getAsString("actividad"), 273, 0, 1);
	
	        this.FcnZebra.WrLabel("Uso:                    ", this._tempRegistro.getAsString("uso"), 3, 0, 1);
	        this.FcnZebra.WrLabel("No Personas Residentes: ", this._tempRegistro.getAsString("residentes"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Estado Inmueble:        ", this._tempRegistro.getAsString("estado"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Habitado Por:           ", this._tempRegistro.getAsString("habitado"), 3, 0, 1);	
	
	        this.FcnZebra.WrSubTitulo("INFORMACION TECNICA", 3, 1, 1);
	        this.FcnZebra.WrLabel("Servicio de Acueducto Administrado Por: ", this._tempRegistro.getAsString("acueducto"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Escape:           ", this._tempRegistro.getAsString("escapecamara"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Servicio Directo: ", this._tempRegistro.getAsString("serviciodirecto"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Bypass:           ", this._tempRegistro.getAsString("bypass"), 3, 0, 1);
	
	        this.FcnZebra.WrSubTitulo("Camara Medidor y/o Nicho: ", 3, 1, 1);
	        this.FcnZebra.WrLabel("Tamano: ", this._tempRegistro.getAsString("camaramedidor"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Estado: ", this._tempRegistro.getAsString("estadocamara"), 3, 0, 1);
	
	        dobleColumna = this.FcnZebra.getCurrentLine();
	
	        this.FcnZebra.WrSubTitulo("Medidor Individual", 3, 1, 1);
	        this.FcnZebra.WrLabel("No: ", this._tempRegistro.getAsString("serieindividual"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Marca: ", this._tempRegistro.getAsString("marcaindividual"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Diametro: ", this._tempRegistro.getAsString("diametroindividual") + " pulg.", 3, 0, 1);
	        this.FcnZebra.WrLabel("Lectura: ", this._tempRegistro.getAsString("lecturaindividual"), 3, 0, 1);
	
	        this.FcnZebra.setCurrentLine(dobleColumna);
	
	        this.FcnZebra.WrSubTitulo("Medidor Totalizador", 270, 1, 1);
	        this.FcnZebra.WrLabel("No: ", this._tempRegistro.getAsString("serietotalizador"), 270, 0, 1);
	        this.FcnZebra.WrLabel("Marca: ", this._tempRegistro.getAsString("marcatotalizador"), 270, 0, 1);
	        this.FcnZebra.WrLabel("Diametro: ", this._tempRegistro.getAsString("diametrototalizador") + " pulg.", 270, 0, 1);
	        this.FcnZebra.WrLabel("Lectura: ", this._tempRegistro.getAsString("lecturatotalizador"), 270, 0, 1);
	
	        this.FcnZebra.WrSubTitulo("INFORMACION VISITA TECNICA", 3, 1, 1);
	
	        if(!this._tempRegistro.getAsString("subterraneos").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Tanque Subterraneo", 3, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("subterraneos"), 270, 0, 1);
		
		        AllItems = this._tempRegistro.getAsString("itemsubterraneos").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadosubterraneos").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	        
	        if(!this._tempRegistro.getAsString("lavaplatos").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Lavaplatos", 3, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("lavaplatos"), 270, 0, 1);
		
		        AllItems = this._tempRegistro.getAsString("itemlavaplatos").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadolavaplatos").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("cisterna").equals("0")){
	        	this.FcnZebra.WrSubTitulo("UNIDAD SANITARIA", 3, 1, 1);
	        	this.FcnZebra.WrSubTitulo("Cisternas", 3, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("cisterna"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemcisterna").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadocisterna").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("ducha").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Duchas", 3, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("ducha"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemducha").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadoducha").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("lavamanos").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Lavamanos", 10, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("lavamanos"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemlavamanos").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadolavamanos").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	        
	        if(!this._tempRegistro.getAsString("lavaderos").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Tanque Lavadero", 3, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("lavaderos"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemlavadero").toString().split("-");
		        AllEstado = this._tempRegistro.getAsString("estadolavadero").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	
	        //'setCurrentLine(dobleColumna)
	        if(!this._tempRegistro.getAsString("elevados").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Tanque Elevado", 3, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("elevados"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemelevado").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadoelevado").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	
	        if(!this._tempRegistro.getAsString("internas").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Instalaciones Hidraulicas Internas", 3, 1, 1);
		        AllItems = this._tempRegistro.getAsString("iteminternas").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadointernas").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("piscinas").equals("0")){
	        	this.FcnZebra.WrSubTitulo("Piscina", 3, 1, 0);
	        	this.FcnZebra.WrLabel("Cantidad: ", this._tempRegistro.getAsString("piscinas"), 270, 0, 1);		
		        AllItems = this._tempRegistro.getAsString("itempiscina").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadopiscina").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		        	this.FcnZebra.WrLabel(AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        this.FcnZebra.WrSubTitulo("Medidor", 3, 1, 1);
	        this.FcnZebra.WrLabel("Registro de Paso:        ", this._tempRegistro.getAsString("medidorregpaso"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Registro Antifraude:     ", this._tempRegistro.getAsString("medidorregantifraude"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Destruido:               ", this._tempRegistro.getAsString("medidordestruido"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Invertido:               ", this._tempRegistro.getAsString("medidorinvertido"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Vidrio Ilegible:         ", this._tempRegistro.getAsString("medidorilegible"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Precinto Seguridad Roto: ", this._tempRegistro.getAsString("medidorprecintoroto"), 3, 0, 1);
	
	        this.FcnZebra.WrSubTitulo("Prueba Hermeticidad Inst. Hidraulicas Internas", 3, 1, 1);
	        this.FcnZebra.WrLabel("Cierre de Registros Internos:  ", this._tempRegistro.getAsString("hermeticidadreginternos"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Verificacion Equipo de Medida: ", this._tempRegistro.getAsString("hermeticidadequipomedida"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Revision de Fugas:             ", this._tempRegistro.getAsString("hermeticidadfugas"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Fuga Imperceptible:            ", this._tempRegistro.getAsString("hermeticidadfugaimperceptible"), 3, 0, 1);
	        this.FcnZebra.WrLabel("Fuga Visible:                  ", this._tempRegistro.getAsString("hermeticidadfugavisible"), 3, 0, 1);
	
	        this.FcnZebra.WrSubTitulo("PRUEBA DE ESTANQUEIDAD TANQUES", 3, 1, 1);
	        this.FcnZebra.WrLabel("Item/Tanque ", "", 3, 0, 0);
	        this.FcnZebra.WrLabel("Resultado", "", 150, 0, 0);
	        this.FcnZebra.WrLabel("Capacidad", "", 280, 0, 0);
	        this.FcnZebra.WrLabel("Magnitud Fuga", "", 415, 0, 1);
	        
	        AllItems = new String[3];
	        if (this._tempRegistro.getAsString("estanqueidadressubterraneo").equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = this._tempRegistro.getAsString("estanqueidadressubterraneo");
                AllItems[1] = this._tempRegistro.getAsString("estanqueidadcapsubterraneo");
                AllItems[2] = this._tempRegistro.getAsString("estanqueidadfugasubterrano");
	        }
	        
	        this.FcnZebra.WrLabel("Subterraneo", "", 3, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[0], 150, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[1], 280, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[2], 415, 0, 1);
	
	        if (this._tempRegistro.getAsString("estanqueidadreselevado").equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = this._tempRegistro.getAsString("estanqueidadreselevado");
                AllItems[1] = this._tempRegistro.getAsString("estanqueidadcapelevado");
                AllItems[2] = this._tempRegistro.getAsString("estanqueidadfugaelevado");
	        }
	        
	        this.FcnZebra.WrLabel("Elevado", "", 3, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[0], 150, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[1], 280, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[2], 415, 0, 1);
	
	        if (this._tempRegistro.getAsString("estanqueidadreslavadero").equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = this._tempRegistro.getAsString("estanqueidadreslavadero");
                AllItems[1] = this._tempRegistro.getAsString("estanqueidadcaplavadero");
                AllItems[2] = this._tempRegistro.getAsString("estanqueidadfugalavadero");
	        }
	        
	        this.FcnZebra.WrLabel("Lavadero", "", 3, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[0], 150, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[1], 280, 0, 0);
	        this.FcnZebra.WrSubTitulo(AllItems[2], 415, 0, 1);
	
	        this.FcnZebra.WrSubTitulo("Diagnostico Tecnico", 3, 1, 1);
	        this.FcnZebra.JustInformation(this._tempRegistro.getAsString("diagnostico"), 3, 0, 2);
	
	        if(!this._tempRegistro.getAsString("nombreusuario").isEmpty()){
	        	this.FcnZebra.WrRectangle(3, this.FcnZebra.getCurrentLine(), 550, this.FcnZebra.getCurrentLine() + 100, 1, 0);
	        	this.FcnZebra.WrLabel("Firma Cliente: ", this._tempRegistro.getAsString("nombreusuario"), 3, 0, 1);
	        	this.FcnZebra.WrLabel("CC.", this._tempRegistro.getAsString("cedulausuario"), 3, 0, 3);
	        }else{
	        	this.FcnZebra.WrTitulo("Se deja copia bajo puerta", 1.5, 1.5);
	        }
	
	        if(!this._tempRegistro.getAsString("nombretestigo").isEmpty()){
	        	this.FcnZebra.WrRectangle(3, this.FcnZebra.getCurrentLine(), 550, this.FcnZebra.getCurrentLine() + 100, 1, 0);
	        	this.FcnZebra.WrLabel("Firma Testigo y/o Interventor: ", this._tempRegistro.getAsString("nombretestigo"), 3, 0, 1);
	        	this.FcnZebra.WrLabel("CC.", this._tempRegistro.getAsString("cedulatestigo"), 3, 0, 3);
	        }
	
	        String Tecnico 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'nombre_tecnico'");
	        String Cedula 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'cedula_tecnico'");
	
	        this.FcnZebra.JustInformation("Declaro bajo la gravedad de juramento que los datos aqui consignados son veraces, fueron verificados y obdecen al estado actual del predio.", 3, 0, 1);
	        
	        this.FcnZebra.WrRectangle(3, this.FcnZebra.getCurrentLine(), 550, this.FcnZebra.getCurrentLine() + 100, 1, 0);
	        this.FcnZebra.WrLabel("Firma Tecnico Contratista: ", Tecnico, 3, 0, 1);
	        this.FcnZebra.WrLabel("CC.", Cedula, 3, 0, 3);
	
	        this.FcnZebra.WrLabel(TipoImpresion, "", 3, 0, 1);
	        this.FcnZebra.WrTitulo("UNION TEMPORAL AGUAS DE LOS LLANOS.", 0, 1);
	        this.FcnZebra.WrTitulo("Contratista de la Empresa de Acueducto y Alcantarillado de Villavicencio E.S.P.", 0, 1);
	        this.FcnZebra.printLabel(this.Impresora);
	        this.FcnZebra.resetEtiqueta();
	    }else{
			Toast.makeText(this.context, "No existen registros de la revision, no olvide guardar los datos antes de imprimir.", Toast.LENGTH_LONG).show();   		
	    }
	}
}
