package ie.reflexivity.flexer.flexapi.model


data class Project(
        val projectType: ProjectType,
        val projectHomePage: String
)

enum class ProjectType {
    ETHERUM,
    TEZOS,
    EOS,
    IOTA,
    NEM,
    STEEM,
    GOLEM,
    HUMANIG,
    FILE_COIN,
    IPFS,
    ARAGON,
    MELON_PORT
}


