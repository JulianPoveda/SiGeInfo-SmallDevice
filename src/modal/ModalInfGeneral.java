package modal;

import clases.ClassRevision;
import eaav.android_v1.FormLoggin;
import eaav.android_v1.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;


public class ModalInfGeneral extends Activity {
	private ClassRevision 	FcnRevision;	
	private EditText 		_txtRevision, _txtCodigo, _txtSerie, _txtNombre, _txtDireccion, _txtMarca, _txtCiclo, _txtRuta, _txtLectura, _txtNovedad;
	private String 			_revision;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modal_inf_general);
		
		Bundle bundle = getIntent().getExtras();
		this._revision 	= bundle.getString("Revision");
		
		this.FcnRevision = new ClassRevision(this, FormLoggin.CARPETA_RAIZ);
		
		this._txtRevision 	= (EditText) findViewById(R.id.GeneralTxtRevision);
		this._txtCodigo 	= (EditText) findViewById(R.id.GeneralTxtCodigo);
		this._txtSerie 		= (EditText) findViewById(R.id.GeneralTxtSerie);
		this._txtNombre 	= (EditText) findViewById(R.id.GeneralTxtNombre);
		this._txtDireccion 	= (EditText) findViewById(R.id.GeneralTxtDireccion);
		this._txtMarca 		= (EditText) findViewById(R.id.GeneralTxtMarca);
		this._txtCiclo 		= (EditText) findViewById(R.id.GeneralTxtCiclo);
		this._txtRuta 		= (EditText) findViewById(R.id.GeneralTxtRuta);
		this._txtLectura 	= (EditText) findViewById(R.id.GeneralTxtLectura);
		this._txtNovedad 	= (EditText) findViewById(R.id.GeneralTxtNovedad);
		
		this._txtRevision.setText(_revision);
		this._txtCodigo.setText(this.FcnRevision.getCodigo(_revision));
		this._txtSerie.setText(this.FcnRevision.getMarca(_revision));
		this._txtNombre.setText(this.FcnRevision.getNombre(_revision));
		this._txtDireccion.setText(this.FcnRevision.getDireccion(_revision));
		this._txtMarca.setText(this.FcnRevision.getSerie(_revision));
		this._txtCiclo.setText(this.FcnRevision.getCiclo(_revision));
		this._txtRuta.setText(this.FcnRevision.getRuta(_revision));
		this._txtLectura.setText(this.FcnRevision.getLectura1(_revision));
		this._txtNovedad.setText(this.FcnRevision.getNovedad1(_revision));
		
		this._txtRevision.setEnabled(false);
		this._txtCodigo.setEnabled(false);
		this._txtSerie.setEnabled(false);
		this._txtNombre.setEnabled(false);
		this._txtDireccion.setEnabled(false);
		this._txtMarca.setEnabled(false);
		this._txtCiclo.setEnabled(false);
		this._txtRuta.setEnabled(false);
		this._txtLectura.setEnabled(false);
		this._txtNovedad.setEnabled(false);		
	}
}
