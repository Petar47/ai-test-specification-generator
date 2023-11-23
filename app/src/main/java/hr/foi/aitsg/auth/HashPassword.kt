package hr.foi.aitsg.auth

import java.security.MessageDigest

fun getHashPassword(email : String, password : String): String{
    var hashPassword = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
    hashPassword += MessageDigest.getInstance("SHA-256").digest(email.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
    hashPassword = MessageDigest.getInstance("SHA-256").digest(hashPassword.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
    return hashPassword
}