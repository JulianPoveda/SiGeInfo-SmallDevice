package eaav.android_v1;

import java.util.ArrayList;

import Miscelanea.ManagerBluetooth;
import Miscelanea.SQLite;
import android.content.Context;
import android.widget.Toast;

public class Impresiones {
	private Context context;
	private ManagerBluetooth MnBt;
	private ArrayList<String>  CamposImpresion= new ArrayList<String>();	
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
		MnBt = new ManagerBluetooth(this.context);
		SQLite ImpSQL = new SQLite(this.context);
		String periodo_ini = ImpSQL.SelectShieldWhere("db_solicitudes", "periodo_ini", "revision = '" + Solicitud + "'");
		String periodo_fin = ImpSQL.SelectShieldWhere("db_solicitudes", "periodo_fin", "revision = '" + Solicitud + "'");
		String factura = ImpSQL.SelectShieldWhere("db_solicitudes", "factura", "revision = '" + Solicitud + "'");
		String NumVisita = "";
		String CamposGeneral = "";
		String Nombre= "";
		String Direccion = "";
		InfToPrinter = "";
		
		CamposGeneral = ImpSQL.SelectShieldWhere("db_solicitudes", "id_serial", "revision = '" + Solicitud + "'");
		Impresora = ImpSQL.SelectShieldWhere("db_parametros", "valor", "item = 'impresora'");
	    ImpSQL.SelectData(	CamposImpresion, 
							"db_notificaciones", 
							"revision,codigo,nombre,direccion,serie,ciclo,promedio,visita,lectura,medidor,precinto,observacion,fecha_visita,hora_visita,motivo,fecha_notificacion,jornada_notificacion", 
							"revision = '" + Solicitud + "'");
		   
