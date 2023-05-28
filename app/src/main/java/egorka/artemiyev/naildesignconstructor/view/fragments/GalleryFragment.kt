package egorka.artemiyev.naildesignconstructor.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.FragmentGalleryBinding
import egorka.artemiyev.naildesignconstructor.model.FireImageModel
import egorka.artemiyev.naildesignconstructor.model.utils.Case
import egorka.artemiyev.naildesignconstructor.view.adapter.GalleryAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.GalleryViewModel


class GalleryFragment : Fragment() {

    private val binding: FragmentGalleryBinding by lazy {
        FragmentGalleryBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: GalleryViewModel by viewModels()
    private var list = mutableListOf<FireImageModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setText()
        applyClick()
        checkButtonVisibility()
        viewModel.fillListFavorite()
        setObservers()
        setAdapter()
    }

    private fun applyClick() {
        with(binding) {
            favoriteImageView.setOnClickListener {
                Case.idListGallery = 3
                checkButtonVisibility()
                setText()
            }
            arrowBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setText() {
        var title = ""
        when (Case.idListGallery) {
            1 -> title = getString(R.string.gallery_design)
            2 -> title = getString(R.string.my_designs)
            3 -> title = getString(R.string.favorite)
        }
        binding.titleText.text = title
    }

    private fun checkButtonVisibility() {
        binding.favoriteImageView.visibility =
            if (Case.idListGallery == 3) View.GONE else View.VISIBLE
    }

    private fun setAdapter() {
//        when(Case.idListGallery){
//            1 ->
//            2 ->
//            3 ->
//        }
        binding.rvGallery.layoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.rvGallery.adapter =
            GalleryAdapter(
                requireActivity(),
                viewModel.listFull.value!!
            )
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers(){
        viewModel.listFavorite.observe(viewLifecycleOwner){
            list = viewModel.listFavorite.value ?: mutableListOf()
            binding.rvGallery.adapter?.notifyDataSetChanged()
        }
    }
}