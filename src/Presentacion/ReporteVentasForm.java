package Presentacion;

import Negocio.VentaNegocio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReporteVentasForm extends JFrame {

    private final VentaNegocio negocio = new VentaNegocio();
    private DefaultTableModel modeloDia, modeloProducto;
    private JLabel lblTotalDias, lblTotalProductos;

    public ReporteVentasForm() {
        setTitle("Reporte de Ventas");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI(); cargarReportes();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JTabbedPane tabs = new JTabbedPane();

        // Pestaña 1: Por día
        modeloDia = new DefaultTableModel(
                new String[]{"Fecha", "Cant. Ventas", "Total del día"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tablaDia = new JTable(modeloDia);
        tablaDia.getColumnModel().getColumn(1).setMaxWidth(100);
        lblTotalDias = new JLabel("Total general: -", SwingConstants.RIGHT);
        lblTotalDias.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalDias.setBorder(BorderFactory.createEmptyBorder(5,0,5,10));
        JPanel panelDia = new JPanel(new BorderLayout(5,5));
        panelDia.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelDia.add(new JScrollPane(tablaDia), BorderLayout.CENTER);
        panelDia.add(lblTotalDias, BorderLayout.SOUTH);

        // Pestaña 2: Por producto
        modeloProducto = new DefaultTableModel(
                new String[]{"Producto", "Unidades vendidas", "Total recaudado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaProducto = new JTable(modeloProducto);
        tablaProducto.getColumnModel().getColumn(1).setMaxWidth(140);
        lblTotalProductos = new JLabel("Total general: -", SwingConstants.RIGHT);
        lblTotalProductos.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalProductos.setBorder(BorderFactory.createEmptyBorder(5,0,5,10));
        JPanel panelProducto = new JPanel(new BorderLayout(5,5));
        panelProducto.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelProducto.add(new JScrollPane(tablaProducto), BorderLayout.CENTER);
        panelProducto.add(lblTotalProductos, BorderLayout.SOUTH);

        tabs.addTab("Por día", panelDia);
        tabs.addTab("Por producto", panelProducto);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarReportes());
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelBtn.add(btnActualizar);

        add(tabs, BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private void cargarReportes() {
        modeloDia.setRowCount(0);
        double totalDia = 0;
        for (Object[] f : negocio.reportePorDia()) {
            modeloDia.addRow(new Object[]{f[0], f[1], "$" + String.format("%.2f", (double) f[2])});
            totalDia += (double) f[2];
        }
        lblTotalDias.setText("Total general: $" + String.format("%.2f", totalDia));

        modeloProducto.setRowCount(0);
        double totalProd = 0;
        for (Object[] f : negocio.reportePorProducto()) {
            modeloProducto.addRow(new Object[]{f[0], f[1], "$" + String.format("%.2f", (double) f[2])});
            totalProd += (double) f[2];
        }
        lblTotalProductos.setText("Total general: $" + String.format("%.2f", totalProd));
    }
}