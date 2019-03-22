package com.example

import akka.actor.Actor

class Player extends Actor {
    var moves: Set[Int] = Set()

    override def receive: Receive = {
        case move: Int    => {
            println(moves,"My moves")
            moves += move
        }
        case CheckMoves() => sender ! moves
    }
}
