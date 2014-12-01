package eaav.android_v1;

import java.io.File;

import modal.ModalConfirmacion;
import modal.ModalDate;
import modal.ModalInfGeneral;
import modal.ModalInputSingle;

import clases.ClassNotificacion;
import clases.ClassRevision;

import Miscelanea.Archivos;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
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
import android.widget.Toast;


public class FormNotificacion extends Activity implements OnItemSelectedListener, OnClickListener{
	static int 				INFORMACION_GENERAL 	= 1;
	static int 				INGRESO_NOMBRE_USUARIO	= 2;
	static int 				DATE_PICKER				= 3;
	static int 				CONFIRMACION_CAMBIO_ACTA= 4;
	static int 				INICIAR_CAMARA			= 5;
	
	private Intent			ModalConfirmacion;
	private Intent			ModalDate;
	private Intent 			ModalInputSingle;
	private Intent 			ModalInformacionSolicitud;
	private Intent 			FormSolicitudes;
	private Intent 			IniciarCamara; 
		
	//Instancias
	private Impresiones 		Imp; 
	private	Archivos			FcnArchivos;
	private ClassNotificacion 	FcnNotificacion;
	private ClassRevision		FcnRevision;	
	
	//Adaptadores
	String[] MotivoNotificacion={"...","Casa Sola","Inmueble Desocupado","No Permitir Acceso","Solo Menores De Edad","Sin Agua En El Sector"};																
	String[] JornadaNotificacion={"am","pm"};																
	ArrayAdapter<String> AdapMotivoNotificacion;
	ArrayAdapter<String> AdapJornadaNotificacion;
	
	//Variables
	private String 		StrNombreUsuario= "";
	private String 		StrTipoUsuario	= "";
	private boolean 	MenuEnabled = false;
	private String 		Revision;
	private String 		FolderAplicacion;
	
