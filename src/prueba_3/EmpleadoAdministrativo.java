package prueba_3;

public class EmpleadoAdministrativo extends Empleado {

    private double bonificacion;
    private double descuento;

    public EmpleadoAdministrativo(String codigo, String nombre, double salarioBase, double bonificacion, double descuento) {
        super(codigo, nombre, salarioBase);
        setBonificacion(bonificacion);
        setDescuento(descuento);
    }

    public double getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(double bonificacion) {
        this.bonificacion = Math.max(0, bonificacion);
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = Math.min(100, Math.max(0, descuento));
    }

    @Override
    public double calcularSalario() {
        return (getSalarioBase() + bonificacion) * (1.0 - (descuento / 100.0));
    }

    @Override
    public String getTipo() {
        return "Administrativo";
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion()
                + "\nBonificación: L. " + String.format("%.2f", bonificacion)
                + "\nDescuento: " + String.format("%.2f", descuento) + "%";
    }
}
