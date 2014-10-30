package Miscelanea;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialogos{
	private Context CtxDialogos;
	private AlertDialog.Builder  MsgDialog = null;
	
	
	public Dialogos(Context ctx){
		this.CtxDialogos = ctx;
		MsgDialog = new AlertDialog.Builder(this.CtxDialogos);
	}
	
	public void DialogoInformativo(String Titulo, String Mensaje){
		MsgDialog.setTitle(Titulo);
		MsgDialog.setMessage(Mensaje);
		MsgDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				dialog.cancel();
			}
		});
		MsgDialog.show();
	}
}
