package com.overinfo.processors

import akka.actor.Props
import com.overinfo.processors.PersistenceActor
import com.overinfo.streams

/**
 * Created: Miguel A. Iglesias
 * Date: 1/23/14
 */
trait Persistence {
 def persistence(account_id:Long) = streams.system.actorOf(Props(new PersistenceActor),name = s"account-$account_id-persistence")
}
