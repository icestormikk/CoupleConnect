package com.icestormikk.coupleconnect.ui.addRelationships

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.denzcoskun.imageslider.models.SlideModel
import com.icestormikk.coupleconnect.R
import com.icestormikk.coupleconnect.database.entities.image.Image
import com.icestormikk.coupleconnect.database.entities.image.ImageViewModel
import com.icestormikk.coupleconnect.database.entities.relationships.Relationships
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsViewModel
import com.icestormikk.coupleconnect.databinding.FragmentAddRelationshipsBinding
import com.icestormikk.coupleconnect.utilities.validators.TextValidator
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddRelationshipsFragment : Fragment() {
    private var _binding: FragmentAddRelationshipsBinding? = null
    private val binding get() = _binding!!
    private lateinit var _relationshipsViewModel: RelationshipsViewModel
    private lateinit var _imageViewModal: ImageViewModel
    private val selectedDate = MutableLiveData(LocalDate.now())
    private var _photos: List<SlideModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRelationshipsBinding.inflate(inflater, container, false)
        _relationshipsViewModel = ViewModelProvider(this)[RelationshipsViewModel::class.java]
        _imageViewModal = ViewModelProvider(this)[ImageViewModel::class.java]
        val root = binding.root

        val ownerName = binding.editTextOwner
        val partnerName = binding.editTextPartner
        arrayListOf(ownerName, partnerName).forEach {
            it.addTextChangedListener(object: TextValidator(it) {
                override fun validate(textView: TextView?, text: String?) {
                    if (text == null || textView == null) return

                    if (text.isEmpty()) {
                        textView.error = getString(R.string.validation_message_field_not_empty)
                    }
                }
            })
        }

        binding.startDateCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate.value = LocalDate.of(year, month + 1, dayOfMonth)
        }

        val uploadedPhotosSlider = binding.uploadedPhotosSlider
        uploadedPhotosSlider.apply {
            visibility = View.GONE
        }
        val uploadedPhotosText = binding.textViewUploadedPhotos
        val uploadedPhotosButton = binding.addJointPhotosButton
        val getPhotos = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { imageUrls ->
            _photos = imageUrls.map { SlideModel(it.toString()) }
            with (uploadedPhotosSlider) {
                setImageList(_photos)
                visibility = if (_photos.isNotEmpty()) View.VISIBLE else View.GONE
                uploadedPhotosText.text = String.format(
                    "%s (%s):", getString(R.string.fragment_add_rel_uploaded_joint_photos), _photos.size
                )
            }
        }
        uploadedPhotosButton.setOnClickListener {
            getPhotos.launch("image/*")
        }

        val createRelationshipsButton = binding.createRelationshipsButton
        createRelationshipsButton.setOnClickListener {
            if (ownerName.text.isEmpty() || ownerName.text.isEmpty()) {
                Toast
                    .makeText(root.context, getString(R.string.validation_message_field_not_empty), Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val urls = _photos.mapNotNull { photo -> photo.imageUrl }
            if (urls.isEmpty()) {
                Toast
                    .makeText(root.context, getString(R.string.fragment_add_rel_joint_photos_not_empty), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            try {
                lifecycleScope.launch {
                    val relationships = Relationships(
                        ownerName = ownerName.text.toString(),
                        partnerName = partnerName.text.toString(),
                        startDateTime = selectedDate.value!!.atStartOfDay()
                    )

                    val newRelationshipId = _relationshipsViewModel.insert(relationships).await()
                    _imageViewModal.insertMany(
                        _photos.map { photo ->
                            val bitmap = getBitmap(photo.imageUrl.toString())
                            Image(data = bitmap, relationshipsId = newRelationshipId)
                        }
                    ).await()

                    binding.root.findNavController().navigate(R.id.action_navigation_add_relationships_to_navigation_welcome)
                }
            } catch (ex: Error) {
                Toast.makeText(root.context, ex.message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private suspend fun getBitmap(uri: String): Bitmap {
        val loader = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(uri)
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}