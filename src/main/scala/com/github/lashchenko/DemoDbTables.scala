package com.github.lashchenko

import com.google.inject.Inject
import scala.concurrent.{Future, ExecutionContext}

////////////////////////////////////////////////////////////////////////////////
// TABLES AND DAOs
////////////////////////////////////////////////////////////////////////////////

class DemoBudgetDao @Inject() (val dbComponent: DemoDbComponent) {

  import dbComponent._
  import driver.profile.api._
  import slick.profile.SqlProfile.ColumnOption.Nullable

  class DemoBudgetTable(tag: Tag)
    extends Table[DemoBudget](tag, "DEMO_BUDGET") {

    def iId = column[Int]("iId")
    def sId = column[String]("sId", O.Length(10))

    def total = column[Int]("total", Nullable)
    def daily = column[Int]("daily", Nullable)
    def enabled = column[Boolean]("enabled")

    def pk = primaryKey("pk_id", (iId, sId))

    def demoBudgetId = (iId, sId) <> (DemoBudgetId.tupled, DemoBudgetId.unapply)
    def * = (demoBudgetId, total.?, daily.?, enabled) <> (DemoBudget.tupled, DemoBudget.unapply)
  }

  val demoBudget = TableQuery[DemoBudgetTable]

}


////////////////////////////////////////////////////////////////////////////////
// IT IS JUST EXAMPLE FOR DEMO
////////////////////////////////////////////////////////////////////////////////

class DemoBudgetDbService @Inject()(val dao: DemoBudgetDao) {
  import dao._
  import dbComponent._
  import driver.profile.api._

  def setup()(implicit ec: ExecutionContext) = {
    val q = DBIO.seq(
      demoBudget.schema.create,

      demoBudget += DemoBudget(DemoBudgetId(1, "A"), Some(1024), Some(32), enabled = true),
      demoBudget += DemoBudget(DemoBudgetId(2, "B"), Some(2048), Some(64), enabled = false),
      demoBudget += DemoBudget(DemoBudgetId(3, "C"), Some(4096), Some(128), enabled = true),
      demoBudget += DemoBudget(DemoBudgetId(4, "D"), None, Some(16), enabled = false)
    )
    db.run(q)
  }

  def findAll()(implicit ec: ExecutionContext): Future[Seq[DemoBudget]] = {
    db.run(demoBudget.result)
  }

}
