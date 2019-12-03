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
import akka.http.javadsl.unmarshalling.StringUnmarshallers;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import scala.concurrent.Future;
import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;

import static akka.http.javadsl.server.Directives.complete;
import static akka.http.javadsl.server.Directives.parameter;


import org.asynchttpclient.*;
import static org.asynchttpclient.Dsl.*;


import io.netty.handler.codec.http.HttpContentDecoder;



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

        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        AsyncHttpClient client = Dsl.asyncHttpClient();
    }


    //AsyncHttpClient c = asyncHttpClient(config().setProxyServer(proxyServer("127.0.0.1", 38080)));

    private Route createRoute(ActorRef routerActor) {
        final Route route = parameter("url", url ->
                parameter(StringUnmarshallers.INTEGER,"count", count ->
                        complete("url = "+ url + "count = " + count);
                                                //Future<Object> future = Patterns.ask(routerActor, new TestGetter(Integer.parseInt(count)), 5000);
                                                //return completeOKWithFuture(future, Jackson.marshaller());

                )))

                                        /*{
                                            AsyncHttpClient asyncHttpClient = asyncHttpClient();
                                            Future<Response> whenResponse = asyncHttpClient.prepareGet("http://www.rambler.com").execute();
                                            try {
                                                Response response = whenResponse.get();
                                                return complete(response.getResponseBody());
                                            } catch (InterruptedException | ExecutionException e) {
                                                e.printStackTrace();
                                            }
                                            return complete("fault");

                                            //return complete("Test started!");
                                        }*/
                                        /*
                                        parameter("packageId", packageId -> {
                                            Future<Object> future = Patterns.ask(routerActor, new TestGetter(Integer.parseInt(packageId)), 5000);
                                            return completeOKWithFuture(future, Jackson.marshaller());
                                        })
                                        */
                                )));
    }
}