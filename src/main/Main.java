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
		int opcion;

		if (!liga.exists()) {
			fillDataLiga(liga);
		}
		if (!equipo.exists()) {
			fillDataEquipo(equipo);
		}
		do {
			opcion = menu();
			switch (opcion) {

			case 1:
				añadirEntrenador(equipo, liga);
				break;

			case 2:
				modifEquipoNom(equipo, liga);
				break;

			case 3:
				visualizar(liga, equipo);
				break;

			case 4:
				borrarIntr(equipo, liga);
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
				System.out.println("\nHasta luego!");
				break;
			}
		} while (opcion != 0);
	}

	public static int menu() {
		System.out.println("0. Salir");
		System.out.println("1. Añadir entrenador.");
		System.out.println("2. Cambiar nombre de un equipo.");
		System.out.println("3. Mostrar todos los datos.");
		System.out.println("4. Borrar un integrante.");
		System.out.println("5. Eliminar equipo.");
		System.out.println("6. Añadir equipo.");
		System.out.println("7. Mostrar mayores goleadores.");
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

	public static void añadirEntrenador(File equipo, File liga) {
		File aux = new File("fichAux.dat");
		ObjectInputStream ois;
		ObjectOutputStream oos;
		int codE, codL;
		boolean end = false, modificado = false, excepcion=false, esta=false;
		Entrenador entr;

		end = false;

		visualizar(liga, equipo);

		System.out.println("Introduce el codigo de la liga del equipo:");
		codL = Utilidades.leerInt();
		esta=comprobarLiga(liga, codL);

		if(esta) {
			esta=false;

			do {
				System.out.println("\nIntroduce el codigo del equipo que quieras añadir el nuevo entrenador:");
				codE = Utilidades.leerInt();
				esta = comprobarEquipo(liga, equipo, codL, codE);

				if(esta) {
					try {
						validarEntrenadores(equipo, codE, codL);

						if(!excepcion) {
							entr = datosEntrenador(equipo, codL, codE);
							System.out.println(entr.toString());
							ois = new ObjectInputStream(new FileInputStream(equipo));
							oos = new ObjectOutputStream(new FileOutputStream(aux));

							while (!end) {
								try {
									Equipo en = (Equipo) ois.readObject();

									if (en.getCodE() == codE && en.getCodL()==codL) {

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
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();

					} catch (IOException e) {
						e.printStackTrace();

					} catch (ExcepcionEntrenador e) {
						System.out.println(e.getMessage());				
						excepcion = true;
					}
				}else {
					System.out.println("Ese equipo no esta");
				}
			} while (!esta);

		}else {
			System.out.println("\nEsta liga no esta");
		}

		if (modificado) {
			if (equipo.delete()){
				aux.renameTo(equipo);
				System.out.println("Entrenador creado correctamente!");
			}
		}

		System.out.println("\nVolviendo al menu...\n");
	}

	public static void validarEntrenadores(File equipo, int codE, int codL) throws ExcepcionEntrenador {
		int contadorEntrenadores = 0;
		boolean finArchivo = false;

		if (equipo.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(equipo));
				while (!finArchivo) {
					try {
						Equipo en = (Equipo) ois.readObject();
						if (en.getCodE() == codE && en.getCodL()==codL) {
							// Contamos los entrenadores de este equipo
							for (Integrante integrante : en.getListIntegrante()) {
								if (integrante instanceof Entrenador) {
									contadorEntrenadores++;
								}
							}
						}
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Error al leer el archivo: " + e.getMessage());
			}

		}

		if (contadorEntrenadores >= 4) {
			throw new ExcepcionEntrenador("\nError: No se pueden agregar más de 4 entrenadores al equipo.");
		}

	}

	public static Entrenador datosEntrenador(File equipo, int codL, int codE) {
		String nom, pais, setTipo;
		int codEntr;
		boolean error = false, esta = false;
		TipoEntr tipo = null;

		System.out.println("\nIntroduce el nombre del entrenador:");
		nom = Utilidades.introducirCadena();

		System.out.println("Introduce el pais:");
		pais = Utilidades.introducirCadena();

		do {
			System.out.println("Introduce el codigo del entrenador:");
			codEntr = Utilidades.leerInt();

			esta = buscarEntrenador(equipo, codL, codEntr, codE);

			if (esta) {
				System.out.println("Ese codigo de entrenador ya existe, introduce otro.\n");
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
		boolean finArchivo = false, existe=false;
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
									existe=true;
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

		return existe;
	}

	public static void modifEquipoNom(File equipo, File liga) {
		String nom, nuevoNom;
		boolean modificado = false, fin = false;
		File fichAux = new File("fichAux.dat");

		visualizar(liga, equipo);  

		System.out.println("Introduce el nombre que quieres cambiar");
		nom = Utilidades.introducirCadena();

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(equipo));
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichAux));

			while (!fin) {
				try {
					Equipo aux = (Equipo) ois.readObject();

					if (aux.getNomE().equalsIgnoreCase(nom)) {
						System.out.println(aux.toString());
						System.out.println("\n¿Cual es el nuevo nombre?");
						nuevoNom = Utilidades.introducirCadena();
						aux.setNomE(nuevoNom);
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
				System.out.println("\nCambio realizado correctamente!");

				if (equipo.delete()) {
					fichAux.renameTo(equipo);
				}
			} else {
				System.out.println("\nNo existe un equipo con ese nombre");
				fichAux.delete();
			}

		} catch (Exception e) {
			System.out.println("Fatal error");
		}

		System.out.println("\nVolviendo al menu...\n");
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
					finArchivo = true;

				}
			}

			ois.close();
		} catch (Exception e) {
			System.out.println("Fatal error");
		}

		System.out.println("");

		for (Liga l : listaLigas) {
			existe = false;
			System.out.println("<" + l.toString()+">");

			for (Equipo eq : listaEquipos) {
				if (eq.getCodL() == l.getCodL()) {
					System.out.println(eq.toString());
					existe = true;
				}
			}

			if (!existe) {
				System.out.println("\nNo existe ningun equipo en esta liga.");
			}

			System.out.println("------------------------------------------\n");
		}
	}

	public static void borrarIntr(File equipo, File liga) {
		File aux = new File("aux.dat");
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		String nombreEquipo;
		boolean existe = false, finArchivo = false;

		visualizar(liga, equipo);

		System.out.println("Introduce el nombre del equipo al que pertenece el integrante: ");
		nombreEquipo = Utilidades.introducirCadena();

		try {
			ois = new ObjectInputStream(new FileInputStream(equipo));
			oos = new ObjectOutputStream(new FileOutputStream(aux));

			while (!finArchivo) {
				try {
					Equipo eq1 = (Equipo) ois.readObject();

					if (eq1.getNomE().equalsIgnoreCase(nombreEquipo)) {
						eq1 = entrenadorJugador(eq1);
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
				System.out.println("\nNo se ha encontrado ningun equipo con ese nombre.");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\nVolviendo al menu...\n");
	}

	public static Equipo entrenadorJugador(Equipo eq1) {
		Equipo equipo = null;
		String tipo, codigoj;
		int codigoE;
		boolean existe = false, encontrado = false;

		System.out.println("\n¿Quieres eliminar un entrenador o un jugador?");
		tipo = Utilidades.introducirCadena("entrenador", "jugador");

		if (tipo.equalsIgnoreCase("entrenador")) {
			for (Integrante integr : eq1.getListIntegrante()) {
				if (integr instanceof Entrenador) {
					encontrado = true;
					System.out.println("");
					integr.visualizar();
				}
			}

			if (encontrado) {
				do {
					System.out.println("\nIntroduzca el codigo del entrenador que quieres borrar:");
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
						System.out.println("\nNo existe ningun entrenador con ese codigo!");
					}
				} while (!existe);

				System.out.println("\nEntrenador borrado con exito!");
			}

			else {
				equipo = eq1;
				System.out.println("No hay entrenadores registrados en " + eq1.getNomE() + ".");
			}
		} else {
			for (Integrante integr : eq1.getListIntegrante()) {
				if (integr instanceof Jugador) {
					encontrado = true;
					System.out.println("");
					integr.visualizar();
				}
			}
			
			if (encontrado) {
				do {
					System.out.println("\nIntroduzca el codigo del jugador que quieres borrar:");
					codigoj = Utilidades.introducirCadena();

					Iterator<Integrante> listaintegrantes = eq1.getListIntegrante().iterator();
					while (listaintegrantes.hasNext()) {
						Integrante i = listaintegrantes.next();
						
						if (i instanceof Jugador) {
							if (((Jugador) i).getCodJ().equalsIgnoreCase(codigoj)) {
								listaintegrantes.remove();
								equipo = eq1;
								existe = true;
							}
						}
					}

					if (!existe) {
						System.out.println("\nNo existe ningun jugador con ese codigo!");
					}
				} while (!existe);

				System.out.println("\nJugador borrado con exito!");
			}

			else {
				equipo = eq1;
				System.out.println("No hay jugadores registrados en " + eq1.getNomE() + ".");
			}
		}

		return equipo;
	}

	public static void eliminarEquipo(File liga, File equipo) {		
		int idLiga, idEquipo;
		File fichAux = new File("equipoAux.dat");
		boolean finArchivo = false, modificado = false, esta=false;
		ObjectOutputStream oos;
		ObjectInputStream ois;

		visualizar(liga, equipo);

		do {
			System.out.println("Introduce el id de la liga del equipo que quieres borrar:");
			idLiga = Utilidades.leerInt();
			esta = comprobarLiga(liga, idLiga);
			if(!esta){
				System.out.println("\nEsa liga no esta.");
			}
		} while(!esta);

		esta = false;

		do {
			System.out.println("\nIntroduce el id del equipo que quieres borrar");
			idEquipo = Utilidades.leerInt();

			esta = comprobarEquipo(liga, equipo, idLiga, idEquipo);
			if (!esta){
				System.out.println("\nEse equipo no esta, introduce otro id.");
			}
		} while(!esta);

		if (equipo.exists()) {
			try {
				ois = new ObjectInputStream(new FileInputStream(equipo));
				oos = new ObjectOutputStream(new FileOutputStream(fichAux));
				while (!finArchivo) {
					try {
						Equipo aux = (Equipo) ois.readObject();

						if (aux.getCodE()==idEquipo&&aux.getCodL()==idLiga) {								
							modificado = true;
						} else {
							oos.writeObject(aux);
						}						

					} catch (EOFException e) {
						finArchivo = true;
					}
				}
				oos.close();
				ois.close();
				
				if (modificado) {
					System.out.println("\nEquipo eliminado con exito!");
					
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

		System.out.println("\nVolviendo al menu...\n");
	}

	public static void añadirEquipo(File liga, File equipo) {
		MyObjectOutputStream moos;
		boolean lesionado = false, esta = false;
		String nombreEquipo, respuesta, nombreJugador, pais, lesionaStr;
		int idEquipo, goles, idLiga;

		visualizar(liga, equipo);

		do {
			System.out.println("Introduce el id de la liga a la que va a pertenecer el equipo: ");
			idLiga = Utilidades.leerInt();
			esta = comprobarLiga(liga, idLiga);
			
			if(!esta){
				System.out.println("\nEsa liga no esta.");
			}
		}while(!esta);

		esta = false;

		do {
			System.out.println("\nIntroduce el id del equipo: ");
			idEquipo = Utilidades.leerInt();

			esta = comprobarEquipo(liga, equipo, idLiga, idEquipo);
			
			if (esta){
				System.out.println("\nEse equipo ya esta, introduce otro id.");
			}
		}while(esta);

		try {
			moos = new MyObjectOutputStream(new FileOutputStream(equipo, true));

			nombreEquipo=comprobarNombreEquipo(equipo);

			Equipo aux2 = new Equipo(idEquipo, nombreEquipo, idLiga);				

			System.out.println("\n¿Que quieres añadir al equipo, un entrenador o un jugador?");
			respuesta = Utilidades.introducirCadena("entrenador", "jugador");

			if (respuesta.equalsIgnoreCase("entrenador")) {
				Entrenador entr = datosEntrenador(equipo, idLiga, idEquipo);
				aux2.getListIntegrante().add(entr);
			}

			if (respuesta.equalsIgnoreCase("jugador")) {
				do {
					System.out.println("\nNombre del jugador: ");
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
					
					System.out.println("\n¿Introducir otro jugador?");
					respuesta = Utilidades.introducirCadena("si", "no");

				}while(respuesta.equalsIgnoreCase("si"));
			}
			
			moos.writeObject(aux2);					
			moos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		System.out.println("\nEquipo creado con exito");

		System.out.println("\nVolviendo al menu...\n");
	}

	public static String comprobarNombreEquipo(File equipo) {
		String nombreEquipo;
		boolean existe, finArchivo=false;
		ObjectInputStream ois=null;

		do {
			existe=false;
			finArchivo=false;

			System.out.println("Introduce el nombre: ");
			nombreEquipo = Utilidades.introducirCadena();

			try {
				ois = new ObjectInputStream(new FileInputStream(equipo));

				while (!finArchivo) {
					try {
						Equipo eq1=(Equipo) ois.readObject();
						
						if (eq1.getNomE().equalsIgnoreCase(nombreEquipo)) {
							existe=true;
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EOFException e) {
						finArchivo=true;
					}
				}

				if (existe) {
					System.out.println("\nEse nombre ya existe. Prueba con otro.\n");
				}
				ois.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (existe);

		return nombreEquipo;
	}

	public static boolean comprobarLiga(File liga, int idLiga) {
		boolean finArchivo = false, esta=false;
		ObjectInputStream ois=null;

		try {	
			ois=new ObjectInputStream(new FileInputStream(liga));

			while (!finArchivo) {
				try{
					Liga aux=(Liga) ois.readObject();
					
					if(aux.getCodL()==idLiga) {
						esta= true;
					}			 
				} catch (EOFException e) {
					// Fin del archivo alcanzado
					finArchivo = true;
				}
			}
			ois.close();	 

		}catch(Exception e) {
			System.out.println("Fatal error");
		}

		return esta;
	}

	public static boolean comprobarEquipo(File liga, File equipo, int idLiga, int idEquipo) {
		boolean esta = false;

		boolean finArchivo = false;
		ObjectInputStream ois = null;

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

		return esta;
	}


	public static void ordenarGoleadoresPorGoles(File equipo){
		boolean finArchivo = false;
		ObjectInputStream ois = null;
		ArrayList<Goleadores> listaGoleadores = new ArrayList<>();

		try {
			ois = new ObjectInputStream(new FileInputStream(equipo));
			
			while (!finArchivo) {
				try {
					Equipo aux = (Equipo) ois.readObject();
					for (Integrante i : aux.getListIntegrante()){
						if (i instanceof Jugador){
							Jugador j = (Jugador) i;
							Goleadores goleador = new Goleadores(j.getGoles(), aux.getNomE(), j.getNombre());
							listaGoleadores.add(goleador);
						}
					}
				} catch (EOFException e) {
					finArchivo = true;
				}
			}

			ois.close();

			Collections.sort(listaGoleadores);

			System.out.println("");
			for (Goleadores g : listaGoleadores){
				System.out.println(g);
			}
		} catch (Exception e) {
			System.out.println("Fatal error");
		}

		System.out.println("\nVolviendo al menu...\n");
	}
}