package shop

import cats.Parallel
import cats.effect._
import cats.implicits._
import fs2.Stream
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import modules._
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    new Main[IO].program.as(ExitCode.Success)

}

class Main[F[_]: ConcurrentEffect: ContextShift: Parallel: Timer] { // HasAppConfig
  //import com.olegpy.meow.hierarchy._

  val program: F[Unit] =
    Slf4jLogger.create.flatMap { implicit logger =>
      for {
        //httpConfig <- Stream.eval(ask[F, HttpConfig])
        config <- config.load[F]
        _ <- logger.info(s"Loaded config $config")
        services <- Services.make[F](config.tokenConfig)
        api <- HttpApi.make[F](services, config.adminJwtConfig, config.tokenConfig)
        _ <- BlazeServerBuilder[F]
              .bindHttp(8080, "0.0.0.0")
              .withHttpApp(api.httpApp)
              .serve
              .compile
              .drain
      } yield ()
    }

}
