package com.github.sandroln.openedboard

interface TicketColor {

    abstract class Abstract(val name: String, val value: String) : TicketColor

    object Red : Abstract("red", "#ef476f")
    object Orange : Abstract("orange", "#f77f00")
    object Purple : Abstract("purple", "#8f2d56")
    object Green : Abstract("green", "#06d6a0")
    object Blue : Abstract("blue", "#118ab2")
    object Yellow : Abstract("yellow", "#ffd166")

    object Factory {

        private val list = listOf(Yellow, Orange, Red, Purple, Green, Blue)

        fun valueByIndex(index: Int) = list[index].value

        fun nameByIndex(index: Int) = list[index].name

        fun indexByValue(value: String): Int {
            val color = list.find { it.value == value } ?: Yellow
            return list.indexOf(color)
        }

        fun nameByValue(hex: String) = (list.find { it.value == hex } ?: Yellow).name

        fun valueByName(name: String) = (list.find { it.name == name } ?: Yellow).value
    }
}