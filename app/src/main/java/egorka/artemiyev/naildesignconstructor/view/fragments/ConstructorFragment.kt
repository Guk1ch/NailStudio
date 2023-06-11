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
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentConstructorBinding
import egorka.artemiyev.naildesignconstructor.model.MyDesign
import egorka.artemiyev.naildesignconstructor.model.NailImage
import egorka.artemiyev.naildesignconstructor.model.utils.Case.mapNails
import egorka.artemiyev.naildesignconstructor.model.utils.NailForm
import egorka.artemiyev.naildesignconstructor.model.utils.NailLength
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
    private var formNail: Int = R.drawable.second_long_knife

    private var nailForm = NailForm.KNIFE
    private var nailLength = NailLength.LONG
    private var tint = "#00FFE228"

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
            pictureColorButton.setOnClickListener {
                setPictureTint()
            }
            arrowBack.setOnClickListener { findNavController().popBackStack() }
            chooseFormButton.setOnClickListener {
                showDialogChooseForm(requireContext().getString(R.string.choose_form))
            }
            chooseLengthButton.setOnClickListener {
                showDialogChooseLength(requireContext().getString(R.string.choose_form))
            }

            prevButton.setOnClickListener {
                if (vpNails.currentItem == 0) vpNails.currentItem = (vpList.size - 1)
                else vpNails.currentItem--
            }
            nextButton.setOnClickListener {
                if (vpNails.currentItem == (vpList.size - 1)) vpNails.currentItem = 0
                else vpNails.currentItem++
            }

            saveButton.setOnClickListener {
                val list = App.dm.getListMy()
                list.add(MyDesign(vpList[vpNails.currentItem].image, formNail, imageTint, tint))
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
                binding.vpNails.adapter = NailImageAdapter(requireContext(), vpList, tint)
            }, {
                binding.vpNails.adapter = NailImageAdapter(requireContext(), NailImage(), tint)
            })
    }

    private fun showDialogChooseForm(title: String) {
        val singleItems = arrayOf(
            "Миндаль",
            "Квадрат",
            "Овал",
            "Балерина",
            "Стилет"
        )
        val checkedItem = when (nailForm) {
            NailForm.ALMOND -> 0
            NailForm.RECTANGLE -> 1
            NailForm.ELLIPSE -> 2
            NailForm.BALLERINA -> 3
            else -> 4
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                dialog.cancel()
            }
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                nailForm = when (which) {
                    0 -> NailForm.ALMOND
                    1 -> NailForm.RECTANGLE
                    2 -> NailForm.ELLIPSE
                    3 -> NailForm.BALLERINA
                    else -> NailForm.KNIFE
                }
                setFormImage()
            }
            .show()
    }

    private fun showDialogChooseLength(title: String) {
        val singleItems = arrayOf("Короткая", "Средняя", "Длинная")
        val checkedItem = when (nailLength) {
            NailLength.SHORT -> 0
            NailLength.MIDDLE -> 1
            else -> 2
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                dialog.cancel()
            }
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                nailLength = when (which) {
                    0 -> NailLength.SHORT
                    1 -> NailLength.MIDDLE
                    else -> NailLength.LONG
                }
                setFormImage()
            }
            .show()
    }
    private fun setFormImage(){
        val key = Pair(nailForm, nailLength)

        if (mapNails.containsKey(key)){
            formNail = mapNails[key] ?: formNail
            Glide.with(binding.imageForm)
                .load(formNail)
                .into(binding.imageForm)
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.form_is_not_exist), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPictureTint(){
        val singleItems = arrayOf("Белый", "Прозрачный", "Чёрный")
        val checkedItem = when (tint) {
            "#000000" -> 2
            "#00FFE228" -> 1
            else -> 0
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.choose_color_picture))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                dialog.cancel()
            }
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                tint = when (which) {
                    0 -> "#FFFFFF"
                    1 -> "#00FFE228"
                    else -> "#000000"
                }
                setTint()
                dialog.cancel()
            }
            .show()
    }
    private fun setTint(){
        val currentItem = binding.vpNails.currentItem
        binding.vpNails.adapter = NailImageAdapter(requireContext(), vpList, tint)
        binding.vpNails.currentItem = currentItem
    }
}