package clases;

import eaav.android_v1.FormLoggin;
import Miscelanea.SQLite;
import android.content.Context;

public class ClassUsuario {
	private SQLite 	FcnSQL;
	private Context _ctxUsuario;
	private String _folderAplicacion;
	

	public ClassUsuario(Context _ctx, String _folder){
		this._ctxUsuario		= _ctx;
		this._folderAplicacion	= _folder;
		this.FcnSQL				= new SQLite(this._ctxUsuario, this._folderAplicacion, FormLoggin.NOMBRE_DATABASE);
	}
	
	public boolean existeUsuario(String _usuario, String _contrasena){
		return this.FcnSQL.ExistRegistros("db_usuarios", "usuario='"+_usuario+"' AND clave='"+_contrasena+"'");
	}
	
	
	public int getNivelUsuario(String _usuario){
		return this.FcnSQL.IntSelectShieldWhere("db_usuarios", "nivel", "usuario = '"+_usuario+ "'");
	}
}
