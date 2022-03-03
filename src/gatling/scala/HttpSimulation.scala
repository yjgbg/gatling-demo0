import io.gatling.core.Predef._
import io.gatling.http.Predef._

class HttpSimulation  extends  Simulation{
  PersonOuterClass.Person.newBuilder().setName("").build()
  private val person =  PersonOuterClass.Person.newBuilder().setName("alice").build()
  private val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  private val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("request_1")
      .post("/").body(ByteArrayBody(person.toByteArray)))
  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}
