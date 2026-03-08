package com.example.techaudit20.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.R
import com.example.techaudit20.data.Equipo

// Adaptador especializado para la entidad Equipo
class EquipoAdapter : ListAdapter<Equipo, EquipoAdapter.EquipoViewHolder>(EquiposComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class EquipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvEquipoNombre)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEquipoEstado)

        fun bind(equipo: Equipo) {
            tvNombre.text = equipo.nombre
            // Formateamos el texto del estado para mayor claridad
            tvEstado.text = "Estado: ${equipo.estado}"
        }
    }

    // Lógica de comparación eficiente para actualizar la lista de equipos
    class EquiposComparator : DiffUtil.ItemCallback<Equipo>() {
        override fun areItemsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem.nombre == newItem.nombre && oldItem.estado == newItem.estado
        }
    }
}