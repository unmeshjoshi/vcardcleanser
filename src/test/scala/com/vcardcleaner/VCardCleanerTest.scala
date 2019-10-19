package com.vcardcleaner

import java.io.{File, FileInputStream}
import java.util.HashMap

import ezvcard.VCard
import org.scalatest.FunSuite

class VCardCleanerTest extends FunSuite {

  test("should parse vcard") {
    import ezvcard.Ezvcard

    val str = "BEGIN:VCARD\r\n" + "VERSION:4.0\r\n" + "N:Doe;Jonathan;;Mr;\r\n" + "FN:John Doe\r\n" + "END:VCARD\r\n"

    val baseDir = "/home/unmesh/Desktop/Nokia C 500Contacts.2018"
    val fileNames = new File(baseDir).list()
    val map = new HashMap[String, java.util.List[VCard]]
    fileNames.foreach(name ⇒ {
      val fStream = new FileInputStream(s"${baseDir}/${name}")
      val card: VCard = Ezvcard.parse(fStream).first()
      if (card == null) {
        println(s"${name} has card null")
      } else {
        val structuredName = card.getStructuredName
        if (structuredName == null) {
          println(s"${card.getTelephoneNumbers.get(0).getText} has no name in file ${name}")
        } else {
          val given = structuredName.getGiven
          var cardList = map.get(given)
          if (cardList == null) {
            cardList = new java.util.ArrayList[VCard]
            map.put(card.getStructuredName.getGiven, cardList)
          }
          cardList.add(card)
        }
      }
    })

    val keys = map.keySet()
    keys.forEach(key ⇒ {
      print(key)
      print(" -> ")
      print(map.get(key).size())
      println("")
    })
  }
}
