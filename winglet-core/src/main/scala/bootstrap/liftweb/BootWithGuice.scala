package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import com.primalrecode.winglet.config.CoreModule
import com.google.inject.{Module, Guice}
import com.primalrecode.winglet.{InjectorHolder, BootPointExecutor}

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
trait BootWithGuice {
  val modules: List[Module]  

  def boot {
    try {
      val withCore = new CoreModule :: modules
      val injector = Guice.createInjector(withCore:_*)
      InjectorHolder.injector = injector
      injector.getInstance(classOf[BootPointExecutor]).boot();
    }
    catch {
      case e => Log.error(e)
    }
  }

}

