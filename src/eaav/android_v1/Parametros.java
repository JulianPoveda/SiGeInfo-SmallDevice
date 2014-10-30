package eaav.android_v1;

import java.util.ArrayList;
import java.util.ListIterator;

import Miscelanea.ConverTypes;
import Miscelanea.ManagerBluetooth;
import Miscelanea.ManagerTablas;
import Miscelanea.SQLite;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Parametros extends Activity {
	ConverTypes Misc = new ConverTypes();
	ManagerBluetooth MB = new ManagerBluetooth(this);
	ManagerTablas MT = new ManagerTablas(this);
	SQLite SQL;
	
	ArrayList<ArrayList<String>> InfParametros;
	ArrayAdapter<String> AdapLstImpresoras;
	
	String Nivel = null;
	private Button BtnGuardar,BtnApertura;
	private String[] _listaImpresoras = Misc.ArrayListToArrayString(MB.GetDeviceBluetooth());
	private ContentValues Informacion = new ContentValues();
	
	Spinner _impresora;
	EditText _pda, _ip_servidor, _puerto, _servicio,_nombreTecnico, _cedulaTecnico, _web_service, _revision,_codigo_apertura;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parametros);
		
		Bundle bundle = getIntent().getExtras();
		Nivel	= bundle.getString("Nivel");
		
				
		//Apertura, consulta y cierre de los parametros del sistema
		SQL = new SQLite(getApplicationContext());
		InfParametros = SQL.SelectDataKeyValue("db_parametros", "item, valor", "nivel >=0");
		
		_pda 			= (EditText) findViewById(R.id.TxtParametrosPDA);
		_ip_servidor 	= (EditText) findViewById(R.id.TxtParametrosIpServidor);
		_puerto 		= (EditText) findViewById(R.id.TxtParametrosPuerto);
		_servicio 		= (EditText) findViewById(R.id.TxtParametrosServicio);
		_nombreTecnico 	= (EditText) findViewById(R.id.TxtParametrosNombreTecnico);
		_cedulaTecnico 	= (EditText) findViewById(R.id.TxtParametrosCedulaTecnico);
		_web_service	= (EditText) findViewById(R.id.TxtParametrosWS);		
		_impresora 		= (Spinner) findViewById(R.id.CmbParametrosImpresoras);
		_revision 		= (EditText) findViewById(R.id.TxtAperturaRevision);
		_codigo_apertura= (EditText) findViewById(R.id.TxtAperturaCodigo);
		
		
		
		AdapLstImpresoras 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_listaImpresoras);
		_impresora.setAdapter(AdapLstImpresoras);
				
		ListIterator<ArrayList<String>> iCiclo = InfParametros.listIterator();
		while(iCiclo.hasNext()){
			ArrayList<String> Registro = iCiclo.next();
			
			if(Registro.get(0).equals("pda")){
				_pda.setText(Registro.get(1));
			}else if(Registro.get(0).equals("servidor")){
				_ip_servidor.setText(Registro.get(1));
			}else if(Registro.get(0).equals("puerto")){
				_puerto.setText(Registro.get(1));
			}else if(Registro.get(0).equals("servicio")){
				_servicio.setText(Registro.get(1));
			}else if(Registro.get(0).equals("nombre_tecnico")){
				_nombreTecnico.setText(Registro.get(1));
			}else if(Registro.get(0).equals("cedula_tecnico")){
				_cedulaTecnico.setText(Registro.get(1));
			}else if(Registro.get(0).equals("impresora")){
				_impresora.setSelection(AdapLstImpresoras.getPosition(Registro.get(1)));
			}else if(Registro.get(0).equals("web_service")){
				_web_service.setText(Registro.get(1));
			}		
		}
		
		if (Nivel.equals("0")){
			_pda.setEnabled(true);
			_ip_servidor.setEnabled(true);
			_puerto.setEnabled(true);
			_servicio.setEnabled(true);
		}else{
			_pda.setEnabled(false);
			_ip_servidor.setEnabled(false);
			_puerto.setEnabled(false);
			_servicio.setEnabled(false);	
		}
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		BtnGuardar = (Button) findViewById(R.id.BtnParametrosGuardar);
		BtnGuardar.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	Informacion.clear();
            	Informacion.put("valor", _pda.getText().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='pda'");
            	
            	Informacion.clear();
            	Informacion.put("valor", _ip_servidor.getText().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='servidor'");
            	
            	Informacion.clear();
            	Informacion.put("valor", _puerto.getText().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='puerto'");
            	
            	Informacion.clear();
            	Informacion.put("valor", _servicio.getText().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='servicio'");
            	
            	Informacion.clear();
            	Informacion.put("valor", _nombreTecnico.getText().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='nombre_tecnico'");
            	
            	Informacion.clear();
            	Informacion.put("valor", _cedulaTecnico.getText().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='cedula_tecnico'");
            	
            	Informacion.clear();
            	Informacion.put("valor", _impresora.getSelectedItem().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='impresora'");
            	
            	Informacion.clear();
            	Informacion.put("valor", _web_service.getText().toString());
            	SQL.ActualizarRegistro("db_parametros", Informacion, "item='web_service'");
            	Toast.makeText(getApplicationContext(),"Parametros Actualizados.", Toast.LENGTH_SHORT).show();
            }
        });
		
		BtnApertura = (Button) findViewById(R.id.BtnAbrirRevision);
		BtnApertura.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if(SQL.ExisteRegistros("db_solicitudes","revision='"+_revision.getText()+"' AND codigo_apertura='"+_codigo_apertura.getText()+"' AND estado<>0")){
            		SQL.BorraRegistro("db_notificaciones", "revision='"+ _revision.getText()+"'");
            		SQL.BorraRegistro("db_desviaciones", "revision='"+ _revision.getText()+"'");
            		Informacion.clear();
            		Informacion.put("estado", 0);
            		SQL.ActualizarRegistro("db_solicitudes", Informacion, "revision='"+_revision.getText()+"'");
            		Toast.makeText(getApplicationContext(),"Revisiones abierta correctamente.", Toast.LENGTH_LONG).show();
            	}else{
            		Toast.makeText(getApplicationContext(),"Error al abrir la revision.", Toast.LENGTH_LONG).show();
            	}
            }
        });
		return true;
	}

}
