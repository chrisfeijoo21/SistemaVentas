package Presentacion;

import Dominio.Stock;
import Negocio.StockNegocio;
import Utilidades.Mensajes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockForm extends JFrame {

    private final StockNegocio negocio = new StockNegocio();
    private JTextField txtCantidad, txtStockMinimo;
    private JLabel lblProducto;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar, btnLimpiar;
    private int idStockSeleccionado = -1;

    public StockForm() {
        setTitle("Gestión de Stock");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI(); cargarTabla(); setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Editar Stock"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,8,5,8); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0;
        panelForm.add(new JLabel("Producto:"), gbc);
        gbc.gridx=1; gbc.weightx=1;
        lblProducto = new JLabel("-"); panelForm.add(lblProducto, gbc);

        gbc.gridx=0; gbc.gridy=1; gbc.weightx=0;
        panelForm.add(new JLabel("Cantidad disponible:"), gbc);
        gbc.gridx=1; gbc.weightx=1;
        txtCantidad = new JTextField(10); panelForm.add(txtCantidad, gbc);

        gbc.gridx=0; gbc.gridy=2; gbc.weightx=0;
        panelForm.add(new JLabel("Stock mínimo:"), gbc);
        gbc.gridx=1; gbc.weightx=1;
        txtStockMinimo = new JTextField(10); panelForm.add(txtStockMinimo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnActualizar = new JButton("Actualizar");
        btnLimpiar = new JButton("Limpiar");
        btnActualizar.setEnabled(false);
        panelBotones.add(btnActualizar); panelBotones.add(btnLimpiar);

        JPanel panelSup = new JPanel(new BorderLayout());
        panelSup.add(panelForm, BorderLayout.CENTER);
        panelSup.add(panelBotones, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Producto", "Categoría", "Disponible", "Mín.", "Última actualización"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(panelSup, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnActualizar.addActionListener(e -> actualizar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                idStockSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                lblProducto.setText((String) modeloTabla.getValueAt(fila, 1));
                txtCantidad.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
                txtStockMinimo.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
                btnActualizar.setEnabled(true);
            }
        });
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Stock s : negocio.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                    s.getIdStock(), s.getProducto().getNombre(),
                    s.getProducto().getCategoria().getNombre(),
                    s.getCantidadDisponible(), s.getStockMinimo(),
                    s.getUltimaActualizacion() != null ? s.getUltimaActualizacion().toString() : "-"
            });
        }
    }

    private void actualizar() {
        if (idStockSeleccionado < 0) return;
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            int minimo = Integer.parseInt(txtStockMinimo.getText().trim());
            if (cantidad < 0 || minimo < 0) { Mensajes.error("Valores no pueden ser negativos."); return; }
            for (Stock s : negocio.listarTodos()) {
                if (s.getIdStock() == idStockSeleccionado) {
                    s.setCantidadDisponible(cantidad); s.setStockMinimo(minimo);
                    if (negocio.actualizar(s)) { Mensajes.exito("Stock actualizado."); limpiar(); cargarTabla(); }
                    else Mensajes.error("No se pudo actualizar.");
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            Mensajes.error("Ingrese valores numéricos válidos.");
        }
    }

    private void limpiar() {
        lblProducto.setText("-"); txtCantidad.setText(""); txtStockMinimo.setText("");
        idStockSeleccionado = -1; tabla.clearSelection(); btnActualizar.setEnabled(false);
    }
}