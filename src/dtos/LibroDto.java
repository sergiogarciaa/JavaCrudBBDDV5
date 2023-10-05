package dtos;

public class LibroDto {
    //Atributos

    private long idLibro;
    private String titulo;
    private String autor;
    private String isbn;
    private int edicion;

    //Constructores

    /**
     *
     * @param idLibro
     * @param titulo
     * @param autor
     * @param isbn
     * @param edicion
     */

    public LibroDto(long idLibro, String titulo, String autor, String isbn, int edicion) {
        super();
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.edicion = edicion;
    }

    public LibroDto() {
        super();
    }

    //Getters y setters

    public long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(long idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getEdicion() {
        return edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }


    //ToString
    /*
     * ¿Por qué el método está sobrescrito?
     */
    @Override
    public String toString() {
        return "librosDto [idLibro=" + idLibro + ", titulo=" + titulo + ", autor=" + autor + ", isbn=" + isbn
                + ", edicion=" + edicion + "]";
    }
    public String dosCampos(){
        return "[Todos los libros] \t [idLibro=" + idLibro + ", titulo=" + titulo + "]";
    }
}
