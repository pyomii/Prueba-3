package prueba_3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorEmpleados {

    private final ArrayList<Empleado> empleados = new ArrayList<>();

    public boolean existeCodigo(String codigo) {
        if (codigo == null) {
            return false;
        }
        String buscado = codigo.trim();
        for (Empleado empleado : empleados) {
            if (empleado.getCodigo().equalsIgnoreCase(buscado)) {
                return true;
            }
        }
        return false;
    }

    public boolean agregar(Empleado empleado) {
        if (empleado == null || existeCodigo(empleado.getCodigo())) {
            return false;
        }
        return empleados.add(empleado);
    }

    public Empleado buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        String buscado = codigo.trim();
        for (Empleado empleado : empleados) {
            if (empleado.getCodigo().equalsIgnoreCase(buscado)) {
                return empleado;
            }
        }
        return null;
    }

    public Empleado obtenerMayorSalario() {
        Empleado mayor = null;
        for (Empleado empleado : empleados) {
            if (mayor == null || empleado.calcularSalario() > mayor.calcularSalario()) {
                mayor = empleado;
            }
        }
        return mayor;
    }

    public double calcularSalarioPromedio() {
        if (empleados.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Empleado empleado : empleados) {
            total += empleado.calcularSalario();
        }
        return total / empleados.size();
    }

    public List<Empleado> getEmpleados() {
        return Collections.unmodifiableList(empleados);
    }

    public int cantidad() {
        return empleados.size();
    }
}
