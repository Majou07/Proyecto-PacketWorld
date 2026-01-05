package dto;


public class EnvioStatus {
    private int idEstatusEnvio;
    private int idColaborador;
    private String comentario;

    public EnvioStatus() {
    }

    public EnvioStatus(int idEstatusEnvio, int idColaborador, String comentario) {
        this.idEstatusEnvio = idEstatusEnvio;
        this.idColaborador = idColaborador;
        this.comentario = comentario;
    }

    public int getIdEstatusEnvio() {
        return idEstatusEnvio;
    }

    public void setIdEstatusEnvio(int idEstatusEnvio) {
        this.idEstatusEnvio = idEstatusEnvio;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
