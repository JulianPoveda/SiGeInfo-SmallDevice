package personalizados;

import java.util.ArrayList;

import eaav.android_v1.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorListaTrabajo extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<InformacionSolicitudes> items;
	
	public AdaptadorListaTrabajo(Activity activity, ArrayList<InformacionSolicitudes> items){
		this.activity = activity;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return items.get(position).getId();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(convertView == null){
			LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.detalle_download_solicitudes, null);
		}
				
		InformacionSolicitudes Sol = items.get(position);
		
		TextView Solicitud = (TextView) v.findViewById(R.id.textView_orden_trabajo);
		TextView Nodo = (TextView) v.findViewById(R.id.textView_direccion);
		TextView Tipo = (TextView) v.findViewById(R.id.textView_tipo);
		
		/*******************************Generacion del color dependiendo del estado en el que se encuentre la orden**********************/
		if(Sol.getEstado()== 0){
			Solicitud.setBackgroundColor(Color.WHITE);
			Nodo.setBackgroundColor(Color.WHITE);
			Tipo.setBackgroundColor(Color.WHITE);			
		}else if(Sol.getEstado() == 1){
			Solicitud.setBackgroundColor(Color.GREEN);
			Nodo.setBackgroundColor(Color.GREEN);
			Tipo.setBackgroundColor(Color.GREEN);			
		}else if(Sol.getEstado() == 2){
			Solicitud.setBackgroundColor(Color.parseColor("#C1BFE6"));
			Nodo.setBackgroundColor(Color.parseColor("#C1BFE6"));
			Tipo.setBackgroundColor(Color.parseColor("#C1BFE6"));			
		}else if(Sol.getEstado() == 3){
			Solicitud.setBackgroundColor(Color.YELLOW);
			Nodo.setBackgroundColor(Color.YELLOW);
			Tipo.setBackgroundColor(Color.YELLOW);	
		}
		
		Solicitud.setText(Sol.getRevision());				
		Nodo.setText(Sol.getDireccion());
		Tipo.setText(Sol.getSerie());	
		return v;
	}
}
