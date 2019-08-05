package Carder_telegram


import java.lang.StringBuilder

interface lhu {
    fun _validate(credit_card:StringBuilder):Boolean//credit_card.length ==16
    fun create_credit_card():String //credit_card.length < 16
    fun _adding_procedure(credit_card_with_one_x: StringBuilder):Int //Somma tutto secondo la formula di lhu
    fun _set_credit_card(bin:String):StringBuilder
}