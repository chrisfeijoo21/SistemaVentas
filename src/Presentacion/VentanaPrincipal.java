package Presentacion;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión Gastronómica");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI(); setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Sistema Gastronómico", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelMenu = new JPanel(new GridLayout(6, 1, 10, 10));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60));

        JButton btnCategorias = new JButton("Gestión de Categorías");
        JButton btnProductos  = new JButton("Gestión de Productos");
        JButton btnStock      = new JButton("Gestión de Stock");
        JButton btnVentas     = new JButton("Registrar Venta");
        JButton btnConsulta   = new JButton("Consultar Ventas");
        JButton btnReporte    = new JButton("Reporte de Ventas");

        panelMenu.add(btnCategorias); panelMenu.add(btnProductos);
        panelMenu.add(btnStock);      panelMenu.add(btnVentas);
        panelMenu.add(btnConsulta);   panelMenu.add(btnReporte);
        add(panelMenu, BorderLayout.CENTER);

        btnCategorias.addActionListener(e -> new CategoriaForm());
        btnProductos.addActionListener(e -> new ProductoForm());
        btnStock.addActionListener(e -> new StockForm());
        btnVentas.addActionListener(e -> new VentasForm());
        btnConsulta.addActionListener(e -> new ConsultaVentasForm());
        btnReporte.addActionListener(e -> new ReporteVentasForm());
    }
}