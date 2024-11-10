package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

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

    public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
                        menuOpciones(telefonoInputAdapterCli,keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
                        menuOpciones(telefonoInputAdapterCli,keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            }  catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }


    private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
                        telefonoInputAdapterCli.historial();					
                        break;
                    case OPCION_CREAR:
                        telefonoInputAdapterCli.crearTelefono(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        telefonoInputAdapterCli.editarTelefono(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        telefonoInputAdapterCli.buscarTelefono(leerNumero(keyboard), DATABASE);
                        break;
                    case OPCION_ELIMINAR:
                        telefonoInputAdapterCli.eliminarTelefono(leerNumero(keyboard), DATABASE);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            }  catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
			}
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los telefonos");
        System.out.println(OPCION_CREAR + " para crear un telefono");
        System.out.println(OPCION_ACTUALIZAR + " para actualizar un telefono");
        System.out.println(OPCION_BUSCAR + " para buscar un telefono");
        System.out.println(OPCION_ELIMINAR + " para eliminar un telefono");
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
            keyboard.nextLine(); // Limpiar el buffer
            return leerOpcion(keyboard);
        }
    }
    

    public PhoneModelCli leerEntidad(Scanner keyboard) {
        try {
            PhoneModelCli telefonoModelCli = new PhoneModelCli();
            keyboard.nextLine();
            System.out.print("Ingrese el numero: ");
            telefonoModelCli.setNumber(keyboard.nextLine());
            System.out.print("Ingrese la compañia: ");
            telefonoModelCli.setCompany(keyboard.nextLine());
            System.out.print("Ingrese el id de la persona: ");
            telefonoModelCli.setIdPerson(keyboard.nextLine());
            return telefonoModelCli;
        } catch (Exception e) {
            System.out.println("Datos incorrectos, ingrese los datos nuevamente.");
            return leerEntidad(keyboard);
        }
    }

    private int leerNumero(Scanner keyboard) {
        try{
            System.out.print("Ingrese el numero: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            return leerNumero(keyboard);
        }
    }
    
    
}