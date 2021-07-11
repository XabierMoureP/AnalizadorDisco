package com.moure.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Esta clase es la encargada de crear un modelo de datos capaz de generar tanto el árbol como la primera tabla
 *
 */

public class MyDataModel extends AbstractTableModel implements TreeModel {

	/**
	 * Nombres de las columnas de la tabla
	 */
	private String[] columnasTabla = new String[] {"Nombre", "Tamaño", "Total Ficheros", 
			"Archivos", "Carpetas", "Porcentaje", "Porcentaje Ocupado"};
	
	/**
	 * Directorios utilizados para mostrar la información según quiere el usuario y la creación del modelo de datos
	 */
	private Directorio root, directorioSeleccionado, directorioFiltrado;
	
	/**
	 * Número de ficheros. Se utiliza para saber dónde acaban las carpetas y empiezan los directorios a la hora de mostrar el árbol
	 */
	private int numFicheros;
	
	/**
	 * Lista de listeners
	 */
	private ArrayList<TreeModelListener> listenerList;
	
	/**
	 * Lista de las extensiones filtradas por el usuario
	 */
	private ArrayList<String> extensionesFiltros;
	/**
	 * Variable donde se guarda el modelo de datos para la segunda tabla
	 */
	private MyDataModel2 mdm2;
	
	/**
	 * Tamaño elegido por el usuario como filtro
	 */
	private long tamFiltro;
	
	/**
	 * Constructor de la clase MyDataModel
	 * @param pathParaAnalizar Path elegido por el usuario desde el diálogo inicial a partir del cual se ejecuta el análisis
	 * @param d Directorio representado por el path anterior
	 */
	public MyDataModel(Path pathParaAnalizar, Directorio d) {
		super();
		listenerList = new ArrayList<>();
		extensionesFiltros = new ArrayList<>();
		numFicheros = 0;
		mdm2 = new MyDataModel2();
		root = createDataModel(pathParaAnalizar, d);
		root.setTamFicheroBytes(this.tamRoot());
		setPorcentajeAFicherosDeDirectorio(root);
		directorioSeleccionado = root;
		mdm2.calcularPorcentajes();
		directorioFiltrado = new Directorio();
		tamFiltro = 0;
		this.setTamFiltrado(tamFiltro);
		mdm2.addFiltroPorExtension(null);
	}
	
