package com.ironcorelabs.scala

package object sdk {
  import java.{util => ju}

  implicit class OptionalOps[A](o: ju.Optional[A]) {
    def toScala: Option[A] =
      if (o.isPresent)
        Some(o.get)
      else
        None
  }

}
