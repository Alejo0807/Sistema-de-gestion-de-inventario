package com.example.sw2.entity;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="Asignacion_tienda")
public class AsignacionTiendas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtiendas;
    @Digits(integer = 6, fraction = 0, message = "Ingrese un número entero.")
    @Positive(message = "Ingrese una cantidad válida.")
    @Column(nullable = false)
    private int stock;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="fecha_asignacion", nullable = false)
    private LocalDate fechaasignacion;
    @ManyToOne
    @JoinColumn(name="tienda",nullable = false)
    @NotNull(message = "Seleccione una tienda de la lista.")
    private Tienda tienda;
    @LastModifiedDate
    @Column(name="fecha_modificacion")
    private LocalDateTime fechamodificacion;
    @CreatedDate
    @Column(name="fecha_creacion",nullable =false)
    private LocalDateTime fechacreacion;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "gestor", referencedColumnName = "gestor"),
            @JoinColumn(name = "sede", referencedColumnName = "sede"),
            @JoinColumn(name = "producto_inventario", referencedColumnName = "producto_inventario"),
            @JoinColumn(name = "estadoasignacion",referencedColumnName = "estadoasignacion"),
            @JoinColumn(name = "precioventa",referencedColumnName = "precioventa")
    })
    private AsignadosSedes asignadosSedes;

    public int getIdtiendas() {
        return idtiendas;
    }

    public void setIdtiendas(int idtiendas) {
        this.idtiendas = idtiendas;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDate getFechaasignacion() {
        return fechaasignacion;
    }

    public void setFechaasignacion(LocalDate fechaasignacion) {
        this.fechaasignacion = fechaasignacion;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public LocalDateTime getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(LocalDateTime fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    public LocalDateTime getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(LocalDateTime fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public AsignadosSedes getAsignadosSedes() {
        return asignadosSedes;
    }

    public void setAsignadosSedes(AsignadosSedes asignadosSedes) {
        this.asignadosSedes = asignadosSedes;
    }
}
