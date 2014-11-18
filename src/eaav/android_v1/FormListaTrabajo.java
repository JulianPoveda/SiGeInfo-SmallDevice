package eaav.android_v1;

import java.util.ArrayList;

import modal.ModalInfGeneral;
import modal.ModalInformacion;
import modal.ModalInputSingle;

import clases.ClassRevision;
import personalizados.AdaptadorListaTrabajo;
import personalizados.InformacionSolicitudes;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;


public class FormListaTrabajo extends Activity {
	static int 		INFORMACION_GENERAL 		= 1;
	static int 		INGRESO_LECTURA_NOTIFICACION= 2;
	static int 		INGRESO_LECTURA_DESVIACION	= 3;
	static int 		CONFIRMACION_INFORMACION	= 4;
	public String _OrdenServicio = "";
	
	private Intent 			ModalInformacion; 
	private Intent 			ModalInformacionSolicitud;
	private Intent 			ModalInputSingle;
	private Intent			FormNotificacion;
	private Intent			FormDesviacion;
	
	
	private ClassRevision 	FcnRevision;
	
	private String FolderAplicacion;
	
	private ArrayList<ContentValues> 	_tempTabla 		= new ArrayList<ContentValues>();
	private ContentValues 				_tempRegistro 	= new ContentValues();
	
	private String RevisionSeleccionada;
		
