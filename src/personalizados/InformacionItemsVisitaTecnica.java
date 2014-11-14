package personalizados;

import java.util.ArrayList;



public class InformacionItemsVisitaTecnica {
	protected String item;
	protected ArrayList<String> opciones;
	protected long Id;
	
	public InformacionItemsVisitaTecnica(String _item, ArrayList<String> _opciones ){
		super();
		this.item 		= _item;
		this.opciones	= _opciones;
	}
	
	
	public String getItem(){
		return this.item;
	}
	
	public ArrayList<String> getOpciones(){
		return this.opciones;
	}
	
	public String getOpcion(int posicion){
		return this.opciones.get(posicion);
	}
	
	
	public long getId(){
		return this.Id;
	}
	
	public void setItem(String _item){
		this.item = _item;
	}
	
	public void setOpciones(ArrayList<String> _opciones){
		this.opciones = _opciones;
	}
	
	public void setId(long id){
		this.Id = id;
	}
}