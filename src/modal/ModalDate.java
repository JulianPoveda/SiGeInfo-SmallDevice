package modal;

import java.text.DecimalFormat;

import eaav.android_v1.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class ModalDate extends Activity implements OnClickListener{
	private Button 		_btnAceptar, _btnCancelar;
	private DatePicker _datePickerFecha;
	
	private DecimalFormat formatoFecha = new DecimalFormat("00"); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modal_date);
		
		this._btnAceptar		= (Button) findViewById(R.id.DatePickerBtnAceptar);
		this._btnCancelar 		= (Button) findViewById(R.id.DatePickerBtnCancelar);
		this._datePickerFecha 	= (DatePicker) findViewById(R.id.DatePickerCmbFecha);
		
		this._btnAceptar.setOnClickListener(this);
		this._btnCancelar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.DatePickerBtnAceptar:
				finish(true);
				break;
				
			case R.id.DatePickerBtnCancelar:
				finish(false);
				break;
		}		
	}
	
	public void finish(boolean _caso) {
		Intent data = new Intent();
		data.putExtra("fecha", formatoFecha.format(this._datePickerFecha.getDayOfMonth())+"-"+formatoFecha.format(this._datePickerFecha.getMonth()+1)+"-"+this._datePickerFecha.getYear());
		data.putExtra("accion", _caso);	
		setResult(RESULT_OK, data);
		super.finish();
	}
}
