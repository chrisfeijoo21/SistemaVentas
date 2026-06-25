package Presentacion;

import Dominio.DetalleVenta;
import Dominio.Venta;
import Negocio.VentaNegocio;
import Utilidades.Mensajes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsultaVentasForm extends JFrame {

    private final VentaNegocio negocio = new VentaNegocio();
    private JTable tablaVentas, tablaDetalle;
    private DefaultTableModel modeloVentas, modeloDetalle;
    private JLabel lblTotalSeleccionado;
    private JButton btnAnular;
    private int idVentaSeleccionada = -1;

    public ConsultaVentasForm() {
        setTitle("Consulta de Ventas"); setSize(750, 580);
        setLocationRelativeTo(null); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI(); cargarVentas(); setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        modeloVentas = new DefaultTableModel(new String[]{"ID Venta", "Fecha", "Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaVentas = new JTable(modeloVentas);
        tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVentas.getColumnModel().getColumn(0).setMaxWidth(80);

        JPanel panelVentas = new JPanel(new BorderLayout());
        panelVentas.setBorder(BorderFactory.createTitledBorder("Ventas realizadas"));
        panelVentas.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        btnAnular = new JButton("Anular venta seleccionada");
        btnAnular.setEnabled(false);
        JPanel panelBtnAnular = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelBtnAnular.add(btnAnular);
        panelVentas.add(panelBtnAnular, BorderLayout.SOUTH);

        modeloDetalle = new DefaultTableModel(
                new String[]{"Producto", "Categoría", "Precio unit.", "Cantidad", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalle = new JTable(modeloDetalle);

        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setBorder(BorderFactory.createTitledBorder("Detalle de la venta seleccionada"));
        panelDetalle.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        lblTotalSeleccionado = new JLabel("Total: -", SwingConstants.RIGHT);
        lblTotalSeleccionado.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalSeleccionado.setBorder(BorderFactory.createEmptyBorder(5,0,5,10));
        panelDetalle.add(lblTotalSeleccionado, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelVentas, panelDetalle);
        split.setDividerLocation(250); split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaVentas.getSelectedRow() >= 0) {
                int fila = tablaVentas.getSelectedRow();
                idVentaSeleccionada = (int) modeloVentas.getValueAt(fila, 0);
                cargarDetalle(idVentaSeleccionada, (String) modeloVentas.getValueAt(fila, 2));
                btnAnular.setEnabled(true);
            }
        });
        btnAnular.addActionListener(e -> anularVenta());
    }

    private void cargarVentas() {
        modeloVentas.setRowCount(0); idVentaSeleccionada = -1;
        btnAnular.setEnabled(false); modeloDetalle.setRowCount(0);
        lblTotalSeleccionado.setText("Total: -");
        for (Venta v : negocio.listarTodas()) {
            modeloVentas.addRow(new Object[]{
                    v.getIdVenta(),
                    v.getFecha() != null ? v.getFecha().toString() : "-",
                    "$" + String.format("%.2f", v.getTotal())
            });
        }
    }

    private void cargarDetalle(int idVenta, String totalVenta) {
        modeloDetalle.setRowCount(0);
        for (DetalleVenta d : negocio.listarDetalles(idVenta)) {
            double sub = d.getProducto().getPrecio() * d.getCantidad();
            modeloDetalle.addRow(new Object[]{
                    d.getProducto().getNombre(), d.getProducto().getCategoria().getNombre(),
                    "$" + String.format("%.2f", d.getProducto().getPrecio()),
                    d.getCantidad(), "$" + String.format("%.2f", sub)
            });
        }
        lblTotalSeleccionado.setText("Total venta: " + totalVenta);
    }

    private void anularVenta() {
        if (idVentaSeleccionada < 0) return;
        if (!Mensajes.confirmar("¿Anular la venta #" + idVentaSeleccionada +
                "?\nSe devolverá el stock automáticamente.")) return;
        if (negocio.anularVenta(idVentaSeleccionada)) {
            Mensajes.exito("Venta anulada y stock restaurado correctamente.");
            cargarVentas();
        } else {
            Mensajes.error("No se pudo anular la venta.");
        }
    }
}
