package bokarev;

import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.asynchttpclient.AsyncHttpClient;

import java.util.Optional;
import java.util.concurrent.CompletionStage;


public class RouterActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef storageActor;

    public RouterActor(ActorSystem system, ActorMaterializer materializer, AsyncHttpClient asyncHttpClient) {
        this.s

        storageActor = system.actorOf (StorageActor.props(), "Storage-Actor");
    }

    public static Props props(ActorSystem system) {
        return Props.create(RouterActor.class, system);
    }

    @Override
    public void preStart() {
        log.info("Starting RouterActor {}", this.getSelf());
    }

    @Override
    public void postStop() {
        log.info("Stopping RouterActor {}", this.getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(UrlWithCount.class, msg -> {
                    //Flow<UrlWithCount, HttpResponse, NotUsed> flow = Flow.of(UrlWithCount.class)
                            //.map(req -> new Pair<String, Integer>(req.getUrl(), req.getCount()))
                            //.mapAsync()
                    //storageActor.tell(msg, getSelf());
                    //Patterns.ask(storageActor)
                    //Sink<UrlWithCount, CompletionStage<Long>> testSink = Sink

                })
                .match(TestWithResult.class, msg -> {
                    ActorRef testPasserActor = getContext().actorOf(TestPasserActor.props(), "TestPasser-Actor");
                    testPasserActor.tell(msg, getSender());
                })

                .build();
    }




    private Flow<HttpRequest, HttpResponse, NotUsed> createRoute() {
        return Flow.of(HttpRequest.class)
                .map(this::parseReq);

    }

    public UrlWithCount parseReq (HttpRequest req) {
        Query query = req.getUri().query();
        Optional<String> testUrl = query.get("testUrl");
        Optional<String> count = query.get("count");
        return new UrlWithCount(testUrl.get(), Integer.parseInt(count.get()));
    }

    public CompletionStage<TestWithResult> checkTestInStorage (UrlWithCount test) {
        return Patter
    }


}