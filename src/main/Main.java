package main;
import java.io.*;

import utilidades.*;
import clases.*;

public class Main {

	public static void main(String[] args) {
		File intr=new File("integrantes.dat");
		File equipo=new File("equipo.dat");
		int menu=menu();
		
		do {
			switch (menu) {
			
			case 1:
				
				break;
				
			case 2:
				
				break;
				
			case 3:
				
				break;
				
			case 4:
				
				break;
			
			}
		} while (menu!=0);
	}

	public static int menu() {
		System.out.println("0. Salir");
		System.out.println("1. Añadir entrenador");
		System.out.println("2. Cambiar nombre de un equipo");
		System.out.println("3. Mostrar integrantes");
		System.out.println("4. Borrar un integrante");
		//Pueden haber más opciones
		return Utilidades.leerInt(0,4);
	}
	
	public static void addEntr() {
		
	}
	
	public static void modifEquipoName() {
		
	}
	
	public static void showIntr() {
		
	}
	
	public static void deleteIntr() {
		
	}
}
