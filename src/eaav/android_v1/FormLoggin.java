package eaav.android_v1;

import java.io.File;

import clases.ClassConfiguracion;
import clases.ClassRevision;
import clases.ClassUsuario;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FormLoggin extends Activity implements OnClickListener{
	public static String 	NOMBRE_DATABASE	= "BD_EAAV";
	public static String 	CARPETA_RAIZ 	= Environment.getExternalStorageDirectory()+File.separator+"EAAV";		//Ruta donde se encuentra la carpeta principal del programa
	
	private ConnectServer 		ProgramadoCS;
	private ClassRevision		FcnRevision;
	private ClassConfiguracion 	FcnConfiguracion; 
	private ClassUsuario		FcnUsuario;

	final Handler mHandler = new Handler();    
	private int		Nivel = -1;
	private boolean LogginUser  = false; 
	
	private EditText 	_txtUsuario, _txtClave;
	private TextView 	_lblEquipo, _lblVersion;
	private Button 		_btnLoggin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        
        this.FcnRevision	 	= new ClassRevision(this, FormLoggin.CARPETA_RAIZ);
        this.FcnConfiguracion 	= new ClassConfiguracion(this, FormLoggin.CARPETA_RAIZ);
        this.FcnUsuario 		= new ClassUsuario(this, FormLoggin.CARPETA_RAIZ);
        this.ProgramadoCS		= new ConnectServer(this, FormLoggin.CARPETA_RAIZ);
          
        this._lblEquipo 	= (TextView) findViewById(R.id.LogginLblEquipo);
        this._lblVersion 	= (TextView) findViewById(R.id.LogginLblVersion);
        this._txtUsuario	= (EditText) findViewById(R.id.LogginTxtUsuario);
        this._txtClave		= (EditText) findViewById(R.id.LogginTxtClave);
        this._btnLoggin		= (Button) findViewById(R.id.LogginBtnIngresar);
        
        this._lblEquipo.setText("Codigo: " +this.FcnConfiguracion.getEquipo());
        this._lblVersion.setText("Version: " +this.FcnConfiguracion.getVersion());
        
        this._btnLoggin.setOnClickListener(this);
    }
    
    
    //Se deshabilitan opciones justo antes de crear el menu del formulario
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if(LogginUser){
    		menu.findItem(R.id.CargarTrabajoProgramado).setEnabled(true);
        	menu.findItem(R.id.DescargarTrabajo).setEnabled(true);
        	menu.findItem(R.id.IniciarTrabajo).setEnabled(true);
        	menu.findItem(R.id.Parametros).setEnabled(true);
        }else{
    		menu.findItem(R.id.CargarTrabajoProgramado).setEnabled(false);
        	menu.findItem(R.id.DescargarTrabajo).setEnabled(false);
        	menu.findItem(R.id.IniciarTrabajo).setEnabled(false);
        	menu.findItem(R.id.Parametros).setEnabled(false);
        }    	
        return true;  
    }

    
    //Se crea el menu del formulario con las opciones correspondientes
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_loggin, menu);
        return true;
    }
    
    //Control de eventos al momento de seleccionar una opcion del menu del formulario
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent k;
		switch (item.getItemId()) {
		case R.id.Parametros:
			k = new Intent(this, FormParametros.class);
			k.putExtra("Nivel", Nivel);
			k.putExtra("FolderAplicacion", FormLoggin.CARPETA_RAIZ);
			startActivity(k);
			return true;
			
		case R.id.CargarTrabajoProgramado:
			this.ProgramadoCS.DownLoadTrabajoProgramado();
			return true;
		
		case R.id.DescargarTrabajoRealizado:
			if(this.FcnRevision.existeEjecutadasSinDescargar()){
				ConnectServer CS= new ConnectServer(this, FormLoggin.CARPETA_RAIZ);
				CS.UpLoadTrabajoRealizado();
			}else{
				Toast.makeText(getApplicationContext(),"No hay trabajo ejecutado pendiente por enviar.", Toast.LENGTH_SHORT).show();	
			}
			return true;
			
		case R.id.DescargarTrabajoSinRealizar:
			if(this.FcnRevision.existeRevisionesSinRealizar()){
				ConnectServer SinRealizarCS= new ConnectServer(this, FormLoggin.CARPETA_RAIZ);
				SinRealizarCS.UpLoadTrabajoSinRealizar();
			}else{
				Toast.makeText(getApplicationContext(),"No hay trabajo sin realizar por enviar.", Toast.LENGTH_SHORT).show();	
			}
			return true;	
	
		case R.id.IniciarTrabajo:
			k = new Intent(this, FormListaTrabajo.class);
			k.putExtra("FolderAplicacion", FormLoggin.CARPETA_RAIZ);
			startActivity(k);
			return true;
			
		case R.id.Salir:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
	    }
	}
    

	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.LogginBtnIngresar:
				if(this.FcnUsuario.existeUsuario(this._txtUsuario.getText().toString(), this._txtClave.getText().toString())){
					this._txtUsuario.setEnabled(false);
					this._txtClave.setEnabled(false);
					this._btnLoggin.setEnabled(false);
					this.Nivel = this.FcnUsuario.getNivelUsuario(this._txtUsuario.getText().toString());
					this.LogginUser = true;
					invalidateOptionsMenu();
				}else{
					Toast.makeText(getApplicationContext(),"Acceso Denegado, favor verifique el nombre de usuario y/o contraseña", Toast.LENGTH_SHORT).show();
				}
				break;
		}		
	}    
}
