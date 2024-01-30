package com.github.sandroln.chosenboard

data class BoardCache(
    private val id: String,
    private val name: String,
    private val isMyBoard: Boolean,
    private val ownerId: String = ""
) {

    interface Mapper<T : Any> {
        fun map(id: String, name: String, isMyBoard: Boolean, ownerId: String): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T = mapper.map(id, name, isMyBoard, ownerId)
}