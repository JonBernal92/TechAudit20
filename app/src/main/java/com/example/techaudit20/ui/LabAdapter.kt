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

// Añadimos un parámetro lambda 'onItemClicked' para gestionar el clic
class LabAdapter(private val onItemClicked: (Laboratorio) -> Unit) :
    ListAdapter<Laboratorio, LabAdapter.LabViewHolder>(LabsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laboratorio, parent, false)
        return LabViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        val current = getItem(position)
        // Configuramos el clic en la raíz de la vista (la tarjeta)
        holder.itemView.setOnClickListener { onItemClicked(current) }
        holder.bind(current)
    }

    class LabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombre)
        private val tvEdificio: TextView = itemView.findViewById(R.id.tvItemEdificio)

        fun bind(lab: Laboratorio) {
            tvNombre.text = lab.nombre
            tvEdificio.text = "Ubicación: ${lab.edificio}"
        }
    }

    class LabsComparator : DiffUtil.ItemCallback<Laboratorio>() {
        override fun areItemsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean =
            oldItem.nombre == newItem.nombre && oldItem.edificio == newItem.edificio
    }
}