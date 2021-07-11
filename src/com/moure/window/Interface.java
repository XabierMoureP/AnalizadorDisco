package com.moure.window;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import com.moure.data.Directorio;
import com.moure.data.MyDataModel;

/**
 * Esta clase es la principal y se encarga de mostrar los diálogos necesarios y de cambiar el contenido según lo que el
 * usuario hace
 */
public class Interface {

	/**
	 * Dialogos de la interfaz
	 */
	private JDialog initialDialog, dialogoRaices, filtroTam, filtroExtension;
	
	/**
	 * Paneles utilizados en los dialogos
	 */
	private JPanel panelForDialog;
	
	/**
	 * Formato de los elementos que se añaden en los dialogos
	 */
	private GridBagConstraints c;
	
	/**
	 * Botones
	 */
	private JButton abrirRaices, buttonChooseFile, aplicarFiltroTam, aplicarFiltroExt;
	
	/**
	 * Grupo de botones para seleccionar unicamente uno
	 */
	private ButtonGroup group;
	
	/**
	 * Botones pertenecientes al ButtonGroup
	 */
	private JRadioButton mbButton, gbButton;
	
	/**
	 * Labels que dan información al usuario
	 */
	private JLabel infoSelect, infoFiltro1, infoRoots;
	
	/**
	 * Áreas donde el usuario escribe el filtro
	 */
	private JTextField tamFichero, extensionFichero;
	
	/**
	 * JFrame que muestra el árbol y las dos tablas
	 */
	private JFrame finalFrame;
	
	/**
	 * Path elegido por el usuario inicialmente
	 */
	private Path pathAAnalizar;
	
	/**
	 * Modelo de datos creado a partir del path elegido por el usuario
	 */
	private MyDataModel mdm;
	
	/**
	 * Tablas que muestran información sobre el directorio elegido por el usuario
	 */
	private JTable table1, table2;
	
	/**
	 * Árbol que muestra las carpetas del directorio seleccionado inicialmente por el usuario
	 */
	private JTree tree;
	
	/**
	 * Lista de raíces del ordenador
	 */
	private File roots[];
	
	/**
	 * Icono utilizado para las carpetas en el árbol
	 */
	private ImageIcon icon;
	
	/**
	 * Extensión que el usuario quiere filtrar
	 */
	private String extensionFiltro;
	
	/**
	 * Tamaño a partir del cual se muestran arhivos y carpetas
	 */
	private long tamArchivoFiltro;
	
	/**
	 * Constructor de la clase Interface. No recibe parámetros.
	 */
	public Interface() {
		roots = File.listRoots();
		c = new GridBagConstraints();
		this.icon = new ImageIcon("icono2.png");
		generateInitialDialog();
	}
	
	/**
	 * Genera el diálogo inicial desde el que se puede seleccionar un directorio para ser analizado
	 */
	public void generateInitialDialog() {
		initialDialog = new JDialog(null, "Welcome", null);
		initialDialog.setLayout(new GridBagLayout());
		infoRoots = new JLabel("Se han detectado "+roots.length+" raices");
		
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 20);
		c.anchor = GridBagConstraints.PAGE_START;
		initialDialog.add(infoRoots, c);
		
