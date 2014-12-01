package personalizados;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import eaav.android_v1.R;


public class AdaptadorVisitaTecnica extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<InformacionItemsVisitaTecnica> items;
	
	public AdaptadorVisitaTecnica(Activity activity, ArrayList<InformacionItemsVisitaTecnica> items){
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
			v = inf.inflate(R.layout.detalle_visita_tecnica, null);
		}
				
		InformacionItemsVisitaTecnica Item = items.get(position);
		
		TextView	NameItem = (TextView) v.findViewById(R.id.DetalleVisitaLblItem);
		Spinner 	OpcionItem = (Spinner) v.findViewById(R.id.DetalleVisitaCmbOpciones);
		
		/*******************************Generacion del color dependiendo del estado en el que se encuentre la orden**********************/
		NameItem.setText(Item.getItem());				
		//OpcionItem;
		return v;
	}

	public void add(InformacionItemsVisitaTecnica informacionItemsVisitaTecnica) {
		// TODO Auto-generated method stub
		
	}
}