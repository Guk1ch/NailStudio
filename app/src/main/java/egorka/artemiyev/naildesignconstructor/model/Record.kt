package egorka.artemiyev.naildesignconstructor.model

data class Record(
    val cost: Int,
    val id: Int,
    val idClient: Int,
    val idClientNavigation: Any,
    val isDone: Boolean,
    val time: String
)