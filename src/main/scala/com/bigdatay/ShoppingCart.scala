package com.bigdatay

import com.bigdatay.model.Items

import scala.collection.mutable.ArrayBuffer
import scala.collection.{concurrent, mutable}

sealed trait ShoppingCart

case class Cart(inventoryList: concurrent.Map[String, BigDecimal]) extends ShoppingCart {

  protected[bigdatay] val items = new Items(inventoryList)
  protected[bigdatay] val itemPrices = new ArrayBuffer[BigDecimal] with mutable.SynchronizedBuffer[BigDecimal]

  def getOrderPricing[BigDecimal](order: ArrayBuffer[String]) = {

    initOrder(order)
    if (itemPrices.size > 0) itemPrices else 0
  }

  def getTotal(order: ArrayBuffer[String]) = {

    initOrder(order)
    itemPrices.reduceLeft[BigDecimal](_ + _)
  }

  private def initOrder(order: ArrayBuffer[String]) {
    itemPrices.clear()
    order.foreach { item => itemPrices += (items.find(item)) }
  }

}
