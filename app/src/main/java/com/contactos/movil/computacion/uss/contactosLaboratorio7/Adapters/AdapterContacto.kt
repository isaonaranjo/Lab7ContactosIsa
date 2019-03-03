package com.contactos.movil.computacion.uss.contactosLaboratorio7.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.contactos.movil.computacion.uss.contactosLaboratorio7.Modelo.Contacto
import com.contactos.movil.computacion.uss.contactos.R

/**
 * Maria Isabel Ortiz Naranjo
 * 18176
 * Clase adaptador Contacto
 */
class AdapterContacto(private val activity: Activity, private val list: List<Contacto>) : BaseAdapter() {
    // Metodo getCount
    override fun getCount(): Int {
        return list.size
    }
    // Metodo getItem
    override fun getItem(position: Int): Any {
        return list[position]
    }

    // Metodo getItemId
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // metodo GetView
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        var v = convertView
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.detalle_contacto, null)

        }
        val movimiento = list[position]
        val tvNombreV = v!!.findViewById(R.id.tvNombreV) as TextView
        tvNombreV.text = movimiento.nombre
        val tvNumeroV = v.findViewById(R.id.tvNumeroV) as TextView
        tvNumeroV.text = movimiento.telefono
        val tvEmailV = v.findViewById(R.id.tvEmailV) as TextView
        tvEmailV.text = movimiento.email
        return v
    }
}