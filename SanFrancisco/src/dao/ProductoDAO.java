package dao;

import conexion.Conexion;
import modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    @Override
    public boolean registrar(Producto producto) {

        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";

        try {

            Connection conexion = Conexion.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());

            ps.executeUpdate();

            ps.close();
            conexion.close();

            return true;

        } catch (Exception e) {

            System.out.println("Error al registrar producto:");
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public List<Producto> consultar() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos";

        try {

            Connection conexion = Conexion.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Producto producto = new Producto();

                producto.setIdProducto(
                    rs.getInt("id_producto")
                );

                producto.setNombre(
                    rs.getString("nombre")
                );

                producto.setPrecio(
                    rs.getDouble("precio")
                );

                producto.setStock(
                    rs.getInt("stock")
                );

                lista.add(producto);
            }

            rs.close();
            ps.close();
            conexion.close();

        } catch (Exception e) {

            System.out.println("Error al consultar productos:");
            e.printStackTrace();
        }

        return lista;
    }
}