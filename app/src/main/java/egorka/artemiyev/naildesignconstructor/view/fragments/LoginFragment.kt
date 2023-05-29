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
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentLoginBinding
import egorka.artemiyev.naildesignconstructor.model.utils.Case

class LoginFragment : Fragment() {
    private val binding: FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private var authFirebase: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyClick()
    }

    private fun applyClick() {
        with(binding) {
            enterButton.setOnClickListener {
                authorise()
            }
            createTxt.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            forgotPasswordTxt.setOnClickListener {
                resetEmail()
            }
        }
    }

    private fun authorise() {
        val mail = binding.mailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        if (checkInput()) {
            authFirebase.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        App.dm.passLogin()
                        App.dm.setUserKey(authFirebase.currentUser?.uid ?: "")
                        findNavController().navigate(
                            if (authFirebase.currentUser!!.email.toString()
                                == getString(R.string.master_mail)
                            )
                                R.id.action_loginFragment_to_recordsFragment else R.id.action_loginFragment_to_mainFragment
                        )
                    } else {
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        makeToast(getString(R.string.smth_went_wrong))
                    }
                }
        }
    }

    private fun checkInput(): Boolean {
        if (checkEmail(binding.mailEditText.text.toString())) {
            if (!binding.passwordEditText.text.isNullOrEmpty()) {
                return true
            } else makeToast(getString(R.string.enter_the_password))
        }
        return false
    }

    private fun checkEmail(email: String): Boolean {
        when {
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> makeToast(getString(R.string.enter_email))

            email.isEmpty() -> makeToast(getString(R.string.enter_email))
            else -> return true
        }
        return false
    }

    private fun makeToast(m: String) {
        Toast.makeText(activity, m, Toast.LENGTH_SHORT).show()
    }
    private fun resetEmail() {
        if (checkEmail(binding.mailEditText.text.toString())) {
            MaterialDialog(requireActivity())
                .title(text = getString(R.string.do_you_want_to_reset_password_for))
                .message(text = binding.mailEditText.text.toString() + " ?")
                .positiveButton(text = "Yes") {
                    authFirebase.sendPasswordResetEmail(binding.mailEditText.text.toString())
                        .addOnCompleteListener {
                            if (!it.isSuccessful) {
                                Toast.makeText(activity, getString(R.string.some_problems_went), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
                .show { }
        } else Toast.makeText(activity, getString(R.string.your_email_must_be_correct), Toast.LENGTH_SHORT).show()
    }
}