package mx.unam.ciencias.icc;

import java.util.NoSuchElementException;

/**
 * <p>Clase para listas genéricas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas son iterables utilizando sus nodos. Las listas no aceptan a
 * <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> {

    /**
     * Clase interna para nodos.
     */
    public class Nodo {

        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }

        /**
         * Regresa el nodo anterior del nodo.
         * @return el nodo anterior del nodo.
         */
        public Nodo getAnterior() {
            return anterior;
        }

        /**
         * Regresa el nodo siguiente del nodo.
         * @return el nodo siguiente del nodo.
         */
        public Nodo getSiguiente(){ 
            return siguiente;
        }

        /**
         * Regresa el elemento del nodo.
         * @return el elemento del nodo.
         */
        public T get() {
            return elemento;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        inserta(longitud+1, elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        inserta(0, elemento);
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null) 
            throw new IllegalArgumentException ("No se puede insertar un elemento nulo") ;
        Nodo n = new Nodo(elemento);
        if (cabeza == null)
            cabeza = rabo =n;
        else if(i<=0){
            cabeza.anterior = n;
            n.siguiente = cabeza;
            cabeza = n;
        }else if (i>=longitud){
            rabo.siguiente = n;
            n.anterior = rabo;
            rabo = n;
        }else{
            Nodo iesimo = getIesimoNodo (i);
            if (iesimo==null) return;
            iesimo.anterior.siguiente = n;
            n.siguiente = iesimo;
            n.anterior = iesimo.anterior;
            iesimo.anterior = n;
        }
        longitud++;
    }
    /**
     * Busca el nodo de la lista en una posición indicada   
     * @param indice del nodo a buscar
     */
    private Nodo getIesimoNodo(int i){
        if(cabeza == null || i<0 || i>=longitud ) return null;
        Nodo n = cabeza;
        int c = 0;
        while(n!=null){
            if(c==i)
                return n;
            c++;
            n = n.siguiente;    
        }
        return null;
    }
    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {       
        if(elemento == null) return;
        if(esVacia()) return;
        Nodo n = buscaNodo(elemento);
        if(n == null) return;
        if(cabeza == rabo){
            limpia();
        }else if(n.anterior == null){
            eliminaPrimero();
        }else if(n.siguiente==null){
            eliminaUltimo();
        }else{
            longitud--;
            n.anterior.siguiente = n.siguiente;
            n.siguiente.anterior = n.anterior;
        }
    }
    /**
     * Busca  un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método regresa null.
     * @param elemento el elemento a buscar.
     */

    private Nodo buscaNodo(T elemento){
        if (elemento == null) 
            return null;
        Nodo n = cabeza;
        while (n != null){
            if(n.elemento.equals(elemento))
            return n;
            n = n.siguiente;
        }
        return null; 
    }
    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if(cabeza == null) 
            throw new  NoSuchElementException ("La lista es vacía") ;
        T  e = cabeza.elemento;
        if (cabeza==rabo){
            cabeza=rabo=null;
        }else{
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }
        longitud --;
        return e;
    }
    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(rabo == null)  
            throw new  NoSuchElementException ("La lista es vacía");
        T e = rabo.elemento;
        if(cabeza==rabo){
            cabeza=rabo=null;
        }else{
            
            rabo = rabo.anterior;
            rabo.siguiente = null;
        }
        longitud --;
        return e;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(T elemento) {
        return buscaNodo(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Nodo n = cabeza;
        Lista<T> l = new Lista<>();
        while(n!=null){
            l.agregaInicio(n.elemento);
            n = n.siguiente;
        }
        return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Nodo n = cabeza;
        Lista<T> l = new Lista<>();
        while(n!=null){
            l.agregaFinal(n.elemento);
            n = n.siguiente;
        }
        return l;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {  
        longitud = 0;
        rabo = cabeza = null;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() { 
        if (cabeza == null)
            throw new  NoSuchElementException("La lista es vacía");
        else
            return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {    
        if (rabo == null)
            throw new  NoSuchElementException("La lista es vacía");
        else
            return  rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (getIesimoNodo(i) == null)
            throw new  ExcepcionIndiceInvalido ("Indice inválido para esta lista de "+ longitud + " elementos");
        else
            return  getIesimoNodo(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        if (cabeza == null || elemento == null) 
            return -1;
        Nodo n = cabeza;
        int c = 0;
        while(n!=null){
            if (n.elemento.equals(elemento))
                return c;
            n = n.siguiente;
            c++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {   
        if(cabeza==null) 
            return "[]";
        StringBuffer sb = new StringBuffer();
        Nodo n = cabeza.siguiente;
        sb.append("["+cabeza.elemento.toString());
        while (n!=null){
            sb.append(", "+n.elemento.toString());
            n = n.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if ( lista == null || lista.longitud != longitud) 
            return false;
        Nodo n = cabeza;
        Nodo nl = lista.cabeza;
        while (n!=null ){
            if (!n.elemento.equals(nl.elemento))
            return false;
            n = n.siguiente;
            nl = nl.siguiente;
        }
        return true;
    }

    /**
     * Regresa el nodo cabeza de la lista.
     * @return el nodo cabeza de la lista.
     */
    public Nodo getCabeza() {
        return cabeza;
    }

    /**
     * Regresa el nodo rabo de la lista.
     * @return el nodo rabo de la lista.
     */
    public Nodo getRabo() {
        return rabo;
    }
}
