package com.icestormikk.coupleconnect.ui.moments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.icestormikk.coupleconnect.R
import com.icestormikk.coupleconnect.database.entities.moments.MomentViewModel
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsViewModel
import com.icestormikk.coupleconnect.databinding.FragmentMomentBinding
import com.icestormikk.coupleconnect.utilities.initializers.MapInitializer
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter

class MomentFragment : Fragment() {

    private var _binding: FragmentMomentBinding? = null
    private val binding get() = _binding!!
    private val _args by navArgs<MomentFragmentArgs>()
    private lateinit var _momentViewModel: MomentViewModel
    private lateinit var _relationshipsViewModel: RelationshipsViewModel
    private lateinit var momentMapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapInitializer.initialize("057feb66-97f6-4783-9e4b-a7ee9c6970ab", this.requireContext())

        _momentViewModel = ViewModelProvider(this)[MomentViewModel::class.java]
        _relationshipsViewModel = ViewModelProvider(this)[RelationshipsViewModel::class.java]
        _binding = FragmentMomentBinding.inflate(inflater, container, false)
        val root = binding.root
        val currentRelationshipsWithMoments = _args.currentRelationshipsWithMoments
        val currentMoment = _args.currentMoment

        activity?.title = currentMoment.title
        val momentTitle = binding.momentTitleText
        val momentTimestampDate = binding.momentTimestampDate
        val momentTimestampTime = binding.momentTimestampTime
        val momentTypeImage = binding.momentType
        val momentNoteText = binding.momentNoteText
        val momentEditButton = binding.editMomentButton
        val momentDeleteButton = binding.deleteMomentButton

        momentMapView = binding.momentAddressMapView
        momentMapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(currentMoment.address.latitude, currentMoment.address.longitude)
            setIcon(ImageProvider.fromResource(root.context, R.drawable.placemark))
        }

        with (currentMoment) {
            momentTitle.text = title
            momentTimestampDate.text = timestamp.format(DateTimeFormatter.ofPattern("d.MM.uuuu"))
            momentTimestampTime.text = timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME)
            momentTypeImage.setImageDrawable(getIconByType(root.resources))
            momentNoteText.text = note.ifBlank { getString(R.string.fragment_moment_note_empty) }

            momentEditButton.setOnClickListener {
                val action = MomentFragmentDirections
                    .actionNavigationMomentToNavigationEditMoment(currentRelationshipsWithMoments, this)
                root.findNavController().navigate(action)
            }
            momentDeleteButton.setOnClickListener {
                lifecycleScope.launch {
                    runBlocking {
                        _momentViewModel.delete(currentMoment)
                    }

                    val newRelationshipsWithMoments = _relationshipsViewModel
                        .getRelationshipsWithMomentsById(
                            currentRelationshipsWithMoments.relationships.id
                        ).await() ?: return@launch

                    val action = MomentFragmentDirections
                        .actionNavigationMomentToNavigationMoments(newRelationshipsWithMoments)
                    root.findNavController().navigate(
                        action,
                        NavOptions.Builder().setPopUpTo(R.id.navigation_moments, true).build()
                    )
                }
            }
        }

        return root
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        momentMapView.onStart()
    }

    override fun onStop() {
        momentMapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}