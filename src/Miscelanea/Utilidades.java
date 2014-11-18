package Miscelanea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilidades {
	DecimalFormat FormatoUnaDecima = new DecimalFormat("0.0");
	
	public String[] getCmbIncremento(int _inicio, int _final, int _incremento){
		String[] _strRetorno = new String[(int)((_final-_inicio)/_incremento)+1];
		for(int i = 0; i<=(int)((_final-_inicio)/_incremento); i++){
			_strRetorno[i] = String.valueOf(_inicio);
			_inicio += _incremento;
		}
		return _strRetorno;
	}
		
	
	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	public String generarAleatorio(int _limite){
		int numero = 0;
		Random r1 = new Random(System.currentTimeMillis());
		numero = r1.nextInt(999999999)+1;
		
		StringBuilder builder = new StringBuilder(String.valueOf(numero));
		while (builder.length() < _limite) {
            builder.append('0');
        }
		return builder.toString().substring(0, _limite);
	}
	
	
	public ArrayList<String> getRangeAdapter(double _minimo, double _maximo, double _incremento){
		ArrayList<String> _rango = new ArrayList<String>();
		for(double i = _minimo; i<= _maximo;i+=_incremento){
			_rango.add(FormatoUnaDecima.format(i));
		}	
		return _rango;
	}
	
	
	public void ArrayContentValuesToString(ArrayList<String> _strInformacion, ArrayList<ContentValues> _informacion, String _campo, boolean addEmpty){
		_strInformacion.clear();
		if(addEmpty){
			_strInformacion.add("");
		}
		ContentValues _registro = new ContentValues();
		for(int i=0; i<_informacion.size();i++){
			_registro.clear();
			_registro = _informacion.get(i);
			_strInformacion.add(_registro.getAsString(_campo).toString());
		}
	}
	
	
	public void ArrayContentValuesToString(ArrayList<String> _strInformacion, ArrayList<ContentValues> _informacion, String _campo, String _split, int _item){
		String _tempStr[];
		_strInformacion.clear();
		ContentValues _registro = new ContentValues();
		
		for(int i=0; i<_informacion.size();i++){
			_registro.clear();
			_registro = _informacion.get(i);
			_tempStr = _registro.getAsString(_campo).toString().split("\\"+_split);
			_strInformacion.add(_tempStr[_item]);
		}
	}
	
	
	public ArrayList<ContentValues> ArrContentFromJSON(String Cadena){
		ArrayList<ContentValues> Query = new ArrayList<ContentValues>();
		String Tabla 	= null;
		String Registro = null;
		String Campos[] = null;
		String KeyValue[] = null;
		
		int InicioCadena=0;
		int FinCadena=0;
		if(!Cadena.isEmpty()){
			InicioCadena = Cadena.indexOf("[");
			FinCadena = Cadena.indexOf("]");
			
			if((InicioCadena!=-1)&&(FinCadena!=-1)){
				Tabla = Cadena.substring(InicioCadena+1,FinCadena);
			}
			
			while(Tabla.length()>0){	
				ContentValues Diccionario = new ContentValues();
				InicioCadena= Tabla.indexOf("{");
				FinCadena 	= Tabla.indexOf("}");		
				
				Registro = Tabla.substring(InicioCadena+1, FinCadena).replaceAll("\"", "");
				Campos = Registro.split(",");
				KeyValue = null;
				Diccionario.clear();
				for(int i=0;i<Campos.length;i++){
					KeyValue= Campos[i].split(":");
					Diccionario.put(KeyValue[0],KeyValue[1]);	
				}
				Tabla = Tabla.substring(FinCadena+1);
				Query.add(Diccionario);
			}
		}
		return Query;
	}
	
	
	public int FindStringIntoArrayString(String _aguja, String _pajar, String _separador){
		int _retorno = -1;
		try{
			String[] _arrayString = _pajar.split("\\"+_separador);
			for(int i=0; i<_arrayString.length; i++){
				if(_arrayString[i].equals(_aguja)){
					_retorno = i;
					break;
				}
			}	
		}catch(Exception e){
			
		}
		return _retorno;
	}
	
	
	public String RemoveStringIntoArrayString(String _aguja, String _pajar, String _separador){
		String _retorno = "";
		try{
			String[] _arrayString = _pajar.split("\\"+_separador);
			for(int i=0; i<_arrayString.length; i++){
				if(!_arrayString[i].equals(_aguja)){
					_retorno = _retorno+" "+_arrayString[i];
				}
			}	
		}catch(Exception e){
			
		}
		return _retorno;
	}
	
	
	public  String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
