package bokarev;

import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.model.*;
//import akka.pattern.Patterns;
import akka.pattern.Patterns;
import akka.pattern.Patterns$;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;


public class RouterActor {
    ActorMaterializer actorMaterializer;
    AsyncHttpClient asyncHttpClient;
    ActorRef storageActor;

    public RouterActor(ActorSystem system, ActorMaterializer materializer, AsyncHttpClient asyncHttpClient) {
        this.actorMaterializer = materializer;
        this.asyncHttpClient = asyncHttpClient;
        this.storageActor = system.actorOf (StorageActor.props(), "Storage-Actor");
    }

    Flow<HttpRequest, HttpResponse, NotUsed> createRoute() {
        return Flow.of(HttpRequest.class)
                .map(this::parseReq)
                .mapAsync(5, this::checkTestInStorage)
                .map(this::makeResponse);

    }

    public UrlWithCount parseReq (HttpRequest req) {
        Query query = req.getUri().query();
        Optional<String> testUrl = query.get("testUrl");
        Optional<String> count = query.get("count");
        return new UrlWithCount(testUrl.get(), Integer.parseInt(count.get()));
    }

    public CompletionStage<TestWithResult> checkTestInStorage (UrlWithCount test) {
        return Patterns.ask(storageActor, test, Duration.ofMillis(5000))
                .thenApply(t -> (TestWithResult)t)
                .thenCompose(res -> {
                    Optional<TestWithResult> r = res.getOptResult();
                    if (r.isPresent()) {
                        return CompletableFuture.completedFuture(r.get());
                    } else {
                        System.out.println("ASFDFDFDFDFD");
                        return runNewTest(test);
                    }
                });
    }

    public CompletionStage<TestWithResult> runNewTest(UrlWithCount test) {
        Sink<UrlWithCount, CompletionStage<Long>> testSink = Flow.of(UrlWithCount.class)
                .mapConcat(r -> Collections.nCopies(r.getCount(), r.getUrl()))
                .mapAsync(5, r-> {
                    Instant startTime = Instant.now();
                    return asyncHttpClient.prepareGet(r).execute()
                            .toCompletableFuture()
                            .thenCompose(p -> CompletableFuture.completedFuture(
                                    Duration.between(startTime, Instant.now()).getSeconds()
                            ));
                })
                .toMat(Sink.fold(0L,Long::sum), Keep.right());
        return Source.from(Collections.singleton(test))
                .toMat(testSink, Keep.right())
                .run(actorMaterializer)
                .thenApply(m -> new TestWithResult(test, m/test.getCount()));

    }

    public HttpResponse makeResponse (TestWithResult testResult) throws JsonProcessingException {
        storageActor.tell(testResult, ActorRef.noSender());
        return HttpResponse.create()
                .withStatus(200)
                .withEntity(ContentTypes.APPLICATION_JSON, ByteString.fromString(
                        new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(testResult)
                ));
    }


}