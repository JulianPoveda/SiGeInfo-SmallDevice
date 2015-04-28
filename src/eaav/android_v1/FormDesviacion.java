package eaav.android_v1;

import java.io.File;
import java.util.ArrayList;

import modal.ModalInfGeneral;
import modal.ModalInputSingle;
import modal.ModalConfirmacion;

import clases.ClassDataSpinner;
import clases.ClassDesviacion;
import clases.ClassRevision;

import Miscelanea.Archivos;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


public class FormDesviacion extends Activity implements OnClickListener, OnItemSelectedListener{
	static int 				INGRESO_ACUEDUCTO_OTRO	= 1;
	static int 				INFORMACION_GENERAL		= 2;
	static int 				CONFIRMACION_TERMINACION= 3;
	static int 				CONFIRMACION_CAMBIO_ACTA= 4;
	static int 				INICIAR_CAMARA			= 5;
	
	private Intent			ModalConfirmacion;
	private Intent 			ModalInputSingle;
	private Intent 			ModalInformacionSolicitud;
	private Intent 			FormSolicitudes;
	private Intent 			IniciarCamara; 
	
	//Instancias
	private Impresiones 		Imp; 
	private	Archivos			FcnArchivos;
	private ClassDesviacion 	FcnDesviacion;
	private ClassRevision		FcnRevision;
	private ClassDataSpinner    FcnDataS;
	
	/**Variables del activity**/
	private String Revision;
	private String FolderAplicacion;
	private ContentValues _tempRegistro = new ContentValues();
	boolean MenuEnabled = false;
	
	/*String's Adaptadores Informacion General*/
	//String[] strEstrato			= {"","1","2","3","4","5","6"};
	private ArrayList<String> strEstrato;
	//String[] strEstado 			= {"","Habitado","Deshabitado","Abandonado","Demolido","Lote","Construccion","Predio Solo"};
	private ArrayList<String> strEstado;
	//String[] strHabitado 		= {"","Propietario","Arrendatario"};
	private ArrayList<String> strHabitado;
	//String[] strTipo			= {"","Apartamentos","Centro Comercial","Conjunto Residencial","Hotel y/o Residencias","Inquilinato","Oficinas","Unidad Locativa","Vivienda","Vivienda-Local Anexo","Otros"};
	private ArrayList<String> strTipo;
	//String[] strAcueducto 		= {"","Si","No"};
	private ArrayList<String> strAcueducto;
	String[] strAlcantarillado 	= {"","Si","No"};
	String[] strUso 			= {"","Oficial","Residencial","Comercial","Especial","Industrial","Provisional"};
	
	/*String's Adaptadores Informacion Tecnica*/
	String[] strClaseAcueducto 		= {"","EAAV","Pozo Profundo","Aljibe","J.A.C.","Otro"};
	String[] strEscapeMedidor 		= {"","No Tiene","En El Medidor","Antes del Medidor","Despues del Medidor"};
	String[] strServicioDirecto 	= {"","Si","No"};
	String[] strBypass 				= {"","Si","No"};
	String[] strCamaraMedidor 		= {"","Grande","Pequena","No Tiene"};
	String[] strEstadoCamaraMedidor	= {"","Buena","Regular","Mala","No Aplica"};
	String[] strRespuestaDesviacion	= {	"",
										"Casa sola",
										"Error de lectura",
										"Equipo de medida dañado o robado",
										"Fuga o daño interno",
										"Normal por consumo",
										"No presta colaboracion",
										"Posible fraude encontrado",
										"Segundo concepto",
										"Servicio suspendido"};
	String[] strSegundoConcepto 	= {"","Si","No"};
	String[] strMedidor				= {"","Individual","Totalizador"};
	String[] strDiametro			= {"","1/2","3/4","1","1-1/2","2"};
	
	/*String´s Adaptadores Informacion Visita Tecnica*/
	ArrayList<String> strClase;//		= {"","Elementos","Estanqueidad","Instalaciones","Medidor"};
	ArrayList<String> strSubClase;
	ArrayList<String> strItem1;
	ArrayList<String> strItem2;
	ArrayList<String> strItem3;
	ArrayList<String> strItem4;
	ArrayList<String> strItem5;
	ArrayList<String> strItem6;
	
	/*Adaptadores Informacion General*/
	ArrayAdapter<String> AdaptadorEstrato;
	ArrayAdapter<String> AdaptadorEstado;
	ArrayAdapter<String> AdaptadorHabitado;
	ArrayAdapter<String> AdaptadorTipo;
	ArrayAdapter<String> AdaptadorAcueducto;
	ArrayAdapter<String> AdaptadorAlcantarillado;
	ArrayAdapter<String> AdaptadorUso;
	
	
	/*Adaptadores Informacion Tecnica*/
	ArrayAdapter<String> AdaptadorClaseAcueducto;
	ArrayAdapter<String> AdaptadorEscapeMedidor;
	ArrayAdapter<String> AdaptadorServicioDirecto;
	ArrayAdapter<String> AdaptadorBypass;
	ArrayAdapter<String> AdaptadorCamaraMedidor;
	ArrayAdapter<String> AdaptadorEstadoCamaraMedidor;
	ArrayAdapter<String> AdaptadorRespuestaDesviacion;
	ArrayAdapter<String> AdaptadorSegundoConcepto;
	ArrayAdapter<String> AdaptadorMedidor;
	ArrayAdapter<String> AdaptadorDiametro;
	
		
	/*Adaptadores Informacion Visita Tecnica*/
	ArrayAdapter<String> AdaptadorClase; 	
	ArrayAdapter<String> AdaptadorSubclase;
	ArrayAdapter<String> AdaptadorItem1;
	ArrayAdapter<String> AdaptadorItem2;
	ArrayAdapter<String> AdaptadorItem3;
	ArrayAdapter<String> AdaptadorItem4;
	ArrayAdapter<String> AdaptadorItem5;
	ArrayAdapter<String> AdaptadorItem6;
	
	
	
