/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.kernel.laws.discipline.EqTests
import cats.syntax.all._
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

final class SecretSpec extends DisciplineSuite with Generators {
  checkAll("Secret", EqTests[Secret[String]].eqv)

  property("Secret.equals.other") {
    forAll { (secret: Secret[String]) =>
      (secret: Any) != secret.value
    }
  }

  property("Secret.equals.secret") {
    forAll { (first: Secret[String], second: Secret[String]) =>
      val expected = first.value == second.value
      val actual = first == second
      actual === expected
    }
  }

  property("Secret.hashCode") {
    forAll { (secret: Secret[String]) =>
      secret.hashCode === secret.value.hashCode
    }
  }

  property("Secret.show") {
    forAll { (secret: Secret[String]) =>
      secret.show === secret.toString
    }
  }

  property("Secret.toString") {
    forAll { (secret: Secret[String]) =>
      secret.toString === s"Secret(${secret.valueShortHash})"
    }
  }

  property("Secret.unapply") {
    forAll { (secret: Secret[String]) =>
      secret match {
        case Secret(value) => value === secret.value
      }
    }
  }

  property("Secret.value") {
    forAll { (value: String) =>
      Secret(value).value === value
    }
  }

  property("Secret.valueHash") {
    val gen =
      Gen.oneOf(
        Secret("RacrqvWjuu4KVmnTG9b6xyZMTP7jnX") -> "0a7425a3f46ceef2bada1f9ebddd2a857184997e",
        Secret("bc6Bt5uKfW2dgiyjiGa8dscapBFTXp") -> "ef252534aa72ba5006ff1c0532fe399845831854",
        Secret("9XVaCoj9bMoAYEu54JrbyZpJgB8yF9") -> "fd46dc3450f412d8f0dbdaf35c73392ff0aff0ea",
        Secret("4Y4vZxpowWFEk7YskMgQDVq4Q84Jca") -> "552f6054a7e8f71e1f8bd2348aa73c03f232d528",
        Secret("s546d495dNE7ryvqtvzsFJTcwkBVvs") -> "e756e80be9db6e9ccca8de8068718181a4befa29",
        Secret("ZKaDnb8wjWgdsL8Syi2UewYS69F5bB") -> "fbf1bfaf3e634711c5b24f61a62bdeebf0168f44",
        Secret("65JAstR8DK639LCUC5QgqFhjyK2BVL") -> "2b80d17fdd365f992a5005b15e8cbe09400111c4",
        Secret("48LPzHesU9ctbskYdu2FTSJuRQV8tB") -> "a3d9d4f7a701006fca9603110cd5a8eb8314b206",
        Secret("UcZvUdSmqtp4rnKVAQAWtnm3LM7uQJ") -> "4d1794369b59fdb15d08eb795686bd94fdde3cba",
        Secret("4N4VoriUwnvNr4HR8Qjto5RxNEnQVV") -> "ac8fec13cba5308b2d360450150a381bdf7c3d00"
      )

    forAll(gen) { case (secret, expectedHash) =>
      secret.valueHash === expectedHash
    }
  }

  property("Secret.valueShortHash") {
    forAll { (secret: Secret[String]) =>
      secret.valueShortHash === secret.valueHash.substring(0, 7)
    }
  }
}
