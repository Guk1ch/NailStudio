package egorka.artemiyev.naildesignconstructor.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.model.SqlClientRegistration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegistrationViewModel : ViewModel() {
    fun createSqlUser(client: SqlClientRegistration) {
        val disp = App.dm.api.createUser(client)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
    }
}