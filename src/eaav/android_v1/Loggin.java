package eaav.android_v1;

import java.io.File;
import Miscelanea.Archivos;
import Miscelanea.SQLite;
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

public class Loggin extends Activity {
	private static String 	CarpetaRaiz = Environment.getExternalStorageDirectory()+File.separator +"EAAV";		//Ruta donde se encuentra la carpeta principal del programa
	Archivos 	Arch 	= new Archivos(this, CarpetaRaiz);
	SQLite 		SQL;
	final Handler mHandler = new Handler();    
	String	Nivel = null;
	boolean LogginUser  = false; 
	TextView LblLogginCodigo,LblLogginVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
          
        LblLogginCodigo = (TextView) findViewById(R.id.LblLogginCodigo);
        LblLogginVersion = (TextView) findViewById(R.id.LblLogginVersion);
        
        
        SQL = new SQLite(getApplicationContext(), CarpetaRaiz, "BdEAAV_Android");
        LblLogginCodigo.setText("Codigo: " +SQL.SelectShieldWhere("db_parametros", "valor", "item='pda'"));
        LblLogginVersion.setText("Version: " +SQL.SelectShieldWhere("db_parametros", "valor", "item='version'"));
        
        Button Loggin = (Button) findViewById(R.id.BtnIngresar);
        Loggin.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
            ValidarUsuario((Menu) findViewById(R.menu.menu_loggin));
            }
        });        
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
			k = new Intent(this, Parametros.class);
			k.putExtra("Nivel", Nivel);
			startActivity(k);
			return true;
			
		case R.id.CargarTrabajoProgramado:
			ConnectServer ProgramadoCS= new ConnectServer(this, CarpetaRaiz);
			ProgramadoCS.DownLoadTrabajoProgramado();
			return true;
		
		case R.id.DescargarTrabajoRealizado:
			if((SQL.SelectCountWhere("db_notificaciones", "revision IS NOT NULL")>0)||(SQL.SelectCountWhere("db_desviaciones", "revision IS NOT NULL")>0)){
				ConnectServer CS= new ConnectServer(this, CarpetaRaiz);
				CS.UpLoadTrabajoRealizado();
			}else{
				Toast.makeText(getApplicationContext(),"No hay notificaciones por enviar.", Toast.LENGTH_SHORT).show();	
			}
			return true;
			
		case R.id.DescargarTrabajoSinRealizar:
			if(SQL.SelectCountWhere("db_solicitudes", "estado = 0")>0){
				ConnectServer SinRealizarCS= new ConnectServer(this, CarpetaRaiz);
				SinRealizarCS.UpLoadTrabajoSinRealizar();
			}else{
				Toast.makeText(getApplicationContext(),"No hay trabajo sin realizar por enviar.", Toast.LENGTH_SHORT).show();	
			}
			return true;	
	
		case R.id.IniciarTrabajo:
			k = new Intent(this, Lista_trabajo.class);
			startActivity(k);
			return true;
			
		case R.id.Salir:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
	    }
	}
    
    
    public void ValidarUsuario(Menu menu){
    	EditText InputUsuario = (EditText) findViewById(R.id.TxtUsuario);
		EditText InputClave = (EditText) findViewById(R.id.TxtClave);
		SQL = new SQLite(getApplicationContext());
		//SQL.abrir();
		
		if(SQL.ExisteRegistros("db_usuarios", "usuario='"+ InputUsuario.getText()+ "' AND clave='"+ InputClave.getText() +"'")){
			Nivel = SQL.SelectShieldWhere("db_usuarios", "nivel", "usuario = '"+ InputUsuario.getText() + "'");
			Toast.makeText(getApplicationContext(),"Acceso Concedido, Nivel " + Nivel, Toast.LENGTH_SHORT).show();
			InputUsuario.setEnabled(false);
			InputClave.setEnabled(false);
			LogginUser = true;
			invalidateOptionsMenu(); 
		}else{
			Toast.makeText(getApplicationContext(),"Acceso Denegado", Toast.LENGTH_SHORT).show();
		}
	}    
}
