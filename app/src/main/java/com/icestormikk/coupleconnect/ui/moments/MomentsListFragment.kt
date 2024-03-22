package com.icestormikk.coupleconnect.ui.moments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.icestormikk.coupleconnect.R
import com.icestormikk.coupleconnect.database.entities.moments.MomentViewModel
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithMoments
import com.icestormikk.coupleconnect.databinding.FragmentMomentsListBinding
import com.icestormikk.coupleconnect.utilities.adapters.MomentsAdapter

class MomentsListFragment : Fragment() {
    private var _binding: FragmentMomentsListBinding? = null
    private val binding get() = _binding!!
    private val _args by navArgs<MomentsListFragmentArgs>()
    private lateinit var _momentViewModel: MomentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMomentsListBinding.inflate(inflater, container, false)
        _momentViewModel = ViewModelProvider(this)[MomentViewModel::class.java]
        val root = binding.root
        val currentRelationshipsWithMoments = _args.currentRelationshipsWithMoments

        val addMomentButton = binding.addMomentButton
        val statisticsButton = binding.statisticsButton
        val momentsListView = binding.momentsListView

        momentsListView.apply {
            adapter = MomentsAdapter(currentRelationshipsWithMoments)
            layoutManager = LinearLayoutManager(root.context)
        }

        addMomentButton.setOnClickListener {
            val action = MomentsListFragmentDirections
                .actionNavigationMomentsToAddMomentFragment(currentRelationshipsWithMoments)
            it.findNavController().navigate(action)
        }
        statisticsButton.setOnClickListener {
            val action = MomentsListFragmentDirections
                .actionNavigationMomentsToStatisticsFragment(currentRelationshipsWithMoments.relationships)
            it.findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}