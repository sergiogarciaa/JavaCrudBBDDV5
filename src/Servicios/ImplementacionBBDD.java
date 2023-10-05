package Servicios;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ImplementacionBBDD implements InterfazBBDD{

    /**
     * Implementacion de la conexion con Base De Datos
     * @return Conexion con la base de datos
     */

    @Override
    public Connection llamadaBBDD() {
        Connection conexion = null;
        String[] parametrosConexion = configuracionConexion(); //url, user, pass

        if(!parametrosConexion[2].isEmpty()) { //Se controla que los parámetros de conexión se completen
            try {
                //Instancia un objeto de la clase interfaz que se le pasa
                Class.forName("org.postgresql.Driver");

                //Se establece la conexión
                //Si pgadmin no tiene abierta la bd, no será posible establecer conexion contra ella
                conexion = DriverManager.getConnection(parametrosConexion[0],parametrosConexion[1],parametrosConexion[2]);
                boolean esValida = conexion.isValid(50000);
                if(esValida == false) {
                    conexion = null;
                }
                System.out.println(esValida ? "[INFORMACIÓN-Conexion-Implementacion-llamadaBBDD] La conexion es válida" : "[ERROR-Conexion-Implementacion-llamadaBBDD] La conexion a la BD no válida");
                return conexion;

            } catch (ClassNotFoundException cnfe) {
                System.out.println("[ERROR-Conexion-Implementacion-llamadaBBDD] Error en el registro del driver: " + cnfe);
                return conexion;
            } catch (SQLException jsqle) {
                System.out.println("[ERROR-Conexion-Implementacion-llamadaBBDD] Error de conexión a la Base De Datos (" + parametrosConexion[0] + "): " + jsqle);
                return conexion;
            }

        }else {
            System.out.println("[ERROR-Conexion-Implementacion-llamadaBBDD] Los parametros de conexion no se han establecido correctamente");
            return conexion;
        }
    }
    /**
     * Método configura los parámetros de la conexión de propiedasConex.properties
     * return ventor de string con: url, user, pass
     */
    private String[] configuracionConexion() {

        String user = "", pass = "", port = "", host = "", db = "", url = "";

        Properties propiedadesConex = new Properties();
        try {
            //propiedadesConexionPostgresql.load(getClass().getResourceAsStream("conexion_postgresql.properties"));
            propiedadesConex.load(new FileInputStream(new File("C:\\Users\\Puesto14\\Desktop\\DWS\\JavaCrudBBDDV2\\ConexionBD\\src\\util\\propiedasConex.properties")));
            user = propiedadesConex.getProperty("user");
            pass = propiedadesConex.getProperty("pass");
            port = propiedadesConex.getProperty("port");
            host = propiedadesConex.getProperty("host");
            db = propiedadesConex.getProperty("db");
            url = "jdbc:postgresql://" + host + ":" + port + "/" + db;
            String[] stringConfiguracion = {url, user, pass};

            return stringConfiguracion;

        } catch (Exception e) {
            System.out.println("[ERROR-Conexion-Implementacion-configuracionConexion] - Error al acceder al fichero propiedades de conexion.");
            return null;
        }
    }
}

