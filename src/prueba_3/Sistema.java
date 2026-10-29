package prueba_3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import static javax.swing.JOptionPane.*;

public class Sistema extends JFrame {

    private final GestorEmpleados gestor = new GestorEmpleados();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtSalario = new JTextField();
    private final JTextField txtValor1 = new JTextField();
    private final JTextField txtValor2 = new JTextField();
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Administrativo", "Por Horas"});
    private final JLabel lblValor1 = new JLabel("Bonificación");
    private final JLabel lblValor2 = new JLabel("Descuento (%)");
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Tipo", "Salario", "Categoría"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public Sistema() {
        setTitle("Sistema de Gestión de Empleados");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del empleado"));
        formulario.add(new JLabel("Código"));
        formulario.add(txtCodigo);
        formulario.add(new JLabel("Nombre"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Salario base"));
        formulario.add(txtSalario);
        formulario.add(new JLabel("Tipo"));
        formulario.add(cbTipo);
        formulario.add(lblValor1);
        formulario.add(txtValor1);
        formulario.add(lblValor2);
        formulario.add(txtValor2);

        JPanel botones = new JPanel(new GridLayout(2, 4, 5, 5));
        botones.add(boton("Registrar", this::registrar));
        botones.add(boton("Calcular salario", this::calcular));
        botones.add(boton("Buscar", this::buscar));
        botones.add(boton("Clonar empleado", this::clonar));
        botones.add(boton("Comparar", this::comparar));
        botones.add(boton("Salario máximo", this::salarioMaximo));
        botones.add(boton("Promedio", this::promedio));
        botones.add(boton("Limpiar", this::limpiar));

        JPanel superior = new JPanel(new BorderLayout(0, 5));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        cbTipo.addActionListener(e -> actualizarEtiquetas());
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    private void actualizarEtiquetas() {
        boolean administrativo = cbTipo.getSelectedIndex() == 0;
        lblValor1.setText(administrativo ? "Bonificación" : "Horas trabajadas");
        lblValor2.setText(administrativo ? "Descuento (%)" : "Tarifa por hora");
        txtValor1.setText("");
        txtValor2.setText("");
    }

    private void registrar() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            if (codigo.isEmpty() || nombre.isEmpty() || txtSalario.getText().trim().isEmpty()
                    || txtValor1.getText().trim().isEmpty() || txtValor2.getText().trim().isEmpty()) {
                aviso("Complete todos los campos.");
                return;
            }
            if (gestor.existeCodigo(codigo)) {
                error("El código ya existe.");
                return;
            }

            double salario = Double.parseDouble(txtSalario.getText().trim());
            double valor1 = Double.parseDouble(txtValor1.getText().trim());
            double valor2 = Double.parseDouble(txtValor2.getText().trim());
            if (salario < 0 || valor1 < 0 || valor2 < 0) {
                error("No se permiten valores negativos.");
                return;
            }

            Empleado empleado;
            if (cbTipo.getSelectedIndex() == 0) {
                if (valor2 > 100) {
                    error("El descuento debe estar entre 0 y 100.");
                    return;
                }
                empleado = new EmpleadoAdministrativo(codigo, nombre, salario, valor1, valor2);
            } else {
                if (valor1 != Math.rint(valor1)) {
                    error("Las horas deben ser un número entero.");
                    return;
                }
                empleado = new EmpleadoPorHoras(codigo, nombre, salario, (int) valor1, valor2);
            }

            gestor.agregar(empleado);
            actualizarTabla();
            limpiar();
            seleccionar(empleado);
            mensaje("Empleado registrado correctamente.", "Registro", INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            error("Ingrese valores numéricos válidos.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void calcular() {
        Empleado empleado = seleccionado();
        if (empleado == null) {
            aviso("Seleccione un empleado.");
            return;
        }
        mensaje(empleado.mostrarInformacion(), "Salario", INFORMATION_MESSAGE);
    }

    private void buscar() {
        String codigo = showInputDialog(this, "Código del empleado:", "Buscar", QUESTION_MESSAGE);
        if (codigo == null) {
            return;
        }
        Empleado empleado = gestor.buscar(codigo);
        if (empleado == null) {
            error("No existe el código " + codigo.trim() + ".");
            return;
        }
        seleccionar(empleado);
        mensaje(empleado.mostrarInformacion(), "Empleado encontrado", INFORMATION_MESSAGE);
    }

    private void clonar() {
        Empleado original = seleccionado();
        if (original == null) {
            aviso("Seleccione un empleado para clonar.");
            return;
        }
        String codigo = showInputDialog(this, "Nuevo código para la copia:", "Clonar", QUESTION_MESSAGE);
        if (codigo == null) {
            return;
        }
        codigo = codigo.trim();
        if (codigo.isEmpty()) {
            aviso("El código no puede estar vacío.");
            return;
        }
        if (gestor.existeCodigo(codigo)) {
            error("El código ya existe.");
            return;
        }

        Empleado clon = original.clone();
        clon.setCodigo(codigo);
        gestor.agregar(clon);
        actualizarTabla();
        seleccionar(clon);
        mensaje("Copia registrada correctamente.", "Clonar", INFORMATION_MESSAGE);
    }

    private void comparar() {
        int[] filas = tabla.getSelectedRows();
        if (filas.length != 2) {
            aviso("Seleccione dos empleados con Ctrl o Shift.");
            return;
        }
        Empleado primero = gestor.getEmpleados().get(filas[0]);
        Empleado segundo = gestor.getEmpleados().get(filas[1]);
        boolean mismaReferencia = primero == segundo;
        boolean mismosDatos = primero.getCodigo().equalsIgnoreCase(segundo.getCodigo())
                && primero.getNombre().equalsIgnoreCase(segundo.getNombre())
                && primero.getTipo().equals(segundo.getTipo())
                && Double.compare(primero.calcularSalario(), segundo.calcularSalario()) == 0;
        mensaje("Misma referencia: " + mismaReferencia
                + "\nMismos datos: " + mismosDatos
                + "\n\n" + primero.mostrarInformacion()
                + "\n\n" + segundo.mostrarInformacion(), "Comparar", INFORMATION_MESSAGE);
    }

    private void salarioMaximo() {
        if (gestor.cantidad() == 0) {
            aviso("No hay empleados registrados.");
            return;
        }
        mensaje(gestor.obtenerMayorSalario().mostrarInformacion(), "Salario máximo", INFORMATION_MESSAGE);
    }

    private void promedio() {
        if (gestor.cantidad() == 0) {
            aviso("No hay empleados registrados.");
            return;
        }
        mensaje("Salario promedio: L. " + String.format("%.2f", gestor.calcularSalarioPromedio()), "Promedio", INFORMATION_MESSAGE);
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtSalario.setText("");
        txtValor1.setText("");
        txtValor2.setText("");
        cbTipo.setSelectedIndex(0);
        actualizarEtiquetas();
        tabla.clearSelection();
        txtCodigo.requestFocusInWindow();
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (Empleado empleado : gestor.getEmpleados()) {
            modelo.addRow(new Object[]{
                empleado.getCodigo(),
                empleado.getNombre(),
                empleado.getTipo(),
                String.format("L. %.2f", empleado.calcularSalario()),
                empleado.obtenerCategoria()
            });
        }
    }

    private void seleccionar(Empleado empleado) {
        int indice = gestor.getEmpleados().indexOf(empleado);
        if (indice >= 0) {
            tabla.setRowSelectionInterval(indice, indice);
            tabla.scrollRectToVisible(tabla.getCellRect(indice, 0, true));
        }
    }

    private Empleado seleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : gestor.getEmpleados().get(fila);
    }

    private void aviso(String texto) {
        mensaje(texto, "Aviso", WARNING_MESSAGE);
    }

    private void error(String texto) {
        mensaje(texto, "Error", ERROR_MESSAGE);
    }

    private void mensaje(String texto, String titulo, int tipo) {
        showMessageDialog(this, texto, titulo, tipo);
    }
}
