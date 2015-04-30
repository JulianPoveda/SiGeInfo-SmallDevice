package eaav.android_v1;

import java.util.ArrayList;
import clases.ClassConfiguracion;
import modal.Modal_FileExplorer;
import Miscelanea.Bluetooth;
import Miscelanea.Zebra_QL420plus;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class FormParametros extends Activity implements OnClickListener{
	private static int 			ARCHIVO_LOCAL = 1;
	
	private ClassConfiguracion 	FcnConfiguracion;
	private Bluetooth 			MB;
	private Zebra_QL420plus		FcnZebra;

	private ArrayAdapter<String> 	AdapLstImpresoras;	
	private int 					Nivel;
	private String 					FolderAplicacion;
	private ArrayList<String> 		_listaImpresoras;		
	private Button 					_btnGuardar;
	private Spinner 				_cmbImpresora;
	private EditText 				_txtServidor, _txtPuerto, _txtServicio,  _txtWebService, _txtPDA, _txtNombreTecnico, _txtCedulaTecnico;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parametros);
		
		Bundle bundle = getIntent().getExtras();
		this.Nivel				= bundle.getInt("Nivel");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.MB 				= Bluetooth.getInstance();
		
		
		this.FcnZebra 			= new Zebra_QL420plus(570, 10, 3, 15, 100, false);		
		this.FcnConfiguracion 	= new ClassConfiguracion(this, this.FolderAplicacion);
		
		this._txtServidor 		= (EditText) findViewById(R.id.CfgTxtServidor);
		this._txtPuerto 		= (EditText) findViewById(R.id.CfgTxtPuerto);
		this._txtServicio 		= (EditText) findViewById(R.id.CfgTxtServicio);
		this._txtWebService		= (EditText) findViewById(R.id.CfgTxtWebService);		
		this._txtPDA 			= (EditText) findViewById(R.id.CfgTxtEquipo);
		this._txtNombreTecnico 	= (EditText) findViewById(R.id.CfgTxtNombreTecnico);
		this._txtCedulaTecnico 	= (EditText) findViewById(R.id.CfgTxtCedulaTecnico);
		this._cmbImpresora 		= (Spinner) findViewById(R.id.CfgCmbImpresora);
		this._btnGuardar 		= (Button) findViewById(R.id.CfgBtnGuardar);
		
		this._listaImpresoras 	= this.MB.GetDeviceBluetoothByAddress();
		this.AdapLstImpresoras 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_listaImpresoras);
		this._cmbImpresora.setAdapter(this.AdapLstImpresoras);
				
		this._txtServidor.setText(this.FcnConfiguracion.getServidor());
		this._txtPuerto.setText(this.FcnConfiguracion.getPuerto());
		this._txtServicio.setText(this.FcnConfiguracion.getServicio());
		this._txtWebService.setText(this.FcnConfiguracion.getWebService());
		this._txtPDA.setText(this.FcnConfiguracion.getEquipo());
		this._txtNombreTecnico.setText(this.FcnConfiguracion.getNombreTecnico());
		this._txtCedulaTecnico.setText(this.FcnConfiguracion.getCedulaTecnico());
		this._cmbImpresora.setSelection(this.AdapLstImpresoras.getPosition(this.FcnConfiguracion.getImpresora()));
		
		if (this.Nivel== 0){
			this._txtServidor.setEnabled(true);
			this._txtPuerto.setEnabled(true);
			this._txtServicio.setEnabled(true);
			this._txtWebService.setEnabled(true);
			this._txtPDA.setEnabled(true);
			this._txtNombreTecnico.setEnabled(true);
			this._txtCedulaTecnico.setEnabled(true);
		}else{
			this._txtServidor.setEnabled(false);
			this._txtPuerto.setEnabled(false);
			this._txtServicio.setEnabled(false);
			this._txtWebService.setEnabled(false);
			this._txtPDA.setEnabled(false);
			this._txtNombreTecnico.setEnabled(false);
			this._txtCedulaTecnico.setEnabled(false);
		}
		this._btnGuardar.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.CfgBtnGuardar:
				this.FcnConfiguracion.setServidor(this._txtServidor.getText().toString());
				this.FcnConfiguracion.setPuerto(this._txtPuerto.getText().toString());
				this.FcnConfiguracion.setServicio(this._txtServicio.getText().toString());
				this.FcnConfiguracion.setWebService(this._txtWebService.getText().toString());
				this.FcnConfiguracion.setEquipo(this._txtPDA.getText().toString());
				this.FcnConfiguracion.setNombreTecnico(this._txtNombreTecnico.getText().toString());
				this.FcnConfiguracion.setCedulaTecnico(this._txtCedulaTecnico.getText().toString());
				this.FcnConfiguracion.setImpresora(this._cmbImpresora.getSelectedItem().toString());
				Toast.makeText(getApplicationContext(),"Datos actualizados.", Toast.LENGTH_SHORT).show();
				break;
		}		
	}
	
	
	/*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ARCHIVO_LOCAL){
			if(!data.getExtras().getString("archivo_seleccionado").isEmpty()){
				this.FcnZebra.sendFile(	data.getExtras().getString("archivo_seleccionado"), 
										this.FcnConfiguracion.getImpresora());
			}
		}
    }*/

}
