package main;
import java.io.*;

import utilidades.*;
import clases.*;

public class Main {

	public static void main(String[] args) {
		File liga=new File("liga.dat");
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

	public static void fillDataLiga(File liga) {
		ObjectOutputStream oos;
		Liga l1=new Liga(1, "LaLiga");
		Liga l2=new Liga(2, "Premier League");
		Liga l3=new Liga(3, "Bundesliga");

		try {
			oos=new ObjectOutputStream(new FileOutputStream(liga));
			oos.writeObject(l1);
			oos.writeObject(l2);
			oos.writeObject(l3);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fillDataEquipo(File equipo) {
		ObjectOutputStream oos;
		Equipo e1=new Equipo(1, "Sestao River", 1);
		Equipo e2=new Equipo(2, "Barcelona", 1);
		Equipo e3=new Equipo(1, "Chelsea", 2);
		Equipo e4=new Equipo(1, "Huesca", 3);
		
		Entrenador entr1=new Entrenador("Pablo", "España", 1, TipoEntr.PRINCIPAL);
		Entrenador entr2=new Entrenador("Juan", "España", 2, TipoEntr.TECNICO);
		Entrenador entr3=new Entrenador("Pepe", "Brasil", 1, TipoEntr.PRINCIPAL);
		Entrenador entr4=new Entrenador("Charlie", "China", 1, TipoEntr.PRINCIPAL);
		Entrenador entr5=new Entrenador("Henry", "Australia", 1, TipoEntr.PRINCIPAL);
		
		Jugador j1=new Jugador("Carlos", "España", 1, false, 58);
		Jugador j2=new Jugador("Kevin", "España", 1, true, 3);
		Jugador j3=new Jugador("Mohammed", "Arabia Saudi", 1, false, 27);
		Jugador j4=new Jugador("Aritz", "Alemania", 1, false, 69);

		e1.getListIntegrante().add(entr1);
		e1.getListIntegrante().add(entr2);
		e1.getListIntegrante().add(j1);
		
		e2.getListIntegrante().add(entr3);
		e2.getListIntegrante().add(j2);
		
		e3.getListIntegrante().add(entr4);
		e3.getListIntegrante().add(j3);
		
		e4.getListIntegrante().add(entr5);
		e4.getListIntegrante().add(j4);

		try {
			oos=new ObjectOutputStream(new FileOutputStream(equipo));
			oos.writeObject(e1);
			oos.writeObject(e2);
			oos.writeObject(e3);
			oos.writeObject(e4);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void añadirEntr() {

	}

	public static void modifEquipoNom() {

	}

	public static void mostrIntr() {

	}

	public static void borrarIntr() {

	}
}
