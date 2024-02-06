package mx.unam.ciencias.icc.proyecto2;
import mx.unam.ciencias.icc.Lista;

public class LineaDeComandos {

    //Enumeración de las banderas posibles 
    private enum Bandera {

	//Bandera -o
        GUARDAR,
        
	//Bandera -r
        REVERSA;
	
	/*
	 * Regresa la bandera a la que correspende la cadena pasada
	 *  @param bandera. La cadena que se quiere verificar si es una bandera de las específicadas.
	 */
	
        public static Bandera getBandera(String bandera){
            switch (bandera) {
            case "-r": return REVERSA;
            case "-o": return GUARDAR ;
            default: throw new ExcepcionBanderaInvalida(
                "Bandera inválida: " + bandera);
            }
        }
    }
    
    //Modo definido por las banderas que se usan
    public  Modo  modo = new Modo();
    
    //Rutas que se pasarán
    public  Lista<String> rutas ;

    //Define el modo y llena la lista con las rutas
    public  LineaDeComandos (String[] args) 
    {
        rutas = new Lista<>();
        for(int i = 0; i<args.length; i++){
            if (esBandera(args[i])){
                switch (Bandera.getBandera(args[i])) {
                    case REVERSA:
                        modo.setReversa();
                        break;
                    case GUARDAR:
                        if(i+1>= args.length ) 
                            throw new ExcepcionIdentificadorInvalido ();
                        if(!esArchivo(args[i+1])) 
                            throw new ExcepcionIdentificadorInvalido ();
                        modo.setGuardar(args[++i]);
                }
            }else if (esArchivo(args[i]))
                rutas.agregaFinal(args[i]);
            else{
                throw new ExcepcionArchivoInvalido();
                }
        }	
    }
    /*
     *Define si un parámetro pretende ser una bandera
      * @param str La cadena que queremos verificar si tiene la estructura de una cadena
      * @return true si la cadena tiene estructura de una cadena. false en otro caso.
    */
    public static boolean esBandera (String str){
        return str.startsWith("-") && str.length() == 2;
    }   
    /*
    * Define si un parámetro pretende ser un archivo válido
    * @param str La cadena que queremos verificar si tiene la estructura de un archivo de texto
    * @return true si la cadena tiene estructura de un archivo de texto váido. false en otro caso
    */
    public static boolean esArchivo(String str){
        return !esBandera(str) && str.endsWith(".txt");
    }
}
