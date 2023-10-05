package Servicios;

import dtos.LibroDto;

import java.sql.Connection;
import java.util.ArrayList;

public interface InterfazCrud {
    /**
     * Método que realiza crear libro
     * @return lista de libros
     */
    public ArrayList<LibroDto> crearLibros(Connection conexionGenerada);

    /**
     * Método que realiza seleccionar libro
     * @return lista de libros
     */
    public ArrayList<LibroDto> select(Connection conexionGenerada, boolean tipoSelect);
    /**
     * Método que realiza actualizar libro
     * @return lista de libros
     */

    public ArrayList<LibroDto> update(Connection conexionGenerada);
    /**
     * Método que realiza borrar libro
     * @return lista de libros
     */
    public ArrayList<LibroDto> delete(Connection conexionGenerada);
}
