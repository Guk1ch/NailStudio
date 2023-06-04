package egorka.artemiyev.naildesignconstructor.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.FragmentSplashBinding
import egorka.artemiyev.naildesignconstructor.model.FireImageModel
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import egorka.artemiyev.naildesignconstructor.model.MyDesign
import egorka.artemiyev.naildesignconstructor.model.utils.Case.listClients
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SplashFragment : Fragment() {

    private val binding: FragmentSplashBinding by lazy { FragmentSplashBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getClients()

        if (App.dm.isFirstLaunch()) {
            App.dm.setListFavorite(Gson().toJson(MasterWorkList()))
            App.dm.addListMy(Gson().toJson(mutableListOf<MyDesign>()))
            findNavController().navigate(R.id.action_splashFragment_to_registrationFragment)
        }
        else if (App.dm.isLoginPassed() && getString(R.string.master_uid) == App.dm.getUserKey()) findNavController().navigate(R.id.action_splashFragment_to_recordsFragment)
        else if (App.dm.isLoginPassed()) findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        else findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }
    fun getClients(){
        val disp = App.dm.api.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listClients = it
            }, {})
    }
}