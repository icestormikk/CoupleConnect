package com.icestormikk.coupleconnect.utilities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.icestormikk.coupleconnect.database.entities.moments.Moment
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithMoments
import com.icestormikk.coupleconnect.databinding.LayoutMomentCardBinding
import com.icestormikk.coupleconnect.ui.moments.MomentsListFragmentDirections
import java.time.format.DateTimeFormatter

class MomentsAdapter(
    private var relationshipsWithMoments: RelationshipsWithMoments
) : RecyclerView.Adapter<MomentsAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val momentCardBinding: LayoutMomentCardBinding
    ) : RecyclerView.ViewHolder(momentCardBinding.root) {
        fun bind(moment: Moment, holder: MomentsAdapter.ViewHolder) {
            with (momentCardBinding) {
                momentTitle.text = String.format("\"%s\"", moment.title)
                timestampDate.text = moment.timestamp.format(DateTimeFormatter.ofPattern("d.MM.uuuu"))
                timestampTime.text = moment.timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME)

                val drawable = moment.getIconByType(momentCardBinding.root.resources)
                momentTypeImage.setImageDrawable(drawable)
                momentLinearLayout.setOnClickListener {
                    val action = MomentsListFragmentDirections
                        .actionNavigationMomentsToMomentFragment(moment, relationshipsWithMoments)
                    holder.itemView.findNavController().navigate(action)
                }
            }
        }
    }

    fun setData(relationshipsWithMoments: RelationshipsWithMoments) {
        this.relationshipsWithMoments = relationshipsWithMoments
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentsAdapter.ViewHolder {
        val binding = LayoutMomentCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = this.relationshipsWithMoments.moments.size

    override fun onBindViewHolder(holder: MomentsAdapter.ViewHolder, position: Int) {
        val moment = this.relationshipsWithMoments.moments[position]
        holder.bind(moment, holder)
    }
}