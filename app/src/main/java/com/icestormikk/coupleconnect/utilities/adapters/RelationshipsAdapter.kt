package com.icestormikk.coupleconnect.utilities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithMoments
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithPhotos
import com.icestormikk.coupleconnect.databinding.LayoutRelationshipsCardBinding
import com.icestormikk.coupleconnect.ui.welcome.WelcomeFragmentDirections

class RelationshipsAdapter(
    private var relationships: List<RelationshipsWithPhotos>
): RecyclerView.Adapter<RelationshipsAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val relationshipsCardBinding: LayoutRelationshipsCardBinding
    ) : RecyclerView.ViewHolder(relationshipsCardBinding.root) {
        fun bind(relationships: RelationshipsWithPhotos, holder: ViewHolder) {
            with (relationshipsCardBinding) {
                ownerNameText.text = relationships.relationships.ownerName
                partnerNameText.text = relationships.relationships.partnerName
                relationshipsCardLayout.setOnClickListener {
                    val action = WelcomeFragmentDirections.actionNavigationWelcomeToNavigationHome(relationships)
                    holder.itemView.findNavController().navigate(action)
                }
            }
        }
    }

    fun setData(relationshipsWithMoments: List<RelationshipsWithMoments>) {
        this.relationships = relationships
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutRelationshipsCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = this.relationships.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val relationships = this.relationships[position]
        holder.bind(relationships, holder)
    }
}