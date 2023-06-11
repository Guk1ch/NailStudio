package egorka.artemiyev.naildesignconstructor.view.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.ClientRecordBinding
import egorka.artemiyev.naildesignconstructor.databinding.FragmentRecordsBinding
import egorka.artemiyev.naildesignconstructor.databinding.InputDateDialogViewBinding
import egorka.artemiyev.naildesignconstructor.databinding.ListClientsDialogBinding
import egorka.artemiyev.naildesignconstructor.databinding.PriceDialogBinding
import egorka.artemiyev.naildesignconstructor.model.ListClients
import egorka.artemiyev.naildesignconstructor.model.MasterWork
import egorka.artemiyev.naildesignconstructor.model.Record
import egorka.artemiyev.naildesignconstructor.model.RecordsList
import egorka.artemiyev.naildesignconstructor.model.utils.Case
import egorka.artemiyev.naildesignconstructor.view.adapter.ClientTextAdapter
import egorka.artemiyev.naildesignconstructor.view.adapter.RecordAdapter
import egorka.artemiyev.naildesignconstructor.viewmodel.RecordsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class RecordsFragment : Fragment(), RecordAdapter.OnClick, ClientTextAdapter.ClickClient {

    private val binding: FragmentRecordsBinding by lazy {
        FragmentRecordsBinding.inflate(
            layoutInflater
        )
    }
    private val storageRef = FirebaseStorage.getInstance().reference
    private val viewModel: RecordsViewModel by viewModels()
    private val format = SimpleDateFormat("dd/MM/yyyy")
    private val formatTime = SimpleDateFormat("HH:mm")
    private var time = ""
    private var date = ""
    private lateinit var dialogListClients: Dialog

    override fun click(data: Record, position: Int) {
        val disp = App.dm.api.deleteRecord(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                viewModel.trueList.remove(data)
                binding.rvRecords.adapter!!.notifyItemRemoved(position)
            })
    }

    override fun clickClient() {
        viewModel.makeRecord(time, date, requireContext())
        dialogListClients.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getListRecords()
        setObservers()
        applyClick()
    }

    private fun applyClick() {
        with(binding) {
            setPriceBtn.setOnClickListener {
                findNavController().navigate(R.id.action_recordsFragment_to_pricingFragment)
            }
            makeRecordBtn.setOnClickListener {
                val dialogBinding: InputDateDialogViewBinding by lazy {
                    InputDateDialogViewBinding.inflate(
                        layoutInflater
                    )
                }
                Dialog(requireContext()).apply {
                    setContentView(dialogBinding.root)
                    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    dialogBinding.okBtn.setOnClickListener {
                        if (checkDateInput(
                                dialogBinding.timeTxt.text.toString(),
                                dialogBinding.dateTxt.text.toString()
                            )
                        ) {
                            time = dialogBinding.timeTxt.text.toString()
                            date = dialogBinding.dateTxt.text.toString()
                            showDialogClients()
                        }

                        this.cancel()
                    }
                }.show()
            }
            addDesignButton.setOnClickListener {
                val dialogBinding: PriceDialogBinding by lazy {
                    PriceDialogBinding.inflate(
                        layoutInflater
                    )
                }
                val dialog = Dialog(requireContext()).apply {
                    setContentView(dialogBinding.root)
                    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialogBinding.okBtn.setOnClickListener {
                        startActivityForResult(
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            ), dialogBinding.priceTxtView.text.toString().toInt()
                        )
                        this.cancel()
                    }
                    dialogBinding.imgReduce.setOnClickListener {
                        if (dialogBinding.priceTxtView.text.toString()
                                .toInt() > 100
                        ) dialogBinding.priceTxtView.text =
                            (dialogBinding.priceTxtView.text.toString().toInt() - 100).toString()
                    }

                    dialogBinding.imgIncrease.setOnClickListener {
                        dialogBinding.priceTxtView.text =
                            (dialogBinding.priceTxtView.text.toString().toInt() + 100).toString()
                    }
                }
                dialog.show()
            }
            exitBtn.setOnClickListener {
                App.dm.logout()
                findNavController().navigate(R.id.action_recordsFragment_to_loginFragment)
            }

        }
    }

    private fun setAdapter() {
        binding.rvRecords.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            delay(400)
            binding.rvRecords.adapter = RecordAdapter(
                requireActivity(),
                viewModel.trueList, this@RecordsFragment
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val imgGallery: Uri = data.data ?: Uri.EMPTY
            val ref = storageRef.child("images/${imgGallery.lastPathSegment}")

            val uploadTask = ref.putFile(imgGallery)

            uploadTask.addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
            uploadTask.addOnCompleteListener {
                ref.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val disp =
                            App.dm.api.addWork(MasterWork(0, it.result.toString(), requestCode))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({

                                }, {})
                    }
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.isListDone.observe(viewLifecycleOwner) {
            if (it)
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
            if (time.hours > 18 || time.hours < 8 || date.month>12 || date.month<=0 || date.day > 31 || date.day <=0) {
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

    private fun showDialogClients() {
        val dialogBinding: ListClientsDialogBinding by lazy {
            ListClientsDialogBinding.inflate(
                layoutInflater
            )
        }
        dialogListClients = Dialog(requireContext()).apply {
            setContentView(dialogBinding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.rvClients.layoutManager = LinearLayoutManager(requireContext())
            dialogBinding.rvClients.adapter =
                ClientTextAdapter(requireContext(), Case.listClients, this@RecordsFragment)
        }
        dialogListClients.show()
    }
}