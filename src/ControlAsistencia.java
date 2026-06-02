import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class ControlAsistencia {
    private static final LocalTime HORA_LIMITE_INGRESO = LocalTime.of(8, 30);
    private static final LocalTime HORA_SALIDA_OFICIAL = LocalTime.of(17, 0);

    private static final DateTimeFormatter FORMATO_FECHA_ARCHIVO = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public String obtenerNombreArchivoIngreso() {
        return "Ingreso_Empleados_Fecha_" + LocalDate.now().format(FORMATO_FECHA_ARCHIVO) + ".txt";
    }

    public String obtenerNombreArchivoSalida() {
        return "Salida_Empleados_Fecha_" + LocalDate.now().format(FORMATO_FECHA_ARCHIVO) + ".txt";
    }

    public String registrarIngreso(Funcionario funcionario, LocalTime horaIngreso) {
        String baseRegistro = generarRegistroBase(funcionario, "ingreso", horaIngreso);
        if (horaIngreso.isAfter(HORA_LIMITE_INGRESO)) {
            return baseRegistro + " [ATRASO]";
        }
        return baseRegistro;
    }

    public String registrarSalida(Funcionario funcionario, LocalTime horaSalida) {
        String baseRegistro = generarRegistroBase(funcionario, "salida", horaSalida);
        if (horaSalida.isAfter(HORA_SALIDA_OFICIAL)) {
            Duration tiempoExtra = Duration.between(HORA_SALIDA_OFICIAL, horaSalida);
            long horas = tiempoExtra.toHours();
            long minutos = tiempoExtra.toMinutesPart();
            return baseRegistro + String.format(" [TIEMPO EXTRA: %dh %dm]", horas, minutos);
        }
        return baseRegistro;
    }

    private String generarRegistroBase(Funcionario funcionario, String tipoEvento, LocalTime hora) {
        return String.format("Rut: %s | Nombre: %s | Hora de %s: %s",
                funcionario.getRut(),
                funcionario.getNombre(),
                tipoEvento,
                hora.format(FORMATO_HORA));
    }
}
