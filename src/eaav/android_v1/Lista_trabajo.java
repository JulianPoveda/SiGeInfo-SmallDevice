package eaav.android_v1;

import java.util.ArrayList;

import modal.ModalInfGeneral;
import modal.ModalInputSingle;

import clases.ClassRevision;
import personalizados.AdaptadorListaTrabajo;
import personalizados.InformacionSolicitudes;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;


public class Lista_trabajo extends Activity {
	static int 		INFORMACION_GENERAL = 1;
	static int 		INGRESO_LECTURA 	= 2;
	public String _OrdenServicio = "";
	
	private Intent 			ModalInformacionSolicitud;
	private Intent 			ModalInputSingle;
	
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
		
		
		this.ModalInformacionSolicitud	= new Intent(this, ModalInfGeneral.class);
		this.ModalInputSingle			= new Intent(this, ModalInputSingle.class);
		
		this.ListaSolicitudes = (ListView) findViewById(R.id.LstSolicitudes);
		
		Bundle bundle = getIntent().getExtras();
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.FcnRevision = new ClassRevision(this, this.FolderAplicacion);
		
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
				this.ModalInputSingle.putExtra("titulo","INGRESE LECTURA DEL PREDIO");
				this.ModalInputSingle.putExtra("lbl1", "Lectura:");
				this.ModalInputSingle.putExtra("txt1", "");
				startActivityForResult(this.ModalInputSingle, INGRESO_LECTURA);
				/*if(this.FcnInSolicitudes.IniciarSolicitud(this.SolicitudSeleccionada)){
					this.DialogConfirmacion.putExtra("informacion", "Desea Iniciar La Orden "+ this.SolicitudSeleccionada);
					startActivityForResult(DialogConfirmacion, CONFIRMACION_INICIO_ORDEN);
				}else{
					DialogInformacion.putExtra("informacion", "No se puede iniciar la actividad, comprobar:\n->Que no exista otra solicitud abierta\n->Que la solicitud seleccionada no este en estado 'TERMINADA'");
					startActivityForResult(DialogInformacion, CONFIRMACION_INFORMACION);
				}*/					
				return true;
			
			
			case R.id.mnu_desviacion:
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
	

	
	
	//Se deshabilitan opciones justo antes de crear el menu del formulario
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	/*if(_LecturaEncontrada.getText().length() != 0){
    		menu.findItem(R.id.mnu_desviacion).setEnabled(true);
        	menu.findItem(R.id.mnu_notificacion).setEnabled(true);
        }else{
    		menu.findItem(R.id.mnu_desviacion).setEnabled(false);
        	menu.findItem(R.id.mnu_notificacion).setEnabled(false);
        }*/    	
        return true;  
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_lista_trabajo, menu);
		return true;
	}

	
		//Control de eventos al momento de seleccionar una opcion del menu del formulario
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.mnu_notificacion:
				AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
		        dialogo1.setTitle("Confirmacion Inicio Notificacion.");  
		        dialogo1.setMessage("Desea iniciar la notificacion. ?");            
		        dialogo1.setCancelable(false);  
		        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialogo1, int id) {  
		            	finish();
		            	Intent k;
						k = new Intent(getApplicationContext(), Notificacion.class);
						k.putExtra("Solicitud", _OrdenServicio);
						startActivity(k);
		            }  
		        });  
		        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialogo1, int id) {  
		                //cancelar();
		            }  
		        });            
		        dialogo1.show(); 
				return true;
			
			case R.id.mnu_desviacion:
				AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);  
		        dialogo2.setTitle("Confirmacion Inicio Desviacion.");  
		        dialogo2.setMessage("�Desea iniciar la desviacion. ?");            
		        dialogo2.setCancelable(false);  
		        dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialogo1, int id) {  
		            	finish();
		            	Intent k;
						k = new Intent(getApplicationContext(), Desviacion.class);
						k.putExtra("Solicitud", _OrdenServicio);
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
	
	
	
	//Funcion encargada de detectar que item se selecciono del combo que contiene las OS pendientes, para posteriormente entrar a consultar los datos en
	//detalle y mostrarlos en los respectivos TextView
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent,View view, int position, long id){
			/*_OrdenServicio = OrdenesServicio.get(position);													//Se captura el valor de la orden seleccionada
			SQL = new SQLite(getApplicationContext());
			//SQL.abrir();
			InfBasica = new ArrayList<String>();
			SQL.SelectData(	InfBasica,"db_solicitudes",
							"revision,codigo,nombre,direccion,serie,marca,ciclo,ruta,promedio,visita,lectura1,novedad1,lectura2,novedad2",
							"revision='" + _OrdenServicio +"'");
			//SQL.cerrar();							
			_Revision.setText(InfBasica.get(0).toString());
			_Codigo.setText(InfBasica.get(1).toString());
			_Nombre.setText(InfBasica.get(2).toString());
			_Direccion.setText(InfBasica.get(3).toString());
			_Serie.setText(InfBasica.get(4).toString());
			_Marca.setText(InfBasica.get(5).toString());
			_Ciclo.setText(InfBasica.get(6).toString());
			_Ruta.setText(InfBasica.get(7).toString());
			_Promedio.setText(InfBasica.get(8).toString());
			_Visita.setText(InfBasica.get(9).toString());
			_Lectura1.setText(InfBasica.get(10).toString());
			_Novedad1.setText(InfBasica.get(11).toString());
			_Lectura2.setText(InfBasica.get(12).toString());
			_Novedad2.setText(InfBasica.get(13).toString());
			_LecturaEncontrada.setEnabled(true);*/
		}
	};
}