	/*Objetos de la pestaña Informacion General*/
	private TextView	_lblServicioAcueducto, _lblServicioAlcantarillado, _lblCual;
	private EditText 	_txtPrecinto, _txtActividad, _txtPersonas, _txtCual, _txtArea, _txtPisos,  _txtNombreUsuario, _txtCedulaUsuario, _txtNombreTestigo, _txtCedulaTestigo;
	private Spinner		_cmbEstrato, _cmbEstado, _cmbTipo, _cmbHabitado, _cmbAcueducto, _cmbAlcantarillado, _cmbUso;
	private Button		_btnGuardarGeneral;
	
	
	/*Objetos de la pestaña Informacion Tecnica*/
	private TextView	_lblOtro;
	private EditText	_txtObservacion,_txtNumero, _txtMarca, _txtLectura;
	private Spinner		_cmbClaseAcueducto, _cmbEscapeMedidor, _cmbServicioDirecto, _cmbBypass, _cmbCamaraMedidor, _cmbEstadoCamara, _cmbRespuesta, _cmbSegundoConcepto, _cmbMedidor, _cmbDiametro;
	private Button		_btnGuardarTecnica, _btnGuardarDatosMedidor, _btnObservacion;
	
	
	/*Objetos de la pestaña de Visita Tecnica*/
	private TextView	_lblCantidad, _lblItem1, _lblItem2, _lblItem3, _lblItem4, _lblItem5, _lblItem6;
	private EditText	_txtCantidad;
	private Spinner		_cmbClase, _cmbSubClase, _cmbItem1, _cmbItem2, _cmbItem3, _cmbItem4, _cmbItem5, _cmbItem6;
	private Button		_btnGuardarVisita;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_desviacion); 
		
		//Captura de la solicitud seleccionada
		Bundle bundle = getIntent().getExtras();
		this.Revision			= bundle.getString("Solicitud");
		this.FolderAplicacion	= bundle.getString("FolderAplicacion");
		
		this.ModalConfirmacion			= new Intent(this, ModalConfirmacion.class);
		this.ModalInputSingle			= new Intent(this, ModalInputSingle.class);
		this.ModalInformacionSolicitud	= new Intent(this, ModalInfGeneral.class);		
		this.FormSolicitudes 			= new Intent(this, FormListaTrabajo.class);
		
		this.Imp 			= new Impresiones(this);
		this.FcnArchivos	= new Archivos(this, this.FolderAplicacion, 10);
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
		spec.setContent(R.id.InfVisita);
		spec.setIndicator("Informacion Vista Tecnica");
		tabs.addTab(spec);
		tabs.setCurrentTab(0);	
		
		/*Referencia a objetos de la pestaña Informacion General*/
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
		
		
		/*Referencia a objetos de informacion tecnica*/
		_lblOtro			= (TextView) findViewById(R.id.TecnicaLblOtro);
		_txtObservacion		= (EditText) findViewById(R.id.TecnicaTxtObservacion);
		_txtNumero			= (EditText) findViewById(R.id.TecnicaTxtNumero);
		_txtMarca			= (EditText) findViewById(R.id.TecnicaTxtMarca); 
		_txtLectura			= (EditText) findViewById(R.id.TecnicaTxtLectura);
		
		_cmbClaseAcueducto	= (Spinner) findViewById(R.id.TecnicaCmbAcueducto);
		_cmbEscapeMedidor	= (Spinner) findViewById(R.id.TecnicaCmbEscape);
		_cmbServicioDirecto	= (Spinner) findViewById(R.id.TecnicaCmbServicioDirecto); 
		_cmbBypass			= (Spinner) findViewById(R.id.TecnicaCmbBypass);
		_cmbCamaraMedidor	= (Spinner) findViewById(R.id.TecnicaCmbCamaraMedidor);
		_cmbEstadoCamara	= (Spinner) findViewById(R.id.TecnicaCmbEstadoCamara); 
		_cmbRespuesta		= (Spinner) findViewById(R.id.TecnicaCmbRespuesta);
		_cmbSegundoConcepto	= (Spinner) findViewById(R.id.TecnicaCmbSegundoConcepto);
		_cmbMedidor			= (Spinner)	findViewById(R.id.TecnicaCmbDatosMedidor);
		_cmbDiametro		= (Spinner) findViewById(R.id.TecnicaCmbDiametro);
		
		_btnGuardarTecnica		= (Button) findViewById(R.id.TecnicaBtnGuardar); 
		_btnGuardarDatosMedidor	= (Button) findViewById(R.id.TecnicaBtnGuardarDatosMedidor);
		_btnObservacion			= (Button) findViewById(R.id.TecnicaBtnObservacion);
		
		
		/*Referencia a objetos Visita Tecnica*/
		_lblCantidad		= (TextView) findViewById(R.id.VisitaLblCantidad);
		_lblItem1			= (TextView) findViewById(R.id.VisitaLblItem1);
		_lblItem2			= (TextView) findViewById(R.id.VisitaLblItem2);
		_lblItem3			= (TextView) findViewById(R.id.VisitaLblItem3);
		_lblItem4			= (TextView) findViewById(R.id.VisitaLblItem4);
		_lblItem5			= (TextView) findViewById(R.id.VisitaLblItem5);
		_lblItem6			= (TextView) findViewById(R.id.VisitaLblItem6);
		_txtCantidad		= (EditText) findViewById(R.id.VisitaTxtCantidad);
		_cmbClase 			= (Spinner) findViewById(R.id.VisitaCmbClase);
		_cmbSubClase		= (Spinner) findViewById(R.id.VisitaCmbSubclase);
		_cmbItem1			= (Spinner) findViewById(R.id.VisitaCmbItem1);
		_cmbItem2			= (Spinner) findViewById(R.id.VisitaCmbItem2);
		_cmbItem3			= (Spinner) findViewById(R.id.VisitaCmbItem3);
		_cmbItem4			= (Spinner) findViewById(R.id.VisitaCmbItem4);
		_cmbItem5			= (Spinner) findViewById(R.id.VisitaCmbItem5);
		_cmbItem6			= (Spinner) findViewById(R.id.VisitaCmbItem6);
		_btnGuardarVisita	= (Button) findViewById(R.id.VisitaBtnGuardar);
		
		this.FcnDataS   = new ClassDataSpinner(this,this.FolderAplicacion);
		
		this.strEstrato = new ArrayList<String>();
		this.strEstrato.clear();
		this.strEstrato = this.FcnDataS.getDataSpinner("Estrato");
		
		this.strEstado = new ArrayList<String>();
		this.strEstado.clear();
		this.strEstado = this.FcnDataS.getDataSpinner("Estado");
		
		/*Asociacion de adaptadores y objetos Informacion General*/
		AdaptadorEstrato 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strEstrato);
		this.AdaptadorEstrato.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_cmbEstrato.setAdapter(AdaptadorEstrato);
		
		AdaptadorEstado= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strEstado);
		this.AdaptadorEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
		
		
		/*Asociacion de adaptadores y objetos Informacion Tecnica*/
		AdaptadorClaseAcueducto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strClaseAcueducto);
		_cmbClaseAcueducto.setAdapter(AdaptadorClaseAcueducto);
		
		AdaptadorEscapeMedidor= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strEscapeMedidor);
		_cmbEscapeMedidor.setAdapter(AdaptadorEscapeMedidor);
		
		AdaptadorServicioDirecto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strServicioDirecto);
		_cmbServicioDirecto.setAdapter(AdaptadorServicioDirecto);
		
		AdaptadorBypass= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strBypass);
		_cmbBypass.setAdapter(AdaptadorBypass);
		
		AdaptadorCamaraMedidor= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strCamaraMedidor);
		_cmbCamaraMedidor.setAdapter(AdaptadorCamaraMedidor);
		
		AdaptadorEstadoCamaraMedidor= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strEstadoCamaraMedidor);
		_cmbEstadoCamara.setAdapter(AdaptadorEstadoCamaraMedidor);
		
		AdaptadorRespuestaDesviacion= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strRespuestaDesviacion);
		_cmbRespuesta.setAdapter(AdaptadorRespuestaDesviacion);
		
		AdaptadorSegundoConcepto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strSegundoConcepto);
		_cmbSegundoConcepto.setAdapter(AdaptadorSegundoConcepto);
		
		AdaptadorMedidor= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strMedidor);
		_cmbMedidor.setAdapter(AdaptadorMedidor);
		
		AdaptadorDiametro= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strDiametro);
		_cmbDiametro.setAdapter(AdaptadorDiametro);
		
		
		/*Asociacion de adaptadores y objetos Informacion Visita Tecnica*/
		this.strClase = this.FcnDesviacion.getClaseItems();
		AdaptadorClase = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strClase);
		_cmbClase.setAdapter(AdaptadorClase);
		
		this.strSubClase = this.FcnDesviacion.getSubClaseItems(_cmbClase.getSelectedItem().toString());
		AdaptadorSubclase = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strSubClase);
		_cmbSubClase.setAdapter(AdaptadorSubclase);		
		
					
		/*Listener Informacion General*/
		_cmbUso.setOnItemSelectedListener(this);
		_cmbTipo.setOnItemSelectedListener(this);
		_btnGuardarGeneral.setOnClickListener(this);
		
		/*Listener Informacion Tecnica*/
		_cmbClaseAcueducto.setOnItemSelectedListener(this);
		_cmbMedidor.setOnItemSelectedListener(this);
		_btnGuardarTecnica.setOnClickListener(this);
		_btnGuardarDatosMedidor.setOnClickListener(this);
		_btnObservacion.setOnClickListener(this);
		
		/*Listener Informacion Visita Tecnica*/
		_cmbClase.setOnItemSelectedListener(this);
		_cmbSubClase.setOnItemSelectedListener(this);
		_btnGuardarVisita.setOnClickListener(this);
		this.CargarInfGuardada();
	}

	

	/*****************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_desviacion, menu);
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
			case R.id.menu_ver_informacion:
				this.ModalInformacionSolicitud.putExtra("Revision", this.Revision);
				startActivityForResult(this.ModalInformacionSolicitud, INFORMACION_GENERAL);
				return true;
    		
			case R.id.menu_ver_solicitudes:
				finish();
				this.FormSolicitudes.putExtra("FolderAplicacion",this.FolderAplicacion);
				startActivity(this.FormSolicitudes );
				return true;	
				
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
					this.ModalConfirmacion.putExtra("informacion", "¿ Desea terminar la desviacion. ?");
					startActivityForResult(this.ModalConfirmacion, CONFIRMACION_TERMINACION);
				}else{
					Toast.makeText(getApplicationContext(),"No se puede terminar la desviacion datos incompletos.", Toast.LENGTH_LONG).show();	
				}
				return true;	
				
			case R.id.DesviacionSwapNotificacion:
				this.ModalConfirmacion.putExtra("informacion", "Confirma cancelar la desviacion. ?");
				startActivityForResult(this.ModalConfirmacion, CONFIRMACION_CAMBIO_ACTA);
				return true;
	
			case R.id.DesviacionTomarFoto:
				if(!this.FcnArchivos.ExistFolderOrFile(this.Revision, true)){
					this.FcnArchivos.MakeDirectory(this.Revision, true);
				}
				File imagesFolder = new File(FormLoggin.CARPETA_RAIZ, this.Revision);
				File image = new File(imagesFolder, this.Revision +"_"+this.FcnArchivos.numArchivosInFolder(this.Revision, true)+".jpeg"); 
				Uri uriSavedImage = Uri.fromFile(image);
				this.IniciarCamara.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
				startActivityForResult(IniciarCamara, INICIAR_CAMARA);
				return true;	
				
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		
		private boolean ValidarImpresionDesviacion(){
			boolean ValorRetorno = false;
			if(this.FcnDesviacion.getTipo(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el tipo de predio, recuerde guardar los cambios.", Toast.LENGTH_LONG).show();	
			}else if(this.FcnDesviacion.getNombreUsuario(this.Revision).isEmpty() && this.FcnDesviacion.getNombreTestigo(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"Debe ingresar un nombre de usuario y/o testigo.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getCedulaUsuario(this.Revision).isEmpty() && this.FcnDesviacion.getCedulaTestigo(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"Debe ingresar el numero de cedula del usuario y/o testigo.", Toast.LENGTH_LONG).show();	
			}else if(this.FcnDesviacion.getArea(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha digitado el area.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getPisos(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha digitado la cantidad de pisos del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getUso(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el uso del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getActividad(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha digitado la actividad del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getPersonas(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha digitado el numero de personas residentes.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getEstado(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado del predio.", Toast.LENGTH_LONG).show();		
			}else if(!this.FcnDesviacion.getEstado(this.Revision).equals("Predio Solo") && this.FcnDesviacion.getHabitado(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado quien habita el predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getAcueducto(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado quien administra el acueducto.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getCamaraMedidor(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor tiene camara.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getEscapeCamara(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si la camara presenta escape.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getEstadoCamaraMedidor(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado de la camara.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getServicioDirecto(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si existe servicio directo.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getBypass(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si existe bypass.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getSerieMedidor(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha digitado la serie del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getMarcaMedidor(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha digitado la marca del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getLecturaMedidor(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha digitado la lectura del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getDiametroMedidor(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el diametro del medidor individual.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getSegundoConcepto(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el usuario solicita segundo concepto.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getRespuestaDesviacion(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado la respuesta a la desviacion.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getObservacion(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha ingresado la observacion.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getRegistroPaso(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado del registro de paso.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getRegistroAntifraude(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado el estado del registro antifraude.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getMedidorDestruido(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor esta destruido.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getMedidorInvertido(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor esta invertido", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getMedidorIlegible(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha seleccionado si el medidor es ilegible.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getMedidorPrecintoRoto(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha si el estado del precinto del medidor.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getHermeticidadRegInternos(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Cierre registros internos.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getHermeticidadEquipoMedida(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Verificacion equipo de medida.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getHermeticidadFugas(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Verificacion de fugas.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getHermeticidadFugaImperceptible(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha ingresado la prueba de hermeticidad. Fuga imperceptible.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnDesviacion.getHermeticidadFugaVisible(this.Revision).isEmpty()){
				Toast.makeText(getApplicationContext(),"No ha ingresado el estado del precinto del medidor. Fuga visible.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getSubterraneos(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado los subterraneos del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getLavaplatos(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado los lavaplatos del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getLavaderos(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado los lavaderos del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getElevados(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado los tanques elevados del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getInternas(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado las instalaciones internas del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getPiscinas(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado las piscinas del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getCisterna(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado las cisternas del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getDucha(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado las duchas del predio.", Toast.LENGTH_LONG).show();		
			}else if(this.FcnRevision.getLavamanos(this.Revision)<0){
				Toast.makeText(getApplicationContext(),"No ha registrado los lavamanos del predio.", Toast.LENGTH_LONG).show();		
			}else{
				ValorRetorno = true;
			}			
			return ValorRetorno;
		}
		
		
		private void CargarInfGuardada(){
			this._tempRegistro = this.FcnDesviacion.getDatosDesviacion(this.Revision);
			
			String[] tempString;
			tempString = this._tempRegistro.getAsString("tipo").split("@");			
			if(tempString.length==2){
				_txtCual.setText(tempString[1]);
			}
			_txtNombreUsuario.setText(this._tempRegistro.getAsString("nombreusuario"));
			_txtNombreTestigo.setText(this._tempRegistro.getAsString("nombretestigo"));
			_txtCedulaUsuario.setText(this._tempRegistro.getAsString("cedulausuario"));
			_txtCedulaTestigo.setText(this._tempRegistro.getAsString("cedulatestigo"));
			_cmbTipo.setSelection(AdaptadorTipo.getPosition(tempString[0]));
			_cmbEstrato.setSelection(AdaptadorEstrato.getPosition(this._tempRegistro.getAsString("estrato")));
			_txtArea.setText(this._tempRegistro.getAsString("area"));
			_txtPisos.setText(this._tempRegistro.getAsString("pisos"));
			_cmbUso.setSelection(AdaptadorUso.getPosition(this._tempRegistro.getAsString("uso")));
			_txtActividad.setText(this._tempRegistro.getAsString("actividad"));
			_txtPersonas.setText(this._tempRegistro.getAsString("residentes"));
			_cmbEstado.setSelection(AdaptadorEstado.getPosition(this._tempRegistro.getAsString("estado")));
			_cmbHabitado.setSelection(AdaptadorHabitado.getPosition(this._tempRegistro.getAsString("habitado")));
			
			tempString = this._tempRegistro.getAsString("acueducto").split("@");
			if(tempString.length>0){
				_cmbClaseAcueducto.setSelection(AdaptadorClaseAcueducto.getPosition(tempString[0]));
				if(tempString.length==2){
					_lblOtro.setText(tempString[1]);
				}
			}
			
			_cmbCamaraMedidor.setSelection(AdaptadorCamaraMedidor.getPosition(this._tempRegistro.getAsString("camaramedidor")));
			_cmbEscapeMedidor.setSelection(AdaptadorEscapeMedidor.getPosition(this._tempRegistro.getAsString("escapecamara")));
			_cmbEstadoCamara.setSelection(AdaptadorEstadoCamaraMedidor.getPosition(this._tempRegistro.getAsString("estadocamara")));
			_cmbServicioDirecto.setSelection(AdaptadorServicioDirecto.getPosition(this._tempRegistro.getAsString("serviciodirecto")));
			_cmbBypass.setSelection(AdaptadorBypass.getPosition(this._tempRegistro.getAsString("bypass")));
			
			_cmbSegundoConcepto.setSelection(AdaptadorSegundoConcepto.getPosition(this._tempRegistro.getAsString("segundoconcepto")));
			_cmbRespuesta.setSelection(AdaptadorRespuestaDesviacion.getPosition(this._tempRegistro.getAsString("respuestadesviacion")));
			_txtObservacion.setText(this._tempRegistro.getAsString("diagnostico"));
			_txtPrecinto.setText(this._tempRegistro.getAsString("precinto"));
		}
		
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
					Toast.makeText(this, "Datos informacion general guardados correctamente.", Toast.LENGTH_SHORT).show(); 
					break;
					
				case R.id.TecnicaBtnObservacion:					
	            	_txtObservacion.setText(this.FcnDesviacion.generarObservacion(this.Revision));
					break;
					
				case R.id.TecnicaBtnGuardarDatosMedidor:
					this.FcnDesviacion.setDatosMedidor(this.Revision, _cmbMedidor.getSelectedItem().toString(), _txtNumero.getText().toString(), _txtMarca.getText().toString(), _txtLectura.getText().toString(), _cmbDiametro.getSelectedItem().toString());
					break;
					
				case R.id.TecnicaBtnGuardar:
					this.FcnDesviacion.setAcueducto(this.Revision, this._cmbClaseAcueducto.getSelectedItem().toString()+"@"+this._lblOtro.getText().toString());
					this.FcnDesviacion.setEscapeCamaraMedidor(this.Revision, this._cmbEscapeMedidor.getSelectedItem().toString());
					this.FcnDesviacion.setServicioDirecto(this.Revision, this._cmbServicioDirecto.getSelectedItem().toString());
					this.FcnDesviacion.setBypass(this.Revision, this._cmbBypass.getSelectedItem().toString());
					this.FcnDesviacion.setCamaraMedidor(this.Revision, this._cmbCamaraMedidor.getSelectedItem().toString());
					this.FcnDesviacion.setEstadoCamaraMedidor(this.Revision, this._cmbEstadoCamara.getSelectedItem().toString());
					this.FcnDesviacion.setSegundoConcepto(this.Revision, this._cmbSegundoConcepto.getSelectedItem().toString());
					this.FcnDesviacion.setRespuestaDesviacion(this.Revision, this._cmbRespuesta.getSelectedItem().toString());
					this.FcnDesviacion.setDiagnostico(this.Revision, this._txtObservacion.getText().toString());
					Toast.makeText(this, "Datos informacion tecnica guardados correctamente.", Toast.LENGTH_SHORT).show(); 
					break;
					
				case R.id.VisitaBtnGuardar:
					if(this._cmbClase.getSelectedItem().toString().equals("Instalaciones") && this._cmbSubClase.getSelectedItem().toString().equals("Instalaciones")){
						this.FcnDesviacion.registrarInstalaciones(this.Revision, this._cmbItem1.getSelectedItem().toString(), this._cmbItem2.getSelectedItem().toString(),this._cmbItem3.getSelectedItem().toString(),this._cmbItem4.getSelectedItem().toString(),this._cmbItem5.getSelectedItem().toString());
					}else if(this._cmbClase.getSelectedItem().toString().equals("Medidor") && this._cmbSubClase.getSelectedItem().toString().equals("Medidor")){
						this.FcnDesviacion.registrarDatosMedidor(this.Revision, this._cmbItem1.getSelectedItem().toString(), this._cmbItem2.getSelectedItem().toString(),this._cmbItem3.getSelectedItem().toString(),this._cmbItem4.getSelectedItem().toString(),this._cmbItem5.getSelectedItem().toString(),this._cmbItem6.getSelectedItem().toString());
					}else if(this._cmbClase.getSelectedItem().toString().equals("Elementos") && !this._cmbSubClase.getSelectedItem().toString().isEmpty()){
						this.FcnDesviacion.registrarElementos(this.Revision, this._cmbSubClase.getSelectedItem().toString(), this._txtCantidad.getText().toString(),this._lblItem1.getText().toString(),this._lblItem2.getText().toString(),this._lblItem3.getText().toString(),this._cmbItem1.getSelectedItem().toString(), this._cmbItem2.getSelectedItem().toString(),this._cmbItem3.getSelectedItem().toString());
					}else if(this._cmbClase.getSelectedItem().toString().equals("Estanqueidad") && !this._cmbSubClase.getSelectedItem().toString().isEmpty()){
						this.FcnDesviacion.registrarPruebasEstanqueidad(this.Revision, this._cmbSubClase.getSelectedItem().toString(), this._cmbItem1.getSelectedItem().toString(), this._cmbItem2.getSelectedItem().toString(),this._cmbItem3.getSelectedItem().toString(),this._cmbItem4.getSelectedItem().toString(),this._cmbItem5.getSelectedItem().toString());
					}
					break;
			}
		}
		
		
		public void ActualizarOpcionesItems(){
			AdaptadorItem1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strItem1);
			AdaptadorItem2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strItem2);
			AdaptadorItem3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strItem3);
			AdaptadorItem4 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strItem4);
			AdaptadorItem5 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strItem5);
			AdaptadorItem6 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strItem6);
			
			_cmbItem1.setAdapter(AdaptadorItem1);	
			_cmbItem2.setAdapter(AdaptadorItem2);	
			_cmbItem3.setAdapter(AdaptadorItem3);	
			_cmbItem4.setAdapter(AdaptadorItem4);	
			_cmbItem5.setAdapter(AdaptadorItem5);	
			_cmbItem6.setAdapter(AdaptadorItem6);	
			
			AdaptadorItem1.notifyDataSetChanged();
			AdaptadorItem2.notifyDataSetChanged();
			AdaptadorItem3.notifyDataSetChanged();
			AdaptadorItem4.notifyDataSetChanged();
			AdaptadorItem5.notifyDataSetChanged();
			AdaptadorItem6.notifyDataSetChanged();
		}
		
		
		private void setDatosPruebasEstanqueidad(){
			this._tempRegistro = this.FcnDesviacion.getDatosPruebasEstanqueidad(this.Revision, this._cmbSubClase.getSelectedItem().toString());
			this._cmbItem1.setSelection(AdaptadorItem1.getPosition(this._tempRegistro.getAsString("Resultado")));
			this._cmbItem2.setSelection(AdaptadorItem2.getPosition(this._tempRegistro.getAsString("Capacidad")));
			this._cmbItem3.setSelection(AdaptadorItem3.getPosition(this._tempRegistro.getAsString("UnidadesCapacidad")));
			this._cmbItem4.setSelection(AdaptadorItem4.getPosition(this._tempRegistro.getAsString("Fuga")));
			this._cmbItem5.setSelection(AdaptadorItem5.getPosition(this._tempRegistro.getAsString("UnidadesFuga")));
		}
		
		private void setDatosInstalaciones(){
			this._tempRegistro = this.FcnDesviacion.getDatosInstalaciones(this.Revision);
			this._cmbItem1.setSelection(AdaptadorItem1.getPosition(this._tempRegistro.getAsString("RegistrosInternos")));
			this._cmbItem2.setSelection(AdaptadorItem2.getPosition(this._tempRegistro.getAsString("VerificacionEquipo")));
			this._cmbItem3.setSelection(AdaptadorItem3.getPosition(this._tempRegistro.getAsString("RevisionFugas")));
			this._cmbItem4.setSelection(AdaptadorItem4.getPosition(this._tempRegistro.getAsString("FugaImperceptible")));
			this._cmbItem5.setSelection(AdaptadorItem5.getPosition(this._tempRegistro.getAsString("FugaVisible")));
		}
		
		private void setDatosMedidor(){
			this._tempRegistro = this.FcnDesviacion.getDatosMedidor(this.Revision);
			this._cmbItem1.setSelection(AdaptadorItem1.getPosition(this._tempRegistro.getAsString("RegistroPaso")));
			this._cmbItem2.setSelection(AdaptadorItem2.getPosition(this._tempRegistro.getAsString("RegistroAntifraude")));
			this._cmbItem3.setSelection(AdaptadorItem3.getPosition(this._tempRegistro.getAsString("Destruido")));
			this._cmbItem4.setSelection(AdaptadorItem4.getPosition(this._tempRegistro.getAsString("Invertido")));
			this._cmbItem5.setSelection(AdaptadorItem5.getPosition(this._tempRegistro.getAsString("Ilegible")));
			this._cmbItem6.setSelection(AdaptadorItem6.getPosition(this._tempRegistro.getAsString("Precinto")));
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
				
				case R.id.TecnicaCmbAcueducto:
					if(this._cmbClaseAcueducto.getSelectedItem().toString().equals("Otro")&&this._lblOtro.getText().toString().isEmpty()){
						this.ModalInputSingle.putExtra("titulo","INGRESE ACUEDUCTO");
						this.ModalInputSingle.putExtra("lbl1", "OTRO:");
						this.ModalInputSingle.putExtra("txt1", "");
						startActivityForResult(this.ModalInputSingle, INGRESO_ACUEDUCTO_OTRO);
					}
					break;
				
				case R.id.TecnicaCmbDatosMedidor:
					this._tempRegistro = this.FcnDesviacion.getDatosMedidor(this.Revision, this._cmbMedidor.getSelectedItem().toString());
					_txtNumero.setText(this._tempRegistro.getAsString("Numero"));
					_txtMarca.setText(this._tempRegistro.getAsString("Marca"));
					_txtLectura.setText(this._tempRegistro.getAsString("Lectura"));
					_cmbDiametro.setSelection(this.AdaptadorDiametro.getPosition(this._tempRegistro.getAsString("Diametro")));
					break;
					
					
				case R.id.VisitaCmbClase:
					if(this._cmbClase.getSelectedItem().toString().equals("Elementos")){
						_lblCantidad.setVisibility(View.VISIBLE);
						_txtCantidad.setVisibility(View.VISIBLE);
					}else{
						_lblCantidad.setVisibility(View.INVISIBLE);
						_txtCantidad.setVisibility(View.INVISIBLE);
					}
					
					this.strSubClase = this.FcnDesviacion.getSubClaseItems(this._cmbClase.getSelectedItem().toString());
					AdaptadorSubclase = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strSubClase);
					_cmbSubClase.setAdapter(AdaptadorSubclase);	
					AdaptadorSubclase.notifyDataSetChanged();
					break;
					
				case R.id.VisitaCmbSubclase:
					if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Cisterna")){
						_lblItem1.setText("Flotador");
						_lblItem2.setText("Accesorios");
						_lblItem3.setText("Rebosamiento");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.VISIBLE);		strItem2 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem3.setVisibility(View.VISIBLE);		strItem3 = this.FcnDesviacion.getOpciones("SiNoNoAplica");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Ducha")){
						_lblItem1.setText("Llave Terminal");
						_lblItem2.setText("");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.INVISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Instalaciones Internas")){
						_lblItem1.setText("Tuberia");
						_lblItem2.setText("Accesorios");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.VISIBLE);		strItem2 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Lavadero")){
						_lblItem1.setText("Llave Terminal");
						_lblItem2.setText("");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.INVISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Lavamanos")){
						_lblItem1.setText("Llave Terminal");
						_lblItem2.setText("");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.INVISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Lavaplatos")){
						_lblItem1.setText("Llave Terminal");
						_lblItem2.setText("");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.INVISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Piscina")){
						_lblItem1.setText("Registro de Control");
						_lblItem2.setText("Directo Sin Medidor");
						_lblItem3.setText("Filtraciones");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.VISIBLE);		strItem2 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem3.setVisibility(View.VISIBLE);		strItem3 = this.FcnDesviacion.getOpciones("SiNoNoAplica");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Subterraneo")){
						_lblItem1.setText("Flotador");
						_lblItem2.setText("");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.INVISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Elementos")&&_cmbSubClase.getSelectedItem().toString().equals("Tanque Elevado")){
						_lblItem1.setText("Flotador");
						_lblItem2.setText("");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.INVISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}else if(_cmbClase.getSelectedItem().toString().equals("Estanqueidad")&&_cmbSubClase.getSelectedItem().toString().equals("Tanque Elevado")){
						_lblItem1.setText("Resultado");
						_lblItem2.setText("Capacidad");
						_lblItem3.setText("Unidades Capacidad");
						_lblItem4.setText("Fuga de");
						_lblItem5.setText("Unidades Fuga");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.VISIBLE);		strItem2 = this.FcnDesviacion.getOpciones("Capacidad");
						_cmbItem3.setVisibility(View.VISIBLE);		strItem3 = this.FcnDesviacion.getOpciones("UnidadesCapacidad");
						_cmbItem4.setVisibility(View.VISIBLE);		strItem4 = this.FcnDesviacion.getOpciones("Fuga");
						_cmbItem5.setVisibility(View.VISIBLE);		strItem5 = this.FcnDesviacion.getOpciones("UnidadesFuga");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
						setDatosPruebasEstanqueidad();
					}else if(_cmbClase.getSelectedItem().toString().equals("Estanqueidad")&&_cmbSubClase.getSelectedItem().toString().equals("Tanque Lavadero")){
						_lblItem1.setText("Resultado");
						_lblItem2.setText("Capacidad");
						_lblItem3.setText("Unidades Capacidad");
						_lblItem4.setText("Fuga de");
						_lblItem5.setText("Unidades Fuga");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.VISIBLE);		strItem2 = this.FcnDesviacion.getOpciones("Capacidad");
						_cmbItem3.setVisibility(View.VISIBLE);		strItem3 = this.FcnDesviacion.getOpciones("UnidadesCapacidad");
						_cmbItem4.setVisibility(View.VISIBLE);		strItem4 = this.FcnDesviacion.getOpciones("Fuga");
						_cmbItem5.setVisibility(View.VISIBLE);		strItem5 = this.FcnDesviacion.getOpciones("UnidadesFuga");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
						setDatosPruebasEstanqueidad();
					}else if(_cmbClase.getSelectedItem().toString().equals("Estanqueidad")&&_cmbSubClase.getSelectedItem().toString().equals("Tanque Subterraneo")){
						_lblItem1.setText("Resultado");
						_lblItem2.setText("Capacidad");
						_lblItem3.setText("Unidades Capacidad");
						_lblItem4.setText("Fuga de");
						_lblItem5.setText("Unidades Fuga");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoAplica");
						_cmbItem2.setVisibility(View.VISIBLE);		strItem2 = this.FcnDesviacion.getOpciones("Capacidad");
						_cmbItem3.setVisibility(View.VISIBLE);		strItem3 = this.FcnDesviacion.getOpciones("UnidadesCapacidad");
						_cmbItem4.setVisibility(View.VISIBLE);		strItem4 = this.FcnDesviacion.getOpciones("Fuga");
						_cmbItem5.setVisibility(View.VISIBLE);		strItem5 = this.FcnDesviacion.getOpciones("UnidadesFuga");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
						setDatosPruebasEstanqueidad();
					}else if(_cmbClase.getSelectedItem().toString().equals("Instalaciones")&&_cmbSubClase.getSelectedItem().toString().equals("Instalaciones")){
						_lblItem1.setText("Cierre reg. Internos");
						_lblItem2.setText("Verif. Equipo Medida");
						_lblItem3.setText("Revision de Fugas");
						_lblItem4.setText("Fuga Imperceptible");
						_lblItem5.setText("Fuga Visible");
						_lblItem6.setText("");
												
						_cmbItem1.setVisibility(View.VISIBLE);		strItem1 = this.FcnDesviacion.getOpciones("SiNo");
						_cmbItem2.setVisibility(View.VISIBLE);		strItem2 = this.FcnDesviacion.getOpciones("SiNo");
						_cmbItem3.setVisibility(View.VISIBLE);		strItem3 = this.FcnDesviacion.getOpciones("SiNo");
						_cmbItem4.setVisibility(View.VISIBLE);		strItem4 = this.FcnDesviacion.getOpciones("SiNo");
						_cmbItem5.setVisibility(View.VISIBLE);		strItem5 = this.FcnDesviacion.getOpciones("SiNo");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
						setDatosInstalaciones();
					}else if(_cmbClase.getSelectedItem().toString().equals("Medidor")&&_cmbSubClase.getSelectedItem().toString().equals("Medidor")){
						_lblItem1.setText("Registro de Paso");
						_lblItem2.setText("Registro Antifraude");
						_lblItem3.setText("Destruido");
						_lblItem4.setText("Invertido");
						_lblItem5.setText("Vidrio Ilegible");
						_lblItem6.setText("Precinto Seguridad Roto");
						
						_cmbItem1.setVisibility(View.VISIBLE);	strItem1 = this.FcnDesviacion.getOpciones("BuenoMaloNoTiene");
						_cmbItem2.setVisibility(View.VISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("BuenoMaloNoTiene");
						_cmbItem3.setVisibility(View.VISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("SiNoNoAplica");
						_cmbItem4.setVisibility(View.VISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("SiNo");
						_cmbItem5.setVisibility(View.VISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("SiNo");
						_cmbItem6.setVisibility(View.VISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("SiNoNoTiene");	
						ActualizarOpcionesItems();
						setDatosMedidor();
					}else{
						_lblItem1.setText("");
						_lblItem2.setText("");
						_lblItem3.setText("");
						_lblItem4.setText("");
						_lblItem5.setText("");
						_lblItem6.setText("");
						
						_cmbItem1.setVisibility(View.INVISIBLE);	strItem1 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem2.setVisibility(View.INVISIBLE);	strItem2 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem3.setVisibility(View.INVISIBLE);	strItem3 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem4.setVisibility(View.INVISIBLE);	strItem4 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem5.setVisibility(View.INVISIBLE);	strItem5 = this.FcnDesviacion.getOpciones("Vacio");
						_cmbItem6.setVisibility(View.INVISIBLE);	strItem6 = this.FcnDesviacion.getOpciones("Vacio");	
						ActualizarOpcionesItems();
					}
					break;
			}			
		}

		
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
		
		
		@Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode == RESULT_OK && requestCode == INGRESO_ACUEDUCTO_OTRO && data.getExtras().getBoolean("accion")){
				this._lblOtro.setText(data.getExtras().getString("txt1"));
			}else{
				this._lblOtro.setText("");
				this._cmbClaseAcueducto.setSelection(AdaptadorClaseAcueducto.getPosition(""));
			}
			
			if(resultCode == RESULT_OK && requestCode == CONFIRMACION_TERMINACION && data.getExtras().getBoolean("accion")){
				this.FcnRevision.setEstadoRevision(this.Revision, 3);
				this.FcnDesviacion.setFechaCierre(this.Revision);
				this.MenuEnabled = true;
			}
			
			if(resultCode == RESULT_OK && requestCode == CONFIRMACION_CAMBIO_ACTA && data.getExtras().getBoolean("accion")){
				this.FcnRevision.setEstadoRevision(this.Revision, 0);
				this.FcnDesviacion.eliminarDesviacion(this.Revision);
				finish();
				this.FormSolicitudes.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(FormSolicitudes);
			}
	    }
}
