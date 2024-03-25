package com.adilashraf.quizapp.model

class UserModel {

    var name:String = ""
    var age: Int = 0
    var email: String = ""
    var password: String = ""

    constructor()
    constructor(name: String, age: Int, email: String, password: String) {
        this.name = name
        this.age = age
        this.email = email
        this.password = password
    }


}