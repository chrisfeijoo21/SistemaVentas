package Presentacion;

import Dominio.DetalleVenta;
import Dominio.producto;
import Negocio.ProductoNegocio;
import Negocio.VentaNegocio;
import Utilidades.Mensajes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class VentasForm extends JFrame {

    private final VentaNegocio ventaNegocio = new VentaNegocio();
    private final ProductoNegocio productoNegocio = new ProductoNegocio();
    private JComboBox<producto> cboProducto;
    private JTextField txtCantidad;
    private JLabel lblPrecioUnitario, lblTotal;
    private JTable tablaDetalle;
    private DefaultTableModel modeloDetalle;
    private JButton btnAgregar, btnQuitar, btnConfirmar, btnCancelar;
    private final List<DetalleVenta> detalles = new ArrayList<>();

    public VentasForm() {
        setTitle("Registrar Venta");
        setSize(700, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        cargarProductos();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel panelProd = new JPanel(new GridBagLayout());
        panelProd.setBorder(BorderFactory.createTitledBorder("Agregar producto a la venta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,8,5,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0;
        panelProd.add(new JLabel("Producto:"), gbc);
        gbc.gridx=1; gbc.weightx=1;
        cboProducto = new JComboBox<>();
        cboProducto.addActionListener(e -> actualizarPrecio());
        panelProd.add(cboProducto, gbc);

        gbc.gridx=0; gbc.gridy=1; gbc.weightx=0;
        panelProd.add(new JLabel("Precio unitario:"), gbc);
        gbc.gridx=1;
        lblPrecioUnitario = new JLabel("$0.00"); panelProd.add(lblPrecioUnitario, gbc);

        gbc.gridx=0; gbc.gridy=2; gbc.weightx=0;
        panelProd.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx=1;
        txtCantidad = new JTextField("1", 5); panelProd.add(txtCantidad, gbc);

        JPanel panelBtnProd = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAgregar = new JButton("Agregar");
        btnQuitar = new JButton("Quitar seleccionado");
        panelBtnProd.add(btnAgregar); panelBtnProd.add(btnQuitar);

        JPanel panelSup = new JPanel(new BorderLayout());
        panelSup.add(panelProd, BorderLayout.CENTER);
        panelSup.add(panelBtnProd, BorderLayout.SOUTH);

        modeloDetalle = new DefaultTableModel(new String[]{"Producto", "Precio unit.", "Cantidad", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c)
            {
                return false;
            }
        };
        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panelInf = new JPanel(new BorderLayout(10, 5));
        panelInf.setBorder(BorderFactory.createEmptyBorder(5,10,10,10));
        lblTotal = new JLabel("Total: $0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel panelBtnVenta = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnConfirmar = new JButton("Confirmar Venta");
        btnCancelar = new JButton("Cancelar");
        panelBtnVenta.add(btnConfirmar); panelBtnVenta.add(btnCancelar);
        panelInf.add(lblTotal, BorderLayout.CENTER);
        panelInf.add(panelBtnVenta, BorderLayout.SOUTH);

        add(panelSup, BorderLayout.NORTH);
        add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        add(panelInf, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnQuitar.addActionListener(e -> quitarProducto());
        btnConfirmar.addActionListener(e -> confirmarVenta());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarProductos() {
        cboProducto.removeAllItems();
        for (producto p : productoNegocio.listarTodos())
            if (p.isEstado()) cboProducto.addItem(p);
        actualizarPrecio();
    }

    private void actualizarPrecio() {
        producto p = (producto) cboProducto.getSelectedItem();
        if (p != null) lblPrecioUnitario.setText("$" + String.format("%.2f", p.getPrecio()));
    }

    private void agregarProducto() {
        producto p = (producto) cboProducto.getSelectedItem();
        if (p == null) { Mensajes.error("Seleccione un producto."); return; }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) { Mensajes.error("La cantidad debe ser mayor a 0."); return; }
        } catch (NumberFormatException ex) { Mensajes.error("Cantidad inválida."); return; }

        for (DetalleVenta d : detalles) {
            if (d.getProducto().getIdProducto() == p.getIdProducto()) {
                d.setCantidad(d.getCantidad() + cantidad);
                d.setSubtotal(d.getCantidad() * p.getPrecio());
                refrescarTabla(); return;
            }
        }
        detalles.add(new DetalleVenta(0, null, p, cantidad, p.getPrecio() * cantidad));
        refrescarTabla();
    }

    private void quitarProducto() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila < 0) { Mensajes.error("Seleccione un producto de la lista."); return; }
        detalles.remove(fila); refrescarTabla();
    }

    private void refrescarTabla() {
        modeloDetalle.setRowCount(0);
        double total = 0;
        for (DetalleVenta d : detalles) {
            double sub = d.getProducto().getPrecio() * d.getCantidad();
            modeloDetalle.addRow(new Object[]{
                    d.getProducto().getNombre(),
                    "$" + String.format("%.2f", d.getProducto().getPrecio()),
                    d.getCantidad(),
                    "$" + String.format("%.2f", sub)
            });
            total += sub;
        }
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void confirmarVenta() {
        if (detalles.isEmpty()) { Mensajes.error("Agregue al menos un producto."); return; }
        if (!Mensajes.confirmar("¿Confirmar la venta?")) return;
        if (ventaNegocio.registrarVenta(detalles)) {
            Mensajes.exito("Venta registrada correctamente."); dispose();
        } else {
            Mensajes.error("No se pudo registrar la venta.");
        }
    }
}