package egorka.artemiyev.naildesignconstructor.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentMyDesignsBinding
import egorka.artemiyev.naildesignconstructor.model.MyDesign
import egorka.artemiyev.naildesignconstructor.view.adapter.MyListAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.MyDesignsViewModel

class MyDesignsFragment : Fragment(), MyListAdapter.LongClickDelete {

    private val binding: FragmentMyDesignsBinding by lazy {
        FragmentMyDesignsBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: MyDesignsViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun click(data: MyDesign) {
        val list = viewModel.listMy.value!!
        list.remove(data)
        viewModel.listMy.value!!.remove(data)


        App.dm.addListMy(Gson().toJson(list))
        binding.rvGallery.adapter!!.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fillListMy()
        applyClick()
        setAdapter()
    }

    private fun applyClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setAdapter() {
        binding.rvGallery.layoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.rvGallery.adapter = MyListAdapter(requireContext(), viewModel.listMy.value!!, this)
    }
}