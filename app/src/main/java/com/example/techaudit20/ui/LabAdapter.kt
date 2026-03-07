package com.example.techaudit20.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.R
import com.example.techaudit20.data.Laboratorio

// ListAdapter gestiona automáticamente la lista y las actualizaciones eficientes
class LabAdapter : ListAdapter<Laboratorio, LabAdapter.LabViewHolder>(LabsComparator()) {

    // Infla el diseño XML de cada ítem (item_laboratorio.xml) para crear la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laboratorio, parent, false)
        return LabViewHolder(view)
    }

    // Vincula los datos de un laboratorio específico con los elementos de la vista
    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    // Clase interna que mantiene las referencias a los IDs del layout para evitar búsquedas repetitivas
    class LabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombre)
        private val tvEdificio: TextView = itemView.findViewById(R.id.tvItemEdificio)

        fun bind(lab: Laboratorio) {
            tvNombre.text = lab.nombre
            tvEdificio.text = "Ubicación: ${lab.edificio}"
        }
    }

    // Compara los elementos de la lista para actualizar solo lo que ha cambiado (mejor rendimiento)
    class LabsComparator : DiffUtil.ItemCallback<Laboratorio>() {
        override fun areItemsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean {
            // Compara si es el mismo registro por su ID único
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean {
            // Compara si el contenido de los campos ha cambiado
            return oldItem.nombre == newItem.nombre && oldItem.edificio == newItem.edificio
        }
    }
}