package egorka.artemiyev.naildesignconstructor.view.fragments

import android.app.FragmentContainer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.FragmentConstructorBinding
import egorka.artemiyev.naildesignconstructor.view.adapter.ColorAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.ConstructorViewModel

class ConstructorFragment : Fragment() {

    private val firebaseRealtime = FirebaseDatabase.getInstance().reference
    private val binding: FragmentConstructorBinding by lazy { FragmentConstructorBinding.inflate(layoutInflater) }
    private val viewModel: ConstructorViewModel by viewModels()
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
            arrowBack.setOnClickListener { findNavController().popBackStack() }
            chooseFormButton.setOnClickListener {  }
            chooseLengthButton.setOnClickListener {  }

            prevButton.setOnClickListener {
                if (checkViewPagerItems() && vpNails.currentItem-1 >= 0)
                    vpNails.currentItem--
            }
            nextButton.setOnClickListener {
                if (checkViewPagerItems() && vpNails.currentItem+1 < vpNails.size)
                    vpNails.currentItem++
            }

            saveButton.setOnClickListener {
                findNavController().popBackStack()
            }
            pricingImageView.setOnClickListener {
                val dialog =MaterialDialog(requireActivity())
                    .title(text = getString(R.string.pricing))
                    .positiveButton (text = getString(R.string.close)){
                        it.cancel()
                    }
                getPricing(dialog)
            }
        }
    }

    private fun getPricing(dialog: MaterialDialog) {
        var final = ""
        firebaseRealtime.child(getString(R.string.designprice))
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    final += "${getString(R.string.design)} - ${it.result.value}\n\n"
                    firebaseRealtime.child(getString(R.string.nailprice))
                        .get()
                        .addOnCompleteListener {nail ->
                            if (nail.isSuccessful){
                                final += "${getString(R.string.form)} - ${nail.result.value}\n\n"
                                firebaseRealtime.child(getString(R.string.coatingprice))
                                    .get()
                                    .addOnCompleteListener { coating ->
                                        if (coating.isSuccessful){
                                            final += "${getString(R.string.pokrit)} - ${coating.result.value}"
                                            dialog.message(text = final)
                                            dialog.show {  }
                                        }
                                    }
                            }
                        }
                }
                else Toast.makeText(requireContext(), getString(R.string.no_connection), Toast.LENGTH_SHORT).show()
            }
    }
    private fun setAdapter(){
        binding.rvColors.layoutManager = GridLayoutManager(requireContext(), 6, LinearLayoutManager.VERTICAL,  false)
        binding.rvColors.adapter = ColorAdapter(requireContext(), viewModel.listColors)
    }

    private fun checkViewPagerItems(): Boolean{
        return binding.vpNails.size != 0
    }
}