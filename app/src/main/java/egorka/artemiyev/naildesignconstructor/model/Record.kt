package egorka.artemiyev.naildesignconstructor.model

data class Record(
    var cost: Int,
    var id: Int,
    var idClient: Int,
    var date: String,
    var idClientNavigation: SqlClient? = null,
    var isDone: Boolean,
    var time: String
)