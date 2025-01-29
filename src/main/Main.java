package main;
import java.io.*;

import utilidades.*;
import clases.*;

public class Main {

	public static void main(String[] args) {
		File liga=new File("liga.dat");
		File equipo=new File("equipo.dat");
		int menu=menu();
		
		if (!liga.exists()) {
			fillDataLiga(liga);
		}
		if (!equipo.exists()) {
			fillDataEquipo(equipo);
		}
		
		do {
			switch (menu) {

			case 1:
				añadirEntr(equipo,liga);
				break;

			case 2:

				break;

			case 3:

				break;

			case 4:
				borrarIntr(equipo);
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

	public static void añadirEntr(File equipo, File liga) {
		MyObjectOutputStream moos;
		ObjectInputStream ois;
		String nom;
		int codE, codL;
		
		
	}

	public static void modifEquipoNom() {
		
	}

	public static void mostrIntr() {
		
	}

	public static void borrarIntr(File equipo) {
		File aux=new File("aux.dat");
		ObjectInputStream ois=null;
		ObjectOutputStream oos=null;
		String nombreEquipo;
		boolean existe=false, finArchivo=false;

		System.out.println("Introduce el equipo al que pertenece el integrante: ");
		nombreEquipo=Utilidades.introducirCadena();

		try {
			ois=new ObjectInputStream(new FileInputStream(equipo));
			oos=new ObjectOutputStream(new FileOutputStream(aux));
			while(!finArchivo) {
				try {
					Equipo eq1=(Equipo) ois.readObject();
					if (eq1.getNomE().equalsIgnoreCase(nombreEquipo)) {
						eq1=entrJug(eq1);
						existe=true;
					}
					oos.writeObject(eq1);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (EOFException e) {
					finArchivo=true;
				}
			}
			ois.close();
			oos.close();
			if (existe) {
				if (equipo.delete()) {
					aux.renameTo(equipo);
				}
			} else {
				aux.delete();
				System.out.println("No se ha encontrado ningun equipo con ese nombre.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\nVolviendo al menu...\n");
	}

	public static Equipo entrJug(Equipo eq1) {
		Equipo equipo=null;
		String tipo;
		int codigo;
		boolean existe=false;

		System.out.println("¿Quieres eliminar un entrenador o un jugador?");
		tipo=Utilidades.introducirCadena("entrenador", "jugador");
		if (tipo.equalsIgnoreCase("entrenador")) {
			for (Integrante integr:eq1.getListIntegrante()) {
				if (integr instanceof Entrenador) {
					System.out.println(integr.toString());
				}
			}
			do {
				System.out.println("Introduzca el codigo del entrenador que quieres borrar:");
				codigo=Utilidades.leerInt();

				for (int i=0; i<eq1.getListIntegrante().size(); i++) {
					if (eq1.getListIntegrante().get(i) instanceof Entrenador && ((Entrenador) eq1.getListIntegrante().get(i)).getCodEntr()==codigo) {
						eq1.getListIntegrante().remove(i);
						equipo=eq1;
						existe=true;
					}
				}
				if (!existe) {
					System.out.println("No existe ningun entrenador con ese codigo!");
				}
			} while (!existe);
			System.out.println("Entrenador borrado con exito!");
		} else {
			for (Integrante integr:eq1.getListIntegrante()) {
				if (integr instanceof Jugador) {
					System.out.println(integr.toString());
				}
			}
			do {
				System.out.println("Introduzca el codigo del jugador que quieres borrar:");
				codigo=Utilidades.leerInt();
				for (int i=0; i<eq1.getListIntegrante().size(); i++) {
					if (eq1.getListIntegrante().get(i) instanceof Jugador && ((Jugador) eq1.getListIntegrante().get(i)).getCodJ()==codigo) {
						eq1.getListIntegrante().remove(i);
						equipo=eq1;
						existe=true;
					}
				}
				if (!existe) {
					System.out.println("No existe ningun jugador con ese codigo!");
				}
			} while (!existe);
			System.out.println("Jugador borrado con exito!");
		}
		return equipo;
	}
}
