package egorka.artemiyev.naildesignconstructor.model

data class SqlClient(
    var fullName: String,
    var id: Int,
    var phone: String,
    var records: List<Record>,
    var userKey: String
)