	//Objetos
	private EditText 	_txtRevision, _txtLectura, _txtMedidor, _txtPrecinto, _txtObservacion, _txtFechaNotificacion;
	private Spinner  	_cmbJornadaNotificacion, _cmbMotivoNotificacion;
	private Button 	  	_btnFechaNotificacion, _btnGuardar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificacion);
		
		Bundle bundle = getIntent().getExtras();
		this.Revision			= bundle.getString("Solicitud");
		this.FolderAplicacion	= bundle.getString("FolderAplicacion");

		this.ModalConfirmacion			= new Intent(this, ModalConfirmacion.class);
		this.ModalDate					= new Intent(this, ModalDate.class);
		this.ModalInputSingle			= new Intent(this, ModalInputSingle.class);
		this.ModalInformacionSolicitud	= new Intent(this, ModalInfGeneral.class);		
		this.FormSolicitudes 			= new Intent(this, FormListaTrabajo.class);
		this.IniciarCamara				= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
				
		this.Imp 			= new Impresiones(this);
		this.FcnArchivos	= new Archivos(this, this.FolderAplicacion, 10);
		this.FcnNotificacion= new ClassNotificacion(this, this.FolderAplicacion);
		this.FcnRevision	= new ClassRevision(this, this.FolderAplicacion);
		
		if(!this.FcnNotificacion.existeRegistroNotificacion(this.Revision)){
			this.FcnNotificacion.registrarNotificacion(this.Revision);
		}
		
		this._txtRevision 			= (EditText) findViewById(R.id.NotificacionTxtRevision);
		this._txtLectura 			= (EditText) findViewById(R.id.NotificacionTxtLectura);
		this._txtMedidor 			= (EditText) findViewById(R.id.NotificacionTxtMedidor);
		this._txtPrecinto			= (EditText) findViewById(R.id.NotificacionTxtPrecinto);
		this._txtObservacion		= (EditText) findViewById(R.id.NotificacionTxtObservacion);
		this._txtFechaNotificacion	= (EditText) findViewById(R.id.NotificacionTxtFecha);	
		this._cmbMotivoNotificacion	= (Spinner) findViewById(R.id.NotificacionCmbMotivo); 
		this._cmbJornadaNotificacion= (Spinner) findViewById(R.id.NotificacionCmbJornada); 
		this._btnFechaNotificacion 	= (Button) findViewById(R.id.NotificacionBtnFecha);
		this._btnGuardar			= (Button) findViewById(R.id.NotificacionBtnGuardar);
		
		//Adaptadores
		AdapMotivoNotificacion 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MotivoNotificacion);
		_cmbMotivoNotificacion.setAdapter(AdapMotivoNotificacion);
		
		AdapJornadaNotificacion = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,JornadaNotificacion);
		_cmbJornadaNotificacion.setAdapter(AdapJornadaNotificacion);
		
		this._txtRevision.setText(this.Revision);		
		this._txtLectura.setText(this.FcnNotificacion.getLectura(this.Revision));
		this._txtMedidor.setText(this.FcnNotificacion.getMedidor(this.Revision));
		this._txtPrecinto.setText(this.FcnNotificacion.getPrecinto(this.Revision));
		this._txtObservacion.setText(this.FcnNotificacion.getObservacion(this.Revision));
		this._txtFechaNotificacion.setText(this.FcnNotificacion.getFechaNotificacion(this.Revision));
		this._cmbJornadaNotificacion.setSelection(AdapJornadaNotificacion.getPosition(this.FcnNotificacion.getJornadaNotificacion(this.Revision)));
		this._cmbMotivoNotificacion.setSelection(AdapMotivoNotificacion.getPosition(this.FcnNotificacion.getMotivo(this.Revision)));
		
		//Campos deshabilitados para edicion
		this._txtRevision.setEnabled(false);
		this._txtFechaNotificacion.setEnabled(false);
		
		this._btnFechaNotificacion.setOnClickListener(this);
		this._btnGuardar.setOnClickListener(this);
		this._cmbMotivoNotificacion.setOnItemSelectedListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_notificacion, menu);
		return true;
	}
	
	//Se deshabilitan opciones justo antes de crear el menu del formulario
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if(MenuEnabled){
    		menu.findItem(R.id.mnu_imprimir).setEnabled(false);
        	menu.findItem(R.id.mnu_terminar_notificacion).setEnabled(false);
    	}else{
    		menu.findItem(R.id.mnu_imprimir).setEnabled(true);
        	menu.findItem(R.id.mnu_terminar_notificacion).setEnabled(true);
    	}    	
        return true;  
    }
	
	
	//Control de eventos al seleccionar una opcion del menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnu_ver_informacion:
			this.ModalInformacionSolicitud.putExtra("Revision", this.Revision);
    		startActivityForResult(this.ModalInformacionSolicitud, INFORMACION_GENERAL);
			return true;			
		case R.id.ImpOriginal:
			if(verificarDatosNotificacion()){
				Imp.FormatoNotificacion("Original", this.Revision, this.StrNombreUsuario, this.StrTipoUsuario);
			}
			return true;			
		case R.id.ImpUsuario:
			if(verificarDatosNotificacion()){
				Imp.FormatoNotificacion("Usuario", this.Revision, this.StrNombreUsuario, this.StrTipoUsuario);
			}
			return true;			
		case R.id.ImpCopia:
			if(verificarDatosNotificacion()){
				Imp.FormatoNotificacion("Copia", this.Revision, this.StrNombreUsuario, this.StrTipoUsuario);
			}
			return true;			
		case R.id.mnu_terminar_notificacion:
			if(this.verificarDatosNotificacion()){
				this.guardarDatosNotificacion();
				this.FcnRevision.setEstadoRevision(this.Revision, 3);	
				this.MenuEnabled = true;
				Toast.makeText(getApplicationContext(),"Datos guardados correctamente.", Toast.LENGTH_SHORT).show();		
			}
			return true;		
		case R.id.mnu_notificacion_swap_desviacion:
			this.ModalConfirmacion.putExtra("informacion", "Confirma cancelar la notificacion. ?");
			startActivityForResult(this.ModalConfirmacion, CONFIRMACION_CAMBIO_ACTA);
			return true;
		
		case R.id.mnu_solicitudes:
			finish();
			this.FormSolicitudes.putExtra("FolderAplicacion",this.FolderAplicacion);
			startActivity(this.FormSolicitudes );
			return true;	
		
		case R.id.menu_tomar_foto:
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
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.NotificacionBtnGuardar:
				this.guardarDatosNotificacion();
				break;
				
			case R.id.NotificacionBtnFecha:
				startActivityForResult(this.ModalDate, DATE_PICKER);
				break;
		}		
	}
	
	
	private void guardarDatosNotificacion(){
		if(this._cmbMotivoNotificacion.getSelectedItem().toString().equals("...")){
			Toast.makeText(getApplicationContext(),"No ha seleccionado un motivo valido, los datos no se han guardado.", Toast.LENGTH_SHORT).show();
		}else{
			this.FcnNotificacion.setFechaNotificacion(this.Revision, this._txtFechaNotificacion.getText().toString());
			this.FcnNotificacion.setJornadaNotificacion(this.Revision, this._cmbJornadaNotificacion.getSelectedItem().toString());
			this.FcnNotificacion.setLectura(this.Revision, this._txtLectura.getText().toString());
			this.FcnNotificacion.setMedidor(this.Revision, this._txtMedidor.getText().toString());
			this.FcnNotificacion.setPrecinto(this.Revision, this._txtPrecinto.getText().toString());
			this.FcnNotificacion.setObservacion(this.Revision, this._txtObservacion.getText().toString());
			this.FcnNotificacion.setMotivo(this.Revision, this._cmbMotivoNotificacion.getSelectedItem().toString());
			Toast.makeText(getApplicationContext(),"Datos guardados correctamente.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean verificarDatosNotificacion(){
		boolean Retorno = false;
		if(this.FcnNotificacion.getLectura(this.Revision).isEmpty()){
			Toast.makeText(getApplicationContext(),"Falta registrar la lectura, recuerde guardar los datos.", Toast.LENGTH_SHORT).show();
		}else if(this.FcnNotificacion.getMedidor(this.Revision).isEmpty()){
			Toast.makeText(getApplicationContext(),"Falta registrar la medidor, recuerde guardar los datos.", Toast.LENGTH_SHORT).show();
		}else if(this.FcnNotificacion.getPrecinto(this.Revision).isEmpty()){
			Toast.makeText(getApplicationContext(),"Falta registrar la precinto, recuerde guardar los datos.", Toast.LENGTH_SHORT).show();
		}else if(this.FcnNotificacion.getObservacion(this.Revision).isEmpty()){
			Toast.makeText(getApplicationContext(),"Falta registrar la observacion, recuerde guardar los datos.", Toast.LENGTH_SHORT).show(); 		
		}else if(this.FcnNotificacion.getMotivo(this.Revision).isEmpty()){
			Toast.makeText(getApplicationContext(),"Falta registrar el motivo de notificacion, recuerde guardar los datos.", Toast.LENGTH_SHORT).show(); 			
		}else{
			Retorno = true;
		}
		return Retorno;		
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()){
			case R.id.NotificacionCmbMotivo:
				if(_cmbMotivoNotificacion.getSelectedItem().equals("Sin Agua En El Sector")){
					this.StrTipoUsuario = "Usuario";
					this.ModalInputSingle.putExtra("titulo","INGRESE NOMBRE USUARIO");
					this.ModalInputSingle.putExtra("lbl1", "Nombre:");
					this.ModalInputSingle.putExtra("txt1", "");
					startActivityForResult(this.ModalInputSingle, INGRESO_NOMBRE_USUARIO);
				}else if(!_cmbMotivoNotificacion.getSelectedItem().equals("Sin Agua En El Sector") && !_cmbMotivoNotificacion.getSelectedItem().equals("...")){
					this.StrTipoUsuario = "Testigo";
					this.ModalInputSingle.putExtra("titulo","INGRESE NOMBRE TESTIGO");
					this.ModalInputSingle.putExtra("lbl1", "Nombre:");
					this.ModalInputSingle.putExtra("txt1", "");	
					startActivityForResult(this.ModalInputSingle, INGRESO_NOMBRE_USUARIO);
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
		if(resultCode == RESULT_OK && requestCode == DATE_PICKER && data.getExtras().getBoolean("accion")){
			this._txtFechaNotificacion.setText(data.getExtras().getString("fecha"));
		}else if(resultCode == RESULT_OK && requestCode == INGRESO_NOMBRE_USUARIO && data.getExtras().getBoolean("accion")){
			this.StrNombreUsuario = data.getExtras().getString("txt1");	
			Toast.makeText(getApplicationContext(),"Nombre registrado correctamente.", Toast.LENGTH_SHORT).show();
		}else if(resultCode == RESULT_OK && requestCode == INGRESO_NOMBRE_USUARIO && !data.getExtras().getBoolean("accion")){
			_cmbMotivoNotificacion.setSelection(AdapMotivoNotificacion.getPosition("..."));
			Toast.makeText(getApplicationContext(),"No ha ingresado un nombre valido.", Toast.LENGTH_SHORT).show();
		}else if(resultCode == RESULT_OK && requestCode == CONFIRMACION_CAMBIO_ACTA && data.getExtras().getBoolean("accion")){
			this.FcnRevision.setEstadoRevision(this.Revision, 0);
			this.FcnNotificacion.eliminarNotificacion(this.Revision);
			finish();
			this.FormSolicitudes.putExtra("FolderAplicacion", this.FolderAplicacion);
			startActivity(FormSolicitudes);
		}
    }	
}
