package com.contactos.movil.computacion.uss.contactosLaboratorio7.Modelo

/**
 * Maria Isabel Ortiz Naranjo
 * 18176
 */
class Contacto {
    var id: Int = 0
    lateinit var nombre: String
    lateinit var telefono: String
    lateinit var email: String

    constructor() {

    }
    // Constructor
    constructor(email: String, telefono: String, nombre: String, id: Int) {
        this.email = email
        this.telefono = telefono
        this.nombre = nombre
        this.id = id
    }
    // Constructor

    constructor(nombre: String, telefono: String, email: String) : super() {
        this.nombre = nombre
        this.telefono = telefono
        this.email = email
    }

    // Constructor
    override fun toString(): String {
        return this.nombre
    }
}
