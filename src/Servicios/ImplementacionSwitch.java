package Servicios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;


public class ImplementacionSwitch implements InterfazSwitch{
    @Override
    public void Switchcase(Connection conexion) {
        InterfazCrud consultasCrud = new ImplementacionCrud();
        boolean seguir = true;

        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("1. - Select");
            System.out.println("2. - Insert");
            System.out.println("3. - Update");
            System.out.println("4. - Delete");
            System.out.println("5. - Salir");

            System.out.println("Elija la opción que desea");
            int opcion = sc.nextInt();
            switch (opcion){
                case 1 :
                    boolean tipoSelect = false;
                    System.out.println("Quiere hacer un select de todo o de un libro en concreto?(T = Todo | C = Concreto)");
                    String respuesta = sc.next();
                    // ---------- Todos los libros ----------------
                    if (respuesta.toLowerCase().equals("t")){
                        try {
                            /*
                             *   Se establece el bool como true para que
                             *      cuando lo pase al Select, sepa que se tienen que mostrar todos
                             */
                            tipoSelect = true;
                            consultasCrud.select(conexion, tipoSelect);

                        } catch (Exception e) {
                            System.out.println("[ERROR-Implementacion-Switch-TodosLosLibros] Se ha producido un error al intentar mostrar todos los libros: " + e);
                        }
                    }
                    //--------- Uno en concreto -----------------
                    else if (respuesta.toLowerCase().equals("c")){
                        try{
                            /*
                             *   Se establece el bool como false para que
                             *      cuando lo pase al Select, sepa que solo se busca a uno
                             */
                            tipoSelect = false;
                            consultasCrud.select(conexion, tipoSelect);
                            // Pedir cual quiere ver

                        }catch (Exception e){
                            System.out.println("[ERROR-Implementacion-Switch-TodosLosLibros] Se ha producido un error al intentar mostrar libros en concreto: " + e);
                        }
                    }
                    else {
                        System.err.println("No ha elegido ninguna opcion correcta");
                    }
                    break;
                case 2 :
                    System.out.println(consultasCrud.crearLibros(conexion));
                    break;
                case 3 :
                    System.out.println(consultasCrud.update(conexion));
                    break;
                case 4 :
                    System.out.println(consultasCrud.delete(conexion));
                    break;
                case 5 :
                    System.out.println("[INFORMACIÓN-Consultas-Implementacion-seleccionaLibros] Cierre conexión, declaración y resultado");
                    try {
                        System.out.println("La conexion se ha cerrado.");
                        conexion.close();
                        seguir = false;
                        break;
                    } catch (SQLException e) {
                        System.out.println("No se ha podido cerrar la conexion con la base de datos.");
                    }
            }
        }while (seguir);

    }
}
