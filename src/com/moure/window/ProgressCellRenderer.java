package com.moure.window;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Esta clase sirve como renderizador para las dos tablas a la hora de mostrar el porcentaje ocupado en forma de JProgressBar
 *
 */
public class ProgressCellRenderer implements TableCellRenderer{

	/**
	 * Barra que muestra el espacio ocupado sobre el total
	 */
	JProgressBar pBar;
	
	/**
	 * Constructor sin parámetros de la clase
	 */
	public ProgressCellRenderer() {
		pBar = new JProgressBar();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		pBar.setValue((int)value);
		pBar.isStringPainted();
		return pBar;
	}
}
