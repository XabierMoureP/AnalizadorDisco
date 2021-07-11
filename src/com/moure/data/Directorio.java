package com.moure.data;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Clase que contiene información sobre todos los subdirectorios de un directorio raíz
 *
 */
public class Directorio extends FicheroGenerico{

	/**
	 * Lista con todos los ficheros contenidos por el directorio
	 */
	private ArrayList<FicheroGenerico> ficherosGenericos;
	
	/**
	 * Tamaño del directorio en un formato legible
	 */
	private String tamDirectorio;
	
	/**
	 * Enteros que contienen información acerca del número de ficheros contenidos en un directorio
	 */
	private int numFicheros, numArchivos, numCarpetas;

	/**
	 * Constructor heredado de la clase FicheroGenerico
	 */
	public Directorio() {
		super();
		this.ficherosGenericos = new ArrayList<FicheroGenerico>();
	}
	
	/**
	 * Constructor heredado de la clase FicheroGenerico
	 * @param nombre Nombre que recibe el directorio
	 * @param pathFichero Path del directorio
	 */
	public Directorio(String nombre, Path pathFichero) {
		super(nombre, pathFichero);
		ficherosGenericos = new ArrayList<>();
	}

	/**
	 * Getter de {@link #tamDirectorio}
	 * @return el tamaño ocupado por el directorio en un formato legible
	 */
	public String getTamDirectorio() {
		return tamDirectorio;
	}
	
	/**
	 * Setter de {@link #tamDirectorio}
	 * @param tDir Tamaño ocupado por el directorio en formato legible
	 */
	public void setTamDirectorio(String tDir) {
		this.tamDirectorio = tDir;
	}
	
	/**
	 * Getter de {@link #numFicheros}
	 * @return el número de ficheros contenidos por el directorio
	 */
	public int getNumFicheros () {
		return this.numFicheros;
	}
	
	/**
	 * Setter de {@link #numFicheros}
	 * @param n Número de ficheros dentro del directorio
	 */
	public void setNumFicheros(int n) {
		this.numFicheros = n;
	}
	
	/**
	 * Getter de {@link #numArchivos}
	 * @return el número de archivos contenidos en el directorio
	 */
	public int getNumArchivos() {
		return this.numArchivos;
	}
	
	/**
	 * Setter de {@link #numArchivos}
	 * @param n Número de archivos dentro del directorio
	 */
	public void setNumArchivos(int n) {
		this.numArchivos = n;
	}
	
	/**
	 * Getter de {@link #numCarpetas}
	 * @return El número de subdirectorios del directorio
	 */
	public int getNumCarpetas() {
		return this.numCarpetas;
	}
	
	/**
	 * Setter de {@link #numCarpetas}
	 */
	public void setNumCarpetas() {
		this.numCarpetas = this.numFicheros - this.numArchivos;
	}
	
	/**
	 * Getter de {@link #ficherosGenericos}
	 * @return la lista de ficheros del directorio
	 */
	public ArrayList<FicheroGenerico> getFicherosGenericos() {
		return ficherosGenericos;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	/**
	 * Calcula el tamaño total de un directorio recorriendo todos sus hijos
	 * @return el tamaño total ocupado en Bytes
	 */
	public long calcularTamanyoTotal() {
		long aux = 0;
		for(int i = 0; i < this.ficherosGenericos.size(); i++) {
			if(this.ficherosGenericos.get(i) instanceof Archivo) {
				Archivo a = (Archivo) this.ficherosGenericos.get(i);
				aux+= a.getTamFicheroBytes();
			}
			else if (this.ficherosGenericos.get(i) instanceof Directorio) {
				Directorio d = (Directorio) this.ficherosGenericos.get(i);
				aux += d.getTamFicheroBytes();
			}
		}
		return aux;
	}
}
