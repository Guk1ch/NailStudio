package egorka.artemiyev.naildesignconstructor.model

data class SqlClientRegistration (
    var id: Int = 0,
    var fullName: String,
    var userKey: String,
    var phone: String
)