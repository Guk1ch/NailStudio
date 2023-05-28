package egorka.artemiyev.naildesignconstructor.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private val binding: FragmentSplashBinding by lazy { FragmentSplashBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (App.dm.isFirstLaunch()) findNavController().navigate(R.id.action_splashFragment_to_registrationFragment)
        else if (App.dm.isLoginPassed() && getString(R.string.master_uid) == App.dm.getUserKey()) findNavController().navigate(R.id.action_splashFragment_to_recordsFragment)
        else if (App.dm.isLoginPassed()) findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        else findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }
}