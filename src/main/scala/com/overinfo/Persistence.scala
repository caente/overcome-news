package com.overinfo

import akka.actor.Props

/**
 * Created: Miguel A. Iglesias
 * Date: 1/23/14
 */
trait Persistence {
 def persistence(account_id:Long) = streams.system.actorOf(Props(new PersistenceActor),name = s"account-$account_id-persistence")
}
