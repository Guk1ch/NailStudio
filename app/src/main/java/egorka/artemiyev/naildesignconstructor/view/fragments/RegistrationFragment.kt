package egorka.artemiyev.naildesignconstructor.view.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private val binding: FragmentRegistrationBinding by lazy {
        FragmentRegistrationBinding.inflate(
            layoutInflater
        )
    }
    private var authFirebase: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyClick()
        App.dm.endFirstLaunch()
    }

    private fun applyClick() {
        binding.createButton.setOnClickListener {
            registration()
        }
        binding.enterTxt.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    private fun checkInput(): Boolean {
        with(binding) {
            when {
                !Patterns.EMAIL_ADDRESS.matcher(mailEditText.text.toString())
                    .matches() -> makeToast(getString(R.string.enter_email))

                !Patterns.PHONE.matcher(phoneEditText.text.toString()).matches() -> makeToast(
                    getString(R.string.enter_your_phone)
                )

                mailEditText.text.isNullOrEmpty() -> makeToast(getString(R.string.enter_email))
                fioEditText.text.isNullOrEmpty() -> makeToast(getString(R.string.enter_your_nickname))
                phoneEditText.text.isNullOrEmpty() -> makeToast(getString(R.string.enter_your_phone))
                passwordEditText.text.isNullOrEmpty() -> makeToast(getString(R.string.enter_the_password))
                passwordEditText.text.toString().length < 6 -> makeToast(getString(R.string.password_must_be_6_symbols_at_least))
                repeatPasswordEditText.text.toString() != binding.passwordEditText.text.toString() -> makeToast(
                    getString(R.string.password_mismatch)
                )

                else -> return true
            }
            return false
        }
    }

    private fun makeToast(m: String) {
        Toast.makeText(activity, m, Toast.LENGTH_SHORT).show()
    }

    private fun registration() {
        val mail = binding.mailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (checkInput()) {
            authFirebase.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = authFirebase.currentUser
                        updateUI(user)
                        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    } else {
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        updateUI(null)
                    }
                }
        }
    }

    private fun updateUI(account: FirebaseUser?) {
        if (account != null)
            makeToast(getString(R.string.registered_successfully))
        else makeToast(getString(R.string.smth_went_wrong))
    }
}