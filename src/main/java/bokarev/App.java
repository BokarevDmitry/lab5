package bokarev;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.routing.RouterActor;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import scala.concurrent.Future;
import java.io.IOException;
import java.util.concurrent.CompletionStage;


public class App extends AllDirectives {
    public static void main(String[] args) throws Exception, InterruptedException, IOException {
        ActorSystem system = ActorSystem.create("routes");
        ActorRef routerActor = system.actorOf(bokarev.RouterActor.props(system), "Router-Actor");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        App instance = new App();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(routerActor).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer
        );

        System.out.println("Server online at http://localhost:8080/");
    }

    private Route createRoute(ActorRef routerActor) {
        return route(
                path("get", () ->
                        route(
                                get(() ->
                                        parameter("packageId", packageId -> {
                                            Future<Object> future = Patterns.ask(routerActor, new TestGetter(Integer.parseInt(packageId)), 5000);
                                            return completeOKWithFuture(future, Jackson.marshaller());
                                        })
                                ))),
                path("post", () ->
                        route(
                                post(() ->
                                        entity(Jackson.unmarshaller(TestPackage.class), test -> {
                                            routerActor.tell(test, ActorRef.noSender());
                                            return complete("Test started!");
                                        })))));
    }
}