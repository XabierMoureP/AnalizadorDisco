package com.moure.data;

import java.nio.file.Path;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;

/**
 * Clase que contiene información acerca de los ficheros, tanto carpetas como archivos
 *
 */
public class FicheroGenerico {

	/**
	 * Nombre del fichero
	 */
	private String nombre;
	
	/**
	 * Path del fichero
	 */
	private Path pathFichero;
	
	/**
	 * Porcentaje ocupado por el fichero en un formato fácilmente legible (XX%)
	 */
	private String porcentajeLegible;
	
	/**
	 * Porcentaje ocupado, utilizado para la barra de progreso
	 */
	private int porcentaje;
	
	/**
	 * Tamaño del fichero en bytes
	 */
	protected long tamFicheroBytes;
	
	/**
	 * Constructor de la clase FicheroGenerico 
	 * @param nombre Nombre del fichero
	 * @param pathFichero Path del fichero
	 */
	public FicheroGenerico(String nombre, Path pathFichero) {
		super();
		this.nombre = nombre;
		this.pathFichero = pathFichero;
	}
	
	/**
	 * Constructor vacío de la clase FicheroGenerico. Crea una instancia de FicheroGenerico sin información
	 */
	public FicheroGenerico() {
		
	}
	
	/**
	 * Getter de {@link #nombre}
	 * @return el nombre del fichero
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Getter de {@link #pathFichero}
	 * @return el path del fichero
	 */
	public Path getPathFichero() {
		return pathFichero;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}
	
	/**
	 * Setea el porcentaje que ocupa el fichero dentro de la carpeta que lo contiene
	 * @param f Porcentaje ocupado por e fichero
	 */
	public void setPorcentajeOcupado(double f) {
		this.porcentaje = (int)f;
		DecimalFormat format = new DecimalFormat("#.##");
		this.porcentajeLegible = (format.format(f)+"%");
	}
	
	/**
	 * Getter de {@link #porcentajeLegible}
	 * @return el porcentaje ocupado por el fichero en un formato legible
	 */
	public String getPorcentajeLegible() {
		return this.porcentajeLegible;
	}
	
	/**
	 * Getter de {@link #porcentaje}
	 * @return el porcentaje ocupado por el fichero en un formato numérico
	 */
	public int getPorcentaje() {
		return this.porcentaje;
	}
	
	/**
	 * Setter de {@link #tamFicheroBytes}
	 * @param l Tamaño del fichero
	 */
	public void setTamFicheroBytes(long l) {
		this.tamFicheroBytes = l;
	}
	
	/**
	 * Getter de {@link #tamFicheroBytes}
	 * @return el tamaño ocupado por el fichero en Bytes
	 */
	public long getTamFicheroBytes() {
		return this.tamFicheroBytes;
	}
	
	/**
	 * Transforma el tamaño ocupado por el fichero de Bytes a MB o GB según se necesite
	 * @param bytes Tamaño ocupado por el fichero
	 * @return El tamaño que ocupa el fichero en un formato legible
	 * https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java
	 */
	public String humanReadableByteCountBin(long bytes) {
	    long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
	    if (absB < 1024) {
	        return bytes + " B";
	    }
	    long value = absB;
	    CharacterIterator ci = new StringCharacterIterator("KMGTPE");
	    for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
	        value >>= 10;
	        ci.next();
	    }
	    value *= Long.signum(bytes);
	    return String.format("%.1f %cB", value / 1024.0, ci.current());
	}
}
