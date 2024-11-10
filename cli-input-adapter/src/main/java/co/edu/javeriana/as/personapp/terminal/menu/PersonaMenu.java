package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

	private static String DATABASE = "MARIA";
	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
    private static final int OPCION_ACTUALIZAR = 3;
    private static final int OPCION_BUSCAR = 4;
    private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MODULOS:
					isValid = true;
					break;
				case PERSISTENCIA_MARIADB:
					DATABASE = "MARIA";
					personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					DATABASE = "MONGO";
					personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			}  catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
					isValid = true;
					break;
				case OPCION_VER_TODO:
					personaInputAdapterCli.historial();					
					break;
				case OPCION_CREAR:
					personaInputAdapterCli.crearPersona(leerEntidad(keyboard), DATABASE);
					break;
				case OPCION_ACTUALIZAR:
					personaInputAdapterCli.editarPersona(leerEntidad(keyboard), DATABASE);
					break;
				case OPCION_BUSCAR:
					personaInputAdapterCli.buscarPersona(leerIdentificacion(keyboard), DATABASE);
					break;
				case OPCION_ELIMINAR:
					personaInputAdapterCli.eliminarPersona(leerIdentificacion(keyboard), DATABASE);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todas las personas");
        System.out.println(OPCION_CREAR + " para crear una persona");
        System.out.println(OPCION_ACTUALIZAR + " para actualizar una persona");
        System.out.println(OPCION_BUSCAR + " para buscar una persona");
        System.out.println(OPCION_ELIMINAR + " para eliminar una persona");
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            return leerOpcion(keyboard);
        }
    }

    private int leerIdentificacion(Scanner keyboard) {
        try {
            System.out.print("Ingrese la identificacion: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            return leerOpcion(keyboard);
        }
    }

    public PersonaModelCli leerEntidad(Scanner keyboard) {
        try {
            PersonaModelCli personaModelCli = new PersonaModelCli();
            System.out.print("Ingrese la identificacion: ");
            personaModelCli.setCc(keyboard.nextInt());
            keyboard.nextLine();
            System.out.print("Ingrese el nombre: ");
            personaModelCli.setNombre(keyboard.nextLine());
            System.out.print("Ingrese el apellido: ");
            personaModelCli.setApellido(keyboard.nextLine());
            System.out.println("Ingrese el genero (M/F): ");
            personaModelCli.setGenero(keyboard.nextLine());
            System.out.println("Ingrese la edad: ");
            personaModelCli.setEdad(keyboard.nextInt());
            keyboard.nextLine();
            return personaModelCli;
        } catch (InputMismatchException e) {
            System.out.println("Datos incorrectos, ingrese los datos nuevamente.");
            return leerEntidad(keyboard);
        }
    }

}