package vista;

import conexion.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class FrmConsulta extends JFrame {

    // Componentes
    private JComboBox<String> cmbTablas;
    private JButton btnConsultar;
    private JButton btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public FrmConsulta() {

        configurarVentana();
        crearInterfaz();

    }

    // =========================================================
    // CONFIGURAR VENTANA
    // =========================================================

    private void configurarVentana() {

        setTitle("Pollería San Francisco - Consultas");

        setSize(900, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
    }


    // =========================================================
    // CREAR INTERFAZ
    // =========================================================

    private void crearInterfaz() {

        // -----------------------------------------------------
        // TITULO
        // -----------------------------------------------------

        JLabel lblTitulo = new JLabel(
                "POLLERÍA SAN FRANCISCO",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(lblTitulo, BorderLayout.NORTH);


        // -----------------------------------------------------
        // PANEL DE CONSULTA
        // -----------------------------------------------------

        JPanel panelConsulta = new JPanel(
                new FlowLayout()
        );

        JLabel lblTabla = new JLabel(
                "Consultar tabla:"
        );


        // ComboBox con nuestras tablas

        cmbTablas = new JComboBox<>();

        cmbTablas.addItem("productos");
        cmbTablas.addItem("clientes");
        cmbTablas.addItem("pedidos");
        cmbTablas.addItem("detalle_pedido");


        // Botón consultar

        btnConsultar = new JButton(
                "Consultar"
        );


        // Botón limpiar

        btnLimpiar = new JButton(
                "Limpiar"
        );


        panelConsulta.add(lblTabla);
        panelConsulta.add(cmbTablas);
        panelConsulta.add(btnConsultar);
        panelConsulta.add(btnLimpiar);


        add(
                panelConsulta,
                BorderLayout.NORTH
        );
        
        // -----------------------------------------------------
        // TABLA
        // -----------------------------------------------------

        modeloTabla = new DefaultTableModel();

        tabla = new JTable(modeloTabla);

        tabla.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        tabla.setRowHeight(25);


        JScrollPane scroll =
                new JScrollPane(tabla);


        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Resultados"
                )
        );


        add(
                scroll,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // EVENTOS
        // -----------------------------------------------------

        btnConsultar.addActionListener(e ->
                consultarTabla()
        );


        btnLimpiar.addActionListener(e ->
                limpiarTabla()
        );
    }


    // =========================================================
    // CONSULTAR TABLA
    // =========================================================

    private void consultarTabla() {

        String tablaSeleccionada =
                cmbTablas.getSelectedItem().toString();


        // Limpiar resultados anteriores

        modeloTabla.setRowCount(0);

        modeloTabla.setColumnCount(0);


        String sql =
                "SELECT * FROM `" +
                tablaSeleccionada +
                "`";


        try {

            Connection conexion =
                    Conexion.conectar();


            PreparedStatement ps =
                    conexion.prepareStatement(sql);


            ResultSet rs =
                    ps.executeQuery();


            // -------------------------------------------------
            // OBTENER INFORMACIÓN DE LAS COLUMNAS
            // -------------------------------------------------

            ResultSetMetaData metaData =
                    rs.getMetaData();


            int numeroColumnas =
                    metaData.getColumnCount();


            // -------------------------------------------------
            // CREAR COLUMNAS DEL JTable
            // -------------------------------------------------

            for (int i = 1;
                 i <= numeroColumnas;
                 i++) {

                modeloTabla.addColumn(
                        metaData.getColumnName(i)
                );
            }


            // -------------------------------------------------
            // AGREGAR REGISTROS
            // -------------------------------------------------

            while (rs.next()) {

                Object[] fila =
                        new Object[numeroColumnas];


                for (int i = 1;
                     i <= numeroColumnas;
                     i++) {

                    fila[i - 1] =
                            rs.getObject(i);
                }


                modeloTabla.addRow(fila);
            }


            // -------------------------------------------------
            // CERRAR CONEXIÓN
            // -------------------------------------------------

            rs.close();

            ps.close();

            conexion.close();


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar la tabla:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // LIMPIAR TABLA
    // =========================================================

    private void limpiarTabla() {

        modeloTabla.setRowCount(0);

        modeloTabla.setColumnCount(0);
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FrmConsulta ventana =
                    new FrmConsulta();

            ventana.setVisible(true);

        });
    }
}