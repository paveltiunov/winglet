package com.primalrecode.winglet

import net.liftweb.jpa.RequestVarEM
import org.scala_libs.jpa.LocalEMF

object Model extends LocalEMF("transactions-optional", true) with RequestVarEM