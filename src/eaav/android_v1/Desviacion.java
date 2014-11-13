package eaav.android_v1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import modal.ModalInfGeneral;
import modal.ModalInputSingle;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import clases.ClassDesviacion;
import clases.ClassRevision;

import Miscelanea.DateTime;
import Miscelanea.SQLite;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


public class Desviacion extends Activity implements OnClickListener, OnItemSelectedListener{


	private Intent 			ModalInputSingle;
	private Intent 			ModalInformacionSolicitud;
	private Intent 			FormSolicitudes;
	
	//Instancias
	private Impresiones 		Imp; 
	private ClassDesviacion 	FcnDesviacion;
	private ClassRevision		FcnRevision;
	
	//private SQLite SQL = new SQLite(this);
	DateTime DT = new DateTime();
	private ArrayList<String> InfBasica;	
	private ArrayList<String>  RtaCamposDesviacion= new ArrayList<String>();
	
	/**Variables del activity**/
	private String Revision;
	private String FolderAplicacion;
		
	
	//nusoap
	private static String URL	= "http://190.93.133.127:8080/EAAV-Desviaciones/ServerPDA/WS_EAAV_Desviaciones.php?wsdl";
	private static String NAMESPACE 	= "http://190.93.133.127:8080/EAAV-Desviaciones/ServerPDA";
	private static String METHOD_NAME	= "DesviacionTxt";
	private static String SOAP_ACTION 	= "DesviacionTxt";
	boolean MenuEnabled = false;
	
	/*String's Adaptadores Informacion General*/
	String[] strEstrato			= {"","1","2","3","4","5","6"};																
	String[] strEstado 			= {"","Habitado","Deshabitado","Abandonado","Demolido","Lote","Construccion","Predio Solo"};
	String[] strHabitado 		= {"","Propietario","Arrendatario"};
	String[] strTipo			= {"","Apartamentos","Centro Comercial","Conjunto Residencial","Hotel y/o Residencias","Inquilinato","Oficinas","Unidad Locativa","Vivienda","Vivienda-Local Anexo","Otros"};		
	String[] strAcueducto 		= {"","Si","No"};
	String[] strAlcantarillado 	= {"","Si","No"};
	String[] strUso 			= {"","Oficial","Residencial","Comercial","Especial","Industrial","Provisional"};
	
	
	String[] Elementos = {"Subterraneo","Lavaplatos","Cisterna","Ducha","Lavamanos","Lavadero","Tanque Elevado","Inst. Internas","Piscina"};
	String[] EstadoItem1 = {"Bueno","Malo","N/A"};
	String[] EstadoItem2 = {"Bueno","Malo","N/A"};
	String[] EstadoItem3 = {"Bueno","Malo","N/A"};
	String[] EstadoOtro	 = {"Bueno","Malo","N/A"};
	String[] ItemInstalaciones = {"","Cierre reg. Internos","Verif. Equipo Medida","Revision de Fugas","Fuga Imperceptible","Fuga Visible"};
	String[] RtaInstalaciones = {"","Si","No"};
	String[] RtaRegistroPaso = {"","Bueno","Malo","No Tiene"};
	String[] RtaRegistroAntifraude = {"","Bueno","Malo","No Tiene"};
	String[] RtaDestruido = {"","Si","No","No Aplica"};
	String[] RtaInvertido = {"","Si","No"};
	String[] RtaVidrioIlegible = {"","Si","No"};
	String[] RtaPrecintoSeguridad = {"","Si","No","No Tiene"};
	String[] TanquesEstanqueidad = {"","Subterraneo","Elevado","Lavadero"};
	String[] PruebaEstanqueidad = {"","No Se Realizo", "Buena", "Mala"};
	String[] CapacidadEstanqueidad = {"Lts","Mts3"};
	String[] FugaEstanqueidad = {"Lts/min","Mts3/min"};
	String[] ClaseAcueducto = {"","EAAV","Pozo Profundo","Aljibe","J.A.C.","Otro"};
	String[] CamaraMedidor = {"","Grande","Pequena","No Tiene"};
	String[] EstadoCamaraMedidor = {"","Buena","Regular","Mala","No Aplica"};
	String[] EscapeMedidor = {"","No Tiene","En El Medidor","Antes del Medidor","Despues del Medidor"};
	String[] ServicioDirecto = {"","Si","No"};
	String[] Bypass = {"","Si","No"};
	String[] DiametroIndividual = {"","1/2","3/4","1","1-1/2","2"};
	String[] DiametroTotalizador = {"","1/2","3/4","1","1-1/2","2"};
	String[] SegundoConcepto ={"","Si","No"};
	String[] RespuestaDesviacion ={"","Normal por consumo","Fuga o daño interno","Posible fraude encontrado","Equipo de medida danado o robado","Error de lectura"};
	
	
	/*Adaptadores Informacion General*/
	ArrayAdapter<String> AdaptadorEstrato;
	ArrayAdapter<String> AdaptadorEstado;
	ArrayAdapter<String> AdaptadorHabitado;
	ArrayAdapter<String> AdaptadorTipo;
	ArrayAdapter<String> AdaptadorAcueducto;
	ArrayAdapter<String> AdaptadorAlcantarillado;
	ArrayAdapter<String> AdaptadorUso;
	
	
	
	ArrayAdapter<String> AdaptadorElementos;
	ArrayAdapter<String> AdaptadorEstadoItem1;
	ArrayAdapter<String> AdaptadorEstadoItem2;
	ArrayAdapter<String> AdaptadorEstadoItem3;
	ArrayAdapter<String> AdaptadorEstadoOtro;
	ArrayAdapter<String> AdaptadorItemInstalaciones;
	ArrayAdapter<String> AdaptadorRtaInstalaciones;
	ArrayAdapter<String> AdaptadorRtaRegistroPaso;
	ArrayAdapter<String> AdaptadorRtaRegistroAntifraude;
	ArrayAdapter<String> AdaptadorRtaDestruido;
	ArrayAdapter<String> AdaptadorRtaInvertido;
	ArrayAdapter<String> AdaptadorRtaVidrioIlegible;
	ArrayAdapter<String> AdaptadorRtaPrecintoSeguridad;
	ArrayAdapter<String> AdaptadorTanquesEstanqueidad;
	ArrayAdapter<String> AdaptadorPruebaEstanqueidad;
	ArrayAdapter<String> AdaptadorCapacidadEstanqueidad;
	ArrayAdapter<String> AdaptadorFugaEstanqueidad;
	
	ArrayAdapter<String> AdaptadorClaseAcueducto;
	ArrayAdapter<String> AdaptadorCamaraMedidor;
	ArrayAdapter<String> AdaptadorEstadoCamaraMedidor;
	ArrayAdapter<String> AdaptadorEscapeMedidor;
	ArrayAdapter<String> AdaptadorServicioDirecto;
	ArrayAdapter<String> AdaptadorBypass;
	ArrayAdapter<String> AdaptadorDiametroIndividual;
	ArrayAdapter<String> AdaptadorDiametroTotalizador;
	ArrayAdapter<String> AdaptadorSegundoConcepto;
	ArrayAdapter<String> AdaptadorRespuestaDesviacion;
	
	/**Objetos de la pestaña Informacion General**/
	private TextView	_lblServicioAcueducto, _lblServicioAlcantarillado, _lblCual;
	private EditText 	_txtPrecinto, _txtActividad, _txtPersonas, _txtCual, _txtArea, _txtPisos,  _txtNombreUsuario, _txtCedulaUsuario, _txtNombreTestigo, _txtCedulaTestigo;
	private Spinner		_cmbEstrato, _cmbEstado, _cmbTipo, _cmbHabitado, _cmbAcueducto, _cmbAlcantarillado, _cmbUso;
	private Button		_btnGuardarGeneral;
	
