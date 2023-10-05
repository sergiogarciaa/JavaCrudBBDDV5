package Servicios;

import dtos.LibroDto;
import util.adto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ImplementacionCrud implements InterfazCrud {
    //ejecutar la consulta SQL.
    PreparedStatement declaracionParametizada = null;
    Statement declaracionSQL = null;
    //almacenar el resultado de la consulta
    ResultSet resultadoConsulta = null;

    String query = null;
    Scanner sc = new Scanner(System.in);
    ArrayList<LibroDto> listaLibros = new ArrayList<>();
    adto adto = new adto();

    @Override
    public ArrayList<LibroDto> crearLibros(Connection conexionGenerada) {

        try{
            System.out.println("Va a ingresar solo un libro o más de uno? [U = uno | M = mas libros]");
            String ingresos = sc.next();
            // 1 LIBRO
            if (ingresos.toLowerCase().equals("u")){
                listaLibros.clear();
                // Consumir caracter en blanco.
                sc.nextLine();
                System.out.println("Ingrese los detalles del libro:");
                System.out.print("Título: ");
                String titulo = sc.nextLine();
                System.out.print("Autor: ");
                String autor = sc.nextLine();
                System.out.print("ISBN: ");
                String isbn = sc.nextLine();
                System.out.print("Edición: ");
                int edicion = sc.nextInt();
                sc.nextLine(); // Limpia el buffer de entrada

                // Crear un objeto LibroDto con los detalles ingresados
                LibroDto librosNuevos = new LibroDto(0, titulo, autor, isbn, edicion);
                listaLibros.add(librosNuevos);
            }
            // MUCHOS LIBROS
            else{
                while (true) {
                    for (int i = 0;  i < 5; i++){
                        listaLibros.clear();
                        // Consumir caracter en blanco.
                        sc.nextLine();
                        System.out.println("Ingrese los detalles del libro:");
                        System.out.print("Título: ");
                        String titulo = sc.nextLine();
                        System.out.print("Autor: ");
                        String autor = sc.nextLine();
                        System.out.print("ISBN: ");
                        String isbn =sc.nextLine();
                        System.out.print("Edición: ");
                        int edicion = sc.nextInt();
                        sc.nextLine(); // Limpia el buffer de entrada

                        // Crear un objeto LibroDto con los detalles ingresados
                        LibroDto nuevoLibro = new LibroDto(0, titulo, autor, isbn, edicion);
                        listaLibros.add(nuevoLibro);
                    }
                    System.out.print("¿Desea ingresar otro libro? (S/N): ");
                    String respuesta = sc.next();
                    if (!respuesta.equals("s")) {
                        break; // Salir del bucle si el usuario no quiere ingresar más libros
                    }
                }
            }
            // Insertar todos los libros ingresados en la base de datos
            if (!listaLibros.isEmpty()) {
                System.out.println("Guardando los libros en la base de datos...");
                for (LibroDto libro : listaLibros) {
                    insertarLibro(conexionGenerada, libro);
                }
                System.out.println("Todos los libros se han guardado en la base de datos.");
            } else {
                System.out.println("No se ingresaron libros para guardar.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR-IMPL-CrearLibro] No se ha podido realizar la creacion de un nuevo libro.");
        }
        return listaLibros;
    }

    // Método para insertar un libro en la base de datos
    private void insertarLibro(Connection conexionGenerada, LibroDto libro) {
        // Consulta SQL para insertar un nuevo libro
        try {
            String sql = "INSERT INTO gbp_almacen.gbp_alm_cat_libros (titulo, autor, isbn, edicion) VALUES (?, ?, ?, ?)";
            declaracionParametizada = conexionGenerada.prepareStatement(sql);

            // Establecer los valores de los parámetros de la consulta
            declaracionParametizada.setString(1, libro.getTitulo());
            declaracionParametizada.setString(2, libro.getAutor());
            declaracionParametizada.setString(3, libro.getIsbn());
            declaracionParametizada.setInt(4, libro.getEdicion());

            int filasAfectadas = declaracionParametizada.executeUpdate();

            // Comprobar si se insertó correctamente (una fila afectada indica éxito)
            if (filasAfectadas > 0) {
                System.out.println("El libro '" + libro.getTitulo() + "' se ha creado correctamente.");
            } else {
                System.out.println("No se pudo crear el libro '" + libro.getTitulo() + "'.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear el libro '" + libro.getTitulo() + "': " + e.getMessage());
        }
    }

    // Select
    @Override
    public ArrayList<LibroDto> select(Connection conexionGenerada, boolean tipoSelect) {
        // tipoSelect -> True = TODOS | False = 1 libro en concreto

        try {
            declaracionSQL = conexionGenerada.createStatement();
            if(tipoSelect){
                // Query de lista entera
                query = "SELECT * FROM \"gbp_almacen\".\"gbp_alm_cat_libros\"";
                resultadoConsulta = declaracionSQL.executeQuery(query);
                //Llamada a la conversión a dto
                listaLibros = adto.resultsALibrosDto(resultadoConsulta);
                //Muestra todos los libros
                for(int i=0;i<listaLibros.size();i++) {
                    System.out.println(listaLibros.get(i).dosCampos() + "\t");
                }
                System.out.println("-------------------------------------");

                int i = listaLibros.size();
                System.out.println("[INFORMACIÓN-Consultas-Implementacion-seleccionaLibros] Número libros: "+i);
            }
            // Query de 1 en concreto
            else {
                // Hago una query de todos para poder mostrar los id y libros que existe
                // Luego se pedirá que introduzca de cuál quiere detalles
                query = "SELECT * FROM \"gbp_almacen\".\"gbp_alm_cat_libros\"";
                resultadoConsulta = declaracionSQL.executeQuery(query);
                //Llamada a la conversión a dto]
                listaLibros = adto.resultsALibrosDto(resultadoConsulta);

                // Muestra los id y libros, para luego pedir al usuario que pida uno en concreto
                System.out.println("-------------------------------------");
                for(int i=0;i<listaLibros.size();i++) {
                    System.out.println(listaLibros.get(i).dosCampos() + "\t");
                }
                System.out.println("-------------------------------------");

                System.out.println("Cuál desea ver?");
                int libroElegido = sc.nextInt();

                query = "SELECT * FROM gbp_almacen.gbp_alm_cat_libros WHERE id_libro = ? ";
                declaracionParametizada = conexionGenerada.prepareStatement(query);
                declaracionParametizada.setInt(1, libroElegido);
                resultadoConsulta = declaracionParametizada.executeQuery();
                listaLibros = adto.resultsALibrosDto(resultadoConsulta);

                for (LibroDto libro : listaLibros) {
                    System.out.println("ID Libro: " + libro.getIdLibro());
                    System.out.println("Titulo: " + libro.getTitulo());
                    System.out.println("Autor: " + libro.getAutor());
                    System.out.println("Edicion: " + libro.getEdicion());
                    System.out.println("ISBN: " + libro.getIsbn());
                }
            }

        } catch (SQLException e) {

            System.out.println("[ERROR-Consultas-Implementacion-seleccionaTodosLibros] Error generando o ejecutando la declaracionSQL: " + e);
            return listaLibros;

        }
        return listaLibros;
    }

    @Override
    public ArrayList<LibroDto> update(Connection conexionGenerada) {
        try {
            // Puedes implementar aquí la lógica para actualizar libros en la base de datos
            // Utiliza PreparedStatement para ejecutar la consulta SQL de actualización
            // Ejemplo de consulta de actualización (debes adaptarlo a tus necesidades):

            declaracionSQL = conexionGenerada.createStatement();
            // Luego se pedirá que introduzca de cuál quiere detalles
            query = "SELECT * FROM \"gbp_almacen\".\"gbp_alm_cat_libros\"";
            resultadoConsulta = declaracionSQL.executeQuery(query);
            //Llamada a la conversión a dto]
            listaLibros = adto.resultsALibrosDto(resultadoConsulta);

            // Muestra los id y libros, para luego pedir al usuario que pida uno en concreto
            System.out.println("-------------------------------------");
            for(int i=0;i<listaLibros.size();i++) {
                System.out.println(listaLibros.get(i).dosCampos() + "\t");
            }
            System.out.println("-------------------------------------");
            // Elige el libro
            System.out.println("Que libro desea modificar");
            int libroElegido = sc.nextInt();
            if(libroEncontrado(conexionGenerada, libroElegido)){
                System.out.println("Nuevo titulo");
                String nuevoTitulo = sc.next();
                System.out.println("Nuevo autor");
                String nuevoAutor = sc.next();
                System.out.println("Nuevo ISBN");
                String nuevoISBN = sc.next();
                System.out.println("Nueva Edicion");
                int nuevaEdicion = sc.nextInt();


                String query = "UPDATE gbp_almacen.gbp_alm_cat_libros SET titulo = ?, autor = ?, isbn = ?, edicion = ? WHERE id_libro = ?";
                // Preparar la declaración parametizada SQL
                declaracionParametizada = conexionGenerada.prepareStatement(query);

                // Establecer los valores de los parámetros de la consulta
                declaracionParametizada.setString(1, nuevoTitulo);
                declaracionParametizada.setString(2, nuevoAutor);
                declaracionParametizada.setString(3, nuevoISBN);
                declaracionParametizada.setInt(4, nuevaEdicion); // Nueva edición
                declaracionParametizada.setInt(5, libroElegido); // ID del libro a actualizar

                // Ejecutar la consulta de actualización
                int filasAfectadas = declaracionParametizada.executeUpdate();

                // Comprobar si la actualización ha salido bien
                if (filasAfectadas > 0) {
                    System.out.println("Libro actualizado correctamente.");

                    // Recuperar el libro actualizado de la base de datos
                    String queryComprobacion = "SELECT * FROM gbp_almacen.gbp_alm_cat_libros WHERE id_libro = ?";
                    declaracionParametizada = conexionGenerada.prepareStatement(queryComprobacion);
                    declaracionParametizada.setInt(1, libroElegido); // ID del libro actualizado
                    resultadoConsulta = declaracionParametizada.executeQuery();

                    // Convertir el resultado a un objeto LibroDto y agregarlo a la lista
                    listaLibros = adto.resultsALibrosDto(resultadoConsulta);

                    return listaLibros;
            }
            } else {
                System.out.println("[Error-IMPLcrud-UpdateLibro] No se pudo actualizar el, podría no haber sido encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR-IMPLCrud-Update] Error al actualizar el libro: " + e.getMessage());
        }

        return listaLibros;
    }

    private boolean libroEncontrado(Connection conexionGenerada, int libroID) {
        String query = "SELECT * FROM gbp_almacen.gbp_alm_cat_libros WHERE id_libro = ?";
        // Preparar la declaración parametizada SQL
        try {
            declaracionParametizada = conexionGenerada.prepareStatement(query);
            declaracionParametizada.setInt(1, libroID);
            resultadoConsulta = declaracionParametizada.executeQuery();
            int filasAfectadas = declaracionParametizada.executeUpdate();
            if (filasAfectadas > 0 ){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR-IMPLcrud-buscarLibro] Error al buscar el libro elegido");
            return false;
        }
    }
    @Override
    public ArrayList<LibroDto> delete(Connection conexionGenerada) {
        try {
            query = "SELECT * FROM \"gbp_almacen\".\"gbp_alm_cat_libros\"";
            resultadoConsulta = declaracionSQL.executeQuery(query);
            //Llamada a la conversión a dto
            listaLibros = adto.resultsALibrosDto(resultadoConsulta);

            // Muestra los id y libros, para luego pedir al usuario que pida uno en concreto
            System.out.println("-------------------------------------");
            for(int i=0;i<listaLibros.size();i++) {
                System.out.println(listaLibros.get(i).dosCampos() + "\t");
            }
            System.out.println("-------------------------------------");

            System.out.println("Cuál desea eliminar?");
            int libroSeleccionado = sc.nextInt();

            String sqldelete = "DELETE FROM \"gbp_almacen\".\"gbp_alm_cat_libros\" WHERE id_libro = ?";
            declaracionParametizada = conexionGenerada.prepareStatement(sqldelete);
            declaracionParametizada.setInt(1, libroSeleccionado);
            int filasAfectadas = declaracionParametizada.executeUpdate();
            // Comprobar si la actualización ha salido bien
            if (filasAfectadas > 0) {
                System.out.println("Libro borrado correctamente.");

                //Se compara el ID del libro (libro.getIdLibro()) con el valor de libroSeleccionado.
                // Si la condición es verdadera (es decir, si el ID del libro coincide con libroSeleccionado),
                // se borrara
                listaLibros.removeIf(libro -> libro.getIdLibro() == libroSeleccionado);

                return listaLibros;
            }

            return listaLibros;
        } catch (SQLException e) {
            System.out.println("[ERROR-ImplemCrud-Delete] No se ha podido realizar la operacion de borrar." + e);
            return listaLibros;
        }
    }
}
