package com.example

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class Game(player1: ActorRef, player2: ActorRef) extends Actor {
    val players = List(player1, player2)

    var moveNumber = 0

    val winningCombination = Set(Set(1, 2, 3), Set(4, 5, 6), Set(7, 8, 9), Set(1, 4, 7), Set(2, 5, 8), Set(3, 6, 9), Set(1, 5, 9), Set(3, 5, 7))

    def currentPlayer = players(moveNumber % 2)

    def hasWon(moves: Set[Int]): Boolean = {
        winningCombination exists ((combination: Set[Int]) => (combination intersect moves).size == 3)
    }

    override def receive: Receive = {
        case move: Int       => {
            currentPlayer ! move
            currentPlayer ! CheckMoves()
            moveNumber += 1
        }
        case moves: Set[Int] =>
            if (hasWon(moves)) {
                println(currentPlayer, "won and moves are ", moves)
                context.stop(self)
            }
    }

}

case class CheckMoves()


object Game extends App {
    val system = ActorSystem()
    val player1 = system.actorOf(Props[Player], "player1")
    val player2 = system.actorOf(Props[Player], "player2")
    val game = system.actorOf(Props(new Game(player1, player2)), "game")
    game ! 4
    game ! 1
    game ! 5
    game ! 3
    game ! 7
    game ! 2
}