	/*private EditText 	_txtInfTecnicaCual, _txtNumero, _txtMarca, _txtLectura, _txtVisitaTecnicaObservacion, _txtVisitaTecnicaCapacidad, _txtVisitaTecnicaFuga;
	private EditText 	_txtVisitaTecnicaCantidad;
	private Spinner  	_Estrato, _Tipo, _Acueducto, _Alcantarillado, _Uso, _Estado, _Habitado, _Elementos, _EstadoItem1, _EstadoItem2, _EstadoItem3, _EstadoOtro, _ItemInstalacion, _RtaInstalacion, _RtaRegistroPaso;
	private Spinner  	_RtaRegistroAntifraude, _RtaDestruido, _RtaInvertido, _RtaVidrioIlegible, _RtaPrecintoSeguridad, _TanquesEstanqueidad, _PruebasEstanqueidad, _CapacidadEstanqueidad, _FugaEstanqueidad;
	private Spinner  	_ClaseAcueducto, _CamaraMedidor, _EstadoCamaraMedidor, _EscapeMedidor, _ServicioDirecto, _Bypass, _Diametro, _SegundoConcepto, _RespuestaDesviacion;
	private TextView 	_LblCual, _LblAcueducto, _LblAlcantarillado, _LblInfTecnicaCual, _item1, _item2, _item3;
	private Button 		_BtnGuardarInfGeneral, _BtnGuardarInfTecnica, _BtnVisitaTecnicaInstalaciones, _BtnVisitaTecnicaGuardarElementos, _BtnGuardarDatosMedidor, _BtnVisitaTecnicaPruebaEstanqueidad, _BtnInfTecnicaObservacionSistema;
	*/
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_desviacion); 
		
		//Captura de la solicitud seleccionada
		Bundle bundle = getIntent().getExtras();
		this.Revision			= bundle.getString("Solicitud");
		this.FolderAplicacion	= bundle.getString("FolderAplicacion");
		
		this.ModalInputSingle			= new Intent(this, ModalInputSingle.class);
		this.ModalInformacionSolicitud	= new Intent(this, ModalInfGeneral.class);		
		this.FormSolicitudes 			= new Intent(this, FormListaTrabajo.class);
		
		this.Imp 			= new Impresiones(this);
		this.FcnDesviacion	= new ClassDesviacion(this, this.FolderAplicacion);
		this.FcnRevision	= new ClassRevision(this, this.FolderAplicacion);
		
		if(!this.FcnDesviacion.existeRegistroDesviacion(this.Revision)){
			this.FcnDesviacion.registrarDesviacion(this.Revision);
		}
      		
		//Inicializacion y carga de los tab's
		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		tabs.setup();
		 
		TabHost.TabSpec spec=tabs.newTabSpec("inf_general");		
		spec.setContent(R.id.InfGeneral);
		spec.setIndicator("Informacion General");
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("inf_tecnica");
		spec.setContent(R.id.InfTecnica);
		spec.setIndicator("Informacion Tecnica");
		tabs.addTab(spec);		
		
		spec=tabs.newTabSpec("inf_visita_tecnica");
		spec.setContent(R.id.VisitaBtnGuardar);
		spec.setIndicator("Informacion Vista Tecnica");
		tabs.addTab(spec);
		tabs.setCurrentTab(0);	
		
		/**Referencia a objetos de la pestaña Informacion General**/
		_lblServicioAcueducto		= (TextView) findViewById(R.id.GeneralLblServicioAcueducto);
		_lblServicioAlcantarillado	= (TextView) findViewById(R.id.GeneralLblServicioAlcantarillado);
		_lblCual					= (TextView) findViewById(R.id.GeneralLblCual);
		
		_txtPrecinto		= (EditText) findViewById(R.id.GeneralTxtPrecinto);
		_txtActividad		= (EditText) findViewById(R.id.GeneralTxtActividad);
		_txtPersonas		= (EditText) findViewById(R.id.GeneralTxtPersonas);
		_txtCual			= (EditText) findViewById(R.id.GeneralTxtCual);
		_txtArea			= (EditText) findViewById(R.id.GeneralTxtArea);
		_txtPisos			= (EditText) findViewById(R.id.GeneralTxtPisos);
		_txtNombreUsuario	= (EditText) findViewById(R.id.GeneralTxtUsuario);
		_txtCedulaUsuario	= (EditText) findViewById(R.id.GeneralTxtCedulaUsuario);
		_txtNombreTestigo	= (EditText) findViewById(R.id.GeneralTxtTestigo);
		_txtCedulaTestigo	= (EditText) findViewById(R.id.GeneralTxtCedulaTestigo);
		
		_cmbEstrato			= (Spinner) findViewById(R.id.GeneralCmbEstrato);
		_cmbEstado			= (Spinner) findViewById(R.id.GeneralCmbEstado);
		_cmbHabitado		= (Spinner) findViewById(R.id.GeneralCmbHabitado);
		_cmbTipo			= (Spinner) findViewById(R.id.GeneralCmbTipo);
		_cmbAcueducto		= (Spinner) findViewById(R.id.GeneralCmbServicioAcueducto);
		_cmbAlcantarillado	= (Spinner) findViewById(R.id.GeneralCmbServicioAlcantarillado);
		_cmbUso				= (Spinner) findViewById(R.id.GeneralCmbUso);
		
		_btnGuardarGeneral	= (Button) findViewById(R.id.GeneralBtnGuardar);
		
		//Referencia a objetos
		//_Estrato 		= (Spinner) findViewById(R.id.GeneralCmbEstrato); 
		//_Acueducto  	= (Spinner) findViewById(R.id.CmbDesviacionAcueducto); 
		/*_Estado 	 	= (Spinner) findViewById(R.id.CmbDesviacionEstado); 
		_Habitado  		= (Spinner) findViewById(R.id.CmbDesviacionHabitado); */
		/*_Elementos 		= (Spinner) findViewById(R.id.CmbVisitaTecnicaElemento); 
		_EstadoItem1	= (Spinner) findViewById(R.id.CmbVisitaTecnicaItem1);
		_EstadoItem2	= (Spinner) findViewById(R.id.CmbVisitaTecnicaItem2);
		_EstadoItem3	= (Spinner) findViewById(R.id.CmbVisitaTecnicaItem3);
		_EstadoOtro		= (Spinner) findViewById(R.id.CmbVisitaTecnicaOtro);
		_ItemInstalacion= (Spinner) findViewById(R.id.CmbVisitaTecnicaInstalaciones);
		_RtaInstalacion = (Spinner) findViewById(R.id.CmbVisitaTecnicaRtaInstalaciones);
		_RtaRegistroPaso= (Spinner) findViewById(R.id.CmbVisitaTecnicaRegistroPaso);
		_RtaRegistroAntifraude= (Spinner) findViewById(R.id.CmbVisitaTecnicaRegistro);
		_RtaDestruido	= (Spinner) findViewById(R.id.CmbVisitaTecnicaDestruido); 
		_RtaInvertido	= (Spinner) findViewById(R.id.CmbVisitaTecnicaInvertido);
		_RtaVidrioIlegible = (Spinner) findViewById(R.id.CmbVisitaTecnicaVidrioIlegible);
		_RtaPrecintoSeguridad= (Spinner) findViewById(R.id.CmbVisitaTecnicaPrecintoSeguridad);
		
		_TanquesEstanqueidad	= (Spinner) findViewById(R.id.CmbVisitaTecnicaElementoPrueba);
		_PruebasEstanqueidad	= (Spinner) findViewById(R.id.CmbVisitaTecnicaEstanqueidadResultado);
		_CapacidadEstanqueidad	= (Spinner) findViewById(R.id.CmbVisitaTecnicaCapacidadMedida);
		_FugaEstanqueidad		= (Spinner) findViewById(R.id.CmbVisitaTecnicaMedidaFuga);*/
		
		/*_ClaseAcueducto		= (Spinner) findViewById(R.id.CmbInfTecnicaAcueducto);
		_CamaraMedidor		= (Spinner) findViewById(R.id.CmbInfTecnicaCamaraMedidor);
		_EstadoCamaraMedidor= (Spinner) findViewById(R.id.CmbInfTecnicaEstado);
		_EscapeMedidor 		= (Spinner) findViewById(R.id.CmbInfTecnicaEscape);
		_ServicioDirecto	= (Spinner) findViewById(R.id.CmbInfTecnicaServicioDirecto);
		_Bypass				= (Spinner) findViewById(R.id.TecnicaCmbBypass);
		_Diametro			= (Spinner) findViewById(R.id.TecnicaCmbDiametro);
		//_DiametroTotalizador= (Spinner) findViewById(R.id.CmbInfTecnicaDiametroTotalizador);
		_SegundoConcepto 	= (Spinner) findViewById(R.id.TecnicaCmbSegundoConcepto);
		_RespuestaDesviacion= (Spinner) findViewById(R.id.TecnicaCmbRespuesta);*/
		
		/*_txtRevision	= (EditText) findViewById(R.id.TxtDesviacionRevision); 
		_txtCodigo		= (EditText) findViewById(R.id.TxtDesviacionCodigo);
		_txtNombre		= (EditText) findViewById(R.id.TxtDesviacionNombre);
		_txtDireccion	= (EditText) findViewById(R.id.TxtDesviacionDireccion);
		_txtSerie		= (EditText) findViewById(R.id.TxtDesviacionSerie); 
		_txtCiclo		= (EditText) findViewById(R.id.TxtDesviacionCiclo);
		_txtPromedio	= (EditText) findViewById(R.id.TxtDesviacionPromedio); 
		_txtVisita		= (EditText) findViewById(R.id.TxtDesviacionVisita); */
		//_txtPrecinto	= (EditText) findViewById(R.id.GeneralTxtPrecinto); 
		//_txtUso			= (EditText) findViewById(R.id.TxtDesviacionUso);
		//_txtCual		= (EditText) findViewById(R.id.TxtDesviacionCual); 
		//_txtArea		= (EditText) findViewById(R.id.TxtDesviacionArea);
		//_txtPisos		= (EditText) findViewById(R.id.TxtDesviacionPisos);
		/*_txtActividad	= (EditText) findViewById(R.id.TxtDesviacionActividad);
		_txtPersonas	= (EditText) findViewById(R.id.TxtDesviacionPersonas);
		_txtUsuario		= (EditText) findViewById(R.id.TxtDesviacionNombreUsuario);
		_txtCedulaUsuario= (EditText) findViewById(R.id.TxtDesviacionCedulaUsuario); 
		_txtTestigo		= (EditText) findViewById(R.id.TxtDesviacionNombreTestigo);
		_txtCedulaTestigo= (EditText) findViewById(R.id.TxtDesviacionCedulaTestigo);*/ 
		//_txtOtro		= (EditText) findViewById(R.id.TxtVisitaTecnicaOtro);
		/*_txtVisitaTecnicaCantidad	= (EditText) findViewById(R.id.TxtVisitaTecnicaCantidad);
		
		_txtVisitaTecnicaCapacidad = (EditText) findViewById(R.id.TxtVisitaTecnicaCapacidad);
		_txtVisitaTecnicaFuga = (EditText) findViewById(R.id.TxtVisitaTecnicaFuga);*/
		
		
		//_txtInfTecnicaCual 	= (EditText) findViewById(R.id.TxtInfTecnicaCual);
		/*_txtNumero			= (EditText) findViewById(R.id.TecnicaTxtNumero);
		_txtMarca			= (EditText) findViewById(R.id.TecnicaTxtMarca);
		_txtLectura			= (EditText) findViewById(R.id.TecnicaTxtLectura);
		_txtVisitaTecnicaObservacion = (EditText) findViewById(R.id.TecnicaTxtObservacion);*/
		
		//_LblInfTecnicaCual 	= (TextView) findViewById(R.id.LblInfTecnicaCual);
		//_LblCual 			= (TextView) findViewById(R.id.LblDesviacionCual);
		//_LblAcueducto 		= (TextView) findViewById(R.id.GeneralLblServicioAcueducto);
		//_LblAlcantarillado 	= (TextView) findViewById(R.id.LblDesviacionAlcantarillado);
		/*_item1	= (TextView) findViewById(R.id.LblVisitaTecnicaItem1);
		_item2	= (TextView) findViewById(R.id.LblVisitaTecnicaItem2);
		_item3	= (TextView) findViewById(R.id.LblVisitaTecnicaItem3);*/
		
		//_BtnGuardarInfGeneral = (Button) findViewById(R.id.BtnGuardarInfGeneral);
		//_BtnGuardarInfTecnica = (Button) findViewById(R.id.TecnicaBtnGuardar);
		/*_BtnVisitaTecnicaInstalaciones = (Button) findViewById(R.id.BtnVisitaTecnicaInstalaciones);
		_BtnVisitaTecnicaGuardarElementos = (Button) findViewById(R.id.BtnVisitaTecnicaGuardarElementos);
		_BtnGuardarDatosMedidor = (Button) findViewById(R.id.BtnGuardarDatosMedidor);
		_BtnVisitaTecnicaPruebaEstanqueidad = (Button) findViewById(R.id.BtnVisitaTecnicaPruebaEstanqueidad);*/
		//_BtnInfTecnicaObservacionSistema = (Button) findViewById(R.id.TecnicaBtnObservacion);
		
		//Deshabilitacion de los campos informativos
		/*_txtRevision.setEnabled(false);
		_txtCodigo.setEnabled(false);
		_txtNombre.setEnabled(false);
		_txtDireccion.setEnabled(false);
		_txtSerie.setEnabled(false);
		_txtCiclo.setEnabled(false);
		_txtPromedio.setEnabled(false);
		_txtVisita.setEnabled(false);
		_txtUso.setEnabled(false);*/
	
		/*Asociacion de adaptadores y objetos Informacion General*/
		AdaptadorEstrato 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strEstrato);
		_cmbEstrato.setAdapter(AdaptadorEstrato);
		
		AdaptadorEstado= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strEstado);
		_cmbEstado.setAdapter(AdaptadorEstado);
		
		AdaptadorHabitado= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strHabitado);
		_cmbHabitado.setAdapter(AdaptadorHabitado);
		
		AdaptadorTipo= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strTipo);
		_cmbTipo.setAdapter(AdaptadorTipo);
		
		AdaptadorAcueducto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strAcueducto);
		_cmbAcueducto.setAdapter(AdaptadorAcueducto);
		
		AdaptadorAlcantarillado= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strAlcantarillado);
		_cmbAlcantarillado.setAdapter(AdaptadorAlcantarillado);
		
		AdaptadorUso= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strUso);
		_cmbUso.setAdapter(AdaptadorUso);
		
		
		
		
		
		/*AdaptadorEstadoItem1= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,EstadoItem1);
		_EstadoItem1.setAdapter(AdaptadorEstadoItem1);*/
		
		/*AdaptadorEstadoItem2= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,EstadoItem2);
		_EstadoItem2.setAdapter(AdaptadorEstadoItem2);

		AdaptadorEstadoItem3= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,EstadoItem3);
		_EstadoItem3.setAdapter(AdaptadorEstadoItem3);
		
		AdaptadorEstadoOtro= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,EstadoOtro);
		_EstadoOtro.setAdapter(AdaptadorEstadoOtro);*/
		
		/*AdaptadorItemInstalaciones= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ItemInstalaciones);
		_ItemInstalacion.setAdapter(AdaptadorItemInstalaciones);
		
		AdaptadorRtaInstalaciones= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RtaInstalaciones);
		_RtaInstalacion.setAdapter(AdaptadorRtaInstalaciones);
		
		AdaptadorRtaRegistroPaso= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RtaRegistroPaso);
		_RtaRegistroPaso.setAdapter(AdaptadorRtaRegistroPaso);
		
		AdaptadorRtaRegistroAntifraude= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RtaRegistroAntifraude);
		_RtaRegistroAntifraude.setAdapter(AdaptadorRtaRegistroAntifraude);
		
		AdaptadorRtaDestruido= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RtaDestruido);
		_RtaDestruido.setAdapter(AdaptadorRtaDestruido);
		
		AdaptadorRtaInvertido= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RtaInvertido);
		_RtaInvertido.setAdapter(AdaptadorRtaInvertido);
		
		AdaptadorRtaVidrioIlegible= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RtaVidrioIlegible);
		_RtaVidrioIlegible.setAdapter(AdaptadorRtaVidrioIlegible);
		
		AdaptadorRtaPrecintoSeguridad= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RtaPrecintoSeguridad);
		_RtaPrecintoSeguridad.setAdapter(AdaptadorRtaPrecintoSeguridad);
		
		AdaptadorTanquesEstanqueidad= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,TanquesEstanqueidad);
		_TanquesEstanqueidad.setAdapter(AdaptadorTanquesEstanqueidad);
		
		AdaptadorPruebaEstanqueidad= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,PruebaEstanqueidad);
		_PruebasEstanqueidad.setAdapter(AdaptadorPruebaEstanqueidad);
		
		AdaptadorCapacidadEstanqueidad= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,CapacidadEstanqueidad);
		_CapacidadEstanqueidad.setAdapter(AdaptadorCapacidadEstanqueidad);
		
		AdaptadorFugaEstanqueidad= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,FugaEstanqueidad);
		_FugaEstanqueidad.setAdapter(AdaptadorFugaEstanqueidad);
		
		AdaptadorClaseAcueducto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ClaseAcueducto);
		_ClaseAcueducto.setAdapter(AdaptadorClaseAcueducto);
		
		AdaptadorCamaraMedidor= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,CamaraMedidor);
		_CamaraMedidor.setAdapter(AdaptadorCamaraMedidor);
		
		AdaptadorEstadoCamaraMedidor= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,EstadoCamaraMedidor);
		_EstadoCamaraMedidor.setAdapter(AdaptadorEstadoCamaraMedidor);
		
		AdaptadorEscapeMedidor= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,EscapeMedidor);
		_EscapeMedidor.setAdapter(AdaptadorEscapeMedidor);
		
		AdaptadorServicioDirecto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ServicioDirecto);
		_ServicioDirecto.setAdapter(AdaptadorServicioDirecto);
		
		AdaptadorBypass= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Bypass);
		_Bypass.setAdapter(AdaptadorBypass);
		
		AdaptadorDiametroIndividual= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,DiametroIndividual);
		_DiametroIndividual.setAdapter(AdaptadorDiametroIndividual);
		
		AdaptadorDiametroTotalizador= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,DiametroTotalizador);
		_DiametroTotalizador.setAdapter(AdaptadorDiametroTotalizador);

		AdaptadorSegundoConcepto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,SegundoConcepto);
		_SegundoConcepto.setAdapter(AdaptadorSegundoConcepto);
		
		AdaptadorRespuestaDesviacion= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RespuestaDesviacion);
		_RespuestaDesviacion.setAdapter(AdaptadorRespuestaDesviacion);
				
		AdaptadorElementos= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Elementos);
		AdaptadorElementos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_Elementos.setAdapter(AdaptadorElementos);*/
			
		_cmbUso.setOnItemSelectedListener(this);
		_cmbTipo.setOnItemSelectedListener(this);
		_btnGuardarGeneral.setOnClickListener(this);
		
		//////////////////////////////////////
		/**/
		//CargarInfGuardada();
	}
	
	
	
	/*private boolean ValidarVisitaTecnicaElementos(){
		boolean ValorRetorno= false;
		if(_txtVisitaTecnicaCantidad.getText().length()==0){
			ValorRetorno = false;
		}else{
			ValorRetorno = true;
		}
		return ValorRetorno;
	}*/
	
	
	/*private boolean ValidarVisitaTecnicaPruebaEstanqueidad(){
		boolean ValorRetorno = false;
		if(_TanquesEstanqueidad.getSelectedItem().toString().equals("")||_PruebasEstanqueidad.getSelectedItem().toString().equals("")||_txtVisitaTecnicaFuga.getText().equals("")||_txtVisitaTecnicaCapacidad.getText().equals("")){
			ValorRetorno = false;
		}else{
			ValorRetorno = true;
		}
		return ValorRetorno;
	}*/
	

	
	//funcion para validar las instalaciones
	/*private boolean ValidarVisitaTecnicaInstalaciones(){
		boolean ValorRetorno = false;
		if((_ItemInstalacion.getSelectedItemPosition()==0)||(_RtaInstalacion.getSelectedItemPosition()==0)){
			ValorRetorno = false;
		}else{
			ValorRetorno = true;
		}
		return ValorRetorno;
	}*/
	
	
	
	/*private void GuardarVisitaTecnicaElementos(){
		//"Subterraneo","Lavaplatos","Cisterna","Ducha","Lavamanos","Lavadero","Tanque Elevado","Inst. Internas","Piscina"
		String Cantidad = "";
		String Items = "";
		String ValorItems = "";
		String Estado = "";
		String ValorEstado = "";
		int Caso = 0;
		if(_Elementos.getSelectedItem().toString().equals("Subterraneo")){
			Cantidad= "subterraneos";
			Items	= "itemsubterraneos";
			Estado 	= "estadosubterraneos";
			Caso 	= 1;
			
		}else if(_Elementos.getSelectedItem().toString().equals("Lavaplatos")){
			Cantidad= "lavaplatos";
			Items	= "itemlavaplatos";
			Estado 	= "estadolavaplatos";	
			Caso 	= 1;
		}else if(_Elementos.getSelectedItem().toString().equals("Cisterna")){
			Cantidad= "cisterna";
			Items	= "itemcisterna";
			Estado 	= "estadocisterna";	
			Caso 	= 3;
		}else if(_Elementos.getSelectedItem().toString().equals("Ducha")){
			Cantidad= "ducha";
			Items	= "itemducha";
			Estado 	= "estadoducha";
			Caso 	= 1;
		}else if(_Elementos.getSelectedItem().toString().equals("Lavamanos")){
			Cantidad= "lavamanos";
			Items	= "itemlavamanos";
			Estado 	= "estadolavamanos";
			Caso 	= 1;
		}else if(_Elementos.getSelectedItem().toString().equals("Lavadero")){
			Cantidad= "lavaderos";
			Items	= "itemlavadero";
			Estado 	= "estadolavadero";
			Caso 	= 1;
		}else if(_Elementos.getSelectedItem().toString().equals("Tanque Elevado")){
			Cantidad= "elevados";
			Items	= "itemelevado";
			Estado 	= "estadoelevado";
			Caso 	= 1;
		}else if(_Elementos.getSelectedItem().toString().equals("Inst. Internas")){
			Cantidad= "internas";
			Items	= "iteminternas";
			Estado 	= "estadointernas";
			Caso 	= 2;
		}else if(_Elementos.getSelectedItem().toString().equals("Piscina")){
			Cantidad= "piscinas";
			Items	= "itempiscina";
			Estado 	= "estadopiscina";
			Caso 	= 3;
		}
		
		ValorItems += _item1.getText()+"-";
		ValorEstado += _EstadoItem1.getSelectedItem().toString()+"-";
		
		
		if((Caso==2)||(Caso==3)){
			ValorItems += _item2.getText()+"-";
			ValorEstado += _EstadoItem2.getSelectedItem().toString()+"-";
		}
		
		if(Caso==3){
			ValorItems += _item3.getText()+"-";
			ValorEstado += _EstadoItem3.getSelectedItem().toString()+"-";
		}
		
		if(_txtOtro.getText().length()!=0){
			ValorItems += _txtOtro.getText()+"-";
			ValorEstado += _EstadoOtro.getSelectedItem().toString()+"-";
		}
		
		ValorItems 	= ValorItems.substring(0,ValorItems.length()-1);
		ValorEstado = ValorEstado.substring(0,ValorEstado.length()-1);
		
		Informacion.clear();
		Informacion.put(Cantidad, _txtVisitaTecnicaCantidad.getText().toString());
		Informacion.put(Items, ValorItems);
		Informacion.put(Estado, ValorEstado);
		//SQL.ActualizarRegistro(	"db_desviaciones", Informacion, "revision='" + Solicitud + "'");		
	}*/
	
	
	/*private void GuardarPruebasEstanqueidad(){
		String tanque = null;
		String capacidad = null;
		String fuga = null;
	
		if(_TanquesEstanqueidad.getSelectedItem().toString().equals("Subterraneo")){
			tanque 		= "estanqueidadressubterraneo";
			capacidad 	= "estanqueidadcapsubterraneo";
			fuga 		= "estanqueidadfugasubterrano";
		}else if(_TanquesEstanqueidad.getSelectedItem().toString().equals("Elevado")){
			tanque 		= "estanqueidadreselevado";
			capacidad 	= "estanqueidadcapelevado";
			fuga 		= "estanqueidadfugaelevado";			
		}else if(_TanquesEstanqueidad.getSelectedItem().toString().equals("Lavadero")){
			tanque 		= "estanqueidadreslavadero";
			capacidad 	= "estanqueidadcaplavadero";
			fuga 		= "estanqueidadfugalavadero";
		}
		
		Informacion.clear();
		Informacion.put(tanque, _PruebasEstanqueidad.getSelectedItem().toString());
		Informacion.put(capacidad, _txtVisitaTecnicaCapacidad.getText().toString()+' '+_CapacidadEstanqueidad.getSelectedItem().toString());
		Informacion.put(fuga, _txtVisitaTecnicaFuga.getText().toString()+' '+_FugaEstanqueidad.getSelectedItem().toString());
		//SQL.ActualizarRegistro(	"db_desviaciones", Informacion, "revision='" + Solicitud + "'");
	}*/
	
	
	/*private void GuardarVisitaTecnicaDatosMedidor(){
		Informacion.clear();
		Informacion.put("medidorregpaso", _RtaRegistroPaso.getSelectedItem().toString());
		Informacion.put("medidorregantifraude", _RtaRegistroAntifraude.getSelectedItem().toString());
		Informacion.put("medidordestruido", _RtaDestruido.getSelectedItem().toString());
		Informacion.put("medidorinvertido", _RtaInvertido.getSelectedItem().toString());
		Informacion.put("medidorilegible", _RtaVidrioIlegible.getSelectedItem().toString());
		Informacion.put("medidorprecintoroto", _RtaPrecintoSeguridad.getSelectedItem().toString());		
		//SQL.ActualizarRegistro(	"db_desviaciones", Informacion, "revision='" + Solicitud + "'");		
	}*/
	
	
	/*private void GuardarVisitaTecnicaInstalaciones(){
		String Campo = "";
		if(_ItemInstalacion.getSelectedItem().toString().equals("Cierre reg. Internos")){
			Campo = "hermeticidadreginternos";
		}else if(_ItemInstalacion.getSelectedItem().toString().equals("Verif. Equipo Medida")){
			Campo = "hermeticidadequipomedida";
		}else if(_ItemInstalacion.getSelectedItem().toString().equals("Revision de Fugas")){
			Campo = "hermeticidadfugas";
		}else if(_ItemInstalacion.getSelectedItem().toString().equals("Fuga Imperceptible")){
			Campo = "hermeticidadfugaimperceptible";
		}else if(_ItemInstalacion.getSelectedItem().toString().equals("Fuga Visible")){
			Campo = "hermeticidadfugavisible";
		}
		
		Informacion.clear();
		Informacion.put(Campo, _RtaInstalacion.getSelectedItem().toString());
		//SQL.ActualizarRegistro("db_desviaciones", Informacion, "revision='" + Solicitud + "'");
	}*/
	
	/*****************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.desviacion, menu);
		return true;
	}

	
	//Se deshabilitan opciones justo antes de crear el menu del formulario
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if(MenuEnabled){
    		menu.findItem(R.id.DesviacionImprimir).setEnabled(false);
        	menu.findItem(R.id.DesviacionTerminar).setEnabled(false);
    	}else{
    		menu.findItem(R.id.DesviacionImprimir).setEnabled(true);
        	menu.findItem(R.id.DesviacionTerminar).setEnabled(true);
    	}    	
        return true;  
    }
	
		//Control de eventos al seleccionar una opcion del menu
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.ImpOriginal:
				if(ValidarImpresionDesviacion()){
					Imp.FormatoDesviacion("Original", this.Revision);
				}
				return true;
				
			case R.id.ImpUsuario:
				if(ValidarImpresionDesviacion()){
					Imp.FormatoDesviacion("Usuario", this.Revision);
				}
				return true;
				
			case R.id.ImpCopia:
				if(ValidarImpresionDesviacion()){
					Imp.FormatoDesviacion("Copia", this.Revision);
				}
				return true;
				
			case R.id.DesviacionTerminar:
				if(ValidarImpresionDesviacion()){
					AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
			        dialogo1.setTitle("Terminar Orden Trabajo");  
			        dialogo1.setMessage("¿ Desea terminar la desviacion. ?");            
			        dialogo1.setCancelable(false);  
			        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
			            public void onClick(DialogInterface dialogo1, int id) {  
			            	//Informacion.clear();
			            	//Informacion.put("estado", 3);
							//SQL.ActualizarRegistro("db_solicitudes", Informacion, "revision = '" + Solicitud + "'");
							
							//Informacion.clear();
							//Informacion.put("horacierre", DT.GetHora());
							//SQL.ActualizarRegistro(	"db_desviaciones", Informacion, "revision='" + Solicitud + "'");
							
							new UpLoadDesviacion().execute();
							Intent k;
							k = new Intent(getApplicationContext(), FormListaTrabajo.class);
							startActivity(k);
			            }  
			        });  
			        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
			            public void onClick(DialogInterface dialogo1, int id) {  
			                //cancelar();
			            }  
			        });            
			        dialogo1.show();
				}
				return true;	
				
			case R.id.DesviacionSwapNotificacion:
				AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);  
		        dialogo2.setTitle("Cambio de Acta.");  
		        dialogo2.setMessage("Confirma cancelar la desviacion e iniciar como notificacion?.");            
		        dialogo2.setCancelable(false);  
		        dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialogo1, int id) {  
		            	//SQL.abrir();
		            	//SQL.BorraRegistro("db_desviaciones", "revision ='"+Solicitud+"'");
		            	//SQL.cerrar();
		            	
		            	finish();
		            	Intent k;
						k = new Intent(getApplicationContext(), FormNotificacion.class);
						//k.putExtra("Solicitud", Solicitud);
						startActivity(k);
		            }  
		        });  
		        dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialogo1, int id) {  
		                //cancelar();
		            }  
		        });          
		        dialogo2.show(); 
				return true;
				
				
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		
		private boolean ValidarImpresionDesviacion(){
			boolean ValorRetorno = false;
			//SQL.abrir();
			/*SQL.SelectData(	RtaCamposDesviacion, 												//Se consulta la informacion basica
							"db_desviaciones", 
							"tipo,nombreusuario,nombretestigo,cedulausuario,cedulatestigo,estrato,area,pisos,uso,actividad,residentes,estado,habitado," +
							"acueducto,camaramedidor,escapecamara,estadocamara,serviciodirecto,bypass,serieindividual,marcaindividual,lecturaindividual," +
							"diametroindividual,serietotalizador,marcatotalizador,lecturatotalizador,diametrototalizador,segundoconcepto,respuestadesviacion," +
							"diagnostico,medidorregpaso,medidorregantifraude,medidordestruido,medidorinvertido,medidorilegible,medidorprecintoroto," +
							"hermeticidadreginternos,hermeticidadequipomedida,hermeticidadfugas,hermeticidadfugaimperceptible,hermeticidadfugavisible",
							"revision = '" + Solicitud + "'");*/
			//SQL.cerrar();
			if(RtaCamposDesviacion.get(0).toString().length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el tipo de predio, recuerde guardar los cambios.", Toast.LENGTH_LONG).show();	
			}else if((RtaCamposDesviacion.get(1).length()==0)&&(RtaCamposDesviacion.get(2).length()==0)){
				Toast.makeText(getApplicationContext(),"Debe ingresar un nombre de usuario y/o testigo.", Toast.LENGTH_LONG).show();		
			}else if((RtaCamposDesviacion.get(3).length()==0)&&(RtaCamposDesviacion.get(4).length()==0)){
				Toast.makeText(getApplicationContext(),"Debe ingresar el numero de cedula del usuario y/o testigo.", Toast.LENGTH_LONG).show();	
			}else if(RtaCamposDesviacion.get(6).length()==0){
				Toast.makeText(getApplicationContext(),"No ha digitado el area.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(7).length()==0){
				Toast.makeText(getApplicationContext(),"No ha digitado la cantidad de pisos del predio.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(8).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el uso del predio.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(9).length()==0){
				Toast.makeText(getApplicationContext(),"No ha digitado la actividad del predio.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(10).length()==0){
				Toast.makeText(getApplicationContext(),"No ha digitado el numero de personas residentes.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(11).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado del predio.", Toast.LENGTH_LONG).show();		
			}else if((!RtaCamposDesviacion.get(11).equals("Predio Solo"))&&(RtaCamposDesviacion.get(12).length()==0)){
				Toast.makeText(getApplicationContext(),"No ha seleccionado quien habita el predio.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(13).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado quien administra el acueducto.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(14).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor tiene camara.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(15).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si la camara presenta escape.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(16).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado de la camara.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(17).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si existe servicio directo.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(18).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si existe bypass.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(19).length()==0){
				Toast.makeText(getApplicationContext(),"No ha digitado la serie del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(20).length()==0){
				Toast.makeText(getApplicationContext(),"No ha digitado la marca del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(21).length()==0){
				Toast.makeText(getApplicationContext(),"No ha digitado la lectura del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(22).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el diametro del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(27).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el usuario solicita segundo concepto.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(28).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado la respuesta a la desviacion.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(29).length()==0){
				Toast.makeText(getApplicationContext(),"No ha ingresado la observacion.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(30).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado del registro de paso.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(31).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado del registro antifraude.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(32).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor esta destruido.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(33).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor esta invertido", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(34).length()==0){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor es ilegible.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(35).length()==0){
				Toast.makeText(getApplicationContext(),"No ha si el estado del precinto del medidor.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(36).length()==0){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Cierre registros internos.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(37).length()==0){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Verificacion equipo de medida.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(38).length()==0){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Verificacion de fugas.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(39).length()==0){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Fuga imperceptible.", Toast.LENGTH_LONG).show();		
			}else if(RtaCamposDesviacion.get(40).length()==0){
				Toast.makeText(getApplicationContext(),"No ha si el estado del precinto del medidor. Fuga visible.", Toast.LENGTH_LONG).show();		
			}else{
				ValorRetorno = true;
			}
			
			return ValorRetorno;
		}
		
		
		/*private void CargarInfGuardada(){
			String[] tempString;
			//SQL.abrir();
			SQL.SelectData(	RtaCamposDesviacion, 												//Se consulta la informacion basica
							"db_desviaciones", 
							"tipo,nombreusuario,nombretestigo,cedulausuario,cedulatestigo,estrato,area,pisos,uso,actividad,residentes,estado,habitado," +
							"acueducto,camaramedidor,escapecamara,estadocamara,serviciodirecto,bypass,serieindividual,marcaindividual,lecturaindividual," +
							"diametroindividual,serietotalizador,marcatotalizador,lecturatotalizador,diametrototalizador,segundoconcepto,respuestadesviacion," +
							"diagnostico,precinto,medidorregpaso,medidorregantifraude,medidordestruido,medidorinvertido,medidorilegible,medidorprecintoroto",
							"revision = '" + Solicitud + "'");
			//sSQL.cerrar();
			
			tempString = RtaCamposDesviacion.get(0).split("@");			
			if(tempString.length==2){
				_txtCual.setText(tempString[1]);
			}
			_txtUsuario.setText(RtaCamposDesviacion.get(1));
			_txtTestigo.setText(RtaCamposDesviacion.get(2));
			_txtCedulaUsuario.setText(RtaCamposDesviacion.get(3));
			_txtCedulaTestigo.setText(RtaCamposDesviacion.get(4));
			_Tipo.setSelection(AdaptadorTipo.getPosition(tempString[0]));
			//_Estrato.setSelection(AdaptadorEstrato.getPosition(RtaCamposDesviacion.get(5)));
			_txtArea.setText(RtaCamposDesviacion.get(6));
			_txtPisos.setText(RtaCamposDesviacion.get(7));
			_Uso.setSelection(AdaptadorUso.getPosition(RtaCamposDesviacion.get(8)));
			_txtActividad.setText(RtaCamposDesviacion.get(9));
			_txtPersonas.setText(RtaCamposDesviacion.get(10));
			_Estado.setSelection(AdaptadorEstado.getPosition(RtaCamposDesviacion.get(11)));
			_Habitado.setSelection(AdaptadorHabitado.getPosition(RtaCamposDesviacion.get(12)));
			
			
			tempString = RtaCamposDesviacion.get(13).split("@");			
			if(tempString.length==2){
				_txtInfTecnicaCual.setText(tempString[1]);
			}
			_ClaseAcueducto.setSelection(AdaptadorClaseAcueducto.getPosition(tempString[0]));
			_CamaraMedidor.setSelection(AdaptadorCamaraMedidor.getPosition(RtaCamposDesviacion.get(14)));
			_EscapeMedidor.setSelection(AdaptadorEscapeMedidor.getPosition(RtaCamposDesviacion.get(15)));
			_EstadoCamaraMedidor.setSelection(AdaptadorEstadoCamaraMedidor.getPosition(RtaCamposDesviacion.get(16)));
			_ServicioDirecto.setSelection(AdaptadorServicioDirecto.getPosition(RtaCamposDesviacion.get(17)));
			_Bypass.setSelection(AdaptadorBypass.getPosition(RtaCamposDesviacion.get(18)));
			
			_txtNumero.setText(RtaCamposDesviacion.get(19));
			_txtMarca.setText(RtaCamposDesviacion.get(20));
			_txtLectura.setText(RtaCamposDesviacion.get(21));
			_Diametro.setSelection(AdaptadorDiametroIndividual.getPosition(RtaCamposDesviacion.get(22)));
			
			_SegundoConcepto.setSelection(AdaptadorSegundoConcepto.getPosition(RtaCamposDesviacion.get(27)));
			_RespuestaDesviacion.setSelection(AdaptadorRespuestaDesviacion.getPosition(RtaCamposDesviacion.get(28)));
			_txtVisitaTecnicaObservacion.setText(RtaCamposDesviacion.get(29));
			_txtPrecinto.setText(RtaCamposDesviacion.get(30));
			
			_RtaRegistroPaso.setSelection(AdaptadorRtaRegistroPaso.getPosition(RtaCamposDesviacion.get(31)));
			_RtaRegistroAntifraude.setSelection(AdaptadorRtaRegistroAntifraude.getPosition(RtaCamposDesviacion.get(32)));
			_RtaDestruido.setSelection(AdaptadorRtaDestruido.getPosition(RtaCamposDesviacion.get(33)));
			_RtaInvertido.setSelection(AdaptadorRtaInvertido.getPosition(RtaCamposDesviacion.get(34)));
			_RtaVidrioIlegible.setSelection(AdaptadorRtaVidrioIlegible.getPosition(RtaCamposDesviacion.get(35)));
			_RtaPrecintoSeguridad.setSelection(AdaptadorRtaPrecintoSeguridad.getPosition(RtaCamposDesviacion.get(36)));			
		}*/
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.GeneralBtnGuardar:
					this.FcnDesviacion.setPrecinto(this.Revision, _txtPrecinto.getText().toString());
					this.FcnDesviacion.setActividad(this.Revision, _txtActividad.getText().toString());
					this.FcnDesviacion.setEstrato(this.Revision, _cmbEstrato.getSelectedItem().toString());
					this.FcnDesviacion.setEstado(this.Revision, _cmbEstado.getSelectedItem().toString());
					this.FcnDesviacion.setHabitado(this.Revision, _cmbHabitado.getSelectedItem().toString());
					this.FcnDesviacion.setPersonas(this.Revision, _txtPersonas.getText().toString());
					this.FcnDesviacion.setServicioAcueducto(this.Revision, _cmbAcueducto.getSelectedItem().toString());
					this.FcnDesviacion.setServicioAlcantarillado(this.Revision, _cmbAlcantarillado.getSelectedItem().toString());
					this.FcnDesviacion.setArea(this.Revision, _txtArea.getText().toString());
					this.FcnDesviacion.setPisos(this.Revision, _txtPisos.getText().toString());
					this.FcnDesviacion.setUso(this.Revision, _cmbUso.getSelectedItem().toString());
					this.FcnDesviacion.setNombreUsuario(this.Revision, _txtNombreUsuario.getText().toString());
					this.FcnDesviacion.setCedulaUsuario(this.Revision, _txtCedulaUsuario.getText().toString());
					this.FcnDesviacion.setNombreTestigo(this.Revision, _txtNombreTestigo.getText().toString());
					this.FcnDesviacion.setCedulaTestigo(this.Revision, _txtCedulaTestigo.getText().toString());
					
					if(_cmbTipo.getSelectedItem().toString().equals("Otros")){
						this.FcnDesviacion.setTipo(this.Revision, _cmbTipo.getSelectedItem().toString()+"@"+_txtCual.getText().toString());						
					}else{
						this.FcnDesviacion.setTipo(this.Revision, _cmbTipo.getSelectedItem().toString());		
					}		
					Toast.makeText(this, "Datos generales guardados correctamente.", Toast.LENGTH_SHORT).show(); 
					break;
			}
			
			
			/*_BtnVisitaTecnicaPruebaEstanqueidad.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v){
				 if(ValidarVisitaTecnicaPruebaEstanqueidad()){
					 GuardarPruebasEstanqueidad();
					 Toast.makeText(getApplicationContext(), "Pruebas de estanqueidad guardada correctamente.", Toast.LENGTH_SHORT).show(); 
				 }else{
					 Toast.makeText(getApplicationContext(), "Error al guardar la prueba de estanqueidad.", Toast.LENGTH_SHORT).show(); 
				 }
			 }
		 	});*/
			
			/*_BtnVisitaTecnicaGuardarElementos.setOnClickListener(new OnClickListener(){
			@Override
            public void onClick(View v){
            	if(ValidarVisitaTecnicaElementos()){
            		GuardarVisitaTecnicaElementos();
            		Toast.makeText(getApplicationContext(), "Elemento guardado correctamente.", Toast.LENGTH_SHORT).show(); 
            	}else{
            		Toast.makeText(getApplicationContext(), "Error al guardar el elemento.", Toast.LENGTH_SHORT).show(); 
            	}
            }
		});*/
		
		
		/*_BtnGuardarDatosMedidor.setOnClickListener(new OnClickListener(){
			@Override
            public void onClick(View v){
            	GuardarVisitaTecnicaDatosMedidor();
            	Toast.makeText(getApplicationContext(), "Datos del medidor guardados correctamente.", Toast.LENGTH_SHORT).show(); 
            }
		});*/		
			
			/*_BtnInfTecnicaObservacionSistema.setOnClickListener(new OnClickListener(){
			@Override
            public void onClick(View v){
            	String ObservacionSistema = "";
            	ObservacionSistema ="Se realiza visita tecnica en presencia de " + _txtUsuario.getText() + ",identificado (a) con cedula " + _txtCedulaUsuario.getText() + ", quien no " +
            						"hace uso del derecho que le otorga el art. 12 de la resolucion CRA 413 de 2006 de estar asesorado de un tecnico particular a su costo, predio de " + _txtPisos.getText() + " pisos, habitado por " + _Habitado.getSelectedItem().toString() + ", numero de habitantes " + _txtPersonas.getText() + ", de uso " + _Uso.getSelectedItem().toString();
            	
            	if(_CamaraMedidor.getSelectedItem().toString().equals("Grande")||_CamaraMedidor.getSelectedItem().toString().equals("PequeÃ±a")){
            		if(_EstadoCamaraMedidor.getSelectedItem().toString().equals("Buena")){
            			ObservacionSistema += ", cajilla en buen estado ";
            		}else if(_EstadoCamaraMedidor.getSelectedItem().toString().equals("Regular")){
            			ObservacionSistema += ", cajilla en estado regular ";
                	}else{
                		ObservacionSistema += ", cajilla en mal estado ";
                	}
            	}else{
            		ObservacionSistema += ", predio sin cajilla ";
            	}
            	
            	if(_RtaDestruido.getSelectedItem().toString().equals("No")&&(_RtaVidrioIlegible.getSelectedItem().toString().equals("No"))){
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
            	}
            	//Verificar el estado de las instalaciones internas
            	_txtVisitaTecnicaObservacion.setText(ObservacionSistema);
            }
		});*/
		
		
		/*_BtnGuardarInfTecnica.setOnClickListener(new OnClickListener(){
			@Override
            public void onClick(View v){
            	//if(ValidarInfTecnica()){
            	//GuardarInfTecnica();
            	//}
				String tempAcueducto = "";
				if(_ClaseAcueducto.getSelectedItem().toString().equals("Otro")){
					tempAcueducto = _ClaseAcueducto.getSelectedItem().toString()+"@"+_txtInfTecnicaCual.getText();
				}else{
					tempAcueducto = _ClaseAcueducto.getSelectedItem().toString();
				}
				
				Informacion.clear();
				Informacion.put("acueducto", tempAcueducto);
				Informacion.put("escapecamara", _EscapeMedidor.getSelectedItem().toString());
				Informacion.put("serviciodirecto", _ServicioDirecto.getSelectedItem().toString());
				Informacion.put("bypass", _Bypass.getSelectedItem().toString());
				Informacion.put("camaramedidor", _CamaraMedidor.getSelectedItem().toString());
				Informacion.put("estadocamara", _EstadoCamaraMedidor.getSelectedItem().toString());
				Informacion.put("serieindividual", _txtNumero.getText().toString());
				Informacion.put("marcaindividual", _txtMarca.getText().toString());
				Informacion.put("lecturaindividual", _txtLectura.getText().toString());
				Informacion.put("diametroindividual", _Diametro.getSelectedItem().toString());
				Informacion.put("segundoconcepto", _SegundoConcepto.getSelectedItem().toString());
				Informacion.put("respuestadesviacion", _RespuestaDesviacion.getSelectedItem().toString());
				Informacion.put("diagnostico", _txtVisitaTecnicaObservacion.getText().toString());
				
				//SQL.ActualizarRegistro(	"db_desviaciones", Informacion, "revision='" + Solicitud + "'");
				Toast.makeText(getApplicationContext(), "Informacion tecnica guardada correctamente.", Toast.LENGTH_SHORT).show(); 
            }
		});*/
		
		
		/*_BtnVisitaTecnicaInstalaciones.setOnClickListener(new OnClickListener(){
			@Override
            public void onClick(View v){
            	if(ValidarVisitaTecnicaInstalaciones()){
            		GuardarVisitaTecnicaInstalaciones();
            		Toast.makeText(getApplicationContext(), "Prueba hermeticidad guardada correctamente.", Toast.LENGTH_SHORT).show(); 
            	}else{
            		Toast.makeText(getApplicationContext(), "Error al guardar la prueba de hermeticidad.", Toast.LENGTH_SHORT).show(); 
            	}
            }
		});*/
		}
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			switch(parent.getId()){
				case R.id.GeneralCmbTipo:
					if(this._cmbTipo.getSelectedItem().toString().equals("Otros")){
						this._lblCual.setVisibility(View.VISIBLE);
						this._txtCual.setVisibility(View.VISIBLE);
					}else{
						this._lblCual.setVisibility(View.INVISIBLE);
						this._txtCual.setVisibility(View.INVISIBLE);
					}
					
					if(this._cmbTipo.getSelectedItem().toString().equals("Vivienda-Local Anexo")){
						_lblServicioAcueducto.setVisibility(View.VISIBLE);
						_lblServicioAlcantarillado.setVisibility(View.VISIBLE);
						_cmbAcueducto.setVisibility(View.VISIBLE);
						_cmbAlcantarillado.setVisibility(View.VISIBLE);
					}else{
						_lblServicioAcueducto.setVisibility(View.INVISIBLE);
						_lblServicioAlcantarillado.setVisibility(View.INVISIBLE);
						_cmbAcueducto.setVisibility(View.INVISIBLE);
						_cmbAlcantarillado.setVisibility(View.INVISIBLE);
					}
					_cmbAcueducto.setSelection(0);
					_cmbAlcantarillado.setSelection(0);	
					break;
				
				case R.id.GeneralCmbUso:
					if(this._cmbUso.getSelectedItem().toString().equals("Residencial")){
						_txtActividad.setText("Residencial");
						_txtActividad.setEnabled(false);
					}else{
						_txtActividad.setText("");
						_txtActividad.setEnabled(true);
					}
					break;
			}
			
						
			/*_ClaseAcueducto.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
				if(parentView.getItemAtPosition(position).toString().equals("Otro")){
					_txtInfTecnicaCual.setVisibility(View.VISIBLE);
					_LblInfTecnicaCual.setVisibility(View.VISIBLE);
				}else{
					_txtInfTecnicaCual.setVisibility(View.INVISIBLE);
					_LblInfTecnicaCual.setVisibility(View.INVISIBLE);
				}
				//_txtInfTecnicaCual.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/
			
			/*_Elementos.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
				_txtVisitaTecnicaCantidad.setText("0");
				_txtVisitaTecnicaCantidad.setEnabled(true);
				
				EstadoItem3[0] = "Bueno";
				EstadoItem3[1] = "Malo";
				EstadoItem3[2] = "N/A";
				AdaptadorEstadoItem3.notifyDataSetChanged();
				
				if(parentView.getItemAtPosition(position).toString().equals("Subterraneo")){
					_item1.setText("Flotador");
					_item2.setText("");
					_item3.setText("");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.INVISIBLE);
					_EstadoItem3.setVisibility(View.INVISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Lavaplatos")){
					_item1.setText("Llave Terminal");
					_item2.setText("");
					_item3.setText("");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.INVISIBLE);
					_EstadoItem3.setVisibility(View.INVISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Cisterna")){
					_item1.setText("Flotador");
					_item2.setText("Accesorios");
					_item3.setText("Rebosamiento");
					
					EstadoItem3[0] = "Si";
					EstadoItem3[1] = "No";
					EstadoItem3[2] = "N/A";
					AdaptadorEstadoItem3.notifyDataSetChanged();
									
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.VISIBLE);
					_EstadoItem3.setVisibility(View.VISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Ducha")){
					_item1.setText("Llave Terminal");
					_item2.setText("");
					_item3.setText("");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.INVISIBLE);
					_EstadoItem3.setVisibility(View.INVISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Lavamanos")){
					_item1.setText("Llave Terminal");
					_item2.setText("");
					_item3.setText("");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.INVISIBLE);
					_EstadoItem3.setVisibility(View.INVISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Lavadero")){
					_item1.setText("Llave Terminal");
					_item2.setText("");
					_item3.setText("");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.INVISIBLE);
					_EstadoItem3.setVisibility(View.INVISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Tanque Elevado")){
					_item1.setText("Flotador");
					_item2.setText("");
					_item3.setText("");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.INVISIBLE);
					_EstadoItem3.setVisibility(View.INVISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Inst. Internas")){
					_txtVisitaTecnicaCantidad.setText("1");
					_txtVisitaTecnicaCantidad.setEnabled(false);
					_item1.setText("Tuberia");
					_item2.setText("Accesorios");
					_item3.setText("");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.VISIBLE);
					_EstadoItem3.setVisibility(View.INVISIBLE);
					
				}else if(parentView.getItemAtPosition(position).toString().equals("Piscina")){
					_item1.setText("Registro de Control");
					_item2.setText("Directo Sin Medidor");
					_item3.setText("Filtraciones");
					
					_EstadoItem1.setVisibility(View.VISIBLE);
					_EstadoItem2.setVisibility(View.VISIBLE);
					_EstadoItem3.setVisibility(View.VISIBLE);
					
				}
				// _txtVisitaTecnicaCantidad.setText("");
				//_txtOtro.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/
		}



		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
		
		private class UpLoadDesviacion extends AsyncTask<Void,Void,Void>{
			String path=	Environment.getExternalStorageDirectory() + File.separator + "EAAV" + File.separator + "ArchivoPrueba.txt";
			InputStream is 		= null;
			String CadenaArchivo= null;
			byte[] array;
			
			/*************************************/
			//String res = null;
	    	private ArrayList<String> SendDesviacion;
	    	String pda = null;
	    	String str;
			 
	    	protected void onPreExecute(){
	    		//Se consulta la base de datos
	    		SendDesviacion = new ArrayList<String>();
	    		//SQL.abrir();
	    		/*SQL.SelectData(	SendDesviacion,
								"db_desviaciones",
								"revision, codigo, nombre, direccion, serie, ciclo, promedio, fecha, hora, tipo, area, pisos, actividad, uso, residentes, habitado, estado,"+
  								"acueducto, camaramedidor, estadocamara, serieindividual, marcaindividual, diametroindividual, lecturaindividual, serietotalizador, marcatotalizador,"+ 
  								"diametrototalizador, lecturatotalizador, subterraneos, itemsubterraneos, estadosubterraneos, lavaplatos, itemlavaplatos, estadolavaplatos, lavaderos,"+
  								"itemlavadero, estadolavadero, elevados, itemelevado, estadoelevado, iteminternas, estadointernas, piscinas, itempiscina, estadopiscina, "+
  								"medidorregpaso, medidorregantifraude, medidordestruido, medidorinvertido, medidorilegible, medidorprecintoroto, hermeticidadreginternos, "+
  								"hermeticidadequipomedida," +
  								"estanqueidadreselevado,estanqueidadreslavadero,estanqueidadressubterraneo," +
  								"estanqueidadcapelevado,estanqueidadcaplavadero,estanqueidadcapsubterraneo," +
  								"estanqueidadfugaelevado,estanqueidadfugalavadero,estanqueidadfugasubterrano," +
  								"hermeticidadfugaimperceptible, hermeticidadfugas,"+
  								"hermeticidadfugavisible, diagnostico, estrato, precinto, serviciodirecto, bypass, horaapertura, nombreusuario, cedulausuario, nombretestigo,"+
  								"cedulatestigo, cisterna, itemcisterna, estadocisterna, ducha, itemducha, estadoducha, lavamanos, itemlavamanos,"+
  								"estadolavamanos, servicioacueducto, servicioalcantarillado, horacierre, segundoconcepto, respuestadesviacion",
								"revision='" + Solicitud +"'");
	    		pda = SQL.SelectShieldWhere("db_parametros", "valor", "item='pda'");*/
				//SQL.cerrar();
	    		
	    		CadenaArchivo = SendDesviacion.get(0)+";"+SendDesviacion.get(1)+";"+SendDesviacion.get(2)+";"+
			    				SendDesviacion.get(3)+";"+SendDesviacion.get(4)+";"+SendDesviacion.get(5)+";"+
			    				SendDesviacion.get(6)+";"+SendDesviacion.get(7)+";"+SendDesviacion.get(8)+";"+
					     		SendDesviacion.get(9)+";"+SendDesviacion.get(10)+";"+SendDesviacion.get(11)+";"+
					     		SendDesviacion.get(12)+";"+SendDesviacion.get(13)+";"+SendDesviacion.get(14)+";"+
					    		SendDesviacion.get(15)+";"+SendDesviacion.get(16)+";"+SendDesviacion.get(17)+";"+
					    		SendDesviacion.get(18)+";"+SendDesviacion.get(19)+";"+SendDesviacion.get(20)+";"+
					  			SendDesviacion.get(21)+";"+SendDesviacion.get(22)+";"+SendDesviacion.get(23)+";"+
					  			SendDesviacion.get(24)+";"+SendDesviacion.get(25)+";"+SendDesviacion.get(26)+";"+
					 			SendDesviacion.get(27)+";"+SendDesviacion.get(28)+";"+SendDesviacion.get(29)+";"+
					  			SendDesviacion.get(30)+";"+SendDesviacion.get(31)+";"+SendDesviacion.get(32)+";"+
					  			SendDesviacion.get(33)+";"+SendDesviacion.get(34)+";"+SendDesviacion.get(35)+";"+
					   			SendDesviacion.get(36)+";"+SendDesviacion.get(37)+";"+SendDesviacion.get(38)+";"+
					   			SendDesviacion.get(39)+";"+SendDesviacion.get(40)+";"+SendDesviacion.get(41)+";"+
					    		SendDesviacion.get(42)+";"+SendDesviacion.get(43)+";"+SendDesviacion.get(44)+";"+
					  			SendDesviacion.get(45)+";"+SendDesviacion.get(46)+";"+SendDesviacion.get(47)+";"+
					  			SendDesviacion.get(48)+";"+SendDesviacion.get(49)+";"+SendDesviacion.get(50)+";"+
					  			SendDesviacion.get(51)+";"+SendDesviacion.get(52)+";"+SendDesviacion.get(53)+"-"+
					  			SendDesviacion.get(54)+"-"+SendDesviacion.get(55)+";"+SendDesviacion.get(56)+"-"+
					  			SendDesviacion.get(57)+"-"+SendDesviacion.get(58)+";"+SendDesviacion.get(59)+"-"+
					  			SendDesviacion.get(60)+"-"+SendDesviacion.get(61)+";"+SendDesviacion.get(62)+";"+
					  			SendDesviacion.get(63)+";"+SendDesviacion.get(64)+";"+SendDesviacion.get(65)+";"+
					    		SendDesviacion.get(66)+";"+SendDesviacion.get(67)+";"+SendDesviacion.get(68)+";"+
					    		SendDesviacion.get(69)+";"+SendDesviacion.get(70)+";"+SendDesviacion.get(71)+";"+
					   			SendDesviacion.get(72)+";"+SendDesviacion.get(73)+";"+SendDesviacion.get(74)+";;;"+
					   			SendDesviacion.get(75)+";"+SendDesviacion.get(76)+";"+SendDesviacion.get(77)+";"+
					     		SendDesviacion.get(78)+";"+SendDesviacion.get(79)+";"+SendDesviacion.get(80)+";"+
					    		SendDesviacion.get(81)+";"+SendDesviacion.get(82)+";"+SendDesviacion.get(83)+";"+
					  			SendDesviacion.get(84)+";"+SendDesviacion.get(85)+";"+SendDesviacion.get(86)+";"+
					   			SendDesviacion.get(87)+";"+SendDesviacion.get(88)+";"+pda;

	    	  		
	    		//Primero se crea el archivo de texto a enviar
	    		try {
	    			File file = new File(path);
		    		file.createNewFile();
		    		if (file.exists()&&file.canWrite()){
		    			FileWriter filewriter = new FileWriter(file,false);
		    			filewriter.write(CadenaArchivo);
		    			filewriter.close();
		    		}
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		    		
	    		//Se convierte a tipo array Base64 quedando listo para usar el WS
	    		try{
	    			is = new FileInputStream(path);
	    			if (path != null) {
	    				try {
	    					array=streamToBytes(is);
	    				} finally {
	    					is.close();
	    				}
	    			}
	    		}catch (Exception e){
	    			e.printStackTrace();
	    			try {
	    				throw new IOException("Unable to open R.raw.");
	    			} catch (IOException e1) {
	    				e1.printStackTrace();
	    			}
	    		}
	    		Toast.makeText(getApplicationContext(),"Conectando con el servidor, por favor espere...", Toast.LENGTH_SHORT).show();	
	    		MenuEnabled = true;
	    	}
	    	
	    	private byte[] streamToBytes(InputStream is) {
	    		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
	    		byte[] buffer = new byte[1024];
	    		int len;
	    		try {
		    		while ((len = is.read(buffer)) >= 0) {
		    			os.write(buffer, 0, len);
		    			}
	    		} catch (java.io.IOException e) {
	    		}
	    		return os.toByteArray();
	    	}
	    	
	    	
	    	
	    	@Override
	    	protected Void doInBackground(Void... params) {
	    		try{
	    			SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
	    			so.addProperty("Revision", SendDesviacion.get(0).toString());
	    			so.addProperty("Informacion", array);
	    			SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    			new MarshalBase64().register(sse);
	    			sse.dotNet=true;
	    			sse.setOutputSoapObject(so);
	    			HttpTransportSE htse=new HttpTransportSE(URL);
	    			htse.call(SOAP_ACTION, sse);
	    			SoapPrimitive response=(SoapPrimitive) sse.getResponse();
	    			str = response.toString();
	    			
	    			if(str.equals("Ok")){
	    				//SQL.abrir();
	    				//SQL.BorraRegistro("db_solicitudes", "revision ='" + Solicitud + "'");
	    				//SQL.BorraRegistro("db_desviaciones", "revision ='" + Solicitud + "'");
	    				//SQL.cerrar();
	    			}
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		return null;
	    	}

	    	@Override
	    	protected void onPostExecute(Void unused) {
	    		Toast.makeText(getApplicationContext(),"Conexion terminada.", Toast.LENGTH_SHORT).show();
	    	}	
	    }
}
