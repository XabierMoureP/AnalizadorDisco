package com.moure.data;

import java.nio.file.Path;

/**
 * Esta clase contiene información acerca de un archivo
 *
 */
public class Archivo extends FicheroGenerico{

	/**
	 * Tamaño ocupado por el archivo en un formato legible
	 */
	private String tamArchivo;
	
	/**
	 * Extension del archivo
	 */
	private String extension;
	/**
	 * Constructor heredado de la clase FicheroGenerico
	 * @param nombre Nombre del archivo
	 * @param pathFichero Path del archivo
	 * @param tam Tamaño ocupado por el archivo
	 * @param extension Extension de archivo
	 */
	public Archivo(String nombre, Path pathFichero, long tam, String extension) {
		super(nombre, pathFichero);
		this.tamFicheroBytes = tam;
		this.tamArchivo = this.humanReadableByteCountBin(tam);
		this.extension = extension;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Getter de {@link #tamArchivo}
	 * @return el tamaño ocupado por el archivo en formato legible
	 */
	public String getTamArchivo() {
		return this.tamArchivo;
	}
	
	/**
	 * Getter de {@link #extension}
	 * @return el nombre de la extensión
	 */
	public String getExtension() {
		return this.extension;
	}
}
