package tugas.papb.com.hanlo

data class Room(
        val name: String = "",
        var uuid: String = "",
        val member: MutableList<String> = mutableListOf(),
        val chats: MutableList<Chat> = mutableListOf())