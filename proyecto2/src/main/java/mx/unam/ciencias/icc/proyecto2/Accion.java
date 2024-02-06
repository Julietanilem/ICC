package mx.unam.ciencias.icc.proyecto2;

/**
 * Enumeración para las acciones posibles a realizar por la aplicación
 */

public enum Accion {
    //Modo si se tiene -o y -r
	GUARDA_REVERSA,
	
	//Modo si se tiene -r
	REVERSA,

	//Modo si se tene -o
	GUARDAR,
	
	//Modo si no se especificó ninguna bandera
	NORMAL;
}
