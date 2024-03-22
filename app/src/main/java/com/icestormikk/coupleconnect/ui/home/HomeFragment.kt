package com.icestormikk.coupleconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icestormikk.coupleconnect.R
import com.icestormikk.coupleconnect.database.entities.image.ImageViewModel
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsViewModel
import com.icestormikk.coupleconnect.databinding.FragmentHomeBinding
import com.icestormikk.coupleconnect.utilities.adapters.ImagesAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val _args by navArgs<HomeFragmentArgs>()
    private lateinit var _imageViewModel: ImageViewModel
    private lateinit var _relationshipsViewModel: RelationshipsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        _relationshipsViewModel = ViewModelProvider(this)[RelationshipsViewModel::class.java]
        val root: View = binding.root
        val currentRelationshipsWithPhotos = _args.currentRelationshipsWithPhotos

        val partnerNames = binding.partnerNamesText
        val durationLabel = binding.durationLabel
        val anniversaryText = binding.anniversaryText
        val photosView = binding.jointPhotosView
        val momentsButton = binding.goToMomentsButton
        val removeRelationshipsButton = binding.removeRelationshipsButton

        with (currentRelationshipsWithPhotos) {
            partnerNames.text = getString(
                R.string.fragment_home_partner_names, relationships.ownerName, relationships.partnerName
            )

            val duration = Period.between(relationships.startDateTime.toLocalDate(), LocalDate.now())
            with (duration) {
                durationLabel.text = getString(
                    R.string.fragment_home_duration, years, months, days / 7, days % 7
                )
            }

            val anniversary = relationships.startDateTime.withYear(LocalDate.now().year + 1)
            val anniversaryDuration = Duration.between(LocalDateTime.now(), anniversary)
            with (anniversaryDuration) {
                val formattedAnniversaryDate = anniversary
                    .format(DateTimeFormatter.ofPattern("d MMMM uuuu"))
                anniversaryText.text = getString(
                    R.string.fragment_home_anniversary, toDays(), formattedAnniversaryDate
                )
            }


            lifecycleScope.launch {
                photosView.apply {
                    val layout = LinearLayoutManager(root.context).apply {
                        orientation = RecyclerView.HORIZONTAL
                    }

                    layoutManager = layout
                    adapter = ImagesAdapter(photos)
                }
            }
        }

        momentsButton.setOnClickListener {
            lifecycleScope.launch {
                val relationshipsWithMoments = _relationshipsViewModel
                    .getRelationshipsWithMomentsById(
                        currentRelationshipsWithPhotos.relationships.id
                    )
                    .await() ?: return@launch

                val action = HomeFragmentDirections
                    .actionNavigationHomeToMomentsListFragment(relationshipsWithMoments)
                root.findNavController().navigate(action)
            }
        }

        removeRelationshipsButton.setOnClickListener {
            lifecycleScope.launch {
                runBlocking {
                    _relationshipsViewModel.remove(currentRelationshipsWithPhotos.relationships)
                }
                root.findNavController().navigate(R.id.navigation_welcome)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}