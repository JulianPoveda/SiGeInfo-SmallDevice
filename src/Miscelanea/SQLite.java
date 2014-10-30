package Miscelanea;

import java.io.File;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLite {
	//private static File SDCardRoot = Environment.getExternalStorageDirectory();
	private static String N_BD;
	//private static final String N_BD 	= SDCardRoot + File.separator + "EAAV" + File.separator + "BdEAAV_Android";	//Ruta de la base de datos
	private static final int VERSION_BD = 11;																		//Version de la base de datos
	
	private BDHelper nHelper;
	private final Context nContexto;
	private SQLiteDatabase nBD;
	
	private boolean ValorRetorno;
	
	public SQLite(Context ctx, String RutaBD, String NombreBD){
		this.nContexto = ctx;
		SQLite.N_BD = RutaBD + File.separator + NombreBD;		
	}
	
	
	private static class BDHelper extends SQLiteOpenHelper{
		
		public BDHelper(Context context) {
			super(context, N_BD, null, VERSION_BD);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//Tabla con los usuarios del sistema
			db.execSQL(	"CREATE TABLE db_usuarios (usuario TEXT NOT NULL PRIMARY KEY, clave TEXT NOT NULL, nivel INTEGER)");
			
			//Tabla con los parametros del sistema
			db.execSQL(	"CREATE TABLE db_parametros (item TEXT PRIMARY KEY, valor TEXT NOT NULL, nivel INTEGER NOT NULL)");
			
			//Tabla con las solicitudes de trabajo, esta tabla contiene la informacion basica para iniciar el trabajo
			db.execSQL(	"CREATE TABLE db_solicitudes (	id_serial INTEGER UNIQUE ,  revision VARCHAR(8) NOT NULL," + 
														"codigo VARCHAR(6) NOT NULL, nombre VARCHAR(255) NOT NULL, " +
														"direccion VARCHAR(255) NOT NULL, ciclo INTEGER NOT NULL, " +
														"ruta INTEGER NOT NULL, uso VARCHAR(5), " +
														"marca VARCHAR(20), serie VARCHAR(50), " +
														"novedad1 INTEGER NOT NULL DEFAULT 0, lectura1 INTEGER NOT NULL DEFAULT 0, " +
														"novedad2 INTEGER NOT NULL DEFAULT 0, lectura2 INTEGER NOT NULL DEFAULT 0, "+
														"promedio INTEGER NOT NULL, visita INTEGER NOT NULL DEFAULT 0, " +
														"estado INTEGER  NOT NULL DEFAULT 0, jornada VARCHAR(2) NOT NULL," +
														"periodo_ini DATE, periodo_fin DATE," +
														"factura DOUBLE PRECISION, codigo_apertura VARCHAR(10))");
			
			//Tabla que contiene la informacion de las notificaciones realizadas
			db.execSQL("CREATE TABLE db_notificaciones (fecha_notificacion 		DATE, " + 
														"revision 				VARCHAR(10) NOT NULL," + 
														"codigo 				VARCHAR(10) NOT NULL," + 
														"nombre 				VARCHAR(100) NOT NULL," + 
														"direccion 				VARCHAR(100) NOT NULL," + 
														"serie 					VARCHAR(20)," + 
														"ciclo  				INTEGER NOT NULL," + 
														"promedio 				INTEGER NOT NULL DEFAULT 0," + 
														"visita 				INTEGER NOT NULL DEFAULT 0," + 
														"lectura 				INTEGER," + 
														"medidor 				VARCHAR(30)," + 
														"precinto 				VARCHAR(30)," + 
														"observacion 			VARCHAR(500)," + 
														"fecha_visita 			DATE NOT NULL," + 
														"motivo 				VARCHAR(50)," + 
														"id 					INTEGER PRIMARY KEY AUTOINCREMENT," + 
														"jornada_notificacion 	VARCHAR(5)," + 
														"hora_visita 			VARCHAR(15) NOT NULL)");
			
			//Tabla que contiene la informacion de las notificaciones realizadas
			db.execSQL("CREATE TABLE db_desviaciones   ( id 					INTEGER PRIMARY KEY AUTOINCREMENT," + 
														"revision 				VARCHAR(10) NOT NULL," + 
														"codigo 				VARCHAR(10) NOT NULL," + 
														"nombre 				VARCHAR(100) NOT NULL," + 
														"direccion 				VARCHAR(100) NOT NULL," + 
														"serie 					VARCHAR(20) NOT NULL," + 
														"ciclo  				INTEGER NOT NULL," + 
														"promedio 				INTEGER NOT NULL DEFAULT 0," + 
														"visita 				INTEGER NOT NULL DEFAULT 0," + 
														"fecha 					VARCHAR(10) NOT NULL," +
														"hora					VARCHAR(10) NOT NULL," +
														"tipo 					VARCHAR(50) NOT NULL DEFAULT ''," +
														"area 					INTEGER NOT NULL DEFAULT 0," +
														"pisos					INTEGER  NOT NULL DEFAULT 0," + 
														"actividad 				VARCHAR(50) NOT NULL DEFAULT ''," +
														"uso 					VARCHAR(50) NOT NULL DEFAULT ''," +
														"residentes				INTEGER  NOT NULL DEFAULT 0," +
														"habitado 				VARCHAR(50) NOT NULL DEFAULT ''," +
														"estado 				VARCHAR(50) NOT NULL DEFAULT ''," +
														"acueducto 				VARCHAR(50) NOT NULL DEFAULT ''," +
														"camaramedidor 			VARCHAR(50) NOT NULL DEFAULT ''," +
														"estadocamara 			VARCHAR(50) NOT NULL DEFAULT ''," +
														"escapecamara  			VARCHAR(50) NOT NULL DEFAULT ''," +
														"serieindividual    	VARCHAR(50) NOT NULL DEFAULT ''," +
														"marcaindividual 		VARCHAR(50) NOT NULL DEFAULT ''," +
														"diametroindividual 	VARCHAR(10) NOT NULL DEFAULT ''," +
														"lecturaindividual		VARCHAR(50) NOT NULL DEFAULT ''," +
														"serietotalizador		VARCHAR(50) NOT NULL DEFAULT ''," +
														"marcatotalizador 		VARCHAR(50) NOT NULL DEFAULT ''," +
														"diametrototalizador 	VARCHAR(50) NOT NULL DEFAULT ''," +
														"lecturatotalizador		VARCHAR(50) NOT NULL DEFAULT ''," +
														"subterraneos			INTEGER NOT NULL DEFAULT 0," +
														"itemsubterraneos		VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadosubterraneos		VARCHAR(100) NOT NULL DEFAULT ''," +
														"lavaplatos				INTEGER NOT NULL DEFAULT 0," +
														"itemlavaplatos			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadolavaplatos		VARCHAR(100) NOT NULL DEFAULT ''," +
														"lavaderos				INTEGER NOT NULL DEFAULT 0," + 
														"itemlavadero			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadolavadero 		VARCHAR(100) NOT NULL DEFAULT ''," +
														"elevados				INTEGER NOT NULL DEFAULT 0," +
														"itemelevado			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadoelevado			VARCHAR(100) NOT NULL DEFAULT ''," +
														"internas				INTEGER NOT NULL DEFAULT 0," + 
														"iteminternas			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadointernas 		VARCHAR(100) NOT NULL DEFAULT ''," +
														"piscinas				INTEGER NOT NULL DEFAULT 0," +
														"itempiscina			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadopiscina			VARCHAR(100) NOT NULL DEFAULT ''," +
														"medidorregpaso			VARCHAR(20) NOT NULL DEFAULT ''," +
														"medidorregantifraude 	VARCHAR(20) NOT NULL DEFAULT ''," +
														"medidordestruido		VARCHAR(20) NOT NULL DEFAULT ''," +
														"medidorinvertido		VARCHAR(20) NOT NULL DEFAULT ''," +
														"medidorilegible		VARCHAR(20) NOT NULL DEFAULT ''," +
														"medidorprecintoroto 	VARCHAR(20) NOT NULL DEFAULT ''," +
														"hermeticidadreginternos VARCHAR(20) NOT NULL DEFAULT ''," +
														"hermeticidadequipomedida 	VARCHAR(20) NOT NULL DEFAULT ''," +
														"estanqueidadreselevado   	VARCHAR(100) NOT NULL DEFAULT 'No Se Realizo'," +
														"estanqueidadcapelevado   	VARCHAR(100) NOT NULL DEFAULT ''," +
														"estanqueidadfugaelevado  	VARCHAR(100) NOT NULL DEFAULT ''," +
														"estanqueidadreslavadero  	VARCHAR(100) NOT NULL DEFAULT 'No Se Realizo'," +
														"estanqueidadcaplavadero  	VARCHAR(100) NOT NULL DEFAULT ''," +
														"estanqueidadfugalavadero 	VARCHAR(100) NOT NULL DEFAULT ''," +
														"estanqueidadressubterraneo VARCHAR(100) NOT NULL DEFAULT 'No Se Realizo'," +
														"estanqueidadcapsubterraneo VARCHAR(100) NOT NULL DEFAULT ''," +
														"estanqueidadfugasubterrano VARCHAR(100) NOT NULL DEFAULT ''," +
														"hermeticidadfugaimperceptible VARCHAR(20) NOT NULL DEFAULT ''," +
														"hermeticidadfugas		VARCHAR(20) NOT NULL DEFAULT ''," +
														"hermeticidadfugavisible VARCHAR(20) NOT NULL DEFAULT ''," +
														"diagnostico 			VARCHAR(1000) NOT NULL DEFAULT ''," +
														"estrato 				VARCHAR(2) NOT NULL DEFAULT ''," +
														"precinto 				VARCHAR(50) NOT NULL DEFAULT ''," +
														"serviciodirecto 		VARCHAR(10) NOT NULL DEFAULT ''," +
														"bypass					VARCHAR(10) NOT NULL DEFAULT ''," +
														"horaapertura			VARCHAR(10) NOT NULL DEFAULT ''," +
														"nombreusuario			VARCHAR(100) NOT NULL DEFAULT ''," +
														"cedulausuario 			VARCHAR(20) NOT NULL DEFAULT ''," +
														"nombretestigo 			VARCHAR(100) NOT NULL DEFAULT ''," +
														"cedulatestigo			VARCHAR(20) NOT NULL DEFAULT ''," +
														"cisterna				INTEGER NOT NULL DEFAULT 0," +
														"itemcisterna			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadocisterna			VARCHAR(100) NOT NULL DEFAULT ''," +
														"ducha					INTEGER NOT NULL DEFAULT 0," +
														"itemducha				VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadoducha			VARCHAR(100) NOT NULL DEFAULT ''," +
														"lavamanos				INTEGER NOT NULL DEFAULT 0," +
														"itemlavamanos			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadolavamanos		VARCHAR(100) NOT NULL DEFAULT ''," +
														"servicioacueducto		VARCHAR(10) NOT NULL DEFAULT ''," +
														"servicioalcantarillado VARCHAR(10) NOT NULL DEFAULT ''," +
														"horacierre				VARCHAR(10) NOT NULL DEFAULT ''," +
														"segundoconcepto		VARCHAR(10) NOT NULL DEFAULT ''," +
														"respuestadesviacion 	VARCHAR(100) NOT NULL DEFAULT '')");
			
			
			//Creacion del usuario administrador
			db.execSQL("INSERT INTO db_usuarios (usuario,clave,nivel) VALUES ('86081756','sypelc',0) ");
			db.execSQL("INSERT INTO db_usuarios (usuario,clave,nivel) VALUES ('sypelc','12345',1) ");
			
			//Creacion de los parametros principales del sistema
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('pda','1000',0) ");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('servidor','190.93.133.127',0) ");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('puerto','8080',0) ");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('servicio','EAAV-Desviaciones/ServerPDA/ServerToPDA.php',0) ");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('nombre_tecnico','sin_asignar',1)");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('cedula_tecnico','00000000',1)");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('impresora','MZ320',1)");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('web_service','WS_EAAV_Desviaciones.php',0)");
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('version','2.5',0)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("UPDATE db_parametros SET valor = '2.5' WHERE item = 'version'");
		}
	}
	
	
	public SQLite (Context c){
		nContexto = c;
	}
	
	
	private SQLite abrir(){
		nHelper = new BDHelper(nContexto);
		nBD = nHelper.getWritableDatabase();
		return this;
	}

	
	private void cerrar() {
		nHelper.close();
	}

	
	public boolean InsertarRegistro(String Tabla, ContentValues Informacion){
		abrir();
		ValorRetorno = false;
		try{
			if(nBD.insert(Tabla,null,Informacion)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){			
		}			
		cerrar();
		return ValorRetorno;
	}
	
	
	public boolean ActualizarRegistro(String Tabla, ContentValues Informacion, String Condicion){
		ValorRetorno = false;
		abrir();
		try{
			if(nBD.update(Tabla, Informacion, Condicion, null)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	public boolean BorraRegistro(String Tabla, String Condicion){
		ValorRetorno = false;
		abrir();
		try{
			if(nBD.delete(Tabla, Condicion,null)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return ValorRetorno;
	}
	
		
	

	
	/*public boolean InsertarRegistro(String Tabla, String[] Campos, String[] Datos) {
		ContentValues cv = new ContentValues();
		abrir();
		for(int i=0;i<Campos.length;i++){
			cv.put(Campos[i],Datos[i]);
		}
		if(nBD.insert(Tabla,null,cv)<0)
			return false;
		else
			return true;
		cerrar();
	}
	
	*
	public boolean ActualizarRegistro(String Tabla, String Datos, String Condicion){
		ValorRetorno = false;
		abrir();
		try{
			Cursor cu = nBD.rawQuery("UPDATE "+Tabla+" SET "+Datos+" WHERE "+Condicion, null);
			cu.moveToFirst();
			ValorRetorno = true;
		}catch(Exception e){
			//ValorRetorno = false;
			//Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return ValorRetorno;
	}*/
	
	
	
	//Hay que modificar esta funcion en realidad no deberia ser un String sino un ContentValues
	//Consulta de datos con retorno de lista de array asociativo
	public ArrayList<ArrayList<String>> SelectDataKeyValue(String TablaConsulta, String CamposConsulta, String CondicionConsulta) {
		abrir();
		ArrayList<ArrayList<String>> Matriz = new ArrayList<ArrayList<String>>();
		
		try{
			Cursor c = nBD.rawQuery("SELECT "+CamposConsulta+" FROM "+TablaConsulta+" WHERE "+CondicionConsulta, null);
			String[] Columnas = c.getColumnNames();
					
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ArrayList<String> Registro = new ArrayList<String>();
				for(int i=0;i<Columnas.length;i++){
					Registro.add(c.getString(i));
				}
				Matriz.add(Registro);
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return Matriz;
	}
	
	
	
	//Selecciona un registro
	public void SelectData(ArrayList<String> TxtCampos, String TablaConsulta, String CamposConsulta, String CondicionConsulta) {
		abrir();
		try{
			TxtCampos.clear();
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+CamposConsulta+" FROM "+TablaConsulta+" WHERE "+CondicionConsulta, null);
			String[] Columnas = c.getColumnNames();
					
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				for(int i=0;i<Columnas.length;i++){
					TxtCampos.add(c.getString(i));
				}				
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}	
		cerrar();
	}
	
	
	

	
	public void SelectDistinctLimit(ArrayList<ArrayList<String>> Registros, String Tabla, String Campos, String Condicion, int Limite) {
		Registros.clear();
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT "+Campos+" FROM "+Tabla+" WHERE "+Condicion+" LIMIT "+ Limite, null);
			String[] Columnas = c.getColumnNames();
			
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ArrayList<String> Registro = new ArrayList<String>();
				for(int i=0;i<Columnas.length;i++){
					Registro.add(c.getString(i));
				}
				Registros.add(Registro);
				//Ordenes.add(c.getString(0));
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite SelectMultiple", e.toString());
		}	
		cerrar();
	}
	
	
	public String SelectShieldWhere(String Tabla, String Campo, String Condicion){
		String strRetorno = null;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + Campo + " FROM " + Tabla + " WHERE " + Condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				strRetorno = c.getString(0);
			}			
		}
		catch(Exception e){
			strRetorno = null;
			//Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return strRetorno;
	}
	
	
	public int SelectCountWhere(String Tabla, String Condicion){
		int ValorRetorno = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) FROM "+Tabla+" WHERE "+Condicion, null);
			c.moveToFirst();
			ValorRetorno = c.getInt(0);		
		}
		catch(Exception e){
			//Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/*public void SelectSingle(ArrayList<String> Ordenes,String Tabla, String Campos, String Condicion) {
		Ordenes.clear();
		try{
			Cursor c = nBD.rawQuery("SELECT "+Campos+" FROM "+Tabla+" WHERE "+Condicion+" LIMIT 1", null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				Ordenes.add(c.getString(0));
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite SelectMultiple", e.toString());
		}		
	}*/
	
	
	public boolean ExisteRegistros(String Tabla, String Condicion){
		ValorRetorno = false;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) as cantidad FROM " + Tabla +" WHERE " + Condicion , null);
			c.moveToFirst();
			if(c.getDouble(0)>0)
				ValorRetorno = true;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
			//return false;
		}
		cerrar();
		return ValorRetorno;
	}
}
