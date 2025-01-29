package main;
import java.io.*;

import utilidades.*;
import clases.*;

public class Main {

	public static void main(String[] args) {
		File liga=new File("liga.dat");
		File equipo=new File("equipo.dat");
		int menu;

		if (!liga.exists()) {
			fillDataLiga(liga);
		}
		if (!equipo.exists()) {
			fillDataEquipo(equipo);
		}
		do {
			menu=menu();
			switch (menu) {

			case 1:
				añadirEntr(equipo);
				break;

			case 2:
				modifEquipoNom(equipo);
				break;

			case 3:
				visualizar(liga, equipo);
				break;

			case 4:
				borrarIntr(equipo);
				break;

			case 0:
				System.out.println("Hasta luego!");
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

	public static void añadirEntr(File equipo) {
		File aux=new File("aux.dat");
		ObjectInputStream ois;
		ObjectOutputStream oos;
		String nom, pais, setTipo;
		int codEntr, codE;
		boolean error=false, end=false, found=false;
		TipoEntr tipo=null;

		System.out.println("Introduce el nombre del entrenador:");
		nom=Utilidades.introducirCadena();
		System.out.println("Introduce el pais:");
		pais=Utilidades.introducirCadena();
		//placeholder introducir
		System.out.println("Introduce el codigo del entrenador:");
		codEntr=Utilidades.leerInt();
		do {
			error=false;
			try {
				System.out.println("¿El entrenador es Principal, Tecnico, o de Fisio?");
				setTipo=Utilidades.introducirCadena().toUpperCase();
				switch (setTipo) {
				case "PRINCIPAL":
					tipo=TipoEntr.PRINCIPAL;
					break;

				case "TECNICO":
					tipo=TipoEntr.TECNICO;
					break;

				case "FISIO":
					tipo=TipoEntr.FISIO;
					break;

				default:
					throw new IllegalArgumentException("El tipo introducido es invalido.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				error=true;
			}
		} while (error);
		Entrenador entr=new Entrenador(nom, pais, codEntr, tipo);
		do {
			end=false; found=false;
			System.out.println("Introduce el codigo del equipo que quieras añadir el nuevo entrenador:");
			codE=Utilidades.leerInt();
			try {
				ois=new ObjectInputStream(new FileInputStream(equipo));
				oos=new ObjectOutputStream(new FileOutputStream(aux));
				while (!end||!found) {
					try {
						Equipo en=(Equipo)ois.readObject();
						if (en.getCodE()==codE) {
							found=true;
							en.getListIntegrante().add(entr);
						}
						oos.writeObject(entr);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (EOFException e) {
						end=true;
					}
				}
				oos.close();
				ois.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!found) {
				System.out.println("No se ha encontrado el equipo. Introducelo de nuevo.");
			}
		} while (!found);
		if (equipo.delete()) {
			aux.renameTo(equipo);
		}
	}


	public static void modifEquipoNom(File equipo) {
		String nom, nuevoN;
		boolean modificado=false, fin=false;
		File fichAux=new File("fichAux.dat");

		System.out.println("Introduce el nombre que quieres cambiar");
		nom=Utilidades.introducirCadena();

		if (equipo.exists()) {
			try {
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream(equipo));
				ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fichAux));
				while (!fin) {
					try {
						Equipo aux=(Equipo)ois.readObject();
						if (aux.getNomE().equalsIgnoreCase(nom)) {
							System.out.println(aux.toString());
							System.out.println("Cual es el nuevo Nombre?");
							nuevoN = Utilidades.introducirCadena();
							aux.setNomE(nuevoN);
							modificado=true;
						}
						oos.writeObject(aux);
					} catch (EOFException e) {
						fin=true;
					}
				}
				oos.close();
				ois.close();
				if (modificado) {
					System.out.println("Archivo modificado");
					if (equipo.delete()) {
						fichAux.renameTo(equipo);
					}
				} else {
					System.out.println("No hay una escuderia con ese nombre");
					fichAux.delete();
				}

			} catch (Exception e) {
				System.out.println("Fatal error");
			}
		} else {
			System.out.println("Fichero nuevo");
		}
	}

	public static void visualizar(File liga, File equipo) {
		boolean finArchivo=false;
		ObjectInputStream ois=null;

		try {	
			ois=new ObjectInputStream(new FileInputStream(liga));
			System.out.println("LIGAS");
			while (!finArchivo) {
				try {
					Liga aux=(Liga) ois.readObject();
					System.out.println(aux.toString());				 
				} catch (EOFException e) {
					// Fin del archivo alcanzado
					finArchivo=true;
				}
			}
			ois.close();
		}catch(Exception e) {
			System.out.println("Fatal error");
		}

		finArchivo=false;

		try {	
			ois=new ObjectInputStream(new FileInputStream(equipo));
			System.out.println("EQUIPOS:");
			while (!finArchivo) {
				try{
					Equipo aux=(Equipo) ois.readObject();
					System.out.println(aux.toString());				 
				} catch (EOFException e) {
					// Fin del archivo alcanzado
					finArchivo=true;
				}
			}
			ois.close();
		} catch(Exception e) {
			System.out.println("Fatal error");
		}
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
				System.out.println("TODO BIEN");
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
					integr.visualizar();
					System.out.println("");
				}
			}
			do {
				System.out.println("Introduzca el codigo del entrenador que quieres borrar:");
				codigo=Utilidades.leerInt();

				for (Integrante i:eq1.getListIntegrante()) {
					if (i instanceof Entrenador) {
						if (((Entrenador) i).getCodEntr()==codigo) {
							eq1.getListIntegrante().remove(i);
							equipo=eq1;
							existe=true;
						}
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
					integr.visualizar();
					System.out.println("");
				}
			}
			do {
				System.out.println("Introduzca el codigo del jugador que quieres borrar:");
				codigo=Utilidades.leerInt();

				for (Integrante i:eq1.getListIntegrante()) {
					if (i instanceof Jugador) {
						if (((Jugador) i).getCodJ()==codigo) {
							eq1.getListIntegrante().remove(i);
							equipo=eq1;
							existe=true;
						}
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