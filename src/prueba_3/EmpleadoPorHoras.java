package prueba_3;

public class EmpleadoPorHoras extends Empleado {

    private int horasTrabajadas;
    private double tarifaPorHora;

    public EmpleadoPorHoras(String codigo, String nombre, double salarioBase, int horasTrabajadas, double tarifaPorHora) {
        super(codigo, nombre, salarioBase);
        setHorasTrabajadas(horasTrabajadas);
        setTarifaPorHora(tarifaPorHora);
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = Math.max(0, horasTrabajadas);
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public void setTarifaPorHora(double tarifaPorHora) {
        this.tarifaPorHora = Math.max(0, tarifaPorHora);
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + (horasTrabajadas * tarifaPorHora);
    }

    @Override
    public String getTipo() {
        return "Por Horas";
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion()
                + "\nHoras trabajadas: " + horasTrabajadas
                + "\nTarifa por hora: L. " + String.format("%.2f", tarifaPorHora);
    }
}
