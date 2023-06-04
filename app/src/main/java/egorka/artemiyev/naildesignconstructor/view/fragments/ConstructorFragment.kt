package egorka.artemiyev.naildesignconstructor.view.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentConstructorBinding
import egorka.artemiyev.naildesignconstructor.model.MyDesign
import egorka.artemiyev.naildesignconstructor.model.NailImage
import egorka.artemiyev.naildesignconstructor.view.adapter.ColorAdapter
import egorka.artemiyev.naildesignconstructor.view.adapter.NailImageAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.ConstructorViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ConstructorFragment : Fragment(), ColorAdapter.ColorClick {

    override fun click(color: Int) {
        imageTint = color
        binding.imageForm.setColorFilter(color)
    }

    private val firebaseRealtime = FirebaseDatabase.getInstance().reference
    private var vpList = NailImage()
    private var imageTint: Int = Color.parseColor("#B2A9EC")
    private var formNail: Int = R.drawable.short_second_elepse

    private val binding: FragmentConstructorBinding by lazy {
        FragmentConstructorBinding.inflate(
            layoutInflater
        )
    }
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

    private fun applyClick() {
        with(binding) {
            arrowBack.setOnClickListener { findNavController().popBackStack() }
            chooseFormButton.setOnClickListener { }
            chooseLengthButton.setOnClickListener { }

            prevButton.setOnClickListener {
                if (vpNails.currentItem == 0) vpNails.currentItem = (vpList.size-1)
                else vpNails.currentItem--
            }
            nextButton.setOnClickListener {
                if (vpNails.currentItem == (vpList.size-1)) vpNails.currentItem = 0
                else vpNails.currentItem++
            }

            saveButton.setOnClickListener {
                val list = App.dm.getListMy()
                list.add(MyDesign(vpList[vpNails.currentItem].image, formNail, imageTint))
                App.dm.addListMy(Gson().toJson(list))
                findNavController().popBackStack()
            }
            pricingImageView.setOnClickListener {
                val dialog = MaterialDialog(requireActivity())
                    .title(text = getString(R.string.pricing))
                    .positiveButton(text = getString(R.string.close)) {
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
                if (it.isSuccessful) {
                    final += "${getString(R.string.design)} - ${it.result.value}\n\n"
                    firebaseRealtime.child(getString(R.string.nailprice))
                        .get()
                        .addOnCompleteListener { nail ->
                            if (nail.isSuccessful) {
                                final += "${getString(R.string.form)} - ${nail.result.value}\n\n"
                                firebaseRealtime.child(getString(R.string.coatingprice))
                                    .get()
                                    .addOnCompleteListener { coating ->
                                        if (coating.isSuccessful) {
                                            final += "${getString(R.string.pokrit)} - ${coating.result.value}"
                                            dialog.message(text = final)
                                            dialog.show { }
                                        }
                                    }
                            }
                        }
                } else Toast.makeText(
                    requireContext(),
                    getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setAdapter() {
        binding.rvColors.layoutManager =
            GridLayoutManager(requireContext(), 6, LinearLayoutManager.VERTICAL, false)
        binding.rvColors.adapter = ColorAdapter(requireContext(), viewModel.listColors, this)

        val disp = App.dm.api.getNailImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                vpList = it
                binding.vpNails.adapter = NailImageAdapter(requireContext(), vpList)
            }, {
                binding.vpNails.adapter = NailImageAdapter(requireContext(), NailImage())
            })
    }

    private fun checkViewPagerItems(): Boolean {
        return binding.vpNails.size != 0
    }
}