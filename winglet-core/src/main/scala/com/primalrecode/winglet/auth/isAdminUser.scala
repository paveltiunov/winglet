package com.primalrecode.winglet.auth

import net.liftweb.http.{TransientRequestVar, RequestVar}

object isAdminUser extends TransientRequestVar[Boolean](false) 