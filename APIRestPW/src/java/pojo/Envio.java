package pojo;

public class Envio {
    private Integer idEnvio;
    private String numeroGuia;
    private String destinatarioNombre;
    private String destinatarioApPaterno;
    private String destinatarioApMaterno;
    private String destinoCalle;
    private String destinoNumero;
    private String destinoColonia;
    private String destinoCodigoPostal;
    private String destinoCiudad;
    private String destinoEstado;
    private Double costoTotal;
    private Integer idClienteRemitente;
    private String codigoSucursalOrigen;
    private Integer idConductorAsignado;
    private Integer idEstatusEnvio;
    
    private String nombreCliente;
    private String telefono;
    private String correoElectronico;
    private String sucursalOrigen;
    private String nombreConductor;
    private String estatusEnvio;
    

    public Envio() {
    }

    public Envio(Integer idEnvio, String numeroGuia, String destinatarioNombre, String destinatarioApPaterno, String destinatarioApMaterno, String destinoCalle, String destinoNumero, String destinoColonia, String destinoCodigoPostal, String destinoCiudad, String destinoEstado, Double costoTotal, Integer idClienteRemitente, String codigoSucursalOrigen, Integer idConductorAsignado, Integer idEstatusEnvio, String nombreCliente, String sucursalOrigen, String nombreConductor, String estatusEnvio) {
        this.idEnvio = idEnvio;
        this.numeroGuia = numeroGuia;
        this.destinatarioNombre = destinatarioNombre;
        this.destinatarioApPaterno = destinatarioApPaterno;
        this.destinatarioApMaterno = destinatarioApMaterno;
        this.destinoCalle = destinoCalle;
        this.destinoNumero = destinoNumero;
        this.destinoColonia = destinoColonia;
        this.destinoCodigoPostal = destinoCodigoPostal;
        this.destinoCiudad = destinoCiudad;
        this.destinoEstado = destinoEstado;
        this.costoTotal = costoTotal;
        this.idClienteRemitente = idClienteRemitente;
        this.codigoSucursalOrigen = codigoSucursalOrigen;
        this.idConductorAsignado = idConductorAsignado;
        this.idEstatusEnvio = idEstatusEnvio;
        this.nombreCliente = nombreCliente;
        this.sucursalOrigen = sucursalOrigen;
        this.nombreConductor = nombreConductor;
        this.estatusEnvio = estatusEnvio;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getDestinatarioNombre() {
        return destinatarioNombre;
    }

    public void setDestinatarioNombre(String destinatarioNombre) {
        this.destinatarioNombre = destinatarioNombre;
    }

    public String getDestinatarioApPaterno() {
        return destinatarioApPaterno;
    }

    public void setDestinatarioApPaterno(String destinatarioApPaterno) {
        this.destinatarioApPaterno = destinatarioApPaterno;
    }

    public String getDestinatarioApMaterno() {
        return destinatarioApMaterno;
    }

    public void setDestinatarioApMaterno(String destinatarioApMaterno) {
        this.destinatarioApMaterno = destinatarioApMaterno;
    }

    public String getDestinoCalle() {
        return destinoCalle;
    }

    public void setDestinoCalle(String destinoCalle) {
        this.destinoCalle = destinoCalle;
    }

    public String getDestinoNumero() {
        return destinoNumero;
    }

    public void setDestinoNumero(String destinoNumero) {
        this.destinoNumero = destinoNumero;
    }

    public String getDestinoColonia() {
        return destinoColonia;
    }

    public void setDestinoColonia(String destinoColonia) {
        this.destinoColonia = destinoColonia;
    }

    public String getDestinoCodigoPostal() {
        return destinoCodigoPostal;
    }

    public void setDestinoCodigoPostal(String destinoCodigoPostal) {
        this.destinoCodigoPostal = destinoCodigoPostal;
    }

    public String getDestinoCiudad() {
        return destinoCiudad;
    }

    public void setDestinoCiudad(String destinoCiudad) {
        this.destinoCiudad = destinoCiudad;
    }

    public String getDestinoEstado() {
        return destinoEstado;
    }

    public void setDestinoEstado(String destinoEstado) {
        this.destinoEstado = destinoEstado;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Integer getIdClienteRemitente() {
        return idClienteRemitente;
    }

    public void setIdClienteRemitente(Integer idClienteRemitente) {
        this.idClienteRemitente = idClienteRemitente;
    }

    public String getCodigoSucursalOrigen() {
        return codigoSucursalOrigen;
    }

    public void setCodigoSucursalOrigen(String codigoSucursalOrigen) {
        this.codigoSucursalOrigen = codigoSucursalOrigen;
    }

    public Integer getIdConductorAsignado() {
        return idConductorAsignado;
    }

    public void setIdConductorAsignado(Integer idConductorAsignado) {
        this.idConductorAsignado = idConductorAsignado;
    }

    public Integer getIdEstatusEnvio() {
        return idEstatusEnvio;
    }

    public void setIdEstatusEnvio(Integer idEstatusEnvio) {
        this.idEstatusEnvio = idEstatusEnvio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getSucursalOrigen() {
        return sucursalOrigen;
    }

    public void setSucursalOrigen(String sucursalOrigen) {
        this.sucursalOrigen = sucursalOrigen;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getEstatusEnvio() {
        return estatusEnvio;
    }

    public void setEstatusEnvio(String estatusEnvio) {
        this.estatusEnvio = estatusEnvio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
    
    
    
    
}
