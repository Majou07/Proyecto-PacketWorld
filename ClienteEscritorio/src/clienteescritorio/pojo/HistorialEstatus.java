package clienteescritorio.pojo;

public class HistorialEstatus {
    private String estatus;
    private String fechaCambio;
    private String nombreColaborador;
    private String comentario;

    public HistorialEstatus() {
    }

    public HistorialEstatus(String estatus, String fechaCambio, String nombreColaborador, String comentario) {
        this.estatus = estatus;
        this.fechaCambio = fechaCambio;
        this.nombreColaborador = nombreColaborador;
        this.comentario = comentario;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getNombreColaborador() {
        return nombreColaborador;
    }

    public void setNombreColaborador(String nombreColaborador) {
        this.nombreColaborador = nombreColaborador;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    
}