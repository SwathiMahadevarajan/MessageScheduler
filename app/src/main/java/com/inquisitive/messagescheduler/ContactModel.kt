package com.inquisitive.messagescheduler

class ContactModel(name: String, num: String) {
    private var name: String
    private var num: String
    init {
        this.name= name
        this.num = num
    }
    fun getName(): String {
        return this.name
    }
    fun setName(name: String) {
        this.name = name
    }
    fun getNum(): String {
        return this.num
    }
    fun setNum(num: String) {
        this.num = num
    }
}

