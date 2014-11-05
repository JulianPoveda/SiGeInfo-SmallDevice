package eaav.android_v1;

import java.util.ArrayList;
import java.util.Calendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import clases.ClassNotificacion;

import Miscelanea.DateTime;
import Miscelanea.SQLite;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class Notificacion extends Activity implements OnItemSelectedListener, OnClickListener{
	Intent NombreUsuario;
	public static int INGRESO_NOMBRE_USUARIO =1;
	
	private ClassNotificacion FcnNotificacion;
	
	private String StrNombreUsuario	="";
	private String StrTipoUsuario	="";

	//Instancias
	private Impresiones Imp = new Impresiones(this);
	
	//Adaptadores
	String[] MotivoNotificacion={"...","Casa Sola","Inmueble Desocupado","No Permitir Acceso","Solo Menores De Edad","Sin Agua En El Sector"};																
	String[] JornadaNotificacion={"am","pm"};																
	ArrayAdapter<String> AdapMotivoNotificacion;
	ArrayAdapter<String> AdapJornadaNotificacion;
	
	//Variables
	private String 		Revision;
	static final int 	DATE_DIALOG_ID = 0;
	private int 		pYear, pMonth, pDay;
	private 			String sYear, sMonth, sDay;
	private String 		Campos_Notificacion[]; 
	private String 		Valores_Notificacion[];
	private ArrayList<String>  RtaCamposNotificacion= new ArrayList<String>();
	private boolean MenuEnabled = false;
	private ContentValues Informacion = new ContentValues();
	
	//nusoap
	private static String URL	= "http://190.93.133.127:8080/EAAV-Desviaciones/ServerPDA/WS_EAAV_Desviaciones.php?wsdl";
	private static String NAMESPACE 	= "http://190.93.133.127:8080/EAAV-Desviaciones/ServerPDA";
	private static String METHOD_NAME	= "RegistrarNotificacion";
	private static String SOAP_ACTION 	= "RegistrarNotificacion";
	
	//Objetos
	private EditText _Revision, _Codigo, _Nombre, _Serie, _Ciclo, _Visita, _Uso, _Lectura, _Medidor, _Precinto, _Observacion, _FechaNotificacion;
	private Spinner  _NotificacionJornada, _NotificacionMotivo;
	private Button 	  _BtnFechaNotificacion;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificacion);

		NombreUsuario = new Intent(this, DialogoSimple.class);	
		
		Bundle bundle = getIntent().getExtras();
		Revision	= bundle.getString("Solicitud");
		
		this.FcnNotificacion = new ClassNotificacion(this, "");
		
		if(!this.FcnNotificacion.existeRegistroNotificacion(this.Revision)){
			this.FcnNotificacion.registrarNotificacion(this.Revision);
		}
		
		this._Revision 				= (EditText) findViewById(R.id.TxtNotificacionRevision);
		this._Codigo 				= (EditText) findViewById(R.id.TxtNotificacionCodigo);
		this._Nombre				= (EditText) findViewById(R.id.TxtNotificacionNombre);
		this._Serie 				= (EditText) findViewById(R.id.TxtNotificacionSerie);
		this._Ciclo 				= (EditText) findViewById(R.id.TxtNotificacionCiclo);
		this._Visita 				= (EditText) findViewById(R.id.TxtNotificacionVisita);
		this._Uso 					= (EditText) findViewById(R.id.TxtNotificacionUso);
		this._Lectura 				= (EditText) findViewById(R.id.TxtNotificacionLectura);
		this._Medidor 				= (EditText) findViewById(R.id.TxtNotificacionMedidor);
		this._Precinto				= (EditText) findViewById(R.id.TxtNotificacionPrecinto);
		this._Observacion			= (EditText) findViewById(R.id.TxtNotificacionObservacion);
		this._FechaNotificacion		= (EditText) findViewById(R.id.TxtNotificacionFecha);	
		this._NotificacionMotivo 	= (Spinner) findViewById(R.id.CmbNotificacionMotivo); 
		this._NotificacionJornada 	= (Spinner) findViewById(R.id.CmbNotificacionJornada); 
		this._BtnFechaNotificacion 	= (Button) findViewById(R.id.BtnNotificacionFecha);
		
		//Adaptadores
		AdapMotivoNotificacion 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MotivoNotificacion);
		_NotificacionMotivo.setAdapter(AdapMotivoNotificacion);
		
		AdapJornadaNotificacion 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,JornadaNotificacion);
		_NotificacionJornada.setAdapter(AdapJornadaNotificacion);
		
		//Listener para el boton de seleccionar fecha notificacion
		/*_BtnFechaNotificacion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);			// TODO Auto-generated method stub
			}
		});
		
		
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH)+1;
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        sYear 	= String.valueOf(pYear);
		sMonth 	= "00".substring(String.valueOf(pMonth).length()) + String.valueOf(pMonth);
		sDay 	= "00".substring(String.valueOf(pDay).length()) + String.valueOf(pDay);
		updateDisplay();*/
		
		//Consulta de informacion para mostrar en el formulario
		/*SQL = new SQLite(this);
		InfBasica = new ArrayList<String>();
		InfNotificacion = new ArrayList<String>();
		SQL.SelectData(	InfBasica,
						"db_solicitudes",
						"revision,codigo,nombre,serie,ciclo,visita,uso",
						"revision='" + Solicitud +"'");
		
		SQL.SelectData(	InfNotificacion,
						"db_notificaciones",
						"lectura,medidor,precinto,observacion,fecha_notificacion,jornada_notificacion,motivo",
						"revision='" + Solicitud +"'");
			
		
		_Revision.setText(InfBasica.get(0).toString());
		_Codigo.setText(InfBasica.get(1).toString());
		_Nombre.setText(InfBasica.get(2).toString());
		_Serie.setText(InfBasica.get(3).toString());
		_Ciclo.setText(InfBasica.get(4).toString());
		_Visita.setText(InfBasica.get(5).toString());
		_Uso.setText(InfBasica.get(6).toString());*/
		
		this._Lectura.setText(this.FcnNotificacion.getLectura(this.Revision));
		this._Medidor.setText(this.FcnNotificacion.getMedidor(this.Revision));
		this._Precinto.setText(this.FcnNotificacion.getPrecinto(this.Revision));
		this._Observacion.setText(this.FcnNotificacion.getObservacion(this.Revision));
		this._FechaNotificacion.setText(this.FcnNotificacion.getFechaNotificacion(this.Revision));
		this._NotificacionJornada.setSelection(AdapJornadaNotificacion.getPosition(this.FcnNotificacion.getJornadaNotificacion(this.Revision)));
		this._NotificacionMotivo.setSelection(AdapMotivoNotificacion.getPosition(this.FcnNotificacion.getMotivo(this.Revision)));
		
		//Campos deshabilitados para edicion
		this._Revision.setEnabled(false);
		this._Codigo.setEnabled(false);
		this._Nombre.setEnabled(false);
		this._Serie.setEnabled(false);
		this._Ciclo.setEnabled(false);
		this._Visita.setEnabled(false);
		this._Uso.setEnabled(false);
		this._FechaNotificacion.setEnabled(false);
		
		this._BtnFechaNotificacion.setOnClickListener(this);
		this._NotificacionMotivo.setOnItemSelectedListener(this);
	}
	
	//Se deshabilitan opciones justo antes de crear el menu del formulario
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if(MenuEnabled){
    		menu.findItem(R.id.mnu_guardar).setEnabled(false);
        	menu.findItem(R.id.mnu_imprimir).setEnabled(false);
        	menu.findItem(R.id.mnu_terminar_notificacion).setEnabled(false);
    	}else{
    		menu.findItem(R.id.mnu_guardar).setEnabled(true);
        	menu.findItem(R.id.mnu_imprimir).setEnabled(true);
        	menu.findItem(R.id.mnu_terminar_notificacion).setEnabled(true);
    	}    	
        return true;  
    }
	
	
	//Control de eventos al seleccionar una opcion del menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnu_guardar:
			if(_NotificacionMotivo.getSelectedItem().toString().equals("...")){
				Toast.makeText(getApplicationContext(),"No ha seleccionado un motivo valido, los datos no se han guardado.", Toast.LENGTH_SHORT).show();
			}else{
				Informacion.clear();
				Informacion.put("fecha_notificacion", _FechaNotificacion.getText().toString());
				Informacion.put("jornada_notificacion", _NotificacionJornada.getSelectedItem().toString());
				Informacion.put("lectura", _Lectura.getText().toString());
				Informacion.put("medidor", _Medidor.getText().toString());
				Informacion.put("precinto", _Precinto.getText().toString());
				Informacion.put("observacion", _Observacion.getText().toString());
				Informacion.put("motivo", _NotificacionMotivo.getSelectedItem().toString());
				//SQL.ActualizarRegistro(	"db_notificaciones", Informacion, "revision='" + Solicitud + "'");
				Toast.makeText(getApplicationContext(),"Datos guardados correctamente.", Toast.LENGTH_SHORT).show();
			}			
			return true;
			
		case R.id.ImpOriginal:
			if(VerificarDatosNotificacion()){
				//Imp.FormatoNotificacion("Original", Solicitud, this.StrNombreUsuario, this.StrTipoUsuario);
			}
			return true;
			
		case R.id.ImpUsuario:
			if(VerificarDatosNotificacion()){
				//Imp.FormatoNotificacion("Usuario", Solicitud, this.StrNombreUsuario, this.StrTipoUsuario);
			}
			return true;
			
		case R.id.ImpCopia:
			if(VerificarDatosNotificacion()){
				//Imp.FormatoNotificacion("Copia", Solicitud, this.StrNombreUsuario, this.StrTipoUsuario);
			}
			return true;
			
			
		case R.id.mnu_terminar_notificacion:
			if(VerificarDatosNotificacion()){
				Informacion.clear();
				Informacion.put("estado", 3);
				//SQL.ActualizarRegistro("db_solicitudes", Informacion, "revision = '" + Solicitud + "'");
				
				Informacion.clear();
				Informacion.put("fecha_notificacion", _FechaNotificacion.getText().toString());
				Informacion.put("jornada_notificacion", _NotificacionJornada.getSelectedItem().toString());
				Informacion.put("lectura", _Lectura.getText().toString());
				Informacion.put("medidor", _Medidor.getText().toString());
				Informacion.put("precinto", _Precinto.getText().toString());
				Informacion.put("observacion", _Observacion.getText().toString());
				Informacion.put("motivo", _NotificacionMotivo.getSelectedItem().toString());
				//SQL.ActualizarRegistro(	"db_notificaciones", Informacion, "revision='" + Solicitud + "'");
				
				Toast.makeText(getApplicationContext(),"Datos guardados correctamente.", Toast.LENGTH_SHORT).show();		
				new UpLoadNotificacion().execute();
				Intent k;
				k = new Intent(this, Lista_trabajo.class);
				startActivity(k);
				return true;		
			}
			return true;
		
		
		case R.id.mnu_notificacion_swap_desviacion:
			AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);  
	        dialogo2.setTitle("Cambio de Acta.");  
	        dialogo2.setMessage("Confirma cancelar la notificacion e iniciar como desviacion?.");            
	        dialogo2.setCancelable(false);  
	        dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialogo1, int id) {  
	            	//SQL.BorraRegistro("db_notificaciones", "revision ='"+Solicitud+"'");
	            	
	            	finish();
	            	Intent k;
					k = new Intent(getApplicationContext(), Desviacion.class);
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
			
			
		/*case R.id.mnu_solicitudes:
			Intent k;
			k = new Intent(this, Lista_trabajo.class);
			startActivity(k);
			return true;*/			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	private boolean VerificarDatosNotificacion(){
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
	
	
	/** Updates the date in the TextView */
    private void updateDisplay() { 
    	_FechaNotificacion.setText(new StringBuilder().append(sDay).append("-").append(sMonth).append("-").append(sYear));
    }
    
     
 	/** Callback received when the user "picks" a date in the dialog */
    private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener(){
    	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    		pYear = year;
    		pMonth = monthOfYear+1;
    		pDay = dayOfMonth;
    		updateDisplay();
    		sYear 	= String.valueOf(pYear);
    		sMonth 	= "00".substring(String.valueOf(pMonth).length()) + String.valueOf(pMonth);
    		sDay 	= "00".substring(String.valueOf(pDay).length()) + String.valueOf(pDay);
    	}
    };
	
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);
        }
        return null;
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notificacion, menu);
		return true;
	}
	
	
	//Clase privada para descargar el trabajo asignado
    private class UpLoadNotificacion extends AsyncTask<Void,Void,Void>{
    	String res = null;
    	private ArrayList<String> SendNotificacion;
    	String pda = null;
		 
    	protected void onPreExecute(){
    		res = "";
    		Toast.makeText(getApplicationContext(),"Terminando Notificacion, por favor espere...", Toast.LENGTH_SHORT).show();
    		
    		SendNotificacion = new ArrayList<String>();
    		/*SQL.SelectData(	SendNotificacion,
							"db_notificaciones",
							"fecha_notificacion,revision,codigo,nombre,direccion,serie,ciclo,promedio,visita,lectura,medidor,precinto,observacion,fecha_visita,motivo,jornada_notificacion,hora_visita",
							"revision='" + Solicitud +"'");
    		Toast.makeText(getApplicationContext(),"Revision: " + SendNotificacion.get(1).toString(), Toast.LENGTH_SHORT).show();
    		
			pda 				= SQL.SelectShieldWhere("db_parametros", "valor", "item='pda'");*/
			MenuEnabled = true;    		
    	}
    	
    	@Override
    	protected Void doInBackground(Void... params) {
    		SoapObject soapclient = new SoapObject(NAMESPACE, METHOD_NAME);
			soapclient.addProperty("fecha_notificacion",SendNotificacion.get(0).toString());
			soapclient.addProperty("revision", 			SendNotificacion.get(1).toString());
			soapclient.addProperty("codigo",			SendNotificacion.get(2).toString());
			soapclient.addProperty("nombre",			SendNotificacion.get(3).toString());
			soapclient.addProperty("direccion",			SendNotificacion.get(4).toString());
			soapclient.addProperty("serie",				SendNotificacion.get(5).toString());
			soapclient.addProperty("ciclo",				SendNotificacion.get(6).toString());
			soapclient.addProperty("promedio",			SendNotificacion.get(7).toString());
			soapclient.addProperty("visita",			SendNotificacion.get(8).toString());
			soapclient.addProperty("lectura",			SendNotificacion.get(9).toString());
			soapclient.addProperty("medidor",			SendNotificacion.get(10).toString());
			soapclient.addProperty("precinto",			SendNotificacion.get(11).toString());
			soapclient.addProperty("observacion",		SendNotificacion.get(12).toString());
			soapclient.addProperty("fecha_visita",		SendNotificacion.get(13).toString());
			soapclient.addProperty("motivo",			SendNotificacion.get(14).toString());
			soapclient.addProperty("jornada_notificacion",SendNotificacion.get(15).toString());
			soapclient.addProperty("hora_visita",		SendNotificacion.get(16).toString());
			soapclient.addProperty("pda",pda);
			
			
			//Cargar datos de la notificacion			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(soapclient);
			HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
			
    		try {
    			httpTransportSE.call(SOAP_ACTION, envelope);
    			res = envelope.getResponse().toString();
    			if(res.equals("Ok")){
    				//SQL.BorraRegistro("db_solicitudes", "revision ='" + Solicitud + "'");
    				//SQL.BorraRegistro("db_notificaciones", "revision ='" + Solicitud + "'");
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Void unused) {
    		Toast.makeText(getApplicationContext(),"Notificacion terminada correctamente.", Toast.LENGTH_SHORT).show();
    	}	
    }


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch(parent.getId()){
			case R.id.CmbNotificacionMotivo:
				if(_NotificacionMotivo.getSelectedItem().equals("Sin Agua En El Sector")){
					this.StrTipoUsuario = "Usuario";
					NombreUsuario.putExtra("titulo","NOMBRE USUARIO");
					NombreUsuario.putExtra("lbl1","Nombre:");
					NombreUsuario.putExtra("txt1",StrNombreUsuario);
					startActivityForResult(NombreUsuario, INGRESO_NOMBRE_USUARIO);
				}else if(!_NotificacionMotivo.getSelectedItem().equals("Sin Agua En El Sector") && !_NotificacionMotivo.getSelectedItem().equals("...")){
					this.StrTipoUsuario = "Testigo";
					NombreUsuario.putExtra("titulo","NOMBRE TESTIGO");
					NombreUsuario.putExtra("lbl1","Nombre:");
					NombreUsuario.putExtra("txt1",StrNombreUsuario);	
					startActivityForResult(NombreUsuario, INGRESO_NOMBRE_USUARIO);
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
		try{
			if (resultCode == RESULT_OK && requestCode == INGRESO_NOMBRE_USUARIO) {
				if(data.getExtras().getBoolean("response")){
					if(data.getExtras().getString("txt1").isEmpty()){
						_NotificacionMotivo.setSelection(AdapMotivoNotificacion.getPosition("..."));
						Toast.makeText(getApplicationContext(),"No ha ingresado un nombre valido.", Toast.LENGTH_SHORT).show();
					}else{
						this.StrNombreUsuario = data.getExtras().getString("txt1");
						Toast.makeText(getApplicationContext(),"Nombre guardado correctamente.", Toast.LENGTH_SHORT).show();
					}
				}else{
					_NotificacionMotivo.setSelection(AdapMotivoNotificacion.getPosition("..."));
					Toast.makeText(getApplicationContext(),"No ha ingresado un nombre valido.", Toast.LENGTH_SHORT).show();
					
				}
			}
		}catch(Exception e){
			//e.toString();
		}
		
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
