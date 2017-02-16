package com.github.lashchenko


import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import net.codingwell.scalaguice.ScalaModule


////////////////////////////////////////////////////////////////////////////////
// DEMO APP
////////////////////////////////////////////////////////////////////////////////

class H2DbComponent extends DemoDbComponent {
  import slick.driver.H2Driver.api._

  val driver = slick.driver.H2Driver
  val db = Database.forConfig("h2mem1")
}

object DemoInjector {

  class DemoModule extends ScalaModule {
    override def configure() = {
      bind[DemoDbComponent].to[H2DbComponent].asEagerSingleton()
      bind[DemoBudgetDao].asEagerSingleton()
      bind[DemoBudgetDbService].asEagerSingleton()
    }
  }

  def injector: ScalaInjector = {
    Guice.createInjector(new DemoModule)
  }
}

object DemoApp extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val injector = DemoInjector.injector
  val demoBudgetDbService: DemoBudgetDbService = injector.instance[DemoBudgetDbService]

  demoBudgetDbService.setup().onComplete { case x => println(x) }
  Thread.sleep(1000L)

  demoBudgetDbService.findAll().onComplete { case x => println(x) }
  Thread.sleep(1000L)

}
