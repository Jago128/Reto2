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

			case 5:
				eliminarEquipo(liga, equipo);
				break;

			case 6:
				añadirEquipo(liga, equipo);
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
		System.out.println("3. Mostrar todos los datos");
		System.out.println("4. Borrar un integrante");
		System.out.println("5. Eliminar equipo");
		System.out.println("6. Añadir equipo");
		return Utilidades.leerInt(0,6);
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
		} catch (Exception e) {
			System.out.println("Fatal error");
		}
	}

	public static void fillDataEquipo(File equipo) {
		ObjectOutputStream oos;

		Equipo e1=new Equipo(1, "Sestao River", 1);
		Equipo e2=new Equipo(2, "Barcelona", 1);
		Equipo e3=new Equipo(3, "Chelsea", 2);
		Equipo e4=new Equipo(4, "Huesca", 3);

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
		} catch (Exception e) {
			System.out.println("Fatal error");
		}
	}

	public static void añadirEntr(File equipo) {
		File aux=new File("fichAux.dat");
		ObjectInputStream ois;
		ObjectOutputStream oos;		
		int codE;
		boolean end=false, found=false;
		Entrenador entr=datosEntrenador();

		do {
			end=false; found=false;
			System.out.println("Introduce el codigo del equipo que quieras añadir el nuevo entrenador:");
			codE=Utilidades.leerInt();
			try {
				ois=new ObjectInputStream(new FileInputStream(equipo));
				oos=new ObjectOutputStream(new FileOutputStream(aux));
				while (!end) {
					try {
						Equipo en=(Equipo)ois.readObject();
						if (en.getCodE()==codE) {
							found=true;
							en.getListIntegrante().add(entr);
						}
						oos.writeObject(en);
					} catch (EOFException e) {
						end=true;
					} catch (Exception e) {
						System.out.println("Fatal error");
					}
				}
				ois.close();
				oos.close();
			} catch (Exception e) {
				System.out.println("Fatal error");
			}
			if (!found) {
				System.out.println("No se ha encontrado el equipo. Introducelo de nuevo.");
			}
		} while (!found);
		
		if (equipo.delete()) {
			aux.renameTo(equipo);
		}
	}

	public static Entrenador datosEntrenador() {
		String nom, pais, setTipo;
		int codEntr;
		boolean error=false;
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
		return entr;
	}

	public static void modifEquipoNom(File equipo) {
		String nom, nuevoN;
		boolean modificado=false, fin=false;
		File fichAux=new File("fichAux.dat");
		ObjectInputStream ois;
		ObjectOutputStream oos;

		System.out.println("Introduce el nombre que quieres cambiar:");
		nom=Utilidades.introducirCadena();

		if (equipo.exists()) {
			try {
				ois=new ObjectInputStream(new FileInputStream(equipo));
				oos=new ObjectOutputStream(new FileOutputStream(fichAux));
				while (!fin) {
					try {
						Equipo aux=(Equipo)ois.readObject();
						if (aux.getNomE().equalsIgnoreCase(nom)) {
							System.out.println(aux.toString());
							System.out.println("¿Cual es el nuevo nombre?");
							nuevoN=Utilidades.introducirCadena();
							aux.setNomE(nuevoN);
							modificado=true;
						}
						oos.writeObject(aux);
					} catch (EOFException e) {
						fin=true;
					} catch (Exception e) {
						System.out.println("Fatal error");
					}
				}
				oos.close();
				ois.close();
				if (modificado) {
					System.out.println("El nombre del equipo ha sido modificado.");
					if (equipo.delete()) {
						fichAux.renameTo(equipo);
					}
				} else {
					System.out.println("No hay un equipo con ese nombre.");
					fichAux.delete();
				}
			} catch (Exception e) {
				System.out.println("Fatal error");
			}
		} else {
			System.out.println("El fichero aun no se ha creado.");
		}
	}

	public static void visualizar(File liga, File equipo) {
		boolean finArchivo=false;
		ObjectInputStream ois;

		try {	
			ois=new ObjectInputStream(new FileInputStream(liga));
			System.out.println("LIGAS:");
			while (!finArchivo) {
				try {
					Liga aux=(Liga)ois.readObject();
					System.out.println(aux.toString());				 
				} catch (EOFException e) {
					finArchivo=true;
				} catch (Exception e) {
					System.out.println("Fatal error");
				}
			}
			ois.close();
		} catch(Exception e) {
			System.out.println("Fatal error");
		}

		finArchivo=false;
		try {	
			ois=new ObjectInputStream(new FileInputStream(equipo));
			System.out.println("EQUIPOS:");
			while (!finArchivo) {
				try {
					Equipo aux=(Equipo) ois.readObject();
					System.out.println(aux.toString());				 
				} catch (EOFException e) {
					finArchivo=true;
				} catch (Exception e) {
					System.out.println("Fatal error");
				}
			}
			ois.close();
		} catch (Exception e) {
			System.out.println("Fatal error");
		}
	}

	public static void borrarIntr(File equipo) {
		File aux=new File("aux.dat");
		ObjectInputStream ois;
		ObjectOutputStream oos;
		String nombreEquipo;
		boolean existe=false, finArchivo=false;

		System.out.println("Introduce el equipo al que pertenece el integrante:");
		nombreEquipo=Utilidades.introducirCadena();

		try {
			ois=new ObjectInputStream(new FileInputStream(equipo));
			oos=new ObjectOutputStream(new FileOutputStream(aux));
			while (!finArchivo) {
				try {
					Equipo eq1=(Equipo) ois.readObject();
					if (eq1.getNomE().equalsIgnoreCase(nombreEquipo)) {
						eq1=entrJug(eq1);
						existe=true;
					}
					oos.writeObject(eq1);
				} catch (EOFException e) {
					finArchivo=true;
				} catch (Exception e) {
					System.out.println("Fatal error");
				}
			}
			ois.close();
			oos.close();
			if (existe) {
				if (equipo.delete()) {
					aux.renameTo(equipo);
				}
				System.out.println("El integrante ha sido eliminado correctamente.");
			} else {
				aux.delete();
				System.out.println("No se ha encontrado ningun equipo con ese nombre.");
			}
		} catch (Exception e) {
			System.out.println("Fatal error");
		}
	}

	public static Equipo entrJug(Equipo eq1) {
		Equipo equipo=null;
		String tipo;
		int codigo;
		boolean existe=false;

		System.out.println("¿Quieres eliminar un entrenador o un jugador?");
		tipo=Utilidades.introducirCadena("entrenador","jugador");

		switch (tipo) {

		case "entrenador":
			for (Integrante integr:eq1.getListIntegrante()) {
				if (integr instanceof Entrenador) {
					integr.visualizar();
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
					System.out.println("No existe ningun entrenador con ese codigo.");
				}
			} while (!existe);
			System.out.println("El entrenador ha sido borrado con exito.");
			break;

		case "jugador":
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
					System.out.println("No existe ningun jugador con ese codigo.");
				}
			} while (!existe);
			System.out.println("El jugador borrado con exito.");
			break;
		}
		return equipo;
	}

	public static void eliminarEquipo(File liga, File equipo) {
		String nombre;
		File fichAux= new File("equipoAux.dat");
		boolean finArchivo=false;
		boolean modificado=false;
		ObjectOutputStream oos;
		ObjectInputStream ois;

		visualizar(liga,equipo);
		if (equipo.exists()) {
			System.out.println("Introduce el nombre del equipo que quieres borrar:");
			nombre=Utilidades.introducirCadena();
			try {
				ois=new ObjectInputStream(new FileInputStream(equipo));
				oos=new ObjectOutputStream(new FileOutputStream(fichAux));			
				while (!finArchivo) {
					try {
						Equipo aux=(Equipo)ois.readObject();
						if (!aux.getNomE().equalsIgnoreCase(nombre)) {
							oos.writeObject(aux);
						} else {
							modificado=true;
						}
					} catch (EOFException e) {
						finArchivo = true;
					} catch (Exception e) {
						System.out.println("Fatal error");
					}
				}
				oos.close();
				ois.close();	 
				if (modificado) {
					System.out.println("El equipo ha sido eliminado con exito.");
					if (equipo.delete()) {
						fichAux.renameTo(equipo);
					}
				} else {
					System.out.println("No existe un equipo con ese ID.");
				}
			} catch(Exception e) {
				System.out.println("Fatal error");
			}	
		} else {
			System.out.println("El fichero no existe.");
		}

	}

	public static void añadirEquipo(File liga, File equipo) {				
		MyObjectOutputStream moos;
		boolean finArchivo=false, lesionado=false;
		ObjectInputStream ois;
		String nombreLiga, nombreEquipo, respuesta, nombreJugador, pais, lesionaStr, continuar;
		int id, codJ, goles;

		mostrarLigas(liga);
		System.out.println("Introduce el nombre de la liga a la que va a pertenecer el equipo:");
		nombreLiga = Utilidades.introducirCadena();
		try {	
			ois=new ObjectInputStream(new FileInputStream(liga));
			while (!finArchivo) {
				try {
					Liga aux=(Liga)ois.readObject();
					if (aux.getNomL().equalsIgnoreCase(nombreLiga)) {
						try {
							moos=new MyObjectOutputStream(new FileOutputStream(equipo,true));
							System.out.println("Introduce el id del equipo:");
							id=Utilidades.leerInt();
							System.out.println("Introduce el nombre:");
							nombreEquipo=Utilidades.introducirCadena();
							Equipo aux2=new Equipo(id, nombreEquipo, aux.getCodL());
							
							do {
								System.out.println("¿Quieres añadir al equipo un entrenador o un jugador?");
								respuesta = Utilidades.introducirCadena("entrenador", "jugador");
								
								switch (respuesta) {
								case "entrenador":
									Entrenador entr=datosEntrenador();
									aux2.getListIntegrante().add(entr);	
									break;

								case "jugador":
									System.out.println("Nombre del jugador: ");
									nombreJugador=Utilidades.introducirCadena();
									System.out.println("Pais del jugador: ");
									pais=Utilidades.introducirCadena();
									System.out.println("Codigo del jugador: ");
									codJ=Utilidades.leerInt();
									System.out.println("¿El jugador esta lesionado?");
									lesionaStr = Utilidades.introducirCadena("Si", "No");
									if (lesionaStr.equalsIgnoreCase("si")) {
										lesionado=true;
									}
									System.out.println("Goles que ha metido: ");
									goles=Utilidades.leerInt();
									Jugador j1=new Jugador(nombreJugador, pais, codJ, lesionado,  goles);
									aux2.getListIntegrante().add(j1);
									break;	
								}
								System.out.println("¿Quieres continuar insertando integrantes al equipo?");
								continuar = Utilidades.introducirCadena("si", "no");
							} while(continuar.equalsIgnoreCase("si"));

							moos.writeObject(aux2);
							System.out.println("El equipo ha sido creado con exito.");
							moos.close();
						} catch (Exception e) {
							System.out.println("Fatal error");
						}
					}
				} catch (EOFException e) {
					finArchivo=true;
				} catch (Exception e) {
					System.out.println("Fatal error");
				}
			}
			ois.close();
		} catch (Exception e) {
			System.out.println("Fatal error");
		}
	}

	public static void mostrarLigas(File liga) {
		ObjectInputStream ois;
		boolean finArchivo=false;

		try {	
			ois=new ObjectInputStream(new FileInputStream(liga));
			while (!finArchivo) {
				try {
					Liga aux=(Liga)ois.readObject();
					System.out.println(aux.toString());				 
				} catch (EOFException e) {
					finArchivo=true;
				} catch (Exception e) {
					System.out.println("Fatal error");
				}
			}
			ois.close();	 
		} catch(Exception e) {
			System.out.println("Fatal error");
		}
	} 
}