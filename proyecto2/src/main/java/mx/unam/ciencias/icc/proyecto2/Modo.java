package mx.unam.ciencias.icc.proyecto2;

public class Modo {
    
    //Clase interna del modo reversa
    private class Reversa{
        
        //Define si el modo esta activo
        private boolean activo;

        private Reversa (){
            activo = false;
        }
    }

    //Clase interna del modo guardar
    private class Guardar{

	    //Define si el modo esta activo
        private boolean activo;

        //Define el archivo en que se especifico se guardará
        private String archivo;

        private Guardar (){
            activo = false;
            archivo = null;
        }
    }
    
    //Modo de la bandera -r
    private  Reversa r ;

    //Modo de la banera -o
    private  Guardar o ;
    
    public Modo (){
        r = new Reversa();
        o = new Guardar() ;
    }
    
    //Activa el modo de reversa
    public void setReversa(){
        r.activo = true;
    }
    
     //Activa el modo de guardado y el identificador
    public void setGuardar(String archivo){
        o.activo = true;
        o.archivo = archivo;
    }
    
    
    /*
     *Obtiene el modo en que se pretende ejecutar la aplicación
      * @return una acción que  define lo que se realizará
    */
    public Accion getModo (){
        if (r.activo && !o.activo) return Accion.REVERSA;
        else if( o.activo && !r.activo) return Accion.GUARDAR;
        else if(o.activo && r.activo) return Accion.GUARDA_REVERSA;
        else  return Accion.NORMAL;	 
    }
    
    /*
     *Obtiene el archivo donde se guardará
      *  @return una cadena que es el identificador del archivo a guardar
    */
    public String getArchivo(){
        return o.archivo;
    }
    
}