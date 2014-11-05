package personalizados;


public class InformacionSolicitudes {
	protected String Revision;
	protected String Serie;
	protected String Direccion;
	protected int    Estado;
	protected long Id;
	
	public InformacionSolicitudes(String solicitud, String serie, String direccion, int estado){
		super();
		this.Revision 	= solicitud;
		this.Serie		= serie;
		this.Direccion 	= direccion;
		this.Estado 	= estado;
	}
	
	
	public String getRevision(){
		return this.Revision;
	}
	
	public String getDireccion(){
		return this.Direccion;
	}
	
	public String getSerie(){
		return this.Serie;
	}
	
	public int getEstado(){
		return this.Estado;
	}
	
	public long getId(){
		return this.Id;
	}
	
	public void setRevision(String revision){
		this.Revision = revision;
	}
	
	public void setSerie(String serie){
		this.Serie = serie;
	}
	
	public void setDireccion(String direccion){
		this.Direccion = direccion;
	}
	
	public void setTipo(int estado){
		this.Estado = estado;
	}
	
	public void setId(long id){
		this.Id= id;
	}
}