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
                .match(TestPackage.class, test -> {
                    log.info("REQUEST: route new test package");
                    int count = test.testsLists.size();
                    for (int i=0; i<count; i++) {
                        ActorRef testPasserActor = getContext().actorOf(TestPasserActor.props(), "TestPasser-Actor-"+i);
                        testPasserActor.tell(new TestForImpl(test, i), storageActor);
                    }
                })
                .match(UrlWithCount.class, msg -> storageActor.tell(msg, getSender()))
                .build();
    }
}