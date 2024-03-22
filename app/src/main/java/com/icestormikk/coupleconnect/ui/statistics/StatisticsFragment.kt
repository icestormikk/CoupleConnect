package com.icestormikk.coupleconnect.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.icestormikk.coupleconnect.R
import com.icestormikk.coupleconnect.database.entities.Statistics
import com.icestormikk.coupleconnect.database.entities.StatisticsType
import com.icestormikk.coupleconnect.database.entities.moments.Moment
import com.icestormikk.coupleconnect.database.entities.moments.MomentType
import com.icestormikk.coupleconnect.database.entities.moments.MomentViewModel
import com.icestormikk.coupleconnect.databinding.FragmentStatisticsBinding
import com.icestormikk.coupleconnect.utilities.adapters.StatisticsAdapter
import java.time.LocalDate

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val _args by navArgs<StatisticsFragmentArgs>()
    private lateinit var _momentViewModel: MomentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _momentViewModel = ViewModelProvider(this)[MomentViewModel::class.java]
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root = binding.root
        val typesArrayAdapter = ArrayAdapter(
            root.context,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            enumValues<StatisticsType>().map { getString(it.labelId) }
        )
        val selectedCustomStart = MutableLiveData(LocalDate.now())
        val selectedCustomEnd = MutableLiveData(LocalDate.now())

        val statisticTypesSpinner = binding.statisticsPeriodType
        val collectStatisticsButton = binding.collectStatisticsButton
        val statisticsCustomPeriodLayout = binding.customPeriodLayout
        val statisticsLayout = binding.statisticsLayout
        val statisticsPeriodInfoText = binding.periodInfoText
        val customStartDateCalendar = binding.startDateCalendarView
        val customEndDateCalendar = binding.endDateCalendarView
        customStartDateCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedCustomStart.value = LocalDate.of(year, month + 1, dayOfMonth)
        }
        customEndDateCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedCustomEnd.value = LocalDate.of(year, month + 1, dayOfMonth)
        }

        val statisticsRecyclerView = binding.statisticsList
        statisticsRecyclerView.apply {
            layoutManager = LinearLayoutManager(root.context)
        }

        fun getStatisticsTypeByLabel(label: String): StatisticsType? {
            return enumValues<StatisticsType>().find { getString(it.labelId) == label }
        }

        statisticTypesSpinner.apply {
            adapter = typesArrayAdapter
            onItemSelectedListener = object: OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (parent == null) return
                    val selectedType = getStatisticsTypeByLabel(
                        parent.selectedItem.toString()
                    ) ?: return

                    statisticsCustomPeriodLayout.visibility = if (selectedType == StatisticsType.CUSTOM) View.VISIBLE else View.GONE
                    statisticsLayout.visibility = View.GONE
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        collectStatisticsButton.setOnClickListener {
            val statisticsTypes = getStatisticsTypeByLabel(statisticTypesSpinner.selectedItem.toString())
            if (statisticsTypes == null) {
                Toast.makeText(
                    root.context,
                    getString(R.string.fragment_statistics_period_undefined),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val now = LocalDate.now()
            val period = when (statisticsTypes) {
                StatisticsType.WEEK -> Pair(now.minusWeeks(1), now)
                StatisticsType.MONTH -> Pair(now.minusMonths(1), now)
                StatisticsType.QUARTER -> Pair(now.minusMonths(3), now)
                StatisticsType.YEAR -> Pair(now.minusYears(1), now)
                else -> {
                    if (selectedCustomStart.value == null || selectedCustomEnd.value == null) {
                        Toast.makeText(
                            root.context,
                            getString(R.string.fragment_statistics_period_undefined),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    if (selectedCustomStart.value!! > selectedCustomEnd.value) {
                        Toast.makeText(
                            root.context,
                            getString(R.string.fragment_statistics_period_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    Pair(selectedCustomStart.value, selectedCustomEnd.value)
                }
            }

            try {
                _momentViewModel.allData.observe(viewLifecycleOwner) {
                    val stats = createStatisticsList(it, period)
                    val adapter = StatisticsAdapter(stats)
                    statisticsRecyclerView.adapter = adapter
                }
                statisticsLayout.visibility = View.VISIBLE
                statisticsPeriodInfoText.text = String.format(
                    getString(R.string.fragment_statistics_period_selected),
                    period.first.toString(),
                    period.second.toString()
                )
            } catch (ex: Error) {
                Toast.makeText(root.context, ex.message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        return root
    }

    private fun createStatisticsList(
        moments: List<Moment>, period: Pair<LocalDate, LocalDate>
    ): MutableList<Statistics> {
        val momentsForCurrentRelationships = moments.filter {
            it.relationshipsId == _args.currentRelationships.id
        }
        val momentsBeforeStart = momentsForCurrentRelationships.filter {
            it.timestamp.toLocalDate() <= period.first
        }
        val momentsBeforeEnd = momentsForCurrentRelationships.filter {
            it.timestamp.toLocalDate() <= period.second
        }

        return enumValues<MomentType>()
            .map {
                val filteredMomentsStart = momentsBeforeStart.filter { moment -> moment.type == it }
                val filteredMomentsEnd = momentsBeforeEnd.filter { moment -> moment.type == it }
                Statistics(
                    getString(it.labelId),
                    filteredMomentsStart.size.toLong(),
                    filteredMomentsEnd.size.toLong()
                )
            }
            .toMutableList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}