package egorka.artemiyev.naildesignconstructor.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentMainBinding
import egorka.artemiyev.naildesignconstructor.model.utils.Case.idListGallery
import egorka.artemiyev.naildesignconstructor.view.adapter.DemoListAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private val binding: FragmentMainBinding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val mainViewModel = MainViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyClick()
        mainViewModel.getListFull()
        setObservers()
    }

    private fun applyClick(){
        with(binding){
            recordButton.setOnClickListener {}
            galleryButton.setOnClickListener {
                openGallery(1)
            }
            myDesignButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_myDesignsFragment)
            }
            createDesignButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_constructorFragment)
            }
            favoriteLayout.setOnClickListener {
                openGallery(3)
            }
            exitBtn.setOnClickListener {
                App.dm.logout()
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
    }

    private fun openGallery(idList: Int){
        findNavController().navigate(R.id.action_mainFragment_to_galleryFragment)
        idListGallery = idList
    }

    private fun setAdapter(){
        binding.rvNailDemo.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.rvNailDemo.adapter = DemoListAdapter(requireContext(), mainViewModel.listFull.value!!)
    }
    private fun setObservers() {
        mainViewModel.isRequestComplete.observe(viewLifecycleOwner) {
            setAdapter()
        }
    }
}