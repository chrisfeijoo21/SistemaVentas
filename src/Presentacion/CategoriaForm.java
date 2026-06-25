package Presentacion;

import Dominio.Categoria; import Negocio.CategoriaNegocio; import Utilidades.Mensajes;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.util.List;

public class CategoriaForm extends JFrame {
    private final CategoriaNegocio negocio = new CategoriaNegocio();
    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private int idSeleccionado = -1;

    public CategoriaForm() {
        setTitle("ABM Categorías");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        cargarTabla();
        setVisible(true);
    }
    // Método encargado de inicializar todos los componentes gráficos
    // de la interfaz del ABM de Categorías.
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de Categoría"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            public boolean isCellEditable(int r, int c)
            {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Eventos: guardar, actualizar, eliminar, limpiar, selección tabla
        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
                btnEditar.setEnabled(true); btnEliminar.setEnabled(true);
                btnGuardar.setEnabled(false);
            }
        });
        JPanel panelSup = new JPanel(new BorderLayout());
        panelSup.add(panelForm, BorderLayout.CENTER);
        panelSup.add(panelBotones, BorderLayout.SOUTH);
        add(panelSup, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Categoria c : negocio.listarTodas())
            modeloTabla.addRow(new Object[]{c.getIdCategoria(), c.getNombre()});
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            Mensajes.error("Ingrese un nombre."); // validacion de ingreso de datos
            return;
        }
        if (negocio.guardar(nombre)) {
            Mensajes.exito("Guardado."); //si esta escrita la catergoria la guarda con exito
            limpiar();
            cargarTabla();
        }
        else Mensajes.error("No se pudo guardar.");
    }
    private void actualizar() {
        if (idSeleccionado < 0) return;
        Categoria cat = new Categoria(idSeleccionado, txtNombre.getText().trim());
        if (negocio.actualizar(cat)) {
            Mensajes.exito("Actualizado.");
            limpiar();
            cargarTabla();
        }
        else Mensajes.error("No se pudo actualizar.");
    }
    private void eliminar() {
        if (idSeleccionado < 0 || !Mensajes.confirmar("¿Eliminar?"))
            return;
        if (negocio.eliminar(idSeleccionado)) {
            Mensajes.exito("Eliminado.");
            limpiar();
            cargarTabla();
        }
        else Mensajes.error("No se pudo eliminar.");
    }
    private void limpiar() {
        txtNombre.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
