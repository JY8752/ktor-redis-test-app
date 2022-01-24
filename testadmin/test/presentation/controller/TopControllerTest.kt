package testadmin.presentation.controller

import io.kotest.core.spec.style.StringSpec
import io.ktor.http.*
import io.ktor.server.testing.*
import testadmin.module
import kotlin.test.assertEquals

class TopControllerTest : StringSpec({
   "routing test" {
       withTestApplication({ module(testing = true) }) {
           handleRequest(HttpMethod.Get, "/").apply {
               assertEquals(HttpStatusCode.OK, response.status())
           }
       }
   }
})