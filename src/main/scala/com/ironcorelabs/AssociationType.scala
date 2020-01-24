package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

/** Represents the reason a document can be viewed by the requesting user. */
sealed trait AssociationType extends Product with Serializable

/** User created the document */
case object Owner extends AssociationType

/** User was directly granted access to the document */
case object FromUser extends AssociationType

/** User was granted access to the document via a group they are a member of */
case object FromGroup extends AssociationType

object AssociationType {
  def apply(at: jsdk.AssociationType): AssociationType =
    at match {
      case jsdk.AssociationType.Owner     => Owner
      case jsdk.AssociationType.FromUser  => FromUser
      case jsdk.AssociationType.FromGroup => FromGroup
    }
}
