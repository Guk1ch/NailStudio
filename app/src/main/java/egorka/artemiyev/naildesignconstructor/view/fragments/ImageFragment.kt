package egorka.artemiyev.naildesignconstructor.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.FragmentImageBinding
import egorka.artemiyev.naildesignconstructor.model.utils.Case.item

class ImageFragment : Fragment() {

    private val binding: FragmentImageBinding by lazy { FragmentImageBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.imgFull)
            .load(item.image)
            .into(binding.imgFull)
        binding.priceTxt.text = "Стоимость: ${item.price}"

        applyClick()
    }

    private fun applyClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}