	private AdaptadorListaTrabajo AdaptadorSolicitudes;
	private ArrayList<InformacionSolicitudes> ArraySolicitudes = new ArrayList<InformacionSolicitudes>();
	private ListView ListaSolicitudes;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_trabajo);
		
		Bundle bundle = getIntent().getExtras();
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.ModalInformacion			= new Intent(this, ModalInformacion.class);
		this.ModalInformacionSolicitud	= new Intent(this, ModalInfGeneral.class);
		this.ModalInputSingle			= new Intent(this, ModalInputSingle.class);		
		this.FormNotificacion			= new Intent(this, FormNotificacion.class);
		this.FormDesviacion				= new Intent(this, FormDesviacion.class);
		
		this.FcnRevision = new ClassRevision(this, this.FolderAplicacion);
		
		this.ListaSolicitudes = (ListView) findViewById(R.id.LstSolicitudes);				
		
		this.AdaptadorSolicitudes = new AdaptadorListaTrabajo(this,this.ArraySolicitudes);
		this.ListaSolicitudes.setAdapter(this.AdaptadorSolicitudes);
		
		this.ArraySolicitudes.clear();
		this._tempTabla = this.FcnRevision.getRevisiones();
		for(int i=0;i<this._tempTabla.size();i++){
			this._tempRegistro = this._tempTabla.get(i);
			this.ArraySolicitudes.add(new InformacionSolicitudes(this._tempRegistro.getAsString("revision"),this._tempRegistro.getAsString("marca"),this._tempRegistro.getAsString("direccion"),this._tempRegistro.getAsInteger("estado")));
		}
		this.AdaptadorSolicitudes.notifyDataSetChanged();		
		registerForContextMenu(this.ListaSolicitudes);
	}
	
	
	/**Funciones para el control del menu contextual del listview que muestra las ordenes de trabajo**/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		this.RevisionSeleccionada = ArraySolicitudes.get(info.position).getRevision();		
		switch(v.getId()){
			case R.id.LstSolicitudes:
				menu.setHeaderTitle("Revision " +ArraySolicitudes.get(info.position).getRevision());
			    super.onCreateContextMenu(menu, v, menuInfo);
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.menu_lista_trabajo, menu);
			    break;				
		}
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {			
	    	case R.id.mnu_informacion_general:
	    		this.ModalInformacionSolicitud.putExtra("Revision", this.RevisionSeleccionada);
	    		startActivityForResult(this.ModalInformacionSolicitud, INFORMACION_GENERAL);
	    		return true;
	    	
			case R.id.mnu_notificacion:
				if(this.FcnRevision.iniciarNotificacion(this.RevisionSeleccionada)){
					this.ModalInputSingle.putExtra("titulo","INGRESE LECTURA DEL PREDIO");
					this.ModalInputSingle.putExtra("lbl1", "Lectura:");
					this.ModalInputSingle.putExtra("txt1", "");
					startActivityForResult(this.ModalInputSingle, INGRESO_LECTURA_NOTIFICACION);
				}else{
					this.ModalInformacion.putExtra("informacion", "No se puede iniciar la actividad, comprobar:\n->Que no exista otra solicitud abierta\n->Que la solicitud seleccionada no este en estado 'TERMINADA'");
					startActivityForResult(this.ModalInformacion, CONFIRMACION_INFORMACION);
				}				
				return true;
			
			
			case R.id.mnu_desviacion:
				if(this.FcnRevision.iniciarDesviacion(this.RevisionSeleccionada)){
					this.ModalInputSingle.putExtra("titulo","INGRESE LECTURA DEL PREDIO");
					this.ModalInputSingle.putExtra("lbl1", "Lectura:");
					this.ModalInputSingle.putExtra("txt1", "");
					startActivityForResult(this.ModalInputSingle, INGRESO_LECTURA_DESVIACION);
				}else{
					this.ModalInformacion.putExtra("informacion", "No se puede iniciar la actividad, comprobar:\n->Que no exista otra solicitud abierta\n->Que la solicitud seleccionada no este en estado 'TERMINADA'");
					startActivityForResult(this.ModalInformacion, CONFIRMACION_INFORMACION);
				}	
				/*if(this.FcnInSolicitudes.getEstadoSolicitud(this.SolicitudSeleccionada).equals("E")){
					DialogConfirmacion.putExtra("informacion", "Desea Cerrar La Solicitud "+this.SolicitudSeleccionada);
					startActivityForResult(DialogConfirmacion, CONFIRMACION_CERRAR_ORDEN);	
				}else{
					DialogInformacion.putExtra("informacion", "No se puede dar por terminada la actividad ya que no esta en estado EJECUCION.");
					startActivityForResult(DialogInformacion, CONFIRMACION_INFORMACION);
				}*/
				return true;
							
			default:
	            return super.onContextItemSelected(item);        
	    }
	}
	

	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == INGRESO_LECTURA_NOTIFICACION && data.getExtras().getBoolean("accion")){
			this.FcnRevision.setEstadoRevision(this.RevisionSeleccionada, 1);
			finish();			
			this.FormNotificacion.putExtra("Solicitud", this.RevisionSeleccionada);
			this.FormNotificacion.putExtra("FolderAplicacion", this.FolderAplicacion);
			startActivity(this.FormNotificacion);
		}else if(resultCode == RESULT_OK && requestCode == INGRESO_LECTURA_DESVIACION && data.getExtras().getBoolean("accion")){
			this.FcnRevision.setEstadoRevision(this.RevisionSeleccionada, 2);
			finish();			
			this.FormDesviacion.putExtra("Solicitud", this.RevisionSeleccionada);
			this.FormDesviacion.putExtra("FolderAplicacion", this.FolderAplicacion);
			startActivity(this.FormDesviacion);
		}/*else if(resultCode == RESULT_OK && requestCode == CONFIRMACION_CERRAR_ORDEN && data.getExtras().getBoolean("accion")){
			_tempRegistro.clear();
			_tempRegistro.put("estado","T");
			this.FcnInSolicitudes.setEstadoSolicitud(this.SolicitudSeleccionada, "T");
			CargarOrdenesTrabajo();			
		}else if(resultCode == RESULT_OK && requestCode == CONFIRMACION_COD_APERTURA && data.getExtras().getBoolean("accion")){
			if(this.FcnInSolicitudes.VerificarCodigoApertura(this.SolicitudSeleccionada,data.getExtras().getString("txt1"))){
				this.FcnInSolicitudes.setEstadoSolicitud(this.SolicitudSeleccionada, "E");
				CargarOrdenesTrabajo();
			}else{
				DialogInformacion.putExtra("informacion", "Codigo De Apertura Erroneo");
				startActivityForResult(DialogInformacion, CONFIRMACION_INFORMACION);
			}
		}*/
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_lista_trabajo, menu);
		return true;
	}
}
