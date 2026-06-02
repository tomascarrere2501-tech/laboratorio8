import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ControlAsistencia sistema = new ControlAsistencia();
        int opcion = 0;

        while (opcion != 3) {
            System.out.println("\n---------Menu---------");
            System.out.println("1. Agregar Registro de Ingreso");
            System.out.println("2. Agregar Registro de Salida");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero valido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    procesarRegistro(scanner, sistema, "ingreso");
                    break;
                case 2:
                    procesarRegistro(scanner, sistema, "salida");
                    break;
                case 3:
                    System.out.println("Cerrado");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        }
    }

    private static void procesarRegistro(Scanner scanner, ControlAsistencia sistema, String tipo) {
        System.out.print("Ingrese el RUT (ej: 12.345.678-9): ");
        String rut = scanner.nextLine();

        System.out.print("Ingrese el Nombre y Apellido: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese la hora de " + tipo + " (Formato HH:mm, ej: 08:45 o 18:30): ");
        String horaStr = scanner.nextLine();

        LocalTime hora;
        try {
            hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Error Formato de hora invalido, el registro fue cancelado.");
            return;
        }

        Funcionario empleado = new Funcionario(rut, nombre);
        String lineaARegistrar;
        String nombreArchivo;

        if (tipo.equals("ingreso")) {
            lineaARegistrar = sistema.registrarIngreso(empleado, hora);
            nombreArchivo = sistema.obtenerNombreArchivoIngreso();
        } else {
            lineaARegistrar = sistema.registrarSalida(empleado, hora);
            nombreArchivo = sistema.obtenerNombreArchivoSalida();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            bw.write(lineaARegistrar);
            bw.newLine();
            System.out.println("Registro guardado exitosamente en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}

class Funcionario {
    private String rut;
    private String nombre;

    public Funcionario(String rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public String getRut() { return rut; }
    public String getNombre() { return nombre; }
}

