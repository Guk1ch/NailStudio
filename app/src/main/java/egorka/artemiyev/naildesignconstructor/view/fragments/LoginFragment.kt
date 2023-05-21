package egorka.artemiyev.naildesignconstructor.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private val binding: FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyClick()
    }
    private fun applyClick(){
        with(binding){
            enterButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
            createTxt.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            ForgotPassword.setOnClickListener{

            }
        }


    }

}