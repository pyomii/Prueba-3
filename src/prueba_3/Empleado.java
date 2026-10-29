package prueba_3;

public abstract class Empleado implements Cloneable {

    public static final double UMBRAL_CATEGORIA_SUPERIOR = 25000.0;
    public static final double UMBRAL_CATEGORIA_ESTANDAR = 15000.0;

    private String codigo;
    private String nombre;
    private double salarioBase;

    public Empleado(String codigo, String nombre, double salarioBase) {
        setCodigo(codigo);
        setNombre(nombre);
        setSalarioBase(salarioBase);
    }

    public String getCodigo() {
        return codigo;
    }

    public final void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        this.codigo = codigo.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public final void setSalarioBase(double salarioBase) {
        this.salarioBase = Math.max(0, salarioBase);
    }

    public abstract double calcularSalario();

    public abstract String getTipo();

    public String obtenerCategoria() {
        double salario = calcularSalario();
        if (salario >= UMBRAL_CATEGORIA_SUPERIOR) {
            return "Superior";
        }
        if (salario >= UMBRAL_CATEGORIA_ESTANDAR) {
            return "Estándar";
        }
        return "Básico";
    }

    public String mostrarInformacion() {
        return "Código: " + codigo
                + "\nNombre: " + nombre
                + "\nTipo: " + getTipo()
                + "\nSalario base: L. " + String.format("%.2f", salarioBase)
                + "\nSalario calculado: L. " + String.format("%.2f", calcularSalario())
                + "\nCategoría: " + obtenerCategoria();
    }

    @Override
    public Empleado clone() {
        try {
            return (Empleado) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("La clonación no está soportada.", e);
        }
    }
}