	/**
	 * Método recursivo que genera el modelo de datos
	 * @param path Path de la carpeta raíz que se quiere analizar
	 * @param dir Directorio representado por el path anterior
	 * @return Un directorio con todos sus ficheros incluidos
	 */
	public Directorio createDataModel(Path path, Directorio dir) {
		try {
		DirectoryStream<Path> ds = Files.newDirectoryStream(path);
			for(Path p : ds) {
				numFicheros++;
				if(Files.isRegularFile(p)) {
					File file = p.toFile();
					mdm2.getArchiveInformation(file);
					Archivo a = new Archivo(p.getFileName().toString(), p, file.length(), mdm2.getFileExtension(file));
					dir.getFicherosGenericos().add(a);
				}
				else if(Files.isDirectory(p)) {
					File file = p.toFile();
					Directorio d = new Directorio(p.getFileName().toString(), p);
					dir.getFicherosGenericos().add(0, d);
					createDataModel(p, d);					
					d.setNumFicheros(totalFicheros(file));
					d.setNumArchivos(totalArchivos(file));
					d.setNumCarpetas();					
					d.setTamFicheroBytes(d.calcularTamanyoTotal());
					d.setTamDirectorio(d.humanReadableByteCountBin(d.calcularTamanyoTotal()));
				}
				//Descomentar linea para comprobar funcionamiento en una raiz
				//System.out.println("anadido "+p.toString());
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return dir;
	}
	
	/**
	 * Método que fija el tamaño mínimo a partir del cual se quieren mostrar los ficheros. Filtra todos los ficheros 
	 * del {@link #directorioSeleccionado} y añade los que cumplen la condición a {@link #directorioFiltrado}
	 * @param l Tamaño en Bytes mínimo
	 */
	public void setTamFiltrado(long l) {
		tamFiltro = l;
		directorioFiltrado.getFicherosGenericos().clear();
		for(int i = 0; i < directorioSeleccionado.getFicherosGenericos().size(); i++) {
			if(directorioSeleccionado.getFicherosGenericos().get(i).getTamFicheroBytes() >= tamFiltro) {
				directorioFiltrado.getFicherosGenericos().add(directorioSeleccionado.getFicherosGenericos().get(i));
			}
		}
	}
	
	/**
	 * Método que filtra los ficheros contenidos en un directorio según la extensión que elige el usuario
	 * @param s Nombre de la extensión
	 */
	public void setExtensionFiltro(String s) {
		boolean coincide = false;
		this.extensionesFiltros.add(s);
		this.directorioFiltrado.getFicherosGenericos().clear();
		for(int i = 0; i < this.directorioSeleccionado.getFicherosGenericos().size(); i++) {
			if(this.directorioSeleccionado.getFicherosGenericos().get(i) instanceof Directorio) {
				Directorio d = (Directorio) this.directorioSeleccionado.getFicherosGenericos().get(i);
				if(d.getTamFicheroBytes() >= this.tamFiltro) this.directorioFiltrado.getFicherosGenericos().add(d);
			}
			if(this.directorioSeleccionado.getFicherosGenericos().get(i) instanceof Archivo) {
				Archivo a = (Archivo)this.directorioSeleccionado.getFicherosGenericos().get(i);
				if(a.getTamFicheroBytes() >= this.tamFiltro) {
					for(int j = 0; j < this.extensionesFiltros.size(); j++) {
						if(a.getExtension().equals(this.extensionesFiltros.get(j))) coincide = true;
					}
					if(!coincide) this.directorioFiltrado.getFicherosGenericos().add(a);
				}
			}
			coincide = false;
		}
	}
	
	public void eliminarFiltrosExtension() {
		this.extensionesFiltros.clear();
		this.setExtensionFiltro(null);
	}
	/**
	 * Método que calcula el tamaño que ocupa un directorio según los archivos y subdirectorios que contenga
	 * @return el tamaño en Bytes que ocupa el directorio
	 */
	public long tamRoot() {
		long aux = 0;
		for(int i = 0; i < this.root.getFicherosGenericos().size(); i++) {
			if(this.root.getFicherosGenericos().get(i) instanceof Directorio) {
				Directorio d = (Directorio) this.root.getFicherosGenericos().get(i);
				aux += d.getTamFicheroBytes();
			}
			else if(this.root.getFicherosGenericos().get(i) instanceof Archivo) {
				Archivo a = (Archivo) this.root.getFicherosGenericos().get(i);
				aux += a.getTamFicheroBytes();
			}
		}
		return aux;
	}
	
	/**
	 * Método para conocer el número de ficheros que contiene un directorio
	 * @param directorio Directorio a estudiar
	 * @return el número total de ficheros que contiene el directorio
	 * http://www.espaciolinux.com/foros/programacion/como-saber-los-archivos-de-una-carpeta-en-java-t39520.html 
	 */
	public int totalFicheros(File directorio){
		  int total = 0;
		  String[] arrArchivos = directorio.list();
		  total += arrArchivos.length;
		  File tmpFile;
		  for(int i=0; i<arrArchivos.length; ++i){
		    tmpFile = new File(directorio.getPath() + "/" +arrArchivos[i]);
		    if(tmpFile.isDirectory()){
		      total += totalFicheros(tmpFile);
		    }
		  }
		  return total;
		}
	
	/**
	 * Método que calcula el número total de archivos (sin carpetas)
	 * @param directorio Directorio a estudiar
	 * @return el número total de archivos del directorio
	 */
	public int totalArchivos(File directorio) {
		int total = 0;
		File[] arrArchivos = directorio.listFiles();
		for(int i = 0; i < arrArchivos.length; i++) {
			if(arrArchivos[i].isFile()) total++;
			else if(arrArchivos[i].isDirectory()) total+= totalArchivos(arrArchivos[i]);
		}
		return total;
	}
	
	/**
	 * Método que calcula el porcentaje que ocupa cada fichero dentro de un directorio dado
	 * @param directorio Directorio a estudiar
	 */
	public void setPorcentajeAFicherosDeDirectorio(Directorio directorio) {
		double divisor = (double)directorio.getTamFicheroBytes();
		for(int i = 0; i < directorio.getFicherosGenericos().size(); i++) {
			if(directorio.getFicherosGenericos().get(i) instanceof Archivo) {
				Archivo a = (Archivo) directorio.getFicherosGenericos().get(i);
				double dividendo = a.getTamFicheroBytes();
				double calculo = (dividendo / divisor) * 100;
				a.setPorcentajeOcupado(calculo);
			}
			else if(directorio.getFicherosGenericos().get(i) instanceof Directorio) {
				Directorio d = (Directorio) directorio.getFicherosGenericos().get(i);
				double dividendo = d.getTamFicheroBytes();
				double calculo = (dividendo / divisor) * 100;
				d.setPorcentajeOcupado(calculo);
			}
		}
	}
	
	/**
	 * Cambia el directorio seleccionado para modificar el contenido de la primera tabla
	 * @param d Directorio que el usuario selecciona desde el árbol
	 */
	public void setDirectorioSeleccionado(Directorio d) {
		this.directorioSeleccionado = d;
		this.setPorcentajeAFicherosDeDirectorio(d);
		this.setTamFiltrado(tamFiltro);
		this.setExtensionFiltro(null);
	}
	
	/**
	 * Método que devuelve un modelo de datos
	 * @return Devuelve el modelo de datos
	 */
	public MyDataModel2 getDataModel2() {
		return this.mdm2;
	}
	
	//METODOS IMPLEMENTADOS PARA EL ARBOL
	@Override
	public Object getRoot() {
		return root;
	}

	@Override	
	public Object getChild(Object parent, int index) {
		if(parent instanceof Directorio) {
			Directorio d = (Directorio) parent;
			if(index > numFicheros) {
				index =- numFicheros;
				return d.getFicherosGenericos().get(index);
			}
			else {
				return d.getFicherosGenericos().get(index);
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		int numHijos = 0;
		if(parent instanceof Directorio) {
			Directorio d = (Directorio) parent;
			for(int i = 0; i < d.getFicherosGenericos().size(); i++) {
				if(d.getFicherosGenericos().get(i) instanceof Directorio) numHijos++;
			}
			return numHijos;
		}
		return numHijos;
	}

	@Override
	public boolean isLeaf(Object node) {
		boolean leaf = true;
		Directorio d = (Directorio) node;
		for(int i = 0; i < d.getFicherosGenericos().size(); i++) {
			if(d.getFicherosGenericos().get(i) instanceof Directorio) return !leaf;
		}
		return leaf;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Directorio d = (Directorio) parent;
		return d.getFicherosGenericos().indexOf(child);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(l);		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(l);		
	}
	
	protected void fireTreeStructureChanged() {
		TreeModelEvent event = new TreeModelEvent(this, (TreePath)null);
		for(TreeModelListener l : listenerList)
			l.treeStructureChanged(event);
	}
	
	
	//METODOS IMPLEMENTADOS PARA LA TABLA
	@Override
	public int getRowCount() {
		return directorioFiltrado.getFicherosGenericos().size();
	}

	@Override
	public int getColumnCount() {
		return columnasTabla.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0) {
			return this.directorioFiltrado.getFicherosGenericos().get(rowIndex).getNombre();
		}
		else if (columnIndex == 1) {
			if(this.directorioFiltrado.getFicherosGenericos().get(rowIndex) instanceof Archivo) 
				return ((Archivo) this.directorioFiltrado.getFicherosGenericos().get(rowIndex)).getTamArchivo();
			return ((Directorio) this.directorioFiltrado.getFicherosGenericos().get(rowIndex)).getTamDirectorio();
		}
		else if (columnIndex == 2) {
			if(this.directorioFiltrado.getFicherosGenericos().get(rowIndex) instanceof Directorio)
				return ((Directorio) this.directorioFiltrado.getFicherosGenericos().get(rowIndex)).getNumFicheros();
		}
		else if(columnIndex == 3) {
			if(this.directorioFiltrado.getFicherosGenericos().get(rowIndex) instanceof Directorio)
				return ((Directorio) this.directorioFiltrado.getFicherosGenericos().get(rowIndex)).getNumArchivos();
		}
		else if(columnIndex == 4) {
			if(this.directorioFiltrado.getFicherosGenericos().get(rowIndex) instanceof Directorio)
				return ((Directorio) this.directorioFiltrado.getFicherosGenericos().get(rowIndex)).getNumCarpetas();
		}
		else if(columnIndex == 5)
			return this.directorioFiltrado.getFicherosGenericos().get(rowIndex).getPorcentajeLegible();
		else if(columnIndex == 6)
			return this.directorioFiltrado.getFicherosGenericos().get(rowIndex).getPorcentaje();
		return 0;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 0 || columnIndex == 5) return String.class;
		else if(columnIndex == 1) return long.class;
		return int.class;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnasTabla[columnIndex];
	}
}
