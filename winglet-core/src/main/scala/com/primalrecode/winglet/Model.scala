package com.primalrecode.winglet

import net.liftweb.jpa.RequestVarEM
import org.scala_libs.jpa.LocalEMF
import com.google.inject.Singleton

@Singleton
class Model extends LocalEMF("transactions-optional", true) with RequestVarEM