package dao;

import java.util.List;
import modelo.Producto;

public interface IProductoDAO {

    boolean registrar(Producto producto);

    List<Producto> consultar();

}