package com.primalrecode.winglet

import net.liftweb.http.LiftFilter
import _root_.javax.servlet._
import http.HttpServletRequest

class LiftGaeFilter extends LiftFilter {
  override def doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) = req match {
    case r: HttpServletRequest if !r.getRequestURI.startsWith("/_ah") => super.doFilter(req, res, chain)
    case _ => chain.doFilter(req, res)
  }
}