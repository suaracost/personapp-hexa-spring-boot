package co.edu.javeriana.as.personapp.terminal.menu;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;
import lombok.extern.slf4j.Slf4j;

import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class ProfesionMenu {

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

    public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
                        ProfesionMenu.DATABASE = "MARIA";
                        profesionInputAdapterCli.setProfessionOutputPortInjection(ProfesionMenu.DATABASE);
                        menuOpciones(profesionInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        ProfesionMenu.DATABASE = "MONGO";
                        profesionInputAdapterCli.setProfessionOutputPortInjection(ProfesionMenu.DATABASE);
                        menuOpciones(profesionInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli
            , Scanner keyboard) {
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
                        profesionInputAdapterCli.historial();
                        break;
                    case OPCION_CREAR:
                        profesionInputAdapterCli.crearProfesion(leerEntidad(keyboard), ProfesionMenu.DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        profesionInputAdapterCli.editarProfesion(leerEntidad(keyboard), ProfesionMenu.DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        profesionInputAdapterCli.buscarProfesion(ProfesionMenu.DATABASE, leerIdentificacion(keyboard));
                        break;
                    case OPCION_ELIMINAR:
                        profesionInputAdapterCli.eliminarProfesion(ProfesionMenu.DATABASE, leerIdentificacion(keyboard));
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
        System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
        System.out.println(OPCION_CREAR + " para crear una profesion");
        System.out.println(OPCION_ACTUALIZAR + " para actualizar una profesion");
        System.out.println(OPCION_BUSCAR + " para buscar una profesion");
        System.out.println(OPCION_ELIMINAR + " para eliminar una profesion");
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

    public ProfessionModelCli leerEntidad(Scanner keyboard) {
        try {
            ProfessionModelCli profesion = new ProfessionModelCli();
            System.out.println("Ingrese la identificacion: ");
            profesion.setId(keyboard.nextInt());
            keyboard.nextLine();
            System.out.println("Ingrese el nombre: ");
            profesion.setName(keyboard.nextLine());
            System.out.println("Ingrese la descripcion: ");
            profesion.setDescription(keyboard.nextLine());
            return profesion;
        } catch (InputMismatchException e) {
            System.out.println("Datos incorrectos, ingrese los datos nuevamente.");
            return leerEntidad(keyboard);
        }
    }


}