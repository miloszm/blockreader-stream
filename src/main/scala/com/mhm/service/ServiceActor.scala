package com.mhm.service

import akka.actor.Actor

class ServiceActor extends Actor {
  override def receive: Actor.Receive = {
    case _ => ()
  }

}
