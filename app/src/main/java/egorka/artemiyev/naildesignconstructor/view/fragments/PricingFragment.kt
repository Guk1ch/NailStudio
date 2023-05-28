package egorka.artemiyev.naildesignconstructor.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.FragmentPricingBinding

class PricingFragment : Fragment() {

    private val binding: FragmentPricingBinding by lazy {
        FragmentPricingBinding.inflate(
            layoutInflater
        )
    }
    private val firebaseRealtime = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyClick()
        setValues()
    }

    private fun applyClick() {
        with(binding) {
            saveButton.setOnClickListener {
                savePrice()
                findNavController().popBackStack()
            }
            backButton.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun savePrice() {
        try {
            firebaseRealtime.child(getString(R.string.designprice))
                .setValue(binding.textDesignPrice.text.toString())
            firebaseRealtime.child(getString(R.string.nailprice))
                .setValue(binding.textManicPrice.text.toString())
            firebaseRealtime.child(getString(R.string.coatingprice))
                .setValue(binding.textPokrPrice.text.toString())
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setValues() {
        firebaseRealtime.child(getString(R.string.designprice)).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.textDesignPrice.setText(it.result.value.toString())
                }
            }
        firebaseRealtime.child(getString(R.string.nailprice)).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.textManicPrice.setText(it.result.value.toString())
                }
            }
        firebaseRealtime.child(getString(R.string.coatingprice)).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.textPokrPrice.setText(it.result.value.toString())
                }
            }
    }
}