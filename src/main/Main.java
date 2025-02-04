package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import utilidades.*;
import clases.*;
import exceptions.ExcepcionEntrenador;

public class Main {

	public static void main(String[] args) {
		File liga = new File("liga.dat");
		File equipo = new File("equipo.dat");
		int menu;

		if (!liga.exists()) {
			fillDataLiga(liga);
		}
		if (!equipo.exists()) {
			fillDataEquipo(equipo);
		}
		do {
			menu = menu();
			switch (menu) {

			case 1:
				añadirEntr(equipo, liga);
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

			case 5:
				eliminarEquipo(liga, equipo);
				break;

			case 6:
				añadirEquipo(liga, equipo);
				break;

			case 7:
				ordenarGoleadoresPorGoles(equipo);
				break;

			case 0:
				System.out.println("Hasta luego!");
				break;
			}
		} while (menu != 0);
	}

	public static int menu() {
		System.out.println("\n0. Salir");
		System.out.println("1. Añadir entrenador");
		System.out.println("2. Cambiar nombre de un equipo");
		System.out.println("3. Mostrar todos los datos");
		System.out.println("4. Borrar un integrante");
		System.out.println("5. Eliminar equipo");
		System.out.println("6. Añadir equipo");
		System.out.println("7. Mayores goleadores");
		// Pueden haber más opciones
		return Utilidades.leerInt(0, 7);
	}

