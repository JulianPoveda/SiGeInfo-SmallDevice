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
	private static String		_folderSQL;
	private static String 		_nameBD;
	private static final int 	_versionBD = 13;																		//Version de la base de datos
	
	private BDHelper 		nHelper;
	private final Context	_ctxSQL;
	private SQLiteDatabase 	nBD;
	
	private boolean ValorRetorno;
	
	public SQLite(Context ctx, String RutaBD, String NombreBD){
		this._ctxSQL 		= ctx;
		SQLite._folderSQL 	= RutaBD;
		SQLite._nameBD		= NombreBD;
	}
	
	
	private static class BDHelper extends SQLiteOpenHelper{
		
		public BDHelper(Context context) {
			super(context, SQLite._folderSQL + File.separator + SQLite._nameBD, null, SQLite._versionBD);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//Tabla con los usuarios del sistema
			db.execSQL(	"CREATE TABLE db_usuarios (usuario TEXT NOT NULL PRIMARY KEY, clave TEXT NOT NULL, nivel INTEGER)");
			
			//Tabla con los parametros del sistema
			db.execSQL(	"CREATE TABLE db_parametros (item TEXT PRIMARY KEY, valor TEXT NOT NULL, nivel INTEGER NOT NULL)");
			
			db.execSQL(	"CREATE TABLE db_parametros_valores (item TEXT NOT NULL, valor TEXT NOT NULL, proceso TEXT NOT NUll, PRIMARY KEY(item,valor))");
			
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
														"subterraneos			INTEGER NOT NULL DEFAULT -1," +
														"itemsubterraneos		VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadosubterraneos		VARCHAR(100) NOT NULL DEFAULT ''," +
														"lavaplatos				INTEGER NOT NULL DEFAULT -1," +
														"itemlavaplatos			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadolavaplatos		VARCHAR(100) NOT NULL DEFAULT ''," +
														"lavaderos				INTEGER NOT NULL DEFAULT -1," + 
														"itemlavadero			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadolavadero 		VARCHAR(100) NOT NULL DEFAULT ''," +
														"elevados				INTEGER NOT NULL DEFAULT -1," +
														"itemelevado			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadoelevado			VARCHAR(100) NOT NULL DEFAULT ''," +
														"internas				INTEGER NOT NULL DEFAULT -1," + 
														"iteminternas			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadointernas 		VARCHAR(100) NOT NULL DEFAULT ''," +
														"piscinas				INTEGER NOT NULL DEFAULT -1," +
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
														"cisterna				INTEGER NOT NULL DEFAULT -1," +
														"itemcisterna			VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadocisterna			VARCHAR(100) NOT NULL DEFAULT ''," +
														"ducha					INTEGER NOT NULL DEFAULT -1," +
														"itemducha				VARCHAR(100) NOT NULL DEFAULT ''," +
														"estadoducha			VARCHAR(100) NOT NULL DEFAULT ''," +
														"lavamanos				INTEGER NOT NULL DEFAULT -1," +
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
			db.execSQL("INSERT INTO db_parametros (item,valor,nivel) VALUES ('version','2.7',0)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
	
	
	private SQLite abrir(){
		nHelper = new BDHelper(this._ctxSQL);
		nBD = nHelper.getWritableDatabase();
		return this;
	}

	
	private void cerrar() {
		nHelper.close();
	}

		
	/**Funcion para ejecutar una sentencia SQL recibida por parametro
	 * @param _sql	->Sentencia SQL a ejecutar
	 * @return		->true en caso de ejecutarse correctamente, false en otros casos
	 */
	public boolean EjecutarSQL(String _sql){
		boolean _retorno = false;
		abrir();
		try{
			nBD.execSQL(_sql);  
			_retorno = true;
		}catch(Exception e){	
		}	
		cerrar();
		return _retorno;
	}
	
	
	/**Funcion para realizar INSERT
	 * @param _tabla 		-> tabla a la cual se va a realizar el INSERT
	 * @param _informacion	-> informacion que se va a insertar 
	 * @return				-> true si se realizo el insert correctamente, false en otros casos
	 */	
	public boolean InsertRegistro(String _tabla, ContentValues _informacion){
		abrir();
		ValorRetorno = false;
		try{
			if(nBD.insert(_tabla,null, _informacion)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){			
		}			
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion para realizar UPDATE 
	 * @param _tabla		->tabla sobre la cual se va a realizar el UPDATE	
	 * @param _informacion	->informacion que se va a actualizar
	 * @param _condicion	->condcion que debe tener el registro para ejecutar el UPDATE
	 * @return				->true si se realizo el UPDATE correctamente, flase en otros casos
	 */
	public boolean UpdateRegistro(String Tabla, ContentValues Informacion, String Condicion){
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
	
	
	/**Funcion para realizar un insert en caso de no existir el registro o update en caso de existir
	 * @param _tabla		->tabla sobre la cual se va a operar
	 * @param _informacion	->informacion que se va a insertar o actualizar
	 * @param _condicion	->Condicion que debe cumplirse para realizar el update y/o insert
	 * @return				->String con el mensaje de retorno, ya puede ser insert/update realizado o no realizado.
	 */
	public String InsertOrUpdateRegistro(String _tabla, ContentValues _informacion, String _condicion){
		String _retorno = "Sin acciones";
		if(!this.ExistRegistros(_tabla, _condicion)){
			if(this.InsertRegistro(_tabla, _informacion)){
				_retorno = "Registro ingresado en "+_tabla;
			}else{	
				_retorno = "Error al ingresar el registro en "+_tabla;
			}	
		}else{
			if(this.UpdateRegistro(_tabla, _informacion, _condicion)){
					_retorno = "Registro actualizado en "+_tabla;
			}else{
				_retorno = "Error al actualizar el registro en "+_tabla;
			}
		}		
		return _retorno;		
	}
	
	
	/**Funcion para eliminar un registro de una tabla en particular
	 * @param _tabla		->tabla sobre la cual se va a trabajar
	 * @param _condicion	->condicion que debe cumplirse para ejecutar el delete respectivo	
	 * @return				->true si fue ejecutado el delete correctamente, false en caso contrario
	 */
	public boolean DeleteRegistro(String _tabla, String _condicion){
		ValorRetorno = false;
		abrir();
		try{
			if(nBD.delete(_tabla, _condicion,null)>0){
				ValorRetorno = true;
			}
		}catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion encargada de realizar una consulta y retornarla con un array list de content values, similar a un array de diccionarios
	 * @param _tabla		->tabla sobre la cual va a correr la consulta
	 * @param _campos		->campos que se van a consultar
	 * @param _condicion	->condicion para filtrar la consulta
	 * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
	 */
	public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion){
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion, null);
			String[] _columnas = c.getColumnNames();
					
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_registro.put(_columnas[i], c.getString(i));
				}
				_query.add(_registro);
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}	
		this.cerrar();		
		return _query;		
	}
	
	
	/**Funcion para realizar la consulta de un registro, retorna un contentvalues con la informacion consultada
	 * @param _tabla		->tabla sobre la cual se va a ejecutar la consulta
	 * @param _campos		->campos que se quieren consultar
	 * @param _condicion	->condicion para ejecutar la consulta
	 * @return				-->ContentValues con la informacion del registro producto de la consulta
	 */
	public ContentValues SelectDataRegistro(String _tabla, String _campos, String _condicion){
		ContentValues _query = new ContentValues();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" LIMIT 1", null);
			String[] _columnas = c.getColumnNames();
						
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				//ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_query.put(_columnas[i], c.getString(i));
				}
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}	
		this.cerrar();		
		return _query;		
	}
		
	
	/*//Relizar la consulta teniendo en cuenta varios JOIN a la izquierda
	public ArrayList<ContentValues> SelectNJoinLeftData(String _tabla, String _campos, String _join_left[], String _on_left[], String _condicion){
		String Query = "";
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Query = "SELECT DISTINCT "+ _campos + " FROM "+ _tabla;
			for(int i=0;i<_join_left.length;i++){
				Query += " LEFT JOIN " +_join_left[i] + " ON "+_on_left[i];
			}
			Query += " WHERE "+ _condicion;
			Cursor c = nBD.rawQuery(Query, null);
			String[] _columnas = c.getColumnNames();
					
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					if(c.getString(i) == null){
						_registro.put(_columnas[i], "");
					}else{
						_registro.put(_columnas[i], c.getString(i));
					}
				}
				_query.add(_registro);
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}	
		this.cerrar();		
		return _query;		
	}*/
	

	
	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un entero
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public int IntSelectShieldWhere(String _tabla, String _campo, String _condicion){
		int intRetorno = -1;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				intRetorno = c.getInt(0);
			}			
		}
		catch(Exception e){
			intRetorno = -1;
		}
		cerrar();
		return intRetorno;
	}
	
	
	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un double
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public double DoubleSelectShieldWhere(String _tabla, String _campo, String _condicion){
		double intRetorno = -1;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				intRetorno = c.getDouble(0);
			}			
		}
		catch(Exception e){
			intRetorno = -1;
		}
		cerrar();
		return intRetorno;
	}
	
	
	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un String
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public String StrSelectShieldWhere(String _tabla, String _campo, String _condicion){
		String strRetorno = null;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				strRetorno = c.getString(0);
			}			
		}
		catch(Exception e){
			e.toString();
			strRetorno = null;
		}
		cerrar();
		return strRetorno;
	}
	
	
	/**Funcion retorna la cantidad de registros de una tabla que cumplen la condicion recibida por parametro
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public int CountRegistrosWhere(String _tabla, String _condicion){
		int ValorRetorno = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) FROM "+ _tabla +" WHERE "+ _condicion, null);
			c.moveToFirst();
			ValorRetorno = c.getInt(0);		
		}
		catch(Exception e){
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion que retorna true o falso segun existan o no registros que cumplan la condicion recibida por parametro
	 * @param _tabla		->Tabla sobre la cual se va trabajar
	 * @param _condicion	->Condicion de filtro
	 * @return
	 */
	public boolean ExistRegistros(String _tabla, String _condicion){
		ValorRetorno = false;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) as cantidad FROM " + _tabla +" WHERE " + _condicion , null);
			c.moveToFirst();
			if(c.getDouble(0)>0)
				ValorRetorno = true;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion que retorna la cantidad de minutos transcurridos desde la fecha actual y la recibida por parametro
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->String con el resultado en minutos
	 */
	public String MinutesBetweenDateAndNow(String _oldDate){
		String _retorno = "";
		int _minutos = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','now')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_retorno = c.getString(0);
			_minutos = Integer.parseInt(_retorno)/60;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();		
		return String.valueOf(_minutos);		
	}
	
	
	/**Funcion que retorna la cantidad de minutos transcurridos entre las fechas recibidas por parametro
	 * @param _newDate	->fecha mas reciente contra la cual se quiere caldular la diferencia
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->Strig con el resultado en minutos
	 */
	public String MinutesBetweenDates(String _newDate, String _oldDate){
		String _retorno = "";
		int _minutos = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','"+_newDate+"')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_retorno = c.getString(0);
			_minutos = Integer.parseInt(_retorno)/60;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();		
		return String.valueOf(_minutos);		
	}
}