	    if (CamposImpresion != null){
	    	Nombre = CamposImpresion.get(2).toString();
	        Direccion = CamposImpresion.get(3).toString();
	        
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
	        InfToPrinter = WrLabel(InfToPrinter, "Acta No:", "N" + CamposImpresion.get(0).toString() + "-" + CamposGeneral + "-" + CamposImpresion.get(7).toString(), 3, 0, 1);
	        //String.format("%.7f", var)
	        InfToPrinter = WrLabel(InfToPrinter, "Factura Investigacion: ", String.format("%.0f", factura), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Periodo Investigacion:", periodo_ini, 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, " a ", periodo_fin, 365, 0, 1.5);
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Fecha:  ", CamposImpresion.get(12).toString(), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Hora: ", CamposImpresion.get(13).toString(), 250, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Codigo: ", CamposImpresion.get(1).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Senor:     ", Nombre, 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Direccion: ", "", 3, 0, 1);
	        InfToPrinter = JustInformation(InfToPrinter, Direccion, 3, 0, 2);
	
	        if (CamposImpresion.get(7).toString().equals("1")){
	        	NumVisita = "por primera vez";
	        }else if(CamposImpresion.get(7).toString().equals("2")){
	        	NumVisita = "por segunda vez";
		    }else{
		    	NumVisita = "por segunda vez";
		    }
	        
	        InfToPrinter = JustInformation(InfToPrinter,"En el dia de hoy estuvimos visitando su predio "+NumVisita+", con el fin de realizar revision y practicar las pruebas tecnicas con motivo a la investigacion del incremento del consumo de agua (Art. 149 Ley 142 de 1994). Las cuales no se realizaron por motivo: "+CamposImpresion.get(14).toString().toUpperCase(), 3, 0, 1);
	        
	        if (CamposImpresion.get(16).toString().equals("am")){
	        	 InfToPrinter = WrTitulo(InfToPrinter, "TENIENDO EN CUENTA LO ANTERIOR, SE REQUIERE ESTAR PRESENTE EL DIA " + CamposImpresion.get(15).toString() + " EN HORAS DE LA MANANA..", 1, 1);
	        }else{
	        	InfToPrinter = WrTitulo(InfToPrinter, "TENIENDO EN CUENTA LO ANTERIOR, SE REQUIERE ESTAR PRESENTE EL DIA " + CamposImpresion.get(15).toString() + " EN HORAS DE LA TARDE.", 1, 1);
		    }
	
	        InfToPrinter = JustInformation(InfToPrinter, "Recuerde que es obligatorio que atiendan la visita en la fecha y jornada indicada anteriormente, de no ser asi, la empresa podra suspender el servicio de acueducto a lo establecido en el articulo 26 numeral 26.13 del decreto 302 del 2000.", 3, 1, 1);
	        InfToPrinter = JustInformation(InfToPrinter, "Si presenta dificultad, dentro de las 24 horas siguientes confirmar al numero telefonico 6724308 para concertar una nueva visita.", 3, 1, 1);	
	        InfToPrinter = JustInformation(InfToPrinter, "De conformidad con el articulo 12 de la resolucion CRA 413 de 2006, usted puede solicitar a su costo, la asesoria de un tecnico particular para que la acompane durante la visita, en la revision de medidor y de las instalaciones internas. No obstante, puede renunciar a esta asesoria, de lo cual se dejara constancia. Esta visita debe ser atendida por un mayor de edad.", 3, 1, 1);	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Observacion.", 3, 1, 1);
	        InfToPrinter = JustInformation(InfToPrinter, CamposImpresion.get(11) .toString(), 3, 0, 2);
	        InfToPrinter = WrLabel(InfToPrinter, "Lectura Actual:", CamposImpresion.get(8).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "No del Medidor:", CamposImpresion.get(9).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Precinto:", CamposImpresion.get(10).toString(), 3, 0, 2);
	        
	        
	        InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
            InfToPrinter = WrLabel(InfToPrinter, "Firma "+TipoUsuario, NombreUsuario, 3, 0, 3);
            
	        	        
	        String Tecnico 	= ImpSQL.SelectShieldWhere("db_parametros", "valor", "item = 'nombre_tecnico'");
	        String Cedula 	= ImpSQL.SelectShieldWhere("db_parametros", "valor", "item = 'cedula_tecnico'");
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
		MnBt = new ManagerBluetooth(this.context);
		SQLite ImpSQL = new SQLite(this.context);
		String periodo_ini = ImpSQL.SelectShieldWhere("db_solicitudes", "periodo_ini", "revision = '" + Solicitud + "'");
		String periodo_fin = ImpSQL.SelectShieldWhere("db_solicitudes", "periodo_fin", "revision = '" + Solicitud + "'");
		String factura = ImpSQL.SelectShieldWhere("db_solicitudes", "factura", "revision = '" + Solicitud + "'");
		
		String CamposGeneral = "";
		String Direccion = "";
		String tempEstrato = "sin identificar";
		InfToPrinter = "";
		
		CamposGeneral = ImpSQL.SelectShieldWhere("db_solicitudes", "id_serial", "revision = '" + Solicitud + "'");
		Impresora = ImpSQL.SelectShieldWhere("db_parametros", "valor", "item = 'impresora'");
	    ImpSQL.SelectData(	CamposImpresion, 
							"db_desviaciones", 
							"revision,codigo,nombre,direccion,serie,ciclo,promedio,visita,fecha,hora,tipo,area,pisos,actividad,uso,residentes,habitado,estado,acueducto,camaramedidor,estadocamara,escapecamara,serieindividual,marcaindividual,diametroindividual,lecturaindividual,serietotalizador,marcatotalizador,diametrototalizador,lecturatotalizador,subterraneos,itemsubterraneos,estadosubterraneos,lavaplatos,itemlavaplatos,estadolavaplatos,lavaderos,itemlavadero,estadolavadero,elevados,itemelevado,estadoelevado,internas,iteminternas,estadointernas,piscinas,itempiscina,estadopiscina,medidorregpaso,medidorregantifraude,medidordestruido,medidorinvertido,medidorilegible,medidorprecintoroto,hermeticidadreginternos,hermeticidadequipomedida,estanqueidadreselevado,estanqueidadcapelevado,estanqueidadfugaelevado,estanqueidadreslavadero,estanqueidadcaplavadero,estanqueidadfugalavadero,estanqueidadressubterraneo,estanqueidadcapsubterraneo,estanqueidadfugasubterrano,hermeticidadfugaimperceptible,hermeticidadfugas,hermeticidadfugavisible,diagnostico,estrato,precinto,serviciodirecto,bypass,nombreusuario,cedulausuario,nombretestigo,cedulatestigo,cisterna,itemcisterna,estadocisterna,ducha,itemducha,estadoducha,lavamanos,itemlavamanos,estadolavamanos,servicioacueducto,servicioalcantarillado,segundoconcepto,respuestadesviacion",
							"revision = '" + Solicitud + "'");
	   
	    String AllItems[];
	    String AllEstado[];

	    if (CamposImpresion != null){
	    	Direccion = CamposImpresion.get(3);
	    	String FechaVisita[] = CamposImpresion.get(8).split("-");
	        String Precinto = "No tiene";
	        double dobleColumna = 0;
	    	
	        if (CamposImpresion.get(70).toString().length()!=0){
	        	Precinto = CamposImpresion.get(70).toString();
	        }
	        
	        String InfToPrinter = "";
	        SetPagePrinter(InfToPrinter, 570, 10, 3, 15, 100);
	        InfToPrinter = DrawImage(InfToPrinter, "AGUAS.PCX", 0, 5);
	        InfToPrinter = DrawImage(InfToPrinter, "EAAV.PCX", 410, 5);
	        InfToPrinter = WrTitulo(InfToPrinter, "CONSORCIO AGUAS DEL LLANO", 2.5, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "VISITA TECNICA", 0, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "PROCESO DE FACTURACION", 0, 1.2);
	        InfToPrinter = WrTitulo(InfToPrinter, "INFORME DE VISITA TECNICA DE CRITICA", 0, 2);
		
	        InfToPrinter = WrLabel(InfToPrinter, "No Revision: ", CamposImpresion.get(0).toString(), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Codigo: ", CamposImpresion.get(1).toString(), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Ciclo: ", CamposImpresion.get(5).toString(), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Acta No: ", "D" + CamposImpresion.get(0).toString() + "-" + CamposGeneral, 270, 0, 2);
	        InfToPrinter = WrLabel(InfToPrinter, "Factura Investigacion: ", factura, 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Periodo Investigacion:", periodo_ini, 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, " a ", periodo_fin, 365, 0, 1.5);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Fecha:    ", CamposImpresion.get(8).toString(), 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Hora: ", CamposImpresion.get(9).toString(), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Precinto: ", Precinto, 3, 0, 1.5);
	
	        if(CamposImpresion.get(69).length()!=0){
	        	tempEstrato = CamposImpresion.get(69);
	        }
	        
	        if (Precinto.equals("No tiene")){
	            InfToPrinter = JustInformation(InfToPrinter, "Hoy dia " + FechaVisita[0] + " del mes " + FechaVisita[1] + " de " + FechaVisita[2] + " a las " + CamposImpresion.get(9).toString() + " se realizo la visita al inmueble ubicado en la direccion " + Direccion + ", de estrato " + tempEstrato + ", nombre del suscriptor " + CamposImpresion.get(2).toString() + "." + Precinto, 3, 0, 1);
	        }else{
	        	InfToPrinter = JustInformation(InfToPrinter, "Hoy dia " + FechaVisita[0] + " del mes " + FechaVisita[1] + " de " + FechaVisita[2] + " a las " + CamposImpresion.get(9).toString() + " se realizo la visita al inmueble ubicado en la direccion " + Direccion + ", de estrato " + tempEstrato + ", nombre del suscriptor " + CamposImpresion.get(2).toString() + ", en cuyo medidor se encuentra instalado el precinto " + Precinto, 3, 0, 1);
	        }
	        InfToPrinter = WrLabel(InfToPrinter, "Telefono:", "____________________", 3, 0, 1.5);
	    	
	
	        if (CamposImpresion.get(73).toString() != ""){
	            InfToPrinter = JustInformation(InfToPrinter, "El senor(a) " + CamposImpresion.get(73).toString() + ", identificado con CC. " + CamposImpresion.get(74).toString() + ", manifiesta no hacer uso del derecho que le otorga la resolucion CRA 413 de 2006. Esta visita debe ser atendida por un mayor de edad.", 3, 1, 1);
	        }
	        
	        InfToPrinter = WrSubTitulo(InfToPrinter, "CARACTERISTICAS DEL INMUEBLE", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Tipo:", CamposImpresion.get(10).toString(), 3, 0, 0);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Pisos:    ", CamposImpresion.get(12).toString(), 273, 0, 1);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Area:", CamposImpresion.get(11).toString() + " Mts2", 3, 0, 0);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Actividad:", CamposImpresion.get(13).toString(), 273, 0, 1);
	
	        InfToPrinter = WrLabel(InfToPrinter, "Uso:                    ", CamposImpresion.get(14).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "No Personas Residentes: ", CamposImpresion.get(15).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Estado Inmueble:        ", CamposImpresion.get(17).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Habitado Por:           ", CamposImpresion.get(16).toString(), 3, 0, 1);	
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "INFORMACION TECNICA", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Servicio de Acueducto Administrado Por: ", CamposImpresion.get(18).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Escape:           ", CamposImpresion.get(21).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Servicio Directo: ", CamposImpresion.get(71).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Bypass:           ", CamposImpresion.get(72).toString(), 3, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Camara Medidor y/o Nicho: ", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Tamano: ", CamposImpresion.get(19).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Estado: ", CamposImpresion.get(20).toString(), 3, 0, 1);
	
	        dobleColumna = getCurrentLine();
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Medidor Individual", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "N�: ", CamposImpresion.get(22).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Marca: ", CamposImpresion.get(23).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Diametro: ", CamposImpresion.get(24).toString() + " pulg.", 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Lectura: ", CamposImpresion.get(25).toString(), 3, 0, 1);
	
	        setCurrentLine(dobleColumna);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Medidor Totalizador", 270, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "N�: ", CamposImpresion.get(26).toString(), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Marca: ", CamposImpresion.get(27).toString(), 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Diametro: ", CamposImpresion.get(28).toString() + " pulg.", 270, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Lectura: ", CamposImpresion.get(29).toString(), 270, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "INFORMACION VISITA TECNICA", 3, 1, 1);
	
	        if(!CamposImpresion.get(30).equals("0")){
	        	InfToPrinter = WrSubTitulo(InfToPrinter, "Tanque Subterraneo", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(30).toString(), 270, 0, 1);
		
		        AllItems = CamposImpresion.get(31).toString().split("-");
		        AllEstado = CamposImpresion.get(32).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	        
	        if(!CamposImpresion.get(33).equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Lavaplatos", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(33).toString(), 270, 0, 1);
		
		        AllItems = CamposImpresion.get(34).toString().split("-");
		        AllEstado = CamposImpresion.get(35).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!CamposImpresion.get(77).equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "UNIDAD SANITARIA", 3, 1, 1);
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Cisternas", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(77).toString(), 270, 0, 1);
		        AllItems = CamposImpresion.get(78).toString().split("-");
		        AllEstado = CamposImpresion.get(79).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!CamposImpresion.get(80).equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Duchas", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(80), 270, 0, 1);
		        AllItems = CamposImpresion.get(81).toString().split("-");
		        AllEstado = CamposImpresion.get(82).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!CamposImpresion.get(83).equals("0")){
	        	InfToPrinter = WrSubTitulo(InfToPrinter, "Lavamanos", 10, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(83).toString(), 270, 0, 1);
		        AllItems = CamposImpresion.get(84).toString().split("-");
		        AllEstado = CamposImpresion.get(85).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	        
	        if(!CamposImpresion.get(36).equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Tanque Lavadero", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(36).toString(), 270, 0, 1);
		        AllItems = CamposImpresion.get(37).toString().split("-");
		        AllEstado = CamposImpresion.get(38).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	
	        //'setCurrentLine(dobleColumna)
	        if(!CamposImpresion.get(39).equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Tanque Elevado", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(39).toString(), 270, 0, 1);
		        AllItems = CamposImpresion.get(40).toString().split("-");
		        AllEstado = CamposImpresion.get(41).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	
	        if(!CamposImpresion.get(42).equals("0")){
	        	InfToPrinter = WrSubTitulo(InfToPrinter, "Instalaciones Hidraulicas Internas", 3, 1, 1);
		        AllItems = CamposImpresion.get(43).toString().split("-");
		        AllEstado = CamposImpresion.get(44).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        if(!CamposImpresion.get(45).equals("0")){
		        InfToPrinter = WrSubTitulo(InfToPrinter, "Piscina", 3, 1, 0);
		        InfToPrinter = WrLabel(InfToPrinter, "Cantidad: ", CamposImpresion.get(45).toString(), 270, 0, 1);		
		        AllItems = CamposImpresion.get(46).toString().split("-");
		        AllEstado = CamposImpresion.get(47).toString().split("-");
		        for (int i = 0;i<AllItems.length;i++){
		            InfToPrinter = WrLabel(InfToPrinter, AllItems[i] + ": ", AllEstado[i], 3, 0, 1);
		        }
	        }
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Medidor", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Registro de Paso:        ", CamposImpresion.get(48).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Registro Antifraude:     ", CamposImpresion.get(49).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Destruido:               ", CamposImpresion.get(50).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Invertido:               ", CamposImpresion.get(51).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Vidrio Ilegible:         ", CamposImpresion.get(52).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Precinto Seguridad Roto: ", CamposImpresion.get(53).toString(), 3, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Prueba Hermeticidad Inst. Hidraulicas Internas", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Cierre de Registros Internos:  ", CamposImpresion.get(54).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Verificacion Equipo de Medida: ", CamposImpresion.get(55).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Revision de Fugas:             ", CamposImpresion.get(66).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Fuga Imperceptible:            ", CamposImpresion.get(65).toString(), 3, 0, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Fuga Visible:                  ", CamposImpresion.get(67).toString(), 3, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "PRUEBA DE ESTANQUEIDAD TANQUES", 3, 1, 1);
	        InfToPrinter = WrLabel(InfToPrinter, "Item/Tanque ", "", 3, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Resultado", "", 150, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Capacidad", "", 280, 0, 0);
	        InfToPrinter = WrLabel(InfToPrinter, "Magnitud Fuga", "", 415, 0, 1);
	        
	        AllItems = new String[3];
	        if (CamposImpresion.get(62).toString().equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = CamposImpresion.get(62).toString();
                AllItems[1] = CamposImpresion.get(63).toString();
                AllItems[2] = CamposImpresion.get(64).toString();
	        }
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Subterraneo", "", 3, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[0], 150, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[1], 280, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[2], 415, 0, 1);
	
	        if (CamposImpresion.get(56).toString().equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = CamposImpresion.get(56).toString();
                AllItems[1] = CamposImpresion.get(57).toString();
                AllItems[2] = CamposImpresion.get(58).toString();
	        }
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Elevado", "", 3, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[0], 150, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[1], 280, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[2], 415, 0, 1);
	
	        if (CamposImpresion.get(59).toString().equals("No Se Realizo")){
	        	AllItems[0] = "N/A";
	        	AllItems[1] = "N/A";
	        	AllItems[2] = "N/A";
	        }else{
	        	AllItems[0] = CamposImpresion.get(59).toString();
                AllItems[1] = CamposImpresion.get(60).toString();
                AllItems[2] = CamposImpresion.get(61).toString();
	        }
	        
	        InfToPrinter = WrLabel(InfToPrinter, "Lavadero", "", 3, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[0], 150, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[1], 280, 0, 0);
	        InfToPrinter = WrSubTitulo(InfToPrinter, AllItems[2], 415, 0, 1);
	
	        InfToPrinter = WrSubTitulo(InfToPrinter, "Diagnostico Tecnico", 3, 1, 1);
	        InfToPrinter = JustInformation(InfToPrinter, CamposImpresion.get(68).toString(), 3, 0, 2);
	
	        if(CamposImpresion.get(73).toString().length()!=0){
	            InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
	            InfToPrinter = WrLabel(InfToPrinter, "Firma Cliente: ", CamposImpresion.get(73).toString(), 3, 0, 1);
	            InfToPrinter = WrLabel(InfToPrinter, "CC.", CamposImpresion.get(74).toString(), 3, 0, 3);
	        }else{
	            InfToPrinter = WrTitulo(InfToPrinter, "Se deja copia bajo puerta", 1.5, 1.5);
	        }
	
	        if(CamposImpresion.get(75).toString().length()!=0){
	            InfToPrinter = WrRectangle(InfToPrinter, 3, getCurrentLine(), 550, getCurrentLine() + 100, 1, 0);
	            InfToPrinter = WrLabel(InfToPrinter, "Firma Testigo y/o Interventor: ", CamposImpresion.get(75).toString(), 3, 0, 1);
	            InfToPrinter = WrLabel(InfToPrinter, "CC.", CamposImpresion.get(76).toString(), 3, 0, 3);
	        }
	
	        String Tecnico 	= ImpSQL.SelectShieldWhere("db_parametros", "valor", "item = 'nombre_tecnico'");
	        String Cedula 	= ImpSQL.SelectShieldWhere("db_parametros", "valor", "item = 'cedula_tecnico'");
	
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
