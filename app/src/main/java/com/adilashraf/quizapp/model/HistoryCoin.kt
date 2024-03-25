package com.adilashraf.quizapp.model

import java.util.Date

 class HistoryCoin{
     var date: String = ""
     var coin: String = ""

     constructor()
     constructor(date: String, coin: String) {
         this.date = date
         this.coin = coin
     }
 }