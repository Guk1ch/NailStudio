package egorka.artemiyev.naildesignconstructor.view.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentMainBinding
import egorka.artemiyev.naildesignconstructor.databinding.InputDateDialogViewBinding
import egorka.artemiyev.naildesignconstructor.model.utils.Case.idListGallery
import egorka.artemiyev.naildesignconstructor.view.adapter.DemoListAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.MainViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class MainFragment : Fragment() {

    private val binding: FragmentMainBinding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val mainViewModel = MainViewModel()
    private val format = SimpleDateFormat("dd/MM/yyyy")
    private val formatTime = SimpleDateFormat("HH:mm")

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
            recordButton.setOnClickListener {
                val dialogBinding: InputDateDialogViewBinding by lazy {
                    InputDateDialogViewBinding.inflate(
                        layoutInflater
                    )
                }
                Dialog(requireContext()).apply {
                    setContentView(dialogBinding.root)
                    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    dialogBinding.okBtn.setOnClickListener {
                        if (checkDateInput(dialogBinding.timeTxt.text.toString(), dialogBinding.dateTxt.text.toString()))
                            mainViewModel.makeRecord(dialogBinding.timeTxt.text.toString(), dialogBinding.dateTxt.text.toString(), requireContext())
                        this.cancel()
                    }
                }.show()
            }
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
    @SuppressLint("SimpleDateFormat")
    private fun checkDateInput(timeOld: String, dateOld: String): Boolean {
        val date: Date
        val time: Date
        return try {
            date = format.parse(dateOld)!!
            time = formatTime.parse(timeOld)!!
            if (time.hours > 18 || time.hours < 8){
                Toast.makeText(
                    activity,
                    getString(R.string._8_00_19_00),
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            if (date.after(Date())) true
            else {
                Toast.makeText(
                    activity,
                    getString(R.string._1),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Toast.makeText(
                activity,
                getString(R.string.enter_date_format),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }
}