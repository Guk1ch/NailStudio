package egorka.artemiyev.naildesignconstructor.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentRecordsBinding
import egorka.artemiyev.naildesignconstructor.view.adapter.RecordAdapter

class RecordsFragment : Fragment() {

    private val binding: FragmentRecordsBinding by lazy { FragmentRecordsBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyClick()
        setAdapter()
    }

    private fun applyClick(){
        with(binding){
            createDesignButton.setOnClickListener {
                findNavController().navigate(R.id.action_recordsFragment_to_pricingFragment)
            }
            myDesignButton.setOnClickListener {

            }
            exitBtn.setOnClickListener {
                App.dm.logout()
                findNavController().navigate(R.id.action_recordsFragment_to_loginFragment)
            }
        }
    }
    private fun setAdapter(){
        binding.rvRecords.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecords.adapter = RecordAdapter(requireActivity(), listOf())
    }
}