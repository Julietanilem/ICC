package mx.unam.ciencias.icc;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para listas genéricas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Iterable<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
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
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if  (!hasNext())
                throw new NoSuchElementException();
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            siguiente = anterior;
            anterior = anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            siguiente = cabeza;
            anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
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
        if (elemento == null) throw new IllegalArgumentException ("No se puede insertar un elemento nulo") ;
        Nodo n = new Nodo(elemento);
        if (cabeza == null)
            cabeza = rabo =n;
        else if (i<=0) {
            cabeza.anterior = n;
            n.siguiente = cabeza;
            cabeza = n;
        } else if (i>=longitud) {
            rabo.siguiente = n;
            n.anterior = rabo;
            rabo = n;
        } else {
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
    private Nodo getIesimoNodo(int i) {
        if (cabeza == null || i<0 || i>=longitud) return null;
        Nodo n = cabeza;
        int c = 0;
        while (n!=null) {
            if (c==i)
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
        if (elemento == null) return;
        if (esVacia()) return;
        Nodo n = buscaNodo(elemento);
        if (n == null) return;
        if (cabeza == rabo) {
            limpia();
        } else if (n.anterior == null) {
            eliminaPrimero();
        } else if (n.siguiente == null) {
            eliminaUltimo();
        } else {
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
        if (elemento == null) return null;
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
        if(cabeza == null) throw new  NoSuchElementException ("La lista es vacía") ;
        T  e = cabeza.elemento;
        if (cabeza==rabo){
            cabeza=rabo=null;
        } else {
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
        if (rabo == null)  throw new  NoSuchElementException ("La lista es vacía");
        T e = rabo.elemento;
        if (cabeza == rabo) {
            cabeza=rabo=null;
        } else {
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
        Lista<T> l = new Lista<>();
        for(T n: this)
            l.agregaInicio(n);
        return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> l = new Lista<>();
        for(T n : this)
            l.agregaFinal(n);	
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

    /*
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (getIesimoNodo(i) == null)
            throw new  ExcepcionIndiceInvalido ("Indice inválido para esta lista de "+ longitud + "elementos");
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
        if (cabeza == null || elemento == null) return -1;
        int c = 0;
        for(T n : this){
            if (n.equals(elemento))
                return c;
            c++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (cabeza == null) return "[]";
        StringBuffer sb = new StringBuffer();
        sb.append("["+cabeza.elemento.toString());
        Boolean primero = true;
        for  (T n : this) {
            if (primero) {
                primero = false;
                continue;
            } 
            sb.append(", " + n.toString());
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
        if ( lista == null || lista.longitud != longitud) return false;
        IteradorLista<T>  i = lista.iteradorLista();
        for  (T n : this ) {
            if (!n.equals(i.next()))
                return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        return mergeSortR(copia(), comparador);
    }

    private Lista<T> mergeSortR(Lista<T> lista, Comparator<T> comparador)  {
        if(lista.longitud<2) return lista;
        Lista<T> l1 = new Lista<T> ();
        Lista<T> l2 = new Lista<T>();
        int i = 0;
        for(T elem : lista){
            if (i< lista.longitud/2)
            l1.agregaFinal(elem);
            else
            l2.agregaFinal(elem);
            i++;
        }
        Lista<T> listaIzq = mergeSortR (l1, comparador);
        Lista<T> listaDer = mergeSortR (l2, comparador);
        return mezcla(listaIzq, listaDer, comparador);
    }

    private Lista<T> mezcla (Lista<T> li , Lista<T> ld, Comparator<T> comparador ){
        Lista<T> ordenada = new Lista<>();
        Nodo n1 = li.cabeza;
        Nodo n2 = ld.cabeza;
        while (n1 != null && n2 != null) {
            if (comparador.compare(n1.elemento, n2.elemento)<=0) {
                ordenada.agregaFinal(n1.elemento);
                n1 = n1.siguiente;
            } else { 
            ordenada.agregaFinal(n2.elemento);
            n2 = n2.siguiente;
            }
        }
        Nodo n3 = (n1!=null)? n1 : n2;
        while (n3!=null) {
            ordenada.agregaFinal(n3.elemento);
            n3 = n3.siguiente;
        }
        return ordenada;
    }
    

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
	Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        if (comparador.compare(elemento, cabeza.elemento) <0 ||  comparador.compare(elemento, rabo.elemento) >0 ) return false;
            Nodo n= cabeza;
        while (n!=null) {
            if(comparador.compare(elemento, n.elemento) ==0) return true;
            n =  n.siguiente;
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
	boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
