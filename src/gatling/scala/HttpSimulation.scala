import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef._
import io.gatling.core.body.ByteArrayBody
import io.gatling.http.Predef._
import io.gatling.http.response.ByteArrayResponseBody

import java.nio.charset.StandardCharsets
import scala.concurrent.duration.DurationInt
//https://gatling.io/docs/gatling/reference/current/http/request/#response-transformers
class HttpSimulation extends Simulation {
  private val objectMapper = new ObjectMapper()
  private val personByteArray = PersonOuterClass.Person.newBuilder().setName("alice").build().toByteArray
  private val sce = scenario("during10")
    .exec(addCookie(Cookie("name", "value")
      .withDomain("domain")
      .withPath("path")
      .withSecure(true)))
    .during(3.minutes, "during10") {
      exec()
        .exec(http("theName")
          .post("http://the.server.domain/addPerson")
          .header("ContentType","application/protobuf")
          .body(ByteArrayBody(personByteArray))
          .transformResponse((resp,_) => {
            resp.copy(body = new ByteArrayResponseBody(objectMapper.writeValueAsBytes(PersonOuterClass.Person.parseFrom(resp.body.bytes)),StandardCharsets.UTF_8))
          })
          .check(status.saveAs("status"), bodyBytes.saveAs("body")))
        .pause(2.seconds, 3.seconds) // 停留了2到3秒
    } // 创建场景,在3分钟内不断地向目标发送请求
  private val user = rampUsers(10).during(10.seconds) // 创建虚拟用户，2秒钟内逐渐增加到10个用户
  setUp(sce.inject(user)).assertions(details("").responseTime.percentile(99).lt(10)) // 模拟这些虚拟用户执行这个场景
}
