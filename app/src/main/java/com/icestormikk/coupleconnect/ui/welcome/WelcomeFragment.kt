package com.icestormikk.coupleconnect.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.icestormikk.coupleconnect.R
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsViewModel
import com.icestormikk.coupleconnect.databinding.FragmentWelcomeBinding
import com.icestormikk.coupleconnect.utilities.adapters.RelationshipsAdapter

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var _viewModel: RelationshipsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val root = binding.root

        val relationshipsRecyclerView = binding.relationshipsRecyclerView

        _viewModel = ViewModelProvider(this).get(RelationshipsViewModel::class.java)
        _viewModel.getAllRelationshipsWithPhotos().observe(viewLifecycleOwner) {
            relationshipsRecyclerView.apply {
                adapter = RelationshipsAdapter(it)
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        binding.addRelationshipsButton.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_navigation_welcome_to_navigation_add_relationships
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}