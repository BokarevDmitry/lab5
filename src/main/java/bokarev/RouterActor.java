package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


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
}