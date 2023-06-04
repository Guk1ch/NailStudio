package egorka.artemiyev.naildesignconstructor.view.fragments

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentRecordsBinding
import egorka.artemiyev.naildesignconstructor.databinding.PriceDialogBinding
import egorka.artemiyev.naildesignconstructor.model.MasterWork
import egorka.artemiyev.naildesignconstructor.view.adapter.RecordAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecordsFragment : Fragment() {

    private val binding: FragmentRecordsBinding by lazy {
        FragmentRecordsBinding.inflate(
            layoutInflater
        )
    }
    private val storageRef = FirebaseStorage.getInstance().reference
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
            createDesignButton.setOnClickListener {
                findNavController().navigate(R.id.action_recordsFragment_to_pricingFragment)
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

                    dialogBinding.imgIncrease.setOnClickListener { dialogBinding.priceTxtView.text =
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
        binding.rvRecords.adapter = RecordAdapter(requireActivity(), listOf())
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
}