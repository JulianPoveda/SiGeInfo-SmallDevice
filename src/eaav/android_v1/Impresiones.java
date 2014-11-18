package eaav.android_v1;

import java.text.DecimalFormat;

import Miscelanea.Bluetooth;
import Miscelanea.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class Impresiones {
	private Context 	context;
	private Bluetooth 	MnBt;
	private SQLite 		FcnSQL;
	
	private ContentValues _tempRegistro = new ContentValues();
	DecimalFormat FormatoSinDecima = new DecimalFormat("0");
	
	private String Impresora = null;
	private String InfToPrinter= null;
	private int WidthLabel;
	private int OffsetLabel;
	private int StartLine;
	private double CurrentLine;
	private int EndLabel;
	private int HeightFont;
	private int WidthFont;
	private int HeightText;
	private int WidthText;
    private String LabelFont;
    private String TextFont;
	
	public Impresiones(Context context){
		this.context = context;
	}
	
	
	public void FormatoNotificacion(String TipoImpresion, String Solicitud, String NombreUsuario, String TipoUsuario){
		MnBt = new Bluetooth(this.context);
		this.FcnSQL	= new SQLite(this.context, FormLoggin.CARPETA_RAIZ, FormLoggin.NOMBRE_DATABASE);
		String periodo_ini = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_ini", "revision = '" + Solicitud + "'");
		String periodo_fin = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_fin", "revision = '" + Solicitud + "'");
		Double factura = this.FcnSQL.DoubleSelectShieldWhere("db_solicitudes", "factura", "revision = '" + Solicitud + "'");
		String NumVisita = "";
		String CamposGeneral = "";
		String Nombre= "";
		String Direccion = "";
		InfToPrinter = "";
		
		CamposGeneral 	= this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "id_serial", "revision = '" + Solicitud + "'");
		Impresora 		= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'impresora'");
		
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
	
	        InfToPrinter = "";
	        SetPagePrinter(InfToPrinter, 570, 10, 3, 15, 100);	
	        InfToPrinter = DrawImage(InfToPrinter, "AGUAS.PCX", 0, 5);
	        InfToPrinter = DrawImage(InfToPrinter, "EAAV.PCX", 410, 5);
	        InfToPrinter = WrTitulo(InfToPrinter, "CONSORCIO AGUAS DEL LLANO", 2.5, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "VISITA TECNICA", 0, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "PROCESO DE FACTURACION", 0, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "NOVEDAD DE VISITA TECNICA DE CRITICA", 0, 2);
	        InfToPrinter = WrLabel(InfToPrinter, "Acta No:", "N" + this._tempRegistro.getAsString("revision") + "-" + CamposGeneral + "-" + this._tempRegistro.getAsString("visita"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Factura Investigacion: ", FormatoSinDecima.format(factura), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Periodo Investigacion:", periodo_ini, 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, " a ", periodo_fin, 365, 0, 1.5);
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Fecha:  ", this._tempRegistro.getAsString("fecha_visita"), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Hora: ", this._tempRegistro.getAsString("hora_visita"), 250, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Codigo: ", this._tempRegistro.getAsString("codigo"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Senor:     ", Nombre, 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Direccion: ", "", 3, 0, 1);
	        InfToPrinter = JustInformation(InfToPrinter, Direccion, 3, 0, 2);
	
	        if (this._tempRegistro.getAsString("visita").equals("1")){
	        	NumVisita = "por primera vez";
	        }else if(this._tempRegistro.getAsString("visita").equals("2")){
	        	NumVisita = "por segunda vez";
		    }else{
		    	NumVisita = "por segunda vez";
		    }
	        
	        InfToPrinter = JustInformation(InfToPrinter,"En el dia de hoy estuvimos visitando su predio "+NumVisita+", con el fin de realizar revision y practicar las pruebas tecnicas con motivo a la investigacion del incremento del consumo de agua (Art. 149 Ley 142 de 1994). Las cuales no se realizaron por motivo: "+this._tempRegistro.getAsString("motivo").toUpperCase(), 3, 0, 1);
	        
	        if (this._tempRegistro.getAsString("jornada_notificacion").equals("am")){
	        	 InfToPrinter = WrTitulo(InfToPrinter, "TENIENDO EN CUENTA LO ANTERIOR, SE REQUIERE ESTAR PRESENTE EL DIA " + this._tempRegistro.getAsString("fecha_notificacion") + " EN HORAS DE LA MANANA..", 1, 1);
	        }else{
	        	InfToPrinter = WrTitulo(InfToPrinter, "TENIENDO EN CUENTA LO ANTERIOR, SE REQUIERE ESTAR PRESENTE EL DIA " + this._tempRegistro.getAsString("fecha_notificacion") + " EN HORAS DE LA TARDE.", 1, 1);
		    }
	
	        InfToPrinter = JustInformation(InfToPrinter, "Recuerde que es obligatorio que atiendan la visita en la fecha y jornada indicada anteriormente, de no ser asi, la empresa podra suspender el servicio de acueducto a lo establecido en el articulo 26 numeral 26.13 del decreto 302 del 2000.", 3, 1, 1);
	        InfToPrinter = JustInformation(InfToPrinter, "Si presenta dificultad, dentro de las 24 horas siguientes confirmar al numero telefonico 6724308 para concertar una nueva visita.", 3, 1, 1);	
	        InfToPrinter = JustInformation(InfToPrinter, "De conformidad con el articulo 12 de la resolucion CRA 413 de 2006, usted puede solicitar a su costo, la asesoria de un tecnico particular para que la acompane durante la visita, en la revision de medidor y de las instalaciones internas. No obstante, puede renunciar a esta asesoria, de lo cual se dejara constancia. Esta visita debe ser atendida por un mayor de edad.", 3, 1, 1);	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Observacion.", 3, 1, 1);
	        InfToPrinter = JustInformation(InfToPrinter, this._tempRegistro.getAsString("observacion"), 3, 0, 2);
	        InfToPrinter = WrLabel(InfToPrinter, "Lectura Actual:", this._tempRegistro.getAsString("lectura"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "No del Medidor:", this._tempRegistro.getAsString("medidor"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Precinto:", this._tempRegistro.getAsString("precinto"), 3, 0, 2);
	        
	        
	        InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
            InfToPrinter = WrLabel(InfToPrinter, "Firma "+TipoUsuario, NombreUsuario, 3, 0, 3);
            
	        	        
	        String Tecnico 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'nombre_tecnico'");
	        String Cedula 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'cedula_tecnico'");
	        InfToPrinter = JustInformation(InfToPrinter, "Declaro bajo la gravedad de juramento que los datos aqui consignados son veraces, fueron verificados y obdecen al estado actual del predio.", 3, 0, 1);        
	        
	        InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Nombre del Operario:", Tecnico, 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "CC: ", Cedula, 3, 0, 2);
	        InfToPrinter = WrLabel(InfToPrinter, TipoImpresion, "", 3, 0, 1);
	        InfToPrinter = WrTitulo(InfToPrinter, "CONSORCIO AGUAS DEL LLANO.", 0, 1);
	        InfToPrinter = WrTitulo(InfToPrinter, "Contratista de la Empresa de Acueducto y Alcantarillado de Villavicencio E.S.P.", 0, 1);
	        InfToPrinter = DoLabel(InfToPrinter);
	        MnBt.IntentPrint(Impresora,InfToPrinter);
	    }else{
	    	Toast.makeText(this.context, "No existen registros de la revision, no olvide guardar los datos antes de imprimir.", Toast.LENGTH_LONG).show();   		
        }
	}
	
	
	public void FormatoDesviacion(String TipoImpresion, String Solicitud){
		MnBt = new Bluetooth(this.context);
		this.FcnSQL	= new SQLite(this.context, FormLoggin.CARPETA_RAIZ, FormLoggin.NOMBRE_DATABASE);
		String periodo_ini = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_ini", "revision = '" + Solicitud + "'");
		String periodo_fin = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "periodo_fin", "revision = '" + Solicitud + "'");
		Double factura = this.FcnSQL.DoubleSelectShieldWhere("db_solicitudes", "factura", "revision = '" + Solicitud + "'");
		
		String CamposGeneral = "";
		String Direccion = "";
		String tempEstrato = "sin identificar";
		InfToPrinter = "";
		
		CamposGeneral = this.FcnSQL.StrSelectShieldWhere("db_solicitudes", "id_serial", "revision = '" + Solicitud + "'");
		Impresora = this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'impresora'");
	    /*this.FcnSQL.SelectData(	CamposImpresion, 
							"db_desviaciones", 
							"revision,codigo,nombre,direccion,serie,ciclo,promedio,visita,fecha,hora,tipo,area,pisos,actividad,uso,residentes,habitado,estado,acueducto,camaramedidor,estadocamara,escapecamara,serieindividual,marcaindividual,diametroindividual,lecturaindividual,serietotalizador,marcatotalizador,diametrototalizador,lecturatotalizador,subterraneos,itemsubterraneos,estadosubterraneos,lavaplatos,itemlavaplatos,estadolavaplatos,lavaderos,itemlavadero,estadolavadero,elevados,itemelevado,estadoelevado,internas,iteminternas,estadointernas,piscinas,itempiscina,estadopiscina,medidorregpaso,medidorregantifraude,medidordestruido,medidorinvertido,medidorilegible,medidorprecintoroto,hermeticidadreginternos,hermeticidadequipomedida,estanqueidadreselevado,estanqueidadcapelevado,estanqueidadfugaelevado,estanqueidadreslavadero,estanqueidadcaplavadero,estanqueidadfugalavadero,estanqueidadressubterraneo,estanqueidadcapsubterraneo,estanqueidadfugasubterrano,hermeticidadfugaimperceptible,hermeticidadfugas,hermeticidadfugavisible,diagnostico,estrato,precinto,serviciodirecto,bypass,nombreusuario,cedulausuario,nombretestigo,cedulatestigo,cisterna,itemcisterna,estadocisterna,ducha,itemducha,estadoducha,lavamanos,itemlavamanos,estadolavamanos,servicioacueducto,servicioalcantarillado,segundoconcepto,respuestadesviacion",
							"revision = '" + Solicitud + "'");*/
		
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
	        
	        String InfToPrinter = "";
	        SetPagePrinter(InfToPrinter, 570, 10, 3, 15, 100);
	        InfToPrinter = DrawImage(InfToPrinter, "AGUAS.PCX", 0, 5);
	        InfToPrinter = DrawImage(InfToPrinter, "EAAV.PCX", 410, 5);
	        InfToPrinter = WrTitulo(InfToPrinter, "CONSORCIO AGUAS DEL LLANO", 2.5, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "VISITA TECNICA", 0, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "PROCESO DE FACTURACION", 0, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "INFORME DE VISITA TECNICA DE CRITICA", 0, 2);
		
	        InfToPrinter = WrLabel(InfToPrinter, "No Revision: ", this._tempRegistro.getAsString("revision"), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Codigo: ", this._tempRegistro.getAsString("codigo"), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Ciclo: ", this._tempRegistro.getAsString("ciclo"), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Acta No: ", "D" + this._tempRegistro.getAsString("revision") + "-" + CamposGeneral, 270, 0, 2);
	        InfToPrinter = WrLabel(InfToPrinter, "Factura Investigacion: ", FormatoSinDecima.format(factura), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Periodo Investigacion:", periodo_ini, 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, " a ", periodo_fin, 365, 0, 1.5);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Fecha:    ", this._tempRegistro.getAsString("fecha"), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Hora: ", this._tempRegistro.getAsString("hora"), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Precinto: ", Precinto, 3, 0, 1.5);
	
	        if(!this._tempRegistro.getAsString("estrato").isEmpty()){
	        	tempEstrato = this._tempRegistro.getAsString("estrato");
	        }
	        
	        if (Precinto.equals("No tiene")){
	            InfToPrinter = JustInformation(InfToPrinter, "Hoy dia " + FechaVisita[0] + " del mes " + FechaVisita[1] + " de " + FechaVisita[2] + " a las " + this._tempRegistro.getAsString("hora") + " se realizo la visita al inmueble ubicado en la direccion " + Direccion + ", de estrato " + tempEstrato + ", nombre del suscriptor " + this._tempRegistro.getAsString("nombre") + "." + Precinto, 3, 0, 1);
	        }else{
	        	InfToPrinter = JustInformation(InfToPrinter, "Hoy dia " + FechaVisita[0] + " del mes " + FechaVisita[1] + " de " + FechaVisita[2] + " a las " + this._tempRegistro.getAsString("hora") + " se realizo la visita al inmueble ubicado en la direccion " + Direccion + ", de estrato " + tempEstrato + ", nombre del suscriptor " + this._tempRegistro.getAsString("nombre") + ", en cuyo medidor se encuentra instalado el precinto " + Precinto, 3, 0, 1);
	        }
	        InfToPrinter = WrLabel(InfToPrinter, "Telefono:", "____________________", 3, 0, 1.5);
	    	
	
	        if (!this._tempRegistro.getAsString("nombreusuario").isEmpty()){
	            InfToPrinter = JustInformation(InfToPrinter, "El senor(a) " + this._tempRegistro.getAsString("nombreusuario") + ", identificado con CC. " + this._tempRegistro.getAsString("cedulausuario") + ", manifiesta no hacer uso del derecho que le otorga la resolucion CRA 413 de 2006. Esta visita debe ser atendida por un mayor de edad.", 3, 1, 1);
	        }
	        
	        InfToPrinter = WrSubTitulo(InfToPrinter, "CARACTERISTICAS DEL INMUEBLE", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Tipo:", this._tempRegistro.getAsString("tipo"), 3, 0, 0);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Pisos:    ", this._tempRegistro.getAsString("pisos"), 273, 0, 1);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Area:",this._tempRegistro.getAsString("area") + " Mts2", 3, 0, 0);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Actividad:", this._tempRegistro.getAsString("actividad"), 273, 0, 1);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Uso:                    ", this._tempRegistro.getAsString("uso"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "No Personas Residentes: ", this._tempRegistro.getAsString("residentes"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Estado Inmueble:        ", this._tempRegistro.getAsString("estado"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Habitado Por:           ", this._tempRegistro.getAsString("habitado"), 3, 0, 1);	
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "INFORMACION TECNICA", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Servicio de Acueducto Administrado Por: ", this._tempRegistro.getAsString("acueducto"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Escape:           ", this._tempRegistro.getAsString("escapecamara"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Servicio Directo: ", this._tempRegistro.getAsString("serviciodirecto"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Bypass:           ", this._tempRegistro.getAsString("bypass"), 3, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Camara Medidor y/o Nicho: ", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Tamano: ", this._tempRegistro.getAsString("camaramedidor"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Estado: ", this._tempRegistro.getAsString("estadocamara"), 3, 0, 1);
	
	        dobleColumna = getCurrentLine();
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Medidor Individual", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "No: ", this._tempRegistro.getAsString("serieindividual"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Marca: ", this._tempRegistro.getAsString("marcaindividual"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Diametro: ", this._tempRegistro.getAsString("diametroindividual") + " pulg.", 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Lectura: ", this._tempRegistro.getAsString("lecturaindividual"), 3, 0, 1);
	
	        setCurrentLine(dobleColumna);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Medidor Totalizador", 270, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "No: ", this._tempRegistro.getAsString("serietotalizador"), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Marca: ", this._tempRegistro.getAsString("marcatotalizador"), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Diametro: ", this._tempRegistro.getAsString("diametrototalizador") + " pulg.", 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Lectura: ", this._tempRegistro.getAsString("lecturatotalizador"), 270, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "INFORMACION VISITA TECNICA", 3, 1, 1);
	
	        if(!this._tempRegistro.getAsString("subterraneos").equals("0")){
	        	InfToPrinter = WrSubTitulo(InfToPrinter, "Tanque Subterraneo", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("subterraneos"), 270, 0, 1);
		
		        AllItems = this._tempRegistro.getAsString("itemsubterraneos").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadosubterraneos").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	        
	        if(!this._tempRegistro.getAsString("lavaplatos").equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Lavaplatos", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("lavaplatos"), 270, 0, 1);
		
		        AllItems = this._tempRegistro.getAsString("itemlavaplatos").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadolavaplatos").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("cisterna").equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "UNIDAD SANITARIA", 3, 1, 1);
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Cisternas", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("cisterna"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemcisterna").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadocisterna").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("ducha").equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Duchas", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("ducha"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemducha").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadoducha").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("lavamanos").equals("0")){
	        	InfToPrinter = WrSubTitulo(InfToPrinter, "Lavamanos", 10, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("lavamanos"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemlavamanos").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadolavamanos").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	        
	        if(!this._tempRegistro.getAsString("lavaderos").equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Tanque Lavadero", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("lavaderos"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemlavadero").toString().split("-");
		        AllEstado = this._tempRegistro.getAsString("estadolavadero").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	
	        //'setCurrentLine(dobleColumna)
	        if(!this._tempRegistro.getAsString("elevados").equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Tanque Elevado", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("elevados"), 270, 0, 1);
		        AllItems = this._tempRegistro.getAsString("itemelevado").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadoelevado").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	
	        if(!this._tempRegistro.getAsString("internas").equals("0")){
	        	InfToPrinter = WrSubTitulo(InfToPrinter, "Instalaciones Hidraulicas Internas", 3, 1, 1);
		        AllItems = this._tempRegistro.getAsString("iteminternas").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadointernas").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!this._tempRegistro.getAsString("piscinas").equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Piscina", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", this._tempRegistro.getAsString("piscinas"), 270, 0, 1);		
		        AllItems = this._tempRegistro.getAsString("itempiscina").split("-");
		        AllEstado = this._tempRegistro.getAsString("estadopiscina").split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Medidor", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Registro de Paso:        ", this._tempRegistro.getAsString("medidorregpaso"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Registro Antifraude:     ", this._tempRegistro.getAsString("medidorregantifraude"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Destruido:               ", this._tempRegistro.getAsString("medidordestruido"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Invertido:               ", this._tempRegistro.getAsString("medidorinvertido"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Vidrio Ilegible:         ", this._tempRegistro.getAsString("medidorilegible"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Precinto Seguridad Roto: ", this._tempRegistro.getAsString("medidorprecintoroto"), 3, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Prueba Hermeticidad Inst. Hidraulicas Internas", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Cierre de Registros Internos:  ", this._tempRegistro.getAsString("hermeticidadreginternos"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Verificacion Equipo de Medida: ", this._tempRegistro.getAsString("hermeticidadequipomedida"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Revision de Fugas:             ", this._tempRegistro.getAsString("hermeticidadfugas"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Fuga Imperceptible:            ", this._tempRegistro.getAsString("hermeticidadfugaimperceptible"), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Fuga Visible:                  ", this._tempRegistro.getAsString("hermeticidadfugavisible"), 3, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "PRUEBA DE ESTANQUEIDAD TANQUES", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Item/Tanque ", "", 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Resultado", "", 150, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Capacidad", "", 280, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Magnitud Fuga", "", 415, 0, 1);
	        
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
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Subterraneo", "", 3, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[0], 150, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[1], 280, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[2], 415, 0, 1);
	
	        if (this._tempRegistro.getAsString("estanqueidadreselevado").equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = this._tempRegistro.getAsString("estanqueidadreselevado");
                AllItems[1] = this._tempRegistro.getAsString("estanqueidadcapelevado");
                AllItems[2] = this._tempRegistro.getAsString("estanqueidadfugaelevado");
	        }
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Elevado", "", 3, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[0], 150, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[1], 280, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[2], 415, 0, 1);
	
	        if (this._tempRegistro.getAsString("estanqueidadreslavadero").equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = this._tempRegistro.getAsString("estanqueidadreslavadero");
                AllItems[1] = this._tempRegistro.getAsString("estanqueidadcaplavadero");
                AllItems[2] = this._tempRegistro.getAsString("estanqueidadfugalavadero");
	        }
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Lavadero", "", 3, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[0], 150, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[1], 280, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[2], 415, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Diagnostico Tecnico", 3, 1, 1);
	        InfToPrinter = JustInformation(InfToPrinter, this._tempRegistro.getAsString("diagnostico"), 3, 0, 2);
	
	        if(!this._tempRegistro.getAsString("nombreusuario").isEmpty()){
	            InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
	            InfToPrinter = WrLabel(InfToPrinter, "Firma Cliente: ", this._tempRegistro.getAsString("nombreusuario"), 3, 0, 1);
	            InfToPrinter = WrLabel(InfToPrinter, "CC.", this._tempRegistro.getAsString("cedulausuario"), 3, 0, 3);
	        }else{
	            InfToPrinter = WrTitulo(InfToPrinter, "Se deja copia bajo puerta", 1.5, 1.5);
	        }
	
	        if(!this._tempRegistro.getAsString("nombretestigo").isEmpty()){
	            InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
	            InfToPrinter = WrLabel(InfToPrinter, "Firma Testigo y/o Interventor: ", this._tempRegistro.getAsString("nombretestigo"), 3, 0, 1);
	            InfToPrinter = WrLabel(InfToPrinter, "CC.", this._tempRegistro.getAsString("cedulatestigo"), 3, 0, 3);
	        }
	
	        String Tecnico 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'nombre_tecnico'");
	        String Cedula 	= this.FcnSQL.StrSelectShieldWhere("db_parametros", "valor", "item = 'cedula_tecnico'");
	
	        InfToPrinter = JustInformation(InfToPrinter, "Declaro bajo la gravedad de juramento que los datos aqui consignados son veraces, fueron verificados y obdecen al estado actual del predio.", 3, 0, 1);
	        
	        InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Firma Tecnico Contratista: ", Tecnico, 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "CC.", Cedula, 3, 0, 3);
	
	        InfToPrinter = WrLabel(InfToPrinter, TipoImpresion, "", 3, 0, 1);
	        InfToPrinter = WrTitulo(InfToPrinter, "CONSORCIO AGUAS DEL LLANO.", 0, 1);
	        InfToPrinter = WrTitulo(InfToPrinter, "Contratista de la Empresa de Acueducto y Alcantarillado de Villavicencio E.S.P.", 0, 1);
	        InfToPrinter = DoLabel(InfToPrinter);
	        MnBt.IntentPrint(Impresora,InfToPrinter);
	    }else{
			Toast.makeText(this.context, "No existen registros de la revision, no olvide guardar los datos antes de imprimir.", Toast.LENGTH_LONG).show();   		
	    }
	}
	
	
	private void SetPagePrinter(String InfLabel, int WidthPaper, int MarginTop, int MarginLeft, int MarginRight, int MarginBottom){
		WidthLabel = WidthPaper - MarginLeft - MarginRight;
        OffsetLabel = (555 - WidthLabel) / 2;
        StartLine = MarginTop;
        CurrentLine = StartLine;
        EndLabel = MarginBottom;
	}
	
	
	private String DrawImage(String cpclData, String NameFile, int PosX, int PosY){
		cpclData = cpclData + "PCX " + PosX + " " + (PosY + CurrentLine) + " ";
        cpclData = cpclData + "!<" + NameFile + "\r\n";
        return cpclData;
    }
	
	
	private String WrTitulo(String cpclData, String TextoTitulo, double SaltoLineaPre, double SaltoLineaPos){
		//Function WrTitulo(ByVal cpclData As String, ByVal TextoTitulo As String, ByVal SaltoLineaPre As Double, ByVal SaltoLineaPos As Double)
	    int Char2Line;
	    String Words[];
	    String WordsLine;
	    int Justificacion;
	
	    SetValuesFont("TITULO");
	    WordsLine = "";
	    Justificacion = 0;
	
	    CurrentLine += HeightFont * SaltoLineaPre;
	    Char2Line = WidthLabel / WidthFont;
	    Words = TextoTitulo.split(" ");
	
	    for(int i = 0; i<Words.length;i++){
	        WordsLine += Words[i] + " ";
	        if (i < Words.length){
	            Justificacion = Words[i].length();
	        }else{
	            Justificacion = 0;
	        }
	
	        if ((WordsLine.length() + Justificacion) > Char2Line){
	            Justificacion = (WidthLabel - (WordsLine.length() * WidthFont)) / 2;
	            cpclData += "TEXT " + LabelFont + " 0 " + Justificacion + " " + CurrentLine + " " + WordsLine +"\r\n";
	            CurrentLine += HeightFont;
	            WordsLine = "";
	        }
	    }
	    Justificacion = (WidthLabel - (WordsLine.length() * WidthFont)) / 2;
	    cpclData += "TEXT " + LabelFont + " 0 " + Justificacion + " " + CurrentLine + " " + WordsLine + "\r\n";
	    CurrentLine += HeightFont * SaltoLineaPos;
	    return cpclData;
	//End Function
	}
	
	
	private void SetValuesFont(String Label){
		if (Label.equals("TITULO")){
	        LabelFont = "INSTRUCT.CPF";
	        HeightFont = 22;
	        WidthFont = 13;
	    }else if(Label.equals("SUBTITULO")){
	        LabelFont = "LIBERATI.CPF";
	        HeightFont = 20;
	        WidthFont = 10;
	    }else if(Label.equals("LABEL")){
	        LabelFont = "JACKINPU.CPF";
	        HeightFont = 18;
	        WidthFont = 11;	
	        TextFont = "JACKINPU.CPF";
	        HeightText = 18;
	        WidthText = 11;
	    }else{
	        TextFont = "JACKINPU.CPF";
	        HeightText = 18;
	        WidthText = 11;
	    }
	}
	
	
	private String WrLabel(String cpclData, String Etiqueta, String InfEtiqueta, int OffsetWrLabel, double SaltoLineaPre, double SaltoLineaPos){ 
		//Function WrLabel(ByVal cpclData As String, ByVal Etiqueta As String, ByVal InfEtiqueta As String, ByVal OffsetWrLabel As Integer, ByVal SaltoLineaPre As Double, ByVal SaltoLineaPos As Double)
	    CurrentLine += HeightFont * SaltoLineaPre;
	    SetValuesFont("LABEL");
	    cpclData += "TEXT " + LabelFont + " 0 " + OffsetWrLabel + " " + CurrentLine + " " + Etiqueta + " \r\n";
	    cpclData += "TEXT " + TextFont + " 0 " + (OffsetWrLabel + ((Etiqueta.length() + 1) * WidthFont)) + " " + CurrentLine + " " + InfEtiqueta + "\r\n";
	    CurrentLine += HeightFont * SaltoLineaPos;
	    return cpclData;
	    //End Function
	}
	
	
	private String JustInformation(String cpclData, String Informacion, int OffsetInformation, double SaltoLineaPre, double SaltoLineaPos){
		SetValuesFont("TEXTO");
		int Char2Line = (WidthLabel - OffsetInformation) / WidthText;
		CurrentLine += HeightText * SaltoLineaPre;

		while (Informacion.length() > Char2Line){
			cpclData += "TEXT " + TextFont + " 0 " + OffsetInformation + " " + CurrentLine + " " + Informacion.substring(0, Char2Line) + " \r\n";
			Informacion = Informacion.substring(Char2Line);
			CurrentLine += HeightText;
		}
		cpclData += "TEXT " + TextFont + " 0 " + OffsetInformation + " " + CurrentLine + " " + Informacion + " \r\n";
		CurrentLine += HeightText * SaltoLineaPos;
		return cpclData;
	}
	
	
	private String WrSubTitulo(String cpclData, String InfSubtitulo, int OffsetSub, double SaltoLineaPre, double SaltoLineaPos){
		CurrentLine += HeightFont * SaltoLineaPre;
		SetValuesFont("SUBTITULO");
		cpclData += "TEXT " + LabelFont + " 0 " + OffsetSub + " " + CurrentLine + " " + InfSubtitulo + " " + " \r\n";
		CurrentLine += HeightFont * SaltoLineaPos;
	    return cpclData;
	}
	
	
	private String WrRectangle(String cpclData, int PosX1, double PosY1, int PosX2, double PosY2, double Incremento, int Shadow){
		int IncLine = 0;
	    int i;
	
	    cpclData += "BOX " + PosX1 + " " + PosY1 + " " + PosX2 + " " + PosY2 + " 2 \r\n";
	
	    if (Shadow != 0){
	        IncLine = (PosX2 - PosX1) / Shadow;
	        for (i = 0; i<Shadow;i++){
	            cpclData += "BOX " + PosX1 + (IncLine * i) + " " + PosY1 + " " + PosX1 + (IncLine * i) + " " + PosY2 + " 0 \r\n";
	        }
	
	        for(i = 0;i<Shadow;i++){
	            cpclData += "BOX " + PosX1 + " " + PosY1 + (IncLine * i) + " " + PosX2 + " " + PosY1 + (IncLine * i) + " 0 \r\n";
	        }
	    }
	
	
	    if (Incremento != 0) {
	        CurrentLine += PosY2 - PosY1;
	    }
	    return cpclData;
	}
	
	
	private String DoLabel(String cpclData){
		cpclData = "! " + OffsetLabel + " 200 200 " + (CurrentLine + EndLabel) + " 1" + " \r\n" + "LABEL " + " \r\n" + cpclData + " \r\n";
	    cpclData += "PRINT \r\n";
	    return cpclData;
	}

	
	private double getCurrentLine(){
	    return CurrentLine;
	}	
	
	
	private void setCurrentLine(double valorCurrentLine){
		CurrentLine = valorCurrentLine;
	}
}
