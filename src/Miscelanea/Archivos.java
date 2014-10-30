package Miscelanea;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;


public class Archivos {
	private Context CtxArchivos;
	//private FileInputStream fis;
	private FileReader file;
	
	private String DirectorioRaiz; //= Environment.getExternalStorageDirectory() + File.separator + "EAAV";	
	static 	String Campos_OS = "id_archivo,num_os,tip_os,tip_serv,co_prior_ord,nic,coment_os,nombre,direccion,tip_cli,departamento,municipio,localidad";
	static 	String Campos_Usuarios = "cedula,clave";
	static 	String Campos_Codigos  = "codigo,descripcion";
				
	
	public Archivos(Context ctx, String CarpetaRaiz){
		this.CtxArchivos = ctx;
		this.DirectorioRaiz = CarpetaRaiz;
		
		//Se crea la carpeta de la aplicacion que contendra todos archivos creados en el programa
		if(!ExistFolderOrFile(this.DirectorioRaiz)){
			MakeDirectory(this.DirectorioRaiz);
		}
	}
	
	
	//Metodo para crear una carpeta en el directorio raiz
	public boolean MakeDirectory(String Ruta){
		File f = new File(Ruta);
        if(f.mkdir()){
            return true;
        }else{
        	return false;
        }
	}
	
	//Metodo para comprobar la existencia de un directorio y/o carpeta
	public boolean ExistFolderOrFile(String Archivo){
		File f = new File(this.DirectorioRaiz+File.separator+Archivo);
		return f.exists();
	}
	
	
	//Metodo para eliminar un directorio y/o archivo
	public boolean DeleteFolderOrFile(String Archivo){
		File f = new File(this.DirectorioRaiz+File.separator+Archivo);
		return f.delete();
	}
	
	
	
	//Metodo que realiza la lectura de un archivo y la convierte en un array de bytes
	public byte[] FileToBytes(String Archivo){
		InputStream is 			= null;
		ByteArrayOutputStream os= new ByteArrayOutputStream(4096);
		byte[] buffer 			= new byte[4096];
		int len;
		try{
			is = new FileInputStream(this.DirectorioRaiz+File.separator+Archivo);
			if (this.DirectorioRaiz+File.separator+Archivo != null) {
				try {
					while ((len = is.read(buffer)) >= 0) {
						os.write(buffer, 0, len);
					}
				} finally {
					is.close();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			try {
				throw new IOException("Unable to open R.raw.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return os.toByteArray();
	}
	
	
		
		
	public boolean CrearArchivo(String NombreArchivo, String Informacion){
		boolean ValorRetorno = false;
		try {
			File file = new File(this.DirectorioRaiz + File.separator + NombreArchivo);
			file.createNewFile();
			if (file.exists()&&file.canWrite()){
				FileWriter filewriter = new FileWriter(file,false);
				filewriter.write(Informacion);
				filewriter.close();
				ValorRetorno = true;
			}
		}catch (IOException e2) {
			//e2.printStackTrace();
			ValorRetorno = false;
		}
		return ValorRetorno;
	}
	
	
	//Metodo para saber las lineas de un archivo 
	public int TamanoArchivo(String NombreArchivo){
		int CountBfrArchivo = 0;
		try {
			file = new FileReader(NombreArchivo);
			BufferedReader BfrArchivo = new BufferedReader(file);
			while(BfrArchivo.readLine()!=null){
				CountBfrArchivo++;
			}
		} catch (FileNotFoundException e) {
			CountBfrArchivo = 0;
		}  catch (IOException e) {
			CountBfrArchivo = 0;
		}
		return CountBfrArchivo;
	}
	
	
	//Metodo para convertir el contenido de un archivo a un array 
	public ArrayList<String> FileToArrayString(String Archivo){
		String queryString;
		String storageState = Environment.getExternalStorageState();
		ArrayList<String> InformacionFile = new ArrayList<String>();
		
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
		    File file = new File(this.DirectorioRaiz + File.separator + Archivo);
		    		 
		    BufferedReader inputReader2;
			try {
				inputReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				while ((queryString = inputReader2.readLine()) != null) {
			    	InformacionFile.add(queryString);
			    }
				file.delete();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return InformacionFile;
	}
	
	
	
	/*public boolean ExisteArchivo(String NombreArchivo){
		return true;
	}*/
	
	
	
	public boolean CrearArchivo(String NombreArchivo, String Encabezado, ArrayList<ArrayList<String>> Informacion){
		//Se recorre el ArrayList<ArrayList<String>> y se convierte a un String para generar el archivo
		ArrayList<String> Registro = new ArrayList<String>();
		String CadenaArchivo = Encabezado;
		
		for(int i=0; i<Informacion.size(); i++){
			Registro = Informacion.get(i);
			for(int j=0; j<Registro.size(); j++){
				CadenaArchivo += Registro.get(j)+";";				
			}
			CadenaArchivo += "\n";
		}
		
		try {
			File file = new File(this.DirectorioRaiz + File.separator + NombreArchivo);
    		file.createNewFile();
    		if (file.exists()&&file.canWrite()){
    			FileWriter filewriter = new FileWriter(file,false);
    			filewriter.write(CadenaArchivo);
    			filewriter.close();
    		}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return true;
	}
	
	
	public int LeerArchivoOS(String NombreArchivo){
		int Registros = 0;
		String queryString; 
	    
		//BaseDatos AccesoBD = new BaseDatos(ctx);
		SQLite Insert = new SQLite(CtxArchivos);
		//Insert.abrir();
				
		//StringBuffer stringBuffer2 = new StringBuffer();	    
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
		    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + NombreArchivo);
		    		 
		    BufferedReader inputReader2;
			try {
				String[] Datos; 
				String[] Campos = Campos_OS.split("\\,");
				
				inputReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				while ((queryString = inputReader2.readLine()) != null) {
					Datos = queryString.split("\\|");
			    	ContentValues Informacion = new ContentValues();
					
			    	for(int i=0;i<Campos.length;i++){
						Informacion.put(Campos[i],Datos[i]);
					}
					
			    	if(Insert.InsertarRegistro("db_os",Informacion)){
			    		Registros += 1;
			    	}
			    }
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Insert.cerrar();
		return Registros;
	}
}

