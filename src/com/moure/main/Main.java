package com.moure.main;
import java.awt.EventQueue;
import com.moure.window.Interface;

/**
 * Desde la clase Main se crea una instancia de Interface que da comienzo al programa
 *
 */
public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Interface();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
