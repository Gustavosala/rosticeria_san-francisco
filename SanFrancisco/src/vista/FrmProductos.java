package vista;

import dao.ProductoDAO;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class FrmProductos extends JFrame {

    
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;

    private JButton btnRegistrar;
    private JButton btnConsultar;
    private JButton btnLimpiar;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    
    private ProductoDAO productoDAO;

    public FrmProductos() {
    	getContentPane().setBackground(Color.YELLOW);

        productoDAO = new ProductoDAO();

        configurarVentana();
        crearInterfaz();
        cargarProductos();
    }


    private void configurarVentana() {

        setTitle("Pollería San Francisco - Productos");

        setSize(700, 650);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout(10, 10));
    }


    private void crearInterfaz() {


        JLabel lblTitulo = new JLabel(
                "POLLERÍA SAN FRANCISCO",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        getContentPane().add(lblTitulo, BorderLayout.NORTH);


        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(255, 255, 102));

        panelDatos.setLayout(
                new GridLayout(3, 2, 10, 10)
        );

        panelDatos.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del producto"
                )
        );


        JLabel lblNombre = new JLabel("Nombre:");

        txtNombre = new JTextField();



        JLabel lblPrecio = new JLabel("Precio:");

        txtPrecio = new JTextField();



        JLabel lblStock = new JLabel("Stock:");

        txtStock = new JTextField();


        panelDatos.add(lblNombre);
        panelDatos.add(txtNombre);

        panelDatos.add(lblPrecio);
        panelDatos.add(txtPrecio);

        panelDatos.add(lblStock);
        panelDatos.add(txtStock);



        JPanel panelBotones = new JPanel(
                new FlowLayout()
        );

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setBackground(Color.YELLOW);

        btnConsultar = new JButton("Consultar");
        btnConsultar.setBackground(Color.GREEN);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(Color.RED);


        panelBotones.add(btnRegistrar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnLimpiar);


        JPanel panelSuperior = new JPanel(
                new BorderLayout(10, 10)
        );
        panelSuperior.setBackground(Color.ORANGE);

        panelSuperior.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 20, 10, 20
                )
        );

        panelSuperior.add(
                panelDatos,
                BorderLayout.CENTER
        );

        panelSuperior.add(
                panelBotones,
                BorderLayout.SOUTH
        );


        getContentPane().add(
                panelSuperior,
                BorderLayout.CENTER
        );


        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Stock");


        tablaProductos = new JTable(modeloTabla);

        tablaProductos.setRowHeight(25);

        tablaProductos.getTableHeader()
                .setReorderingAllowed(false);


        JScrollPane scrollTabla =
                new JScrollPane(tablaProductos);

        scrollTabla.setBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Productos registrados", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 102, 51))
        );


        getContentPane().add(
                scrollTabla,
                BorderLayout.SOUTH
        );


        btnRegistrar.addActionListener(e ->
                registrarProducto()
        );

        btnConsultar.addActionListener(e ->
                cargarProductos()
        );

        btnLimpiar.addActionListener(e ->
                limpiarCampos()
        );
    }


    private void registrarProducto() {

        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();


        if (nombre.isEmpty()
                || precioTexto.isEmpty()
                || stockTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todos los campos son obligatorios.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        try {

            double precio =
                    Double.parseDouble(precioTexto);

            int stock =
                    Integer.parseInt(stockTexto);



            if (precio < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "El precio no puede ser negativo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            if (stock < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "El stock no puede ser negativo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            Producto producto =
                    new Producto(
                            nombre,
                            precio,
                            stock
                    );


            boolean registrado =
                    productoDAO.registrar(producto);


            if (registrado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Producto registrado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );


                limpiarCampos();

                cargarProductos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el producto.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Precio y stock deben ser números válidos.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void cargarProductos() {


        modeloTabla.setRowCount(0);



        List<Producto> lista =
                productoDAO.consultar();


        for (Producto producto : lista) {

            modeloTabla.addRow(
                    new Object[]{
                            producto.getIdProducto(),
                            producto.getNombre(),
                            producto.getPrecio(),
                            producto.getStock()
                    }
            );
        }
    }


    private void limpiarCampos() {

        txtNombre.setText("");

        txtPrecio.setText("");

        txtStock.setText("");

        txtNombre.requestFocus();
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FrmProductos ventana =
                    new FrmProductos();

            ventana.setVisible(true);
        });
    }
}