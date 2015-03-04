package Miscelanea;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;

import Miscelanea.UIHelper;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

public class Zebra_QL420plus {
	//Variables para uso de la nueva API de zebra
	private Connection printerConnection;
	private ZebraPrinter printer;
	private UIHelper helper;
	
	private Context	ctx;
	
	
	private boolean _copiaArchivo;
	
	private String _infImpresora;
	private String _infArchivo;
	
	/**Variables de configuracion basica del formato de impresion**/
	private double _anchoPapel;
	private double _margenSuperior;
	private double _margenIzquierda;
	private double _margenDerecha;
	private double _margenInferior;
	
	private double _anchoEtiqueta;
	private double _lineaActual;
	private double _lineaFinal;
	
	private String _tipoFuente1;
	private String _tipoFuente2;
	private double _anchoFuente1;
	private double _altoFuente1;
	private double _anchoFuente2;
	private double _altoFuente2;
	
	
	/**************************************************************************************************************************************
	 * @param _anchoPapel 		-->ancho del papel con el que se va a trabajar, normalmente 200 puntos por pulgada
	 * @param _margenSuperior	
	 * @param _margenIzquierda
	 * @param _margenDerecha
	 * @param _margenInferior
	 */
	
	public Zebra_QL420plus(Context _ctx, double anchoPapel, double margenSuperior, double margenIzquierda, double margenDerecha, double margenInferior, boolean copiaArchivo){
		this._infImpresora 		= "";
		this._infArchivo		= "";
		
		this.ctx	= _ctx;
		
		this._copiaArchivo		= copiaArchivo;
		this._anchoPapel 		= anchoPapel;
		this._margenSuperior	= margenSuperior;
		this._margenIzquierda	= margenIzquierda;
		this._margenDerecha		= margenDerecha;
		this._margenInferior	= margenInferior;	
		
		this._anchoEtiqueta = this._anchoPapel - this._margenIzquierda - this._margenDerecha;
		this._lineaActual 	= this._margenSuperior;
		this._lineaFinal 	= this._margenInferior;
		
		this.helper			= new UIHelper((Activity) this.ctx);
	}
	
