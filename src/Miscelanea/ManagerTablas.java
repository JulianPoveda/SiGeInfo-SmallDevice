package Miscelanea;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerTablas {

	private final Context mtContexto;
	
	
	public ManagerTablas (Context c){
		mtContexto = c;
	}
	
	
    public TableLayout dibujarTabla(int tamBorde, int[] AnchoColumna, String colorBorde, ArrayList<ArrayList<String>> Datos){
    	TableLayout tabla = new TableLayout(mtContexto);
    	ArrayList<String> newColumn = new ArrayList<String>();
    	
    	if(Datos!=null){    		
	    	TableRow fila2 = new TableRow(mtContexto);
	    	RelativeLayout borde2 = null;
	    	//int ancho2 = 200;
	    	int i,j;
	    	for(i=0;i<Datos.size();i++){
	    		newColumn = Datos.get(i);
	    		
				for(j=0;j<newColumn.size();j++){
					borde2 = new RelativeLayout(mtContexto);									//Definimos los bordes de la tabla 		
					if(i==Datos.size()-1)
						borde2.setPadding(tamBorde,tamBorde,0,tamBorde);						//Si es ultima fila dibuja de arriba, abajo e izquierda 
					else 
						borde2.setPadding(tamBorde,tamBorde,0,0);								//Dibuja los de arriba y la izquierda siempre		
					borde2.setBackgroundColor(Color.parseColor(colorBorde));					//Color del borde.
	    			TextView texto = new TextView(mtContexto);									//Ahora el texto..
	    			texto.setText(newColumn.get(j));
	    			texto.setWidth(AnchoColumna[j]);
	    			texto.setGravity(Gravity.CLIP_HORIZONTAL);
	    			texto.setPadding(2, 2, 2, 2);
	    			texto.setBackgroundColor(Color.GREEN);						//Importante el color, porque si no se verá de color del borde!!   
	    			borde2.addView(texto);														//Al borde le añadimos el texto
	    			fila2.addView(borde2);														//Y a la fila el borde con el texto
				}
						
				
				if(i==(Datos.size()-1))
					borde2.setPadding(tamBorde, tamBorde, tamBorde, tamBorde);
				else
					borde2.setPadding(tamBorde, tamBorde, tamBorde, 0);
				
				tabla.addView(fila2);
		        fila2 = new TableRow(mtContexto);
			}
	    	
    	}
    	else{
    		Toast.makeText(mtContexto,"No hay datos para mostrar en la tabla.", Toast.LENGTH_LONG).show();  
    	}
    	return tabla;
    	
    }
}