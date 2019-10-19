package com.vcardcleaner

import java.io.{File, FileInputStream, FileOutputStream}
import java.util
import java.util.HashMap

import scala.jdk.CollectionConverters._
import ezvcard.{Ezvcard, VCard}
import ezvcard.property.{Address, Email, StructuredName, Telephone}
import org.scalatest.FunSuite

import scala.collection.convert.ImplicitConversionsToJava.`buffer AsJavaList`

class VCardCleanerTest extends FunSuite {

  test("should parse vcard") {
    val card1 = new VCard()
    card1.addTelephoneNumber(new Telephone("2220020020"))

    val str = "BEGIN:VCARD\r\n" + "VERSION:4.0\r\n" + "N:Doe;Jonathan;;Mr;\r\n" + "FN:John Doe\r\n" + "END:VCARD\r\n"

    val baseDir = "/home/unmesh/Desktop/"
    val sourceDir = s"${baseDir}/Nokia C 500Contacts.2018"
    val fileNames = new File(sourceDir).list()
    val map = new HashMap[String, java.util.List[VCard]]
    fileNames.foreach(name ⇒ {
      val fStream = new FileInputStream(s"${sourceDir}/${name}")
      val card: VCard = Ezvcard.parse(fStream).first()
      if (card == null) {
        println(s"${name} has card null")
      } else {
        val structuredName = card.getStructuredName
        if (structuredName == null) {
          println(s"${card.getTelephoneNumbers.get(0).getText}")
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
    var diffCount = 0
    var totalContacts = 0
    val keys = map.keySet()

    val uniqContacts = new java.util.ArrayList[VCard]()

    keys.forEach(key ⇒ {

      //      print(key)
      //      print(" -> ")
      val cards = map.get(key)
      //      print(cards.size())
      //      println("")
      //      println(cards)
      if (cards.size() > 1) {

        val card = cards.get(0)
        val card2 = cards.get(1)
        totalContacts += 1
        if (!card.write().equals(card2.write())) {
          diffCount += 1

          val mergedCard = mergeCards(cards.asScala.toList)
          println(mergedCard)

          uniqContacts.add(mergedCard)
          val numbers = card.getTelephoneNumbers.asScala.toSet

          println("-------------------------")
        } else {
          uniqContacts.add(card)
        }
      }

    })

    uniqContacts.asScala.foreach(card ⇒ {
      var givenName = card.getStructuredName.getGiven
      if (givenName == null) {
        givenName = card.getStructuredName.getFamily
      }
      val file = new FileOutputStream(s"${baseDir}/cleancontacts/${givenName.replace("/", "-")}.vcf")
      card.write(file)
      file.flush()
    })

    println(s"${diffCount} are different our of ${totalContacts}")

  }

  def mergeCards(cards:List[VCard]):VCard = {
    val mergedCard = new VCard()
    val addressSet = new util.HashSet[Address]()
    val telephoneNumberSet = new util.HashSet[Telephone]()
    val emails = new util.HashSet[Email]
    val structuredNames = new util.HashSet[StructuredName]
    cards.foreach(card ⇒ {
      card.getAddresses.asScala.foreach(addressSet.add(_))
      card.getTelephoneNumbers.asScala.foreach(telephoneNumberSet.add(_))
      card.getEmails.asScala.foreach(emails.add(_))
      card.getStructuredNames.asScala.foreach(structuredNames.add(_))
    })

    addressSet.asScala.foreach(mergedCard.addAddress(_))
    telephoneNumberSet.asScala.foreach(mergedCard.addTelephoneNumber(_))
    emails.asScala.foreach(mergedCard.addEmail(_))
    structuredNames.asScala.foreach(mergedCard.setStructuredName(_))

    mergedCard
  }
}
