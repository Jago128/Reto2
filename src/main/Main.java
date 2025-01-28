package main;
import java.io.*;

import utilidades.*;
import clases.*;

public class Main {

	public static void main(String[] args) {
		File intr=new File("integrantes.dat");
		File equipo=new File("equipo.dat");
		int menu=menu();

		switch (menu) {

		}
	}

	public static int menu() {
		System.out.println("0. Salir");
		System.out.println("1. Añadir entrenador");
		System.out.println("2. Cambiar nombre de un equipo");
		System.out.println("3. Mostrar integrantes");
		System.out.println("4. Borrar un integrante");
		//more to be added
		return Utilidades.leerInt(0,5);
	}
}
