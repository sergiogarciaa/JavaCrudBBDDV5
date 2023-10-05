package Controladores;

import Servicios.*;
import dtos.LibroDto;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        InterfazBBDD cpi = new ImplementacionBBDD();
        InterfazSwitch switchCase = new ImplementacionSwitch();

        try {
            Connection conexion = cpi.llamadaBBDD();

            if (conexion != null) {
                switchCase.Switchcase(conexion);
            }
        } catch (Exception e) {
            System.out.println("[ERROR-Main] Se ha producido un error al ejecutar la aplicación: " + e);
        }
    }
}
