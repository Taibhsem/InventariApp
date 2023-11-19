package com.example.inventario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class AdapterInventario (private var items: MutableList<ItemInventario>):
RecyclerView.Adapter<AdapterInventario.ViewHolder>(){

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_inventario,parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= items[position]
        holder.nom.text=item.Nombre
        holder.tipo_tela.text = item.Tipo_tela
        holder.precio.text=item.Precio
        holder.ancho.text=item.Ancho
        holder.alto.text=item.Alto
        holder.btnEliminar.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val nom:TextView = view.findViewById(R.id.tvNombreProducto)
        val tipo_tela:TextView = view.findViewById(R.id.tvTipoTela)
        val precio:TextView = view.findViewById(R.id.tvPrecioProducto)
        val ancho:TextView = view.findViewById(R.id.tvAnchoProducto)
        val alto:TextView =view.findViewById(R.id.tvAltoProducto)
        //val btnImagen:Button = view.findViewById(R.id.)
        val btnEliminar:Button = view.findViewById(R.id.btnEliminarProducto)
    }
}