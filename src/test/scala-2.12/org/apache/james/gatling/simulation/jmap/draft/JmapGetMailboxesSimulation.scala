package org.apache.james.gatling.simulation.jmap.draft

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import org.apache.james.gatling.control.{UserCreator, UserFeeder}
import org.apache.james.gatling.jmap.draft.scenari.JmapGetMailboxesScenario
import org.apache.james.gatling.simulation.{Configuration, HttpSettings}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.{Await, Future}

class JmapGetMailboxesSimulation extends Simulation {

  private val users = Await.result(
    awaitable = Future.sequence(
      new UserCreator(Configuration.BaseJamesWebAdministrationUrl, Configuration.BaseJmapUrl).createUsersWithInboxAndOutbox(Configuration.UserCount)),
    atMost = Inf)

  private val scenario = new JmapGetMailboxesScenario()

  setUp(scenario.generate(Configuration.ScenarioDuration, UserFeeder.toFeeder(users))
    .inject(atOnceUsers(Configuration.UserCount)))
    .protocols(HttpSettings.httpProtocol)
}