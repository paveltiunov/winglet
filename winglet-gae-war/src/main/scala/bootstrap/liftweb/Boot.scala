package bootstrap.liftweb

import com.primalrecode.winglet.gaeauth.config.GaeAuthModule

class Boot extends BootWithGuice {
  val modules = new GaeAuthModule :: Nil
}