package Carder_telegram


import java.lang.Exception
import java.lang.StringBuilder
import java.util.Random

class cc_generator(var bin:String, var digits:Int=16):lhu{
    var credit_card_with_x:java.lang.StringBuilder = _set_credit_card(bin)
    var _convToInt = {number:Char -> number.toInt()-48}

    override fun _set_credit_card(bin: String): StringBuilder {
        var temp=""
        when(digits){
            15 -> temp="0"
        }
        return StringBuilder((temp+bin+"xxxxxxxxxxxxxxxx").substring(0,16))

    }

    fun getBrand(Card:String = credit_card_with_x.toString()):String{
        var brand = "None"
        when (Card[0]) {
                '5' -> brand = "MASTERCARD"
                '4' -> brand = "VISA"
                '3' -> when (Card[1]) {
                    '3', '7' -> brand = "AMERICAN EXPRESS"  //ha 15 digit
                }
            }
        return brand
    }

    override  fun _validate(credit_card: StringBuilder): Boolean {
        println("[VALIDATE]   ${credit_card.toString()}")
        val number = _adding_procedure(StringBuilder(credit_card))
        print("NUM $number")
        if(number%10==0) return true
        else return false
    }

    inline fun _dispatcherInt(t:Int):List<Int>{
        val dec:Int
        dec = t/10
        return listOf<Int>(dec,t-dec*10 )
    }
    inline fun _control_digit_x_dispari(card:StringBuilder):Int{
        val number = _adding_procedure(StringBuilder(card))
        val decAndint =  _dispatcherInt(number)
        var result = (decAndint[0]+1)*10 - number
        return  if(result != 10)  result else 0
    }

    override fun create_credit_card(): String {
        //Partendo da destra e spostandosi verso sinistra, moltiplicare per 2 ogni cifra posta in posizione pari
        //Laddove la moltiplicazione ha dato un risultato a due cifre, sommare le due cifre per ottenerne una sola (es. 18 = 1+8)
        //Sommare tutte le cifre, sia quelle che si trovano in posizione pari, sia quelle che si trovano in posizione dispari
        var lista_pari = ArrayList<Int>()
        var lista_dispari=ArrayList<Int>()
        var number :Int
        var temp:Int
        for (x in credit_card_with_x.length-1 downTo 0){
            if(credit_card_with_x[x]=='x'){
                if(x%2==0) lista_pari.add(x)
                else lista_dispari.add(x)
            }
        }
        number = _adding_procedure(credit_card_with_x)
        if(lista_dispari.size ==0){
            if(lista_pari.size==1) {
                if( number%2==0 ){
                    //dispari = 0
                    //pari =1 con numero carta PARI
                    //      println("DISPARI = 0 PARI=1 NUMERO CARTA PARI $number")
                    val digit = _control_digit_x_dispari(credit_card_with_x)
                    credit_card_with_x[lista_pari[0]] = (digit/2).toChar()+48
                    //     println("VALIDATE ${_validate(credit_card_with_x)}")
                    return credit_card_with_x.toString()
                } else {
                    throw Exception("impossibile calcolare")
                }
            }else if (lista_pari.size==0)
            {
                throw Exception("QUESTO PROGRAMMA NON VALIDA CARTE COMPLETE")
            } else {
                //dispari = 0
                //pari >1
                //        println("[DEBUG] DISPARI=0 PARI>1")
                //    println("[DEBUG] NUMBER [$number]")
                //    println("CREDIT CARD: $credit_card_with_x")
                do {
                    for (q in 0..lista_pari.size-3) {
                        credit_card_with_x[lista_pari[q]] = ((0..9).shuffled().first() + 48).toChar()
                    }
                    number = _adding_procedure(credit_card_with_x)

                }while((number % 2 != 0) or (number % 10 == 0) or (_control_digit_x_dispari(StringBuilder(credit_card_with_x))%4!=0))

                number = _control_digit_x_dispari(StringBuilder(credit_card_with_x))
                //    println("elementi rimasti ${lista_pari}   number $number")
                //    println("CREDIT CARD: $credit_card_with_x adding ${_adding_procedure(credit_card_with_x)}")
                if(number!=2)
                    for (el in lista_pari.subList(lista_pari.size-2,lista_pari.size)) credit_card_with_x[el] = (number / 4 + 48).toChar()
                else{
                    val rand =(0..1).shuffled().first()
                    credit_card_with_x[lista_pari[rand]] = '1'
                    credit_card_with_x[lista_pari[listOf<Int>(1,0)[rand]]] = '0'
                }
                //    println("CREDIT CARD: $credit_card_with_x")
                //  println("[VALIDATE] ${_validate(credit_card_with_x)}")
                return  credit_card_with_x.toString()
            }

        }else{
            val tempList = ArrayList<Int>()
            var digit :Int
            tempList.addAll(lista_dispari)
            tempList.addAll(lista_pari)
            for (pos in 1..tempList.size-1){
                credit_card_with_x[tempList[pos]] = (0..9).shuffled().first().toChar()+48
            }
            //   println("adding $number")
            digit = _control_digit_x_dispari(credit_card_with_x)
            //  println("[DEBUG] DIGIT $digit")
            credit_card_with_x[lista_dispari[0]]=digit.toChar()+48
            //  println(credit_card_with_x)
            //   println("[VALIDATE] ${_validate(credit_card_with_x)}")
            return credit_card_with_x.toString()
        }
    }

    override fun _adding_procedure(credit_card: StringBuilder): Int {
        //here, there is a card with a X or complete
        var count = 0
        var dispInt = emptyList<Int>()
        for(x in credit_card.length-2 downTo 0 step 2){
            if(credit_card[x]!='x') {
                var temp = _convToInt(credit_card[x]) * 2
                if (temp > 9) {
                    dispInt = _dispatcherInt(temp)
                    temp = dispInt[0] + dispInt[1]

                }
                count += if ((x != 0) && credit_card[x-1]!='x' ) temp + _convToInt(credit_card[x - 1]) else temp
            }else{
                count += if((x != 0) && credit_card[x-1]!='x' ) _convToInt(credit_card_with_x[x-1]) else 0
            }
        }
        count += if(credit_card.last()!='x')  _convToInt(credit_card.last()) else 0
        return count

    }

}