	public static void fillDataLiga(File liga) {
		ObjectOutputStream oos;
		Liga l1 = new Liga(1, "LaLiga");
		Liga l2 = new Liga(2, "Premier League");
		Liga l3 = new Liga(3, "Bundesliga");

		try {
			oos = new ObjectOutputStream(new FileOutputStream(liga));
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
		Equipo e1 = new Equipo(1, "Sestao River", 1);
		Equipo e2 = new Equipo(2, "Barcelona", 1);
		Equipo e3 = new Equipo(3, "Chelsea", 2);
		Equipo e4 = new Equipo(4, "Huesca", 3);

		Entrenador entr1 = new Entrenador("Pablo", "España", 1, TipoEntr.PRINCIPAL);
		Entrenador entr2 = new Entrenador("Juan", "España", 2, TipoEntr.TECNICO);
		Entrenador entr3 = new Entrenador("Pepe", "Brasil", 1, TipoEntr.PRINCIPAL);
		Entrenador entr4 = new Entrenador("Charlie", "China", 1, TipoEntr.PRINCIPAL);
		Entrenador entr5 = new Entrenador("Henry", "Australia", 1, TipoEntr.PRINCIPAL);

		Jugador j1 = new Jugador("Carlos", "España", false, 58);
		Jugador j2 = new Jugador("Kevin", "España", true, 3);
		Jugador j3 = new Jugador("Mohammed", "Arabia Saudi", false, 27);
		Jugador j4 = new Jugador("Aritz", "Alemania", false, 69);

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
			oos = new ObjectOutputStream(new FileOutputStream(equipo));
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
		File aux = new File("fichAux.dat");
		ObjectInputStream ois;
		ObjectOutputStream oos;
		int codE, codL;
		boolean end = false, found = false, modificado = false, error = false;
		Entrenador entr;

		do {
			end = false;
			found = false;
			
			visualizar(liga, equipo);
			System.out.println("\nIntroduce el codigo de la liga del equipo:");
			codL = Utilidades.leerInt();
			System.out.println("Introduce el codigo del equipo que quieras añadir el nuevo entrenador:");
			codE = Utilidades.leerInt();
			try {
				validarEntrenadores(equipo, codE, error);

				entr = datosEntrenador(equipo, codL, codE);
				ois = new ObjectInputStream(new FileInputStream(equipo));
				oos = new ObjectOutputStream(new FileOutputStream(aux));
				while (!end || !found) {

					try {
						Equipo en = (Equipo) ois.readObject();
						if (en.getCodE() == codE) {
							found = true;
							en.getListIntegrante().add(entr);
							modificado = true;
						}
						oos.writeObject(en);
						
					} catch (ClassNotFoundException e) {

						e.printStackTrace();
					} catch (EOFException e) {
						end = true;
					}
				}
				oos.close();
				ois.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ExcepcionEntrenador e) {
				error = true;
				e.getMessage();
			}
			if (!found) {

				System.out.println("No se ha encontrado el equipo. Introducelo de nuevo.");
			}
		} while (!found);

		if (modificado) {
			if (equipo.delete()) {
				aux.renameTo(equipo);
				System.out.println("Entrenador creado correctamente");
			}
		}

	}

	public static void validarEntrenadores(File equipo, int codE, boolean error) throws ExcepcionEntrenador {
		int contadorEntrenadores = 0;
		if (equipo.exists()) {

			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(equipo));
				boolean end = false;
				while (!end) {
					try {
						Equipo en = (Equipo) ois.readObject();
						if (en.getCodE() == codE) {
							error = true;
							// Contamos los entrenadores de este equipo
							for (Integrante integrante : en.getListIntegrante()) {
								if (integrante instanceof Entrenador) {
									contadorEntrenadores++;
								}
							}
						}
					} catch (EOFException e) {
						end = true;
					}
				}
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Error al leer el archivo: " + e.getMessage());
			}

		}
		if (contadorEntrenadores >= 4) {
			throw new ExcepcionEntrenador("Error: No se pueden agregar más de 4 entrenadores al equipo.");
		}
	}

	public static Entrenador datosEntrenador(File equipo, int codL, int codE) {
		String nom, pais, setTipo;
		int codEntr;
		boolean error = false, esta = false;
		TipoEntr tipo = null;

		System.out.println("Introduce el nombre del entrenador:");
		nom = Utilidades.introducirCadena();
		System.out.println("Introduce el pais:");
		pais = Utilidades.introducirCadena();
		// placeholder introducir

		do {
			System.out.println("Introduce el codigo del entrenador:");
			codEntr = Utilidades.leerInt();

			esta = buscarEntrenador(equipo, codL, codEntr, codE);
			if (esta) {
				System.out.println("Ese codigo de entrenador ya existe, introduce otro");
			}
		} while (esta);

		do {
			error = false;
			try {
				System.out.println("¿El entrenador es Principal, Tecnico, o de Fisio?");
				setTipo = Utilidades.introducirCadena().toUpperCase();
				switch (setTipo) {
				case "PRINCIPAL":
					tipo = TipoEntr.PRINCIPAL;
					break;

				case "TECNICO":
					tipo = TipoEntr.TECNICO;
					break;

				case "FISIO":
					tipo = TipoEntr.FISIO;
					break;

				default:
					throw new IllegalArgumentException("El tipo introducido es invalido.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				error = true;
			}
		} while (error);
		Entrenador entr = new Entrenador(nom, pais, codEntr, tipo);

		return entr;
	}

	public static boolean buscarEntrenador(File equipo, int codL, int codEntr, int codE) {
		boolean finArchivo = false;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(equipo));

			while (!finArchivo) {
				try {
					Equipo aux = (Equipo) ois.readObject();
					if (aux.getCodE() == codE && aux.getCodL()==codL) {
						for (Integrante i : aux.getListIntegrante()) {
							if (i instanceof Entrenador) {
								if (((Entrenador) i).getCodEntr() == codEntr) {
									return true;
								}
							}
						}
					}
				} catch (EOFException e) {
					// Fin del archivo alcanzado
					finArchivo = true;
				}
			}
			ois.close();

		} catch (Exception e) {
			System.out.println("Fatal error");
		}

		return false;
	}

	public static void modifEquipoNom(File equipo) {
		String nom, nuevoN;
		boolean modificado = false, fin = false;
		File fichAux = new File("fichAux.dat");

		System.out.println("Introduce el nombre que quieres cambiar");
		nom = Utilidades.introducirCadena();

		if (equipo.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(equipo));
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichAux));
				while (!fin) {
					try {
						Equipo aux = (Equipo) ois.readObject();
						if (aux.getNomE().equalsIgnoreCase(nom)) {
							System.out.println(aux.toString());
							System.out.println("Cual es el nuevo Nombre?");
							nuevoN = Utilidades.introducirCadena();
							aux.setNomE(nuevoN);
							modificado = true;
						}
						oos.writeObject(aux);
					} catch (EOFException e) {
						fin = true;
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
					System.out.println("No hay una equipo con ese nombre");
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

		boolean finArchivo = false, existe;

		ObjectInputStream ois = null;

		ArrayList<Liga> listaLigas = new ArrayList<>();

		ArrayList<Equipo> listaEquipos = new ArrayList<>();

		try {

			ois = new ObjectInputStream(new FileInputStream(liga));

			while (!finArchivo) {

				try {

					Liga aux = (Liga) ois.readObject();

					listaLigas.add(aux);

				} catch (EOFException e) {

					// Fin del archivo alcanzado

					finArchivo = true;

				}

			}

			ois.close();

		} catch (Exception e) {

			System.out.println("Fatal error");

		}

		finArchivo = false;

		try {

			ois = new ObjectInputStream(new FileInputStream(equipo));

			while (!finArchivo) {

				try {

					Equipo aux = (Equipo) ois.readObject();

					listaEquipos.add(aux);

				} catch (EOFException e) {

					// Fin del archivo alcanzado

					finArchivo = true;

				}

			}

			ois.close();

		} catch (Exception e) {

			System.out.println("Fatal error");

		}

		for (Liga l : listaLigas) {

			existe = false;

			System.out.println("\n" + l.toString());

			for (Equipo eq : listaEquipos) {

				if (eq.getCodL() == l.getCodL()) {

					System.out.println(eq.toString());

					existe = true;

				}

			}

			if (!existe) { // PARA QUE APAREZCA ESTE MENSAJE

				System.out.println("\nNo existe ningun equipo en esta liga.");

			}

		}

	}

	public static void borrarIntr(File equipo) {

		File aux = new File("aux.dat");

		ObjectInputStream ois = null;

		ObjectOutputStream oos = null;

		String nombreEquipo;

		boolean existe = false, finArchivo = false;

		System.out.println("Introduce el equipo al que pertenece el integrante: ");

		nombreEquipo = Utilidades.introducirCadena();

		try {

			ois = new ObjectInputStream(new FileInputStream(equipo));

			oos = new ObjectOutputStream(new FileOutputStream(aux));

			while (!finArchivo) {

				try {

					Equipo eq1 = (Equipo) ois.readObject();

					if (eq1.getNomE().equalsIgnoreCase(nombreEquipo)) {

						eq1 = entrJug(eq1);

						existe = true;

					}

					oos.writeObject(eq1);

				} catch (ClassNotFoundException e) {

					e.printStackTrace();

				} catch (EOFException e) {

					finArchivo = true;

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

		Equipo equipo = null;

		String tipo, codigo;

		int codigoE;

		boolean existe = false, encontrado = false;

		System.out.println("¿Quieres eliminar un entrenador o un jugador?");

		tipo = Utilidades.introducirCadena("entrenador", "jugador");

		if (tipo.equalsIgnoreCase("entrenador")) {

			for (Integrante integr : eq1.getListIntegrante()) {

				if (integr instanceof Entrenador) {

					encontrado = true;

					integr.visualizar();

					System.out.println("");

				}

			}

			if (encontrado) {

				do {

					System.out.println("Introduzca el codigo del entrenador que quieres borrar:");

					codigoE = Utilidades.leerInt();

					Iterator<Integrante> listaintegrantes = eq1.getListIntegrante().iterator();

					while (listaintegrantes.hasNext()) {

						Integrante i = listaintegrantes.next();

						if (i instanceof Entrenador) {

							if (((Entrenador) i).getCodEntr() == codigoE) {

								listaintegrantes.remove();

								equipo = eq1;

								existe = true;

							}

						}

					}

					if (!existe) {

						System.out.println("No existe ningun entrenador con ese codigo!");

					}

				} while (!existe);

				System.out.println("Entrenador borrado con exito!");

			}

			else {

				System.out.println("No hay entrenadores registrados en " + eq1.getNomE() + ".");

			}

		} else {

			for (Integrante integr : eq1.getListIntegrante()) {

				if (integr instanceof Jugador) {

					encontrado = true;

					integr.visualizar();

					System.out.println("");

				}

			}

			if (encontrado) {

				do {

					System.out.println("Introduzca el codigo del jugador que quieres borrar:");

					codigo = Utilidades.introducirCadena();

					Iterator<Integrante> listaintegrantes = eq1.getListIntegrante().iterator();

					while (listaintegrantes.hasNext()) {

						Integrante i = listaintegrantes.next();

						if (i instanceof Jugador) {

							if (((Jugador) i).getCodJ() == codigo) {

								listaintegrantes.remove();

								equipo = eq1;

								existe = true;

							}

						}

					}

					if (!existe) {

						System.out.println("No existe ningun jugador con ese codigo!");

					}

				} while (!existe);

				System.out.println("Jugador borrado con exito!");

			}

			else {

				System.out.println("No hay jugadores registrados en " + eq1.getNomE() + ".");

			}

		}

		return equipo;

	}

	public static void eliminarEquipo(File liga, File equipo) {
		String nombre;
		File fichAux = new File("equipoAux.dat");
		boolean finArchivo = false;
		boolean modificado = false;
		ObjectOutputStream oos;
		ObjectInputStream ois;

		visualizar(liga, equipo);

		if (equipo.exists()) {
			System.out.println("\nIntroduce el nombre del equipo que quieres borrar");
			nombre = Utilidades.introducirCadena();

			try {
				ois = new ObjectInputStream(new FileInputStream(equipo));
				oos = new ObjectOutputStream(new FileOutputStream(fichAux));
				// Leer mientras no se alcance el fin del archivo
				while (!finArchivo) {
					try {
						Equipo aux = (Equipo) ois.readObject();
						if (!aux.getNomE().equalsIgnoreCase(nombre)) {
							oos.writeObject(aux);

						} else {
							modificado = true;
						}

					} catch (EOFException e) {
						// Fin del archivo alcanzado
						finArchivo = true;
					}
				}
				oos.close();
				ois.close();
				if (modificado) {
					System.out.println("Equipo eliminado con exito");
					if (equipo.delete()) {
						fichAux.renameTo(equipo);
					}
				} else {
					System.out.println("No existe un equipo con ese id");
				}

			} catch (Exception e) {
				System.out.println("Fatal error");
			}
		} else {
			System.out.println("El fichero no existe");
		}

	}

	public static void añadirEquipo(File liga, File equipo) {
		MyObjectOutputStream moos;
		boolean lesionado = false, esta = false;
		String nombreEquipo, respuesta, nombreJugador, pais, lesionaStr, continuar="si";
		int idEquipo, goles, idLiga;

		mostrarLigas(liga);

		System.out.println("\nIntroduce el id de la liga a la que va a pertenecer el quipo: ");
		idLiga = Utilidades.leerInt();
		System.out.println("Introduce el id del equipo: ");
		idEquipo = Utilidades.leerInt();

		esta = comprobarEquipo(liga, equipo, idLiga, idEquipo);

		if (!esta) 
		{		
			try {
				moos = new MyObjectOutputStream(new FileOutputStream(equipo, true));

				System.out.println("Introduce el nombre: ");
				nombreEquipo = Utilidades.introducirCadena();

				Equipo aux2 = new Equipo(idEquipo, nombreEquipo, idLiga);

				do {
					System.out.println("¿Que quieres añadir al equipo, un entrenador o un jugador?");
					respuesta = Utilidades.introducirCadena("entrenador", "jugador");

					if (respuesta.equalsIgnoreCase("entrenador")) {

						Entrenador entr = datosEntrenador(equipo, idLiga, idEquipo);
						aux2.getListIntegrante().add(entr);
					}

					if (respuesta.equalsIgnoreCase("jugador")) {
						System.out.println("Nombre del jugador: ");
						nombreJugador = Utilidades.introducirCadena();

						System.out.println("Pais del jugador: ");
						pais = Utilidades.introducirCadena();

						System.out.println("¿El jugador esta lesionado?");
						lesionaStr = Utilidades.introducirCadena("Si", "No");
						if (lesionaStr.equalsIgnoreCase("si")) {
							lesionado = true;
						}

						System.out.println("Goles que ha metido: ");
						goles = Utilidades.leerInt();

						Jugador j1 = new Jugador(nombreJugador, pais, lesionado, goles);
						aux2.getListIntegrante().add(j1);
					}

					System.out.println("¿Quieres continuar insertando integrantes al equipo?");
					continuar = Utilidades.introducirCadena("Si", "No");

				} while (continuar.equalsIgnoreCase("si"));

				moos.writeObject(aux2);
				System.out.println("Equipo creado con exito");
				moos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			

		} else {
			System.out.println("Ese equipo ya esta");
		}
	}

	public static boolean comprobarEquipo(File liga, File equipo, int idLiga, int idEquipo) {
		boolean esta = false;
		boolean estaLiga = false;
		boolean finArchivo = false;
		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream(new FileInputStream(liga));

			while (!finArchivo || !estaLiga) {
				try {
					Liga aux = (Liga) ois.readObject();
					if (aux.getCodL() == idLiga) {
						estaLiga = true;
					}
				} catch (EOFException e) {
					// Fin del archivo alcanzado
					finArchivo = true;
				}
			}
			ois.close();

		} catch (Exception e) {
			System.out.println("Fatal error");
		}

		if (estaLiga) {
			finArchivo = false;
			try {
				ois = new ObjectInputStream(new FileInputStream(equipo));

				while (!finArchivo) {
					try {
						Equipo aux = (Equipo) ois.readObject();
						if (idEquipo == aux.getCodE()&&aux.getCodL()==idLiga) {
							esta = true;
						}
					} catch (EOFException e) {
						// Fin del archivo alcanzado
						finArchivo = true;
					}
				}
				ois.close();

			} catch (Exception e) {
				System.out.println("Fatal error");
			}
		} else {
			System.out.println("Esa liga no esta");
		}
		return esta;
	}

	/*public static Entrenador datosEntrenador2(File equipo, int codE) {
		String nom, pais, setTipo;
		int codEntr;
		boolean error = false, esta = false;
		TipoEntr tipo = null;

		System.out.println("Introduce el nombre del entrenador:");
		nom = Utilidades.introducirCadena();
		System.out.println("Introduce el pais:");
		pais = Utilidades.introducirCadena();
		// placeholder introducir

		do {
			System.out.println("Introduce el codigo del entrenador:");
			codEntr = Utilidades.leerInt();

			esta = buscarEntrenador(equipo, codEntr, codE);
			if (esta) {
				System.out.println("Ese codigo de entrenador ya existe, introduce otro");
			}
		} while (esta);

		do {
			error = false;
			try {
				System.out.println("¿El entrenador es Principal, Tecnico, o de Fisio?");
				setTipo = Utilidades.introducirCadena().toUpperCase();
				switch (setTipo) {
				case "PRINCIPAL":
					tipo = TipoEntr.PRINCIPAL;
					break;

				case "TECNICO":
					tipo = TipoEntr.TECNICO;
					break;

				case "FISIO":
					tipo = TipoEntr.FISIO;
					break;

				default:
					throw new IllegalArgumentException("El tipo introducido es invalido.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				error = true;
			}
		} while (error);
		Entrenador entr = new Entrenador(nom, pais, codEntr, tipo);

		return entr;
	}*/

	public static void mostrarLigas(File liga) {
		ObjectInputStream ois = null;
		boolean finArchivo = false;

		try {
			ois = new ObjectInputStream(new FileInputStream(liga));

			while (!finArchivo) {
				try {
					Liga aux = (Liga) ois.readObject();
					System.out.println(aux.toString());
				} catch (EOFException e) {
					// Fin del archivo alcanzado
					finArchivo = true;
				}
			}
			ois.close();

		} catch (Exception e) {
			System.out.println("Fatal error");
		}
	}

	public static void ordenarGoleadoresPorGoles(File equipo)

	{

		boolean finArchivo = false;

		ObjectInputStream ois = null;

		ArrayList<Goleadores> listaGoleadores = new ArrayList<>();

		try {

			ois = new ObjectInputStream(new FileInputStream(equipo));

			while (!finArchivo) {

				try {

					Equipo aux = (Equipo) ois.readObject();

					for (Integrante i : aux.getListIntegrante())

					{

						if (i instanceof Jugador)

						{

							Jugador j = (Jugador) i;

							Goleadores goleador = new Goleadores(j.getGoles(), aux.getNomE(), j.getNombre());

							listaGoleadores.add(goleador);

						}

					}

				} catch (EOFException e) {

					// Fin del archivo alcanzado

					finArchivo = true;

				}

			}

			ois.close();

			Collections.sort(listaGoleadores);

			for (Goleadores g : listaGoleadores)

			{

				System.out.println(g);

			}

		} catch (Exception e) {

			System.out.println("Fatal error");

		}

	}
}