	/*************************************************Funciones para el manejo de impresion*******************************/
	private ZebraPrinter Zebra_Connect(String _bluetooth) {
		ZebraPrinter printer= null;
		printerConnection 	= new BluetoothConnection(_bluetooth);        
        try {
            printerConnection.open();
        } catch (ConnectionException e) {
            DemoSleeper.sleep(100);
            this.Zebra_Disconnect();
        }      

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
                //PrinterLanguage pl = printer.getPrinterControlLanguage();
            } catch (ConnectionException e) {
                printer = null;
                DemoSleeper.sleep(100);
                this.Zebra_Disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                printer = null;
                DemoSleeper.sleep(100);
                this.Zebra_Disconnect();
            }
        }
        return printer;
    }
	
	
	
	private void Zebra_Disconnect() {
        try {
            if (printerConnection != null) {
                printerConnection.close();
            }
        } catch (ConnectionException e) {
        } finally {
        }
    }
	
	
	
	
	
	/*public void sendFile(String _pathFile, String _bluetooth){
		try {
            helper.showLoadingDialog("Enviando archivo ...");
            this.printer = this.Zebra_Connect(_bluetooth);
            if(this.printer != null){
	            printerConnection.open();
	            //this.printer = ZebraPrinterFactory.getInstance(printerConnection);
	            this.printer.printImage("AGUAS.PCX", 0, 0);//.sendFileContents(_pathFile);
	            //this.printer.storeImage(_pathFile, new ZebraImageAndroid(bitmap), arg2, arg3)
	            printerConnection.close();
	        }
        } catch (ConnectionException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            helper.dismissLoadingDialog();
            this.Zebra_Disconnect();
        }
	}*/
	
	
	
	public void printLabel(String _bluetooth) {
        printer = this.Zebra_Connect(_bluetooth);
        if (printer != null) {
        	try {
                byte[] configLabel = this.convertExtendedAscii(this.getDoLabel());
                //this.getDoLabel().getBytes();
                printerConnection.write(configLabel);
                //setStatus("Sending Data", Color.BLUE);
                DemoSleeper.sleep(150);
                if (printerConnection instanceof BluetoothConnection) {
                    //String friendlyName = ((BluetoothConnection) printerConnection).getFriendlyName();
                    //setStatus(friendlyName, Color.MAGENTA);
                    //DemoSleeper.sleep(500);
                }
            } catch (ConnectionException e) {
                //setStatus(e.getMessage(), Color.RED);
            } finally {
            	this.Zebra_Disconnect();
            }
        } else {
        	this.Zebra_Disconnect();
        }
    }
	
	
	private byte[] convertExtendedAscii(String input){
		int length = input.length();
		byte[] retVal = new byte[length];
		for(int i=0; i<length; i++){
			char c = input.charAt(i);
			if (c < 127){
				retVal[i] = (byte)c;
			}else{
				retVal[i] = (byte)(c - 256);
			}
		}
		return retVal;
	}
	
	
	
	
	/**************************************************Inicio de las funciones propias para la impresion CPCL*****************************************************/
	public void clearInformacion(){
		this._infImpresora 	= "";
		this._infArchivo 	= "";
	}
		
	
	public void DrawImage(String NameFile, double PosX, double PosY){
		this._infImpresora 	+= "PCX "+(this._margenIzquierda+PosX)+" "+(this._lineaActual+PosY)+" !<"+NameFile+"\r\n";
        
        if(this._copiaArchivo){
			this._infArchivo+= "5;"+(this._margenIzquierda+PosX)+";"+(this._lineaActual+PosY)+";"+NameFile+";\r\n";			
		}
    }
	
	
	public void WrTitulo(String TextoTitulo, double SaltoLineaPre, double SaltoLineaPos){
		double Char2Line;
	    String Words[];
	    String WordsLine;
	    double Justificacion;
	
	    SetValuesFont("TITULO");
	    WordsLine = "";
	    Justificacion = 0;
	
	    this._lineaActual += this._altoFuente1 * SaltoLineaPre;
	    Char2Line = this._anchoEtiqueta / this._anchoFuente1;
	    Words = TextoTitulo.split(" ");
	
	    for(int i = 0; i<Words.length;i++){
	        WordsLine += Words[i] + " ";
	        if (i < Words.length){
	            Justificacion = Words[i].length();
	        }else{
	            Justificacion = 0;
	        }
	
	        if ((WordsLine.length() + Justificacion) > Char2Line){
	            Justificacion = (this._anchoEtiqueta - (WordsLine.length() * this._anchoFuente1)) / 2;
	            this._infImpresora += "TEXT " + this._tipoFuente1 + " 0 " + Justificacion + " " + this._lineaActual + " " + WordsLine +"\r\n";
	            if(this._copiaArchivo){
	    			this._infArchivo += "1;"+Justificacion+";"+this._lineaActual+";"+WordsLine+";\r\n";			
	    		}
	            this._lineaActual += this._altoFuente1;	
	            WordsLine = "";
	        }
	    }
	    Justificacion = (this._anchoEtiqueta - (WordsLine.length() * this._anchoFuente1)) / 2;
	    this._infImpresora += "TEXT " + this._tipoFuente1+ " 0 " + Justificacion + " " + this._lineaActual + " " + WordsLine + "\r\n";
	    if(this._copiaArchivo){
			this._infImpresora += "1;"+Justificacion+";"+this._lineaActual+";"+WordsLine+";\r\n";			
		}
	    this._lineaActual += this._altoFuente1 * SaltoLineaPos;
	}
	
	
	private void SetValuesFont(String Label){
		if (Label.equals("TITULO")){
	        this._tipoFuente1 	= "INSTRUCT.CPF";	//LabelFont = "INSTRUCT.CPF";
	        this._altoFuente1	= 22;				//HeightFont = 22;
	        this._anchoFuente1	= 14;				//WidthFont = 13;
	    }else if(Label.equals("SUBTITULO")){
	    	this._tipoFuente1 	= "LIBERATI.CPF";	//LabelFont = "LIBERATI.CPF";
	    	this._altoFuente1	= 20;				//HeightFont = 20;
	    	this._anchoFuente1	= 11;				//WidthFont = 10;
	    }else if(Label.equals("LABEL")){
	    	this._tipoFuente1 	= "JACKINPU.CPF";	//LabelFont = "JACKINPU.CPF";
	    	this._altoFuente1	= 18;				//HeightFont = 18;
	    	this._anchoFuente1	= 12;				//WidthFont = 11;	
	    	this._tipoFuente2 	= "JACKINPU.CPF";	//TextFont = "JACKINPU.CPF";
	    	this._altoFuente2	= 18;				//HeightText = 18;
	    	this._anchoFuente2	= 12;				//WidthText = 11;
	    }else{
	    	this._tipoFuente2 	= "JACKINPU.CPF";	//TextFont = "JACKINPU.CPF";
	        this._altoFuente2	= 18;				//HeightText = 18;
	        this._anchoFuente2	= 12;				//
	    }
	}
	
	
	public void WrLabel(String Etiqueta, String InfEtiqueta, int OffsetWrLabel, double SaltoLineaPre, double SaltoLineaPos){ 
		this._lineaActual += this._altoFuente1 * SaltoLineaPre;
	    SetValuesFont("LABEL");
	    this._infImpresora += "TEXT " + this._tipoFuente1 + " 0 " + OffsetWrLabel + " " + this._lineaActual + " " + Etiqueta + " \r\n";
	    this._infImpresora += "TEXT " + this._tipoFuente2 + " 0 " + (OffsetWrLabel + ((Etiqueta.length() + 1) * this._anchoFuente1)) + " " + this._lineaActual+ " " + InfEtiqueta + "\r\n";
	    
	    if(this._copiaArchivo){
			this._infArchivo += "3;"+OffsetWrLabel+";"+ this._lineaActual+";"+Etiqueta+";"+InfEtiqueta+";\r\n";			
		}
	    
	    this._lineaActual += this._altoFuente1 * SaltoLineaPos;
	 }
	
	
	public void JustInformation(String Informacion, int OffsetInformation, double SaltoLineaPre, double SaltoLineaPos){
		SetValuesFont("TEXTO");
		double Char2Line = (this._anchoEtiqueta - OffsetInformation) / this._anchoFuente2;
		this._lineaActual += this._altoFuente2* SaltoLineaPre;

		while (Informacion.length() > Char2Line){
			this._infImpresora += "TEXT " + this._tipoFuente2 + " 0 " + OffsetInformation + " " + this._lineaActual+ " " + Informacion.substring(0, (int)Char2Line) + " \r\n";
			if(this._copiaArchivo){
				this._infArchivo += "4;"+OffsetInformation+";"+this._lineaActual+";"+Informacion.substring(0, (int)Char2Line)+";\r\n";			
			}
			Informacion = Informacion.substring((int)Char2Line);
			this._lineaActual += this._altoFuente2;
		}
		this._infImpresora += "TEXT " + this._tipoFuente2+ " 0 " + OffsetInformation + " " + this._lineaActual+ " " + Informacion + " \r\n";
		if(this._copiaArchivo){
			this._infArchivo += "4;"+OffsetInformation+";"+this._lineaActual+";"+Informacion+";\r\n";			
		}		
		this._lineaActual += this._altoFuente2* SaltoLineaPos;		
	}
	
	
	public void WrSubTitulo(String InfSubtitulo, int OffsetSub, double SaltoLineaPre, double SaltoLineaPos){
		this._lineaActual += this._altoFuente1 * SaltoLineaPre;
		SetValuesFont("SUBTITULO");
		this._infImpresora += "TEXT " + this._tipoFuente1+ " 0 " + OffsetSub + " " + this._lineaActual+ " " + InfSubtitulo + " \r\n";
		if(this._copiaArchivo){
			this._infArchivo += "2;"+OffsetSub+";"+this._lineaActual+";"+InfSubtitulo+";\r\n";			
		}
		this._lineaActual += this._altoFuente1 * SaltoLineaPos;
	}
	
	
	public void WrRectangle(double PosX1, double PosY1, double PosX2, double PosY2, double Incremento, int Shadow){
		double IncLine = 0;
	    int i;	
	    this._infImpresora += "BOX " + PosX1 + " " + PosY1 + " " + PosX2 + " " + PosY2 + " 2 \r\n";
	
	    if (Shadow != 0){
	        IncLine = (PosX2 - PosX1) / Shadow;
	        for (i = 0; i<Shadow;i++){
	        	this._infImpresora += "BOX " + (PosX1 + (IncLine * i)) + " " + PosY1 + " " + (PosX1 + (IncLine * i)) + " " + PosY2 + " 0 \r\n";
	        }	
	        for(i = 0;i<Shadow;i++){
	        	this._infImpresora += "BOX " + PosX1 + " " + (PosY1 + (IncLine * i)) + " " + PosX2 + " " + (PosY1 + (IncLine * i)) + " 0 \r\n";
	        }
	    }
	
	    if (Incremento != 0) {
	        this._lineaActual += PosY2 - PosY1;
	    }
	}
	
	
	public String getDoLabel(){
		this._infImpresora = "! " + this._margenIzquierda+ " 200 200 " + (this._lineaActual + this._lineaFinal) + " 1" + " \r\n" + "ENCODING UTF-8 \r\n LABEL " + " \r\n" + this._infImpresora+ " \r\n";
		this._infImpresora += "PRINT \r\n";
	    return this._infImpresora;
	}
	
	
	public String getInfArchivo(){
		return this._infArchivo;
	}
	
	
	
	public double getCurrentLine(){
	    return this._lineaActual;
	}	
	
	
	public void setCurrentLine(double valorCurrentLine){
		this._lineaActual = valorCurrentLine;
	}
	
	
	public void resetEtiqueta(){
		this._lineaActual 	= this._margenSuperior;
		this._lineaFinal 	= this._margenInferior;
	}
}
