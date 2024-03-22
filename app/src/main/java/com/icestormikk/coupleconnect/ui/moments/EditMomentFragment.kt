package com.icestormikk.coupleconnect.ui.moments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.icestormikk.coupleconnect.R
import com.icestormikk.coupleconnect.database.entities.moments.Coordinate
import com.icestormikk.coupleconnect.database.entities.moments.Moment
import com.icestormikk.coupleconnect.database.entities.moments.MomentType
import com.icestormikk.coupleconnect.database.entities.moments.MomentViewModel
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsViewModel
import com.icestormikk.coupleconnect.databinding.FragmentEditMomentBinding
import com.icestormikk.coupleconnect.utilities.initializers.MapInitializer
import com.icestormikk.coupleconnect.utilities.validators.TextValidator
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

class EditMomentFragment : Fragment() {
    private var _binding: FragmentEditMomentBinding? = null
    private val binding get() = _binding!!
    private val _args by navArgs<EditMomentFragmentArgs>()
    private lateinit var _momentViewModel: MomentViewModel
    private lateinit var _relationshipsViewModel: RelationshipsViewModel

    private lateinit var _momentMapView: MapView
    private var _placemark: PlacemarkMapObject? = null
    private var _selectedPoint: MutableLiveData<Coordinate?> = MutableLiveData(null)
    private val _listener = object: InputListener {
        override fun onMapTap(p0: com.yandex.mapkit.map.Map, p1: Point) {
            _selectedPoint.value = Coordinate(p1.latitude, p1.longitude)

            if (_placemark == null) {
                _placemark = _momentMapView.mapWindow.map.mapObjects
                    .addPlacemark()
                    .apply {
                        geometry = p1
                        setIcon(ImageProvider.fromResource(binding.root.context, R.drawable.placemark))
                    }
            }
            _placemark!!.geometry = p1
        }
        override fun onMapLongTap(p0: com.yandex.mapkit.map.Map, p1: Point) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapInitializer.initialize("057feb66-97f6-4783-9e4b-a7ee9c6970ab", this.requireContext())

        _binding = FragmentEditMomentBinding.inflate(inflater, container, false)
        _momentViewModel = ViewModelProvider(this)[MomentViewModel::class.java]
        _relationshipsViewModel = ViewModelProvider(this)[RelationshipsViewModel::class.java]
        val currentRelationshipsWithMoments = _args.currentRelationshipsWithMoments
        val root = binding.root
        val typesAdapter = ArrayAdapter(
            root.context,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            enumValues<MomentType>().map { getString(it.labelId) }
        )

        val momentTitleText = binding.momentTitleInput
        val momentTypeSpinner = binding.momentTypeSpinner
        val momentTimestampCalendar = binding.momentTimestampCalendar
        val momentNoteText = binding.momentNote
        val editMomentButton = binding.momentEditButton
        _momentMapView = binding.momentAddressMap
        _momentMapView.mapWindow.map.addInputListener(_listener)

        _selectedPoint.observe(viewLifecycleOwner) {
            Toast.makeText(
                root.context, getString(R.string.fragment_edit_moment_coordinates_updated), Toast.LENGTH_SHORT
            ).show()
        }

        editMomentButton.apply {
            text = getString(R.string.fragment_edit_moment_create_button)
        }
        momentTypeSpinner.apply {
            adapter = typesAdapter
        }
        arrayListOf(momentTitleText).forEach {
            it.addTextChangedListener(object : TextValidator(it) {
                override fun validate(textView: TextView?, text: String?) {
                    if (text == null || textView == null) return

                    if (text.isEmpty()) {
                        textView.error = getString(R.string.validation_message_field_not_empty)
                    }
                }
            })
        }

        editMomentButton.setOnClickListener {
            if (momentTitleText.text.isEmpty()) {
                Toast.makeText(
                    root.context, getString(R.string.fragment_edit_moment_title_not_empty), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val point = _selectedPoint.value
            if (point == null) {
                Toast.makeText(
                    root.context, getString(R.string.fragment_edit_moment_timestamp_undefined), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val momentTitle = momentTitleText.text.toString()
            val momentType = enumValues<MomentType>().find {
                type -> getString(type.labelId) == momentTypeSpinner.selectedItem.toString()
            }
            if (momentType == null) {
                Toast.makeText(
                    root.context, getString(R.string.fragment_edit_moment_type_undefined), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val momentTimestamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(momentTimestampCalendar.date), TimeZone.getDefault().toZoneId()
            )
            val momentNote = momentNoteText.text.toString()

            try {
                lifecycleScope.launch {
                    val moment = Moment(
                        timestamp = momentTimestamp,
                        title = momentTitle,
                        address = Coordinate(point.latitude, point.longitude),
                        note = momentNote,
                        type = momentType,
                        relationshipsId = currentRelationshipsWithMoments.relationships.id
                    )

                    runBlocking {
                        if (_args.existingMoment != null) {
                            _momentViewModel.update(moment.copy(id = _args.existingMoment!!.id))
                        } else {
                            _momentViewModel.insert(moment)
                        }
                    }

                    val newRelationshipsWithMoments = _relationshipsViewModel
                        .getRelationshipsWithMomentsById(
                            currentRelationshipsWithMoments.relationships.id
                        ).await() ?: return@launch

                    val action = EditMomentFragmentDirections
                        .actionEditMomentFragmentToNavigationMoments(newRelationshipsWithMoments)
                    it.findNavController().navigate(
                        action,
                        NavOptions.Builder().setPopUpTo(R.id.navigation_moments, true).build()
                    )
                }
            } catch (ex: Error) {
                Toast.makeText(root.context, ex.message, Toast.LENGTH_SHORT).show()
            }
        }

        with (_args.existingMoment) {
            if (this != null) {
                momentTitleText.text = title.toEditable()

                val existingMomentTypeIndex = typesAdapter.getPosition(getString(type.labelId))
                momentTypeSpinner.setSelection(existingMomentTypeIndex)

                momentTimestampCalendar.date = timestamp.atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()

                _momentMapView.mapWindow.map.mapObjects.addPlacemark().apply {
                    geometry = Point(address.latitude, address.longitude)
                    setIcon(ImageProvider.fromResource(root.context, R.drawable.placemark))
                    setText(getString(R.string.fragment_edit_moment_timestamp_previous))
                }
                momentNoteText.text = note.toEditable()

                editMomentButton.text = getString(R.string.fragment_edit_moment_update_button)
            }
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        _momentMapView.onStart()
    }

    override fun onStop() {
        _momentMapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }
}