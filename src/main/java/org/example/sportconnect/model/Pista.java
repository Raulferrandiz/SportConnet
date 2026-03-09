package org.example.sportconnect.model;

public class Pista {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private String estado;
    private Deporte deporte;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }
}
