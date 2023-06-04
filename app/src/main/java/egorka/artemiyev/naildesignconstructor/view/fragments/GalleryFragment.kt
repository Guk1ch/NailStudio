package egorka.artemiyev.naildesignconstructor.view.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentGalleryBinding
import egorka.artemiyev.naildesignconstructor.model.FireImageModel
import egorka.artemiyev.naildesignconstructor.model.MasterWork
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import egorka.artemiyev.naildesignconstructor.model.utils.Case
import egorka.artemiyev.naildesignconstructor.model.utils.Case.item
import egorka.artemiyev.naildesignconstructor.view.adapter.GalleryAdapter
import egorka.artemiyev.naildesignconstructor.view.adapter.MyListAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.GalleryViewModel


class GalleryFragment : Fragment(), GalleryAdapter.LongClick {

    private val binding: FragmentGalleryBinding by lazy {
        FragmentGalleryBinding.inflate(
            layoutInflater
        )
    }

    override fun onLongClick(data: MasterWork) {
        item = data
        findNavController().navigate(R.id.action_galleryFragment_to_imageFragment)
    }

    private val viewModel: GalleryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setText()
        setObservers()
        applyClick()
        viewModel.getListFull()
    }

    private fun applyClick() {
        with(binding) {
            favoriteImageView.setOnClickListener {
                Case.idListGallery = 3
                setText()
                setAdapter()
                binding.imgNotSupported.visibility = View.GONE
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

    private fun setAdapter() {
        val adapter: GalleryAdapter
        when (Case.idListGallery) {
            1 -> {
                adapter = GalleryAdapter(
                    requireActivity(),
                    viewModel.listFull.value!!,
                    this
                )
            }

            else -> {
                viewModel.getListFavorite()
                adapter = GalleryAdapter(
                    requireActivity(),
                    viewModel.listFavorite.value ?: MasterWorkList(),
                    this
                )
            }
        }
        binding.rvGallery.layoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.rvGallery.adapter = adapter
    }

    private fun setObservers() {
        viewModel.isRequestComplete.observe(viewLifecycleOwner) {
            setAdapter()
            binding.imgNotSupported.visibility = View.GONE
        }
    }
}