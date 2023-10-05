package Servicios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

public interface InterfazBBDD {
    /**
     * Metodo para realizar la conexion con la base de datos
     * @return
     */
    public Connection llamadaBBDD();
}
