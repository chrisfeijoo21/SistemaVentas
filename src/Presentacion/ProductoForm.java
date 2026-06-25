package Presentacion;

import Dominio.*;
import Negocio.*;
import Utilidades.Mensajes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoForm extends JFrame {
    private final ProductoNegocio negocio = new ProductoNegocio();
    private final CategoriaNegocio categoriaNegocio = new CategoriaNegocio();
    private JTextField txtNombre, txtDescripcion, txtPrecio;
    private JComboBox<Categoria> cboCategoria;
    private JCheckBox chkEstado;
    private JTable tabla; private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private int idSeleccionado = -1;

    public ProductoForm() {
        setTitle("ABM Productos");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        cargarCategorias();
        cargarTabla();
        setVisible(true);
    }
    private void cargarTabla() { // Método encargado de cargar y actualizar los datos visualizados
                                // en la tabla de productos.
        modeloTabla.setRowCount(0); // Se limpia la tabla para evitar duplicación de registros
                                    // cada vez que se actualiza la información.
        List<producto> lista = negocio.listarTodos(); // Se obtiene desde la capa de negocio el listado completo
                                                     // de productos registrados en el sistema.
        for (producto p : lista) {  // Se recorren los productos obtenidos para mostrar
                                    // su información dentro de la tabla.
            modeloTabla.addRow(new Object[]{   // Se agregan los datos del producto como una nueva fila
                                                // en el modelo de la JTable.
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getPrecio(),
                    p.getCategoria().getNombre(), // Se accede a la categoría asociada al producto
                                                    // para mostrar su nombre en pantalla.
                    p.isEstado()
            });
        }
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtDescripcion = new JTextField(20);
        panelForm.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelForm.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtPrecio = new JTextField(10);
        panelForm.add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelForm.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cboCategoria = new JComboBox<>();
        panelForm.add(cboCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelForm.add(new JLabel("Activo:"), gbc);
        gbc.gridx = 1;
        chkEstado = new JCheckBox();
        chkEstado.setSelected(true);
        panelForm.add(chkEstado, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGuardar  = new JButton("Guardar");
        btnEditar   = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Descripción", "Precio", "Categoría", "Activo"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(5).setMaxWidth(60);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
                txtDescripcion.setText((String) modeloTabla.getValueAt(fila, 2));
                txtPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
                String nomCat = (String) modeloTabla.getValueAt(fila, 4);
                for (int i = 0; i < cboCategoria.getItemCount(); i++) {
                    if (cboCategoria.getItemAt(i).getNombre().equals(nomCat)) {
                        cboCategoria.setSelectedIndex(i);
                        break;
                    }
                }
                chkEstado.setSelected((boolean) modeloTabla.getValueAt(fila, 5));
                btnEditar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnGuardar.setEnabled(false);
            }
        });
    }

    private void cargarCategorias() {
        cboCategoria.removeAllItems();
        for (Categoria c : categoriaNegocio.listarTodas()) cboCategoria.addItem(c);
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            Mensajes.error("Ingrese el nombre."); return false;
        }
        try {
            double p = Double.parseDouble(txtPrecio.getText().trim());
            if (p <= 0) { Mensajes.error("Precio debe ser mayor a 0."); return false; }
        } catch (NumberFormatException ex) {
            Mensajes.error("Precio inválido."); return false;
        }
        return cboCategoria.getSelectedItem() != null;
    }

    private void guardar() {
        if (!validarCampos()) return;
        boolean ok = negocio.guardar(
                txtNombre.getText().trim(),
                txtDescripcion.getText().trim(),
                chkEstado.isSelected(),
                Double.parseDouble(txtPrecio.getText().trim()),
                (Categoria) cboCategoria.getSelectedItem());
        if (ok) {
            Mensajes.exito("Producto guardado.");
            limpiar();
            cargarTabla();
        }
        else Mensajes.error("No se pudo guardar.");
    }
    private void actualizar() {
        if (idSeleccionado < 0 || !validarCampos()) return;
        producto p = new producto(idSeleccionado, txtNombre.getText().trim(),txtDescripcion.getText().trim(), chkEstado.isSelected(), Double.parseDouble(txtPrecio.getText().trim()), (Categoria) cboCategoria.getSelectedItem());
        if (negocio.actualizar(p)) {
            Mensajes.exito("Actualizado.");
            limpiar();
            cargarTabla();
        }
        else
            Mensajes.error("No se pudo actualizar.");
    }
    private void limpiar() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        chkEstado.setSelected(true);
        idSeleccionado = -1;
        tabla.clearSelection();
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void eliminar() {
        if (idSeleccionado < 0) return;
        if (!Mensajes.confirmar("¿Eliminar este producto?")) return;
        if (negocio.eliminar(idSeleccionado)) {
            Mensajes.exito("Producto eliminado.");
            limpiar();
            cargarTabla();
        } else {
            Mensajes.error("No se pudo eliminar. Puede tener ventas asociadas.");
        }
    }
}