package com.moure.data;

import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;

/**
 * Clase que almacena la información necesaria de cada una de las extensiones encontradas en un directorio
 *
 */
public class Extension {

	/**
	 * Nombre de la extensión
	 */
	private String extension;
	
	/**
	 * Enteros utilizados para conocer el número de veces que se repite la extensión y el porcentaje ocupado por ellas
	 */
	private int contadorExtension, porcentaje;
	
	/**
	 * Espacio ocupado por todas las extensiones del mismo tipo
	 */
	private long espacioTotalOcupado;
	
	/**
	 * Porcentaje ocupado por la extensión en un formato fácilmente legible
	 */
	private String porcentajeLegible;
	
	/**
	 * Constructor de la clase Extension
	 * @param extension Nombre de la extension del archivo
	 * @param tam Tamaño ocupado por el archivo
	 */
	public Extension(String extension, long tam) {
		super();
		this.extension = extension;
		this.contadorExtension = 1;
		this.espacioTotalOcupado = tam;
	}
	
	/**
	 * Getter de {@link #extension}
	 * @return el nombre de la extensión
	 */
	public String getExtension() {
		return this.extension;
	}
	
	/**
	 * Incrementa el contador de la extensión. Ocurre cuando se repite esta extensión en algún directorio
	 */
	public void sumarContador() {
		this.contadorExtension++;
	}
	
	/**
	 * Getter de {@link #contadorExtension}
	 * @return el número de veces que se repite una extensión
	 */
	public int getContadorExtension() {
		return this.contadorExtension;
	}
	
	/**
	 * Getter de {@link #espacioTotalOcupado}
	 * @return el espacio total ocupado por las extensiones del mismo tipo
	 */
	public long getEspacioTotalOcupado() {
		return this.espacioTotalOcupado;
	}
	
	/**
	 * Setter de {@link #espacioTotalOcupado}
	 * @param l El tamaño total
	 */
	public void setEspacioTotalOcupado(long l) {
		this.espacioTotalOcupado += l;
	}
	
	/**
	 * Setea el porcentaje ocupado por la extensión entre todas las extensiones
	 * @param d Porcentaje ocupado
	 */
	public void setPorcentaje(double d) {
		this.porcentaje = (int)d;
		DecimalFormat format = new DecimalFormat("#.##");
		this.porcentajeLegible = (format.format(d)+"%");
	}
	
	/**
	 * Getter de {@link #porcentajeLegible}
	 * @return el porcentaje ocupado de la extensión en un formato legible
	 */
	public String getPorcentajeLegible() {
		return (this.porcentajeLegible);
	}
	
	/**
	 * Getter de {@link #porcentaje}
	 * @return el porcentaje ocupado por la extensión en formato numérico
	 */
	public int getPorcentaje() {
		return this.porcentaje;
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
