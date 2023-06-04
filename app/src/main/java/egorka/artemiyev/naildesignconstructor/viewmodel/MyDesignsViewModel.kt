package egorka.artemiyev.naildesignconstructor.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import egorka.artemiyev.naildesignconstructor.model.MyDesign

class MyDesignsViewModel : ViewModel() {
    var listMy = MutableLiveData<MutableList<MyDesign>>()

    fun fillListMy(){
        listMy.value = mutableListOf()
        listMy.value = App.dm.getListMy()
    }
}