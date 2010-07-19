package com.primalrecode.winglet.gaeauth.config

import com.primalrecode.winglet.BootPoint
import net.liftweb.http.LiftRules
import com.primalrecode.winglet.auth.isAdminUser
import net.liftweb.http.provider.servlet.HTTPRequestServlet


class GaeAuthBootPoint extends BootPoint {
  def boot() = {
    LiftRules.early.prepend(_ match {
      case req:HTTPRequestServlet => isAdminUser(req.req.isUserInRole("admin"))
    })
  }
}