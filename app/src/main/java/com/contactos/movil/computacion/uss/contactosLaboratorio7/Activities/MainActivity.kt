package com.contactos.movil.computacion.uss.contactosLaboratorio7.Activities

/**
 * Maria Isabel Ortiz Naranjo
 * 18176
 * MainActivity
 */

import android.app.AlertDialog
import android.app.Dialog
import android.app.usage.UsageEvents
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.contactos.movil.computacion.uss.contactosLaboratorio7.Adapters.AdapterContacto
import com.contactos.movil.computacion.uss.contactosLaboratorio7.DAO.DaoContacto
import com.contactos.movil.computacion.uss.contactosLaboratorio7.Modelo.Contacto
import com.contactos.movil.computacion.uss.contactos.R

import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    internal lateinit var etNombre: EditText
    internal lateinit var etTelefono: EditText
    internal lateinit var etEmail: EditText
    internal lateinit var btnRegistrar: Button
    internal lateinit var daocontacto: DaoContacto
    internal lateinit var contactos: List<Contacto>
    internal lateinit var listViewContactos: ListView
    internal lateinit var mSearchAction: MenuItem
    internal var isSearchOpened = false
    internal lateinit var edtSeach: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        daocontacto = DaoContacto(this)
        // Se crean los contactos
        if (daocontacto.allStudentsList.size == 0) {
            daocontacto.addContactoDetail(Contacto("Daniela", "123456878", "daniela@gmail.com"))
            daocontacto.addContactoDetail(Contacto("Fernando", "12345678", "fernando@gmail.com"))
            daocontacto.addContactoDetail(Contacto("Diana", "12345678", "diana@gmail.com"))
        }
        contactos = ArrayList()
        listViewContactos = findViewById(R.id.listViewContactos) as ListView
        listViewContactos.isLongClickable = true
        Listar()

        val fab = findViewById(R.id.fab) as FloatingActionButton

        // Listview de los contactos
        listViewContactos.onItemLongClickListener = AdapterView.OnItemLongClickListener { arg0, arg1, pos, arg3 ->
            AlertDialog.Builder(this@MainActivity)
                    .setTitle("Eliminar")
                    .setMessage("¿Desea eliminar este contacto?")
                    .setPositiveButton("Aceptar") { dialog, which ->
                        val contacto = listViewContactos.getItemAtPosition(pos) as Contacto
                        daocontacto.deleteEntry(contacto.id.toLong())
                        Listar()
                    }
                    .setNegativeButton("Cancelar") { dialog, which -> }
                    .setIcon(R.drawable.alert)
                    .show()
            true
        }

        listViewContactos.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val contacto = listViewContactos.getItemAtPosition(position) as Contacto
            val intent = Intent(this, ContactoActivity::class.java)
            intent.putExtra("id", contacto.id)
            intent.putExtra("nombre", contacto.nombre)
            intent.putExtra("phone", contacto.telefono)
            intent.putExtra("email", contacto.email)
            startActivity(intent)
        }
        // Registrar los contactos
        fab.setOnClickListener {
            val inflater = layoutInflater
            val dialoglayout = inflater.inflate(R.layout.dialog, null)
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setView(dialoglayout)
            etNombre = dialoglayout.findViewById(R.id.etNombre) as EditText
            etTelefono = dialoglayout.findViewById(R.id.etTelefono) as EditText
            etEmail = dialoglayout.findViewById(R.id.etEmail) as EditText
            btnRegistrar = dialoglayout.findViewById(R.id.btnRegistrar) as Button
            btnRegistrar.setOnClickListener {
                if (etNombre.text.toString() != "" &&
                        etTelefono.text.toString() != "" &&
                        etEmail.text.toString() != "") {
                    // Restricciones para el Email
                    if (etEmail.text.toString().matches(PATTERN_EMAIL.toRegex())) {
                        val contacto = Contacto(etNombre.text.toString(), etTelefono.text.toString(), etEmail.text.toString())
                        daocontacto.addContactoDetail(contacto)
                        Listar()
                        Toast.makeText(this@MainActivity, "Registrado Correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Email incorrecto", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@MainActivity, "Ingreso los datos requeridos", Toast.LENGTH_SHORT).show()
                }
            }
            builder.show()
        }
    }
    // Listar los contactos
    private fun Listar() {
        contactos = daocontacto.allStudentsList
        val adapterMovimiento = AdapterContacto(this@MainActivity, contactos)
        listViewContactos.adapter = adapterMovimiento
    }
    // Listar los contactos

    private fun Listar(name: String) {
        contactos = daocontacto.searchContact(name)
        val adapterMovimiento = AdapterContacto(this@MainActivity, contactos)
        listViewContactos.adapter = adapterMovimiento
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        mSearchAction = menu.findItem(R.id.action_buscar)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_buscar -> {
                handleMenuSearch()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    protected fun handleMenuSearch() {
        val action = supportActionBar

        if (isSearchOpened) {

            action!!.setDisplayShowCustomEnabled(false)
            action.setDisplayShowTitleEnabled(true)

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edtSeach.windowToken, 0)

            mSearchAction.icon = resources.getDrawable(R.drawable.search)

            isSearchOpened = false
            Listar()
        } else {

            action!!.setDisplayShowCustomEnabled(true)
            action.setCustomView(R.layout.search_bar)
            action.setDisplayShowTitleEnabled(false)

            edtSeach = action.customView.findViewById(R.id.edtSearch) as EditText

            edtSeach.setOnEditorActionListener { v, actionId, event ->
                Listar(edtSeach.text.toString())
                true
            }

            edtSeach.requestFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT)

            mSearchAction.icon = resources.getDrawable(R.drawable.close)

            isSearchOpened = true
        }
    }

    override fun onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch()
            return
        }
        super.onBackPressed()
    }

    private fun doSearch() {

    }

    companion object {

        private val PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }
}
