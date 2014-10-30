package eaav.android_v1;

import java.util.ArrayList;
import Miscelanea.SQLite;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class Lista_trabajo extends Activity {
	SQLite SQL;
	public String _OrdenServicio = "";
		
	ArrayList<String> InfBasica;
	ArrayList<String> OrdenesServicio;																
	ArrayAdapter<String> AdaptadorOS;

	private EditText _LecturaEncontrada,_Revision,_Codigo,_Nombre,_Direccion,_Serie,_Marca,_Ciclo,_Ruta,_Promedio,_Visita,_Lectura1,_Novedad1,_Lectura2,_Novedad2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_trabajo);
		
		_LecturaEncontrada = (EditText) findViewById(R.id.TxtListaTrabajoLectura);
		_Revision 	= (EditText)  findViewById(R.id.TxtListaTrabajoRevision);
		_Codigo		= (EditText)  findViewById(R.id.TxtListaTrabajoCodigo);
		_Nombre 	= (EditText)  findViewById(R.id.TxtListaTrabajoNombre);
		_Direccion 	= (EditText)  findViewById(R.id.TxtListaTrabajoDireccion);
		_Serie		= (EditText)  findViewById(R.id.TxtListaTrabajoSerie);
		_Marca		= (EditText)  findViewById(R.id.TxtListaTrabajoMarca);
		_Ciclo 		= (EditText)  findViewById(R.id.TxtListaTrabajoCiclo);
		_Ruta 		= (EditText)  findViewById(R.id.TxtListaTrabajoRuta);
		_Promedio 	= (EditText)  findViewById(R.id.TxtListaTrabajoPromedio);
		_Visita		= (EditText)  findViewById(R.id.TxtListaTrabajoVisitaRealizada);
		_Lectura1 	= (EditText)  findViewById(R.id.TxtListaTrabajoLectura1);
		_Novedad1 	= (EditText)  findViewById(R.id.TxtListaTrabajoNovedad1);
		_Lectura2 	= (EditText)  findViewById(R.id.TxtListaTrabajoLectura2);
		_Novedad2 	= (EditText)  findViewById(R.id.TxtListaTrabajoNovedad2);
		
		_Revision.setEnabled(false);
		_Codigo.setEnabled(false);
		_Nombre.setEnabled(false);
		_Direccion.setEnabled(false);
		_Serie.setEnabled(false);
		_Marca.setEnabled(false);
		_Ciclo.setEnabled(false);
		_Ruta.setEnabled(false);
		_Promedio.setEnabled(false);
		_Visita.setEnabled(false);
		_Lectura1.setEnabled(false);
		_Novedad1.setEnabled(false);
		_Lectura2.setEnabled(false);
		_Novedad2.setEnabled(false);
		_LecturaEncontrada.setEnabled(false);
				
		//Adapter para listar las ordenes de trabajo
		OrdenesServicio = new ArrayList<String>();
		AdaptadorOS = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,OrdenesServicio);
		ListView ListaTrabajo = (ListView) findViewById(R.id.LstViewSolicitudes);
		ListaTrabajo.setAdapter(AdaptadorOS);
		ListaTrabajo.setOnItemClickListener(onListClick);
		
		
		SQL = new SQLite(getApplicationContext());
		if(SQL.ExisteRegistros("db_solicitudes", "estado = 1")){			//Se verifica si ya existe una solicitud con notificacion abierta
			_OrdenServicio = SQL.SelectShieldWhere("db_solicitudes", "revision", "estado = 1");
			finish();
			Intent k;
			k = new Intent(this, Notificacion.class);
			k.putExtra("Solicitud", _OrdenServicio);
			startActivity(k);			
		}else if(SQL.ExisteRegistros("db_solicitudes", "estado = 2")){		//Se verifica si ya existe una solicitud con desviacion abierta
			_OrdenServicio = SQL.SelectShieldWhere("db_solicitudes", "revision", "estado = 2");
			finish();
			Intent k;
			k = new Intent(this, Desviacion.class);
			k.putExtra("Solicitud", _OrdenServicio);
			startActivity(k);			
		}else{																//Consulta y muestra todas las ordenes con estado = 0 (sin realizar)		
			SQL.SelectData(OrdenesServicio,"db_solicitudes","revision","estado = 0");
			AdaptadorOS.notifyDataSetChanged();		
		}
	}
	
	
	//Se deshabilitan opciones justo antes de crear el menu del formulario
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if(_LecturaEncontrada.getText().length() != 0){
    		menu.findItem(R.id.mnu_desviacion).setEnabled(true);
        	menu.findItem(R.id.mnu_notificacion).setEnabled(true);
        }else{
    		menu.findItem(R.id.mnu_desviacion).setEnabled(false);
        	menu.findItem(R.id.mnu_notificacion).setEnabled(false);
        }    	
        return true;  
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_trabajo, menu);
		return true;
	}

	
		//Control de eventos al momento de seleccionar una opcion del menu del formulario
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.mnu_notificacion:
				AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
		        dialogo1.setTitle("Confirmacion Inicio Notificacion.");  
		        dialogo1.setMessage("�Desea iniciar la notificacion. ?");            
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
			_OrdenServicio = OrdenesServicio.get(position);													//Se captura el valor de la orden seleccionada
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
			_LecturaEncontrada.setEnabled(true);
		}
	};
}