		abrirRaices = new JButton("Abrir");
		abrirRaices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generarDialogoRaices();
				initialDialog.setVisible(false);
			}
		});
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 0;
		c.gridx = 1;
		c.ipadx = 20;
		c.anchor = GridBagConstraints.PAGE_END;
		initialDialog.add(abrirRaices, c);
		
		infoSelect = new JLabel("Elegir otro directorio");
		c.gridy = 1;
		c.gridx = 0;
		c.ipadx = 0;
		c.anchor = GridBagConstraints.PAGE_START;
		initialDialog.add(infoSelect, c);
		
		buttonChooseFile = new JButton("Abrir");
		buttonChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int opcionElegida = jfc.showOpenDialog(initialDialog);
				
				if(opcionElegida == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
					pathAAnalizar = f.toPath();
					mdm = createDataModel();
					generateJFrame();
					generateJMenuBar();
					initialDialog.setVisible(false);
					finalFrame.setVisible(true);
				}
			}
		});
		c.gridx = 1;
		c.gridy = 1;
		c.ipadx = 20;
		c.anchor = GridBagConstraints.PAGE_END;

		initialDialog.add(buttonChooseFile, c);
		initialDialog.pack();
		initialDialog.setVisible(true);
	}
	
	/**
	 * Genera un diálogo con una lista con las raíces del ordenador
	 * @see #listAndAddRootsToJSP()
	 */
	public void generarDialogoRaices() {
		dialogoRaices = new JDialog(null, "Elegir raiz", null);
		panelForDialog = new JPanel();
		panelForDialog.setLayout(new GridBagLayout());
		JScrollPane jsp = new JScrollPane(panelForDialog);
		this.listAndAddRootsToJSP();
		dialogoRaices.add(jsp);
		dialogoRaices.pack();
		dialogoRaices.setVisible(true);
	}
	
	/**
	 * Añade al diálogo que muestra las raíces una lista de botones para seleccionar las diferentes raíces del ordenador
	 */
	public void listAndAddRootsToJSP() {
		int x = 0;
		int y = 0;
		for(int i = 0; i < roots.length; i++) {
			int posicion = i;
			JLabel label = new JLabel("Elegir raiz " +roots[i].toString());
			c.gridx = x;
			c.gridy = y;
			c.anchor = GridBagConstraints.PAGE_START;
			c.insets = new Insets(0, 0, 0, 20);
			c.ipadx = 0;
			panelForDialog.add(label, c);
			
			JButton button = new JButton(roots[i].toString());
			button.setPreferredSize(new Dimension(40, 40));
			button.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					pathAAnalizar = Paths.get(roots[posicion].toString());
					mdm = createDataModelForRoot(roots[posicion].toString());
					generateJFrame();
					generateJMenuBar();
					initialDialog.setVisible(false);
					dialogoRaices.setVisible(false);
					finalFrame.setVisible(true);
				}
			});
			c.gridx = x + 1;
			c.gridy = y;
			c.anchor = GridBagConstraints.PAGE_END;
			c.insets = new Insets(0, 0, 0, 0);
			c.ipadx = 20;
			panelForDialog.add(button, c);
			y++;
		}
	}
	
	/**
	 * Genera el JFrame en el que se muestran todos los componentes (árbol y las dos tablas)
	 */
	public void generateJFrame() {
		finalFrame = new JFrame();
		finalFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		generateJTable1();
		generateJTable2();
		generateJTree();
		generateJMenuBar();
	}
	
	/**
	 * Crea la tabla que muestra el contenido de la carpeta seleccionada en el árbol
	 */
	public void generateJTable1() {
		table1 = new JTable(mdm);
		table1.getColumn(table1.getColumnName(6)).setCellRenderer(new ProgressCellRenderer());
		JScrollPane jspTable = new JScrollPane(table1);
		jspTable.setPreferredSize(new Dimension(450, 0));
		finalFrame.add(jspTable, BorderLayout.CENTER);
	}
	
	/**
	 * Crea la tabla que muestra un resumen de los archivos, agrupados según su extensión
	 */
	public void generateJTable2() {
		table2 = new JTable(mdm.getDataModel2());
		table2.getColumn(table2.getColumnName(4)).setCellRenderer(new ProgressCellRenderer());
		JScrollPane jspTable2 = new JScrollPane(table2);
		jspTable2.setPreferredSize(new Dimension(450, 0));
		finalFrame.add(jspTable2, BorderLayout.EAST);
	}
	
	/**
	 * Crea el árbol que mostrará las carpetas del directorio seleccionado en el diálogo inicial. Solo muestra carpetas
	 */
	public void generateJTree() {
		tree = new JTree(mdm);
		DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) tree.getCellRenderer();
		render.setLeafIcon(icon);
		render.setOpenIcon(icon);
		render.setClosedIcon(icon);
		
		JScrollPane jspTree = new JScrollPane(tree);
		jspTree.setPreferredSize(new Dimension(300, 0));
		finalFrame.add(jspTree, BorderLayout.WEST);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
	            @Override
	            public void valueChanged(TreeSelectionEvent e) {                
	            	Object seleccion = tree.getLastSelectedPathComponent();
	            	if(seleccion instanceof Directorio) {
	            		Directorio d = (Directorio) seleccion;
	            		mdm.setDirectorioSeleccionado(d);
	            		mdm.fireTableDataChanged();
	            	}
	            }
	        });
	}

	/**
	 * Getter de {@link #pathAAnalizar}
	 * @return el path a analizar
	 */
	public Path getPathAAnalizar() {
		return pathAAnalizar;
	}
	
	/**
	 * Método para crear un modelo de datos a partir de un directorio con el que se pueda implementar el árbol y la primera tabla
	 * @return El modelo de datos creado (dataModel)
	 */
	public MyDataModel createDataModel() {
		Directorio directorio = new Directorio(pathAAnalizar.getFileName().toString(), pathAAnalizar);
		MyDataModel dataModel = new MyDataModel(pathAAnalizar, directorio);
		return dataModel;
	}
	
	/**
	 * Método para crear un modelo de datos a partir de una raíz con el que se pueda implementar el árbol y la primera tabla
	 * @param name El nombre que recibe la raíz a partir del cual se crea una instancia de la clase Directorio
	 * @return El modelo de datos creado (dataModel)
	 */
	public MyDataModel createDataModelForRoot(String name) {
		Directorio directorio = new Directorio(name, pathAAnalizar);
		MyDataModel dataModel = new MyDataModel(pathAAnalizar, directorio);
		return dataModel;
	}
	
	/**
	 * Genera un JMenuBar para las distintas opciones que ofrece el programa, tales como aplicar filtros
	 */
	public void generateJMenuBar() {
		JMenuBar jmb = new JMenuBar();
		
		JMenu menuOpciones = new JMenu("Opciones");
		JMenuItem itemNuevoAnalisis = new JMenuItem("Nuevo analisis");
		itemNuevoAnalisis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
 				finalFrame.dispose();
				generateInitialDialog();
			}
		});
		menuOpciones.add(itemNuevoAnalisis);
		
		JMenuItem itemSalir = new JMenuItem("Salir");
		itemSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuOpciones.add(itemSalir);
		jmb.add(menuOpciones);
		
		JMenu menuFiltros = new JMenu("Filtros");
		JMenu menuSeleccionarFiltro = new JMenu("Seleccionar filtro");
		JMenuItem itemResetearFiltros = new JMenuItem("Resetear filtros");
		itemResetearFiltros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mdm.setTamFiltrado(0);
				mdm.eliminarFiltrosExtension();
				mdm.fireTableDataChanged();
				mdm.getDataModel2().eliminarTodosFiltros();
				mdm.getDataModel2().fireTableDataChanged();
			}
		});
		
		JMenu menuFiltroTam = new JMenu("Filtrar por tamanyo");
		JMenuItem itemElegirFiltroTam = new JMenuItem("Elegir filtro");
		itemElegirFiltroTam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generarDialogoFiltroTamanyo();
			}
		});
		menuFiltroTam.add(itemElegirFiltroTam);
		menuSeleccionarFiltro.add(menuFiltroTam);
		
		JMenuItem itemEliminarFiltroTam = new JMenuItem("Eliminar filtro");
		itemEliminarFiltroTam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tamArchivoFiltro = 0;
				mdm.setTamFiltrado(tamArchivoFiltro);
				mdm.fireTableDataChanged();
			}
		});
		menuFiltroTam.add(itemEliminarFiltroTam);
		
		JMenu menuFiltroExt = new JMenu("Filtrar por extension");
		JMenuItem itemElegirFiltroExt = new JMenuItem("Elegir filtro");
		itemElegirFiltroExt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generarDialogoFiltroExtension();
			}
		});
		menuFiltroExt.add(itemElegirFiltroExt);
		
		JMenuItem itemEliminarFiltroExt = new JMenuItem("Eliminar Filtro");
		itemEliminarFiltroExt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extensionFiltro = null;
				mdm.eliminarFiltrosExtension();
				mdm.fireTableDataChanged();
				mdm.getDataModel2().eliminarTodosFiltros();
				mdm.getDataModel2().fireTableDataChanged();
			}
		});
		menuFiltroExt.add(itemEliminarFiltroExt);
		menuSeleccionarFiltro.add(menuFiltroExt);
		
		menuFiltros.add(menuSeleccionarFiltro);
		menuFiltros.add(itemResetearFiltros);
		jmb.add(menuFiltros);
		finalFrame.add(jmb, BorderLayout.NORTH);
	}
	
	/**
	 * Genera el diálogo mostrado cuando se quiere filtrar por tamaño
	 */
	public void generarDialogoFiltroTamanyo() {
		filtroTam = new JDialog(null, "Filtrar por tamanyo", null);
		filtroTam.setLayout(new FlowLayout());
		
		tamFichero = new JTextField();
		tamFichero.setPreferredSize(new Dimension(50, 25));
		tamFichero.setToolTipText("1");
		filtroTam.add(tamFichero);
		
		generateJButtonGroup();
		filtroTam.add(mbButton);
		filtroTam.add(gbButton);
		
		aplicarFiltroTam = new JButton("Aplicar filtro");
		aplicarFiltroTam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mdm.setTamFiltrado(tamArchivoFiltro);
				mdm.fireTableDataChanged();
				filtroTam.setVisible(false);
			}
		});
		filtroTam.add(aplicarFiltroTam);
		
		filtroTam.pack();
		filtroTam.setVisible(true);
	}
	
	/**
	 * Crea un JButtonGroup para que el usuario solo pueda elegir pulsar uno de los botones: MB ó GB
	 */
	public void generateJButtonGroup() {
		group = new ButtonGroup();
		mbButton = new JRadioButton("MB", false);
		gbButton = new JRadioButton("GB", false);
		
		mbButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tamArchivoFiltro = 0;
				tamArchivoFiltro = Integer.parseInt(tamFichero.getText()) * 1024 * 1024;
			}
		});
		gbButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tamArchivoFiltro = 0;
				tamArchivoFiltro = Integer.parseInt(tamFichero.getText()) * 1024 * 1024 * 1024;
			}
		});
		
		group.add(mbButton);
		group.add(gbButton);
	}

	/**
	 * Crea el diálogo que se muestra cuando el usuario quiere filtrar los archivos por extensión
	 */
	public void generarDialogoFiltroExtension() {
		filtroExtension = new JDialog(null, "Filtrar por extension", null);
		filtroExtension.setLayout(new FlowLayout());
		
		infoFiltro1 = new JLabel("Introduce la extension: ");
		filtroExtension.add(infoFiltro1);
		
		extensionFichero = new JTextField();
		extensionFichero.setPreferredSize(new Dimension(100, 25));
		filtroExtension.add(extensionFichero);
		
		aplicarFiltroExt = new JButton("Aplicar filtro");
		aplicarFiltroExt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extensionFiltro = "."+extensionFichero.getText();
				mdm.setExtensionFiltro(extensionFiltro);
				mdm.fireTableDataChanged();
				mdm.getDataModel2().addFiltroPorExtension(extensionFiltro);
				mdm.getDataModel2().fireTableDataChanged();
				filtroExtension.setVisible(false);
			}
		});
		
		filtroExtension.add(aplicarFiltroExt);
		
		filtroExtension.pack();
		filtroExtension.setVisible(true);
	}
}
