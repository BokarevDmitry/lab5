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
import akka.pattern.Patterns;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import scala.Tuple2;


public class RouterActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef storageActor;

    public RouterActor(ActorSystem system) {
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
                    Flow<UrlWithCount, HttpResponse, NotUsed> flow = Flow.of(UrlWithCount.class)
                            .map(req -> new Tuple2<String, Integer>(req.getUrl(), req.getCount()))
                            .mapAsync();
                    //storageActor.tell(msg, getSelf());
                    //Patterns.ask(storageActor)
                })
                .match(NoSuchTest.class, msg -> {
                    ActorRef testPasserActor = getContext().actorOf(TestPasserActor.props(), "TestPasser-Actor");
                    testPasserActor.tell(msg, getSender());
                })

                .build();
    }
}