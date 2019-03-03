package com.contactos.movil.computacion.uss.contactosLaboratorio7.Activities

/**
 * Maria Isabel Ortiz Naranjo
 * 18176
 * Clase contacto
 */

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.contactos.movil.computacion.uss.contactosLaboratorio7.DAO.DaoContacto
import com.contactos.movil.computacion.uss.contactosLaboratorio7.Modelo.Contacto
import com.contactos.movil.computacion.uss.contactos.R

class ContactoActivity : AppCompatActivity() {
    internal lateinit var etNombreE: EditText
    internal lateinit var etTelefonoE: EditText
    internal lateinit var etEmailE: EditText
    internal lateinit var daocontacto: DaoContacto
    internal lateinit var contacto: Contacto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val i = intent
        val bundle = i.extras
        etNombreE = findViewById(R.id.etNombreE) as EditText
        etTelefonoE = findViewById(R.id.etTelefonoE) as EditText
        etEmailE = findViewById(R.id.etEmailE) as EditText
        daocontacto = DaoContacto(this)

        if (bundle != null) {
            contacto = Contacto(bundle.get("email")!!.toString(),
                    bundle.get("phone")!!.toString(),
                    bundle.get("nombre")!!.toString(),
                    Integer.parseInt(bundle.get("id")!!.toString()))
            etNombreE.setText(contacto.nombre)
            etTelefonoE.setText(contacto.telefono)
            etEmailE.setText(contacto.email)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_contacto, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar -> {
                if (etNombreE.text.toString() != "" &&
                        etTelefonoE.text.toString() != "" &&
                        etEmailE.text.toString() != "") {

                    if (etEmailE.text.toString().matches(PATTERN_EMAIL.toRegex())) {

                        daocontacto.updateEntry(Contacto(etEmailE.text.toString(), etTelefonoE.text.toString(), etNombreE.text.toString(), contacto.id))
                        val i = Intent(this@ContactoActivity, MainActivity::class.java)
                        startActivity(i)
                        Toast.makeText(this@ContactoActivity, "Editado Correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ContactoActivity, "Email incorrecto", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@ContactoActivity, "Ingreso los datos requeridos", Toast.LENGTH_SHORT).show()
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {

        private val PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }
}
