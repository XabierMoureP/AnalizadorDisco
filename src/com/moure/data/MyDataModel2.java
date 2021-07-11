package com.moure.data;

import java.io.File;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Esta clase es la encargada de generar un modelo de datos para la segunda tabla y se crea desde MyDataModel a medida que 
 * se van encontrando archivos
 *
 */
public class MyDataModel2 extends AbstractTableModel{

	/**
	 * ArrayList con las extensiones
	 */
	private ArrayList<Extension> extensiones, extensionesFiltradas;
	
	/**
	 * ArrayList con nombres de extensiones para filtrar
	 */
	private ArrayList<String> filtrosExtensiones;
	
	/**
	 * Tama�o total de las extensiones
	 */
	private long tamanyoTotalExtensiones;
	
	/**
	 * Nombres de las columnas de la tabla
	 */
	private String[] columnasTabla = new String[]{"Extension", "Tamanyo", "N� archivos", "Porcentaje", "Porcentaje Ocupado"};
	
	/**
	 * Constructor sin par�metros de la clase MyDataModel2
	 */
	public MyDataModel2() {
		this.extensiones = new ArrayList<>();
		this.extensionesFiltradas = new ArrayList<>();
		this.filtrosExtensiones = new ArrayList<>();
		this.tamanyoTotalExtensiones = 0;
	}
	
	/**
	 * M�todo que filtra los archivos y elimina aquellos que no tienen que ser mostrados por la tabla
	 * @param s Nombre de la extensi�n que el usuario quiere eliminar
	 */
	public void addFiltroPorExtension(String s) {
		boolean coincide = false;
		filtrosExtensiones.add(s);
		this.extensionesFiltradas.clear();
		for(int i = 0; i < extensiones.size(); i++) {
			for(int j = 0; j < this.filtrosExtensiones.size(); j++) {
				if(this.extensiones.get(i).getExtension().equals(this.filtrosExtensiones.get(j))) coincide = true;
			}
			if(!coincide) {
				this.extensionesFiltradas.add(this.extensiones.get(i));
			}
			coincide = false;
		}
	}
	
	/**
	 * Este m�todo vac�a el ArrayList en el que se almacenan las extensiones, y se llama a {@link #addFiltroPorExtension(String)}
	 * sin ninguna extensi�n para recuperar todos los archivos
	 */
	public void eliminarTodosFiltros() {
		this.filtrosExtensiones.clear();
		this.addFiltroPorExtension(null);
	}
	
	/**
	 * A�ade una extensi�n al ArrayList o, en caso de existir ya, incrementa el contador de dicha extensi�n
	 * @param s Nombre de la extensi�n del archivo
	 * @param l Tama�o ocupado por el archivo
	 */
	public void addExtension(String s, long l) {
		for(int i = 0; i < this.extensiones.size(); i++) {
			if(this.extensiones.get(i).getExtension().equals(s)) {
				this.extensiones.get(i).sumarContador();
				this.extensiones.get(i).setEspacioTotalOcupado(l);
				return;
			}
		}
		this.extensiones.add(new Extension(s, l));
	}
	
	/**
	 * Extrae los datos del archivo para a�adirlo al modelo de datos
	 * @param file Archivo del que se obtiene la informaci�n
	 */
	public void getArchiveInformation(File file) {
		 String name = getFileExtension(file);
		 long tam = getFileSize(file);
		 this.addExtension(name, tam);
	}
	
	/**
	 * M�todo para obtener la extensi�n de un archivo
	 * @param file Archivo cuya extensi�n queremos conocer
	 * @return La extensi�n del archivo
	 * https://qastack.mx/programming/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
	 */ 
	public String getFileExtension(File file) {
		String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return "Sin extension"; 
	    }
	    return name.substring(lastIndexOf);
	}
	
	/**
	 * Extrae informaci�n sobre el tama�o del archivo
	 * @param file Archivo cuyo tama�o queremos conocer
	 * @return el tama�o del archivo
	 */
	public long getFileSize(File file) {
		return file.length();
	}
	
	/**
	 * Calcula el tama�o total de todas las extensiones para poder calcular posteriormente el porcentaje ocupado por cada una
	 * @see #calcularPorcentajes()
	 */
	public void calcularTamanyoTotal() {
		for(int i = 0; i < this.extensiones.size(); i++) {
			this.tamanyoTotalExtensiones += this.extensiones.get(i).getEspacioTotalOcupado();
		}
	}
	
	/**
	 * Calcula el porcentaje que ocupa cada extensi�n con respecto al total
	 */
	public void calcularPorcentajes() {
		calcularTamanyoTotal();
		double divisor = (double) this.tamanyoTotalExtensiones;
 		for(int i = 0; i< this.extensiones.size(); i++) {
			double dividendo = (double) this.extensiones.get(i).getEspacioTotalOcupado();
			double calculo = (dividendo / divisor) * 100;
			this.extensiones.get(i).setPorcentaje(calculo);
		}
	}
	
	//METODOS IMPLEMENTADOS PARA LA TABLA
	@Override
	public int getRowCount() {
		return this.extensionesFiltradas.size();
	}

	@Override
	public int getColumnCount() {
		return this.columnasTabla.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0) 
			return this.extensionesFiltradas.get(rowIndex).getExtension();
		else if(columnIndex == 1) 
			return this.extensionesFiltradas.get(rowIndex).humanReadableByteCountBin(this.extensionesFiltradas.get(rowIndex).getEspacioTotalOcupado());
		else if(columnIndex == 2)
			return this.extensionesFiltradas.get(rowIndex).getContadorExtension();
		else if(columnIndex == 3)
			return this.extensionesFiltradas.get(rowIndex).getPorcentajeLegible();
		return this.extensionesFiltradas.get(rowIndex).getPorcentaje();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {			
		if(columnIndex == 2 || columnIndex == 4) return int.class;
		return String.class;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnasTabla[columnIndex];
	}
	
}
