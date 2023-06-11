package egorka.artemiyev.naildesignconstructor.model.utils

import android.graphics.drawable.Drawable
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.model.MasterWork
import egorka.artemiyev.naildesignconstructor.model.SqlClient

object Case {
    var idListGallery = 1  //1 - Галлерея, 2 - Мои Дизайны, 3 - Избранное
    lateinit var item: MasterWork
    var listClients = arrayListOf<SqlClient>()
    val mapNails = mapOf(
        Pair(NailForm.ALMOND, NailLength.LONG) to R.drawable.third_long_nut,
        Pair(NailForm.ALMOND, NailLength.MIDDLE) to R.drawable.third_middle_nut,
        Pair(NailForm.KNIFE, NailLength.LONG) to R.drawable.second_long_knife,
        Pair(NailForm.BALLERINA, NailLength.LONG) to R.drawable.first_long_balerin,
        Pair(NailForm.RECTANGLE, NailLength.MIDDLE) to R.drawable.first_middle_rectangle,
        Pair(NailForm.RECTANGLE, NailLength.SHORT) to R.drawable.short_third_rectangle,
        Pair(NailForm.ELLIPSE, NailLength.SHORT) to R.drawable.short_second_elepse,
        Pair(NailForm.ELLIPSE, NailLength.MIDDLE) to R.drawable.second_middle_elepse
    )
    var clientToRecord = SqlClient("skgjhdkfjg", 0, "999999999", listOf(), "")
}