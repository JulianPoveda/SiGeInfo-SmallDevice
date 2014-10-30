package Miscelanea;

import java.util.ArrayList;


public class ConverTypes {
	
	public String[] ArrayListToArrayString(ArrayList<String> Lista){
		String[] strRetorno = new String[Lista.size()];
		strRetorno = Lista.toArray(strRetorno);
		return strRetorno;
	}
	
}
