import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class LoadSimulation extends Simulation {

  //定义请求
  val baseUrl = "http://localhost:7001"
  val testPath = "/hello"
  val users = 2000

  val httpConf = http.baseUrl(baseUrl)

  //定义模拟请求，重复10次
  val hiReqest = repeat(10) {
    //自定义测试名称
    exec(http("hi_req")
      //执行get请求
      .get(testPath))
      //模拟用户思考时间，随机1~2秒
      .pause(1 second, 2 seconds)
  }

  //定义模拟场景
  val scn = scenario("hiScn")
    //该场景执行上面定义的请求
    .exec(hiReqest)

  //配置并发用户数量，在10s内均匀提升到users指定数量
  setUp(scn.inject(rampUsers(users).during(10 seconds)).protocols(httpConf))
}
