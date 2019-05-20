package com.arnaudbos.quasar_vavr;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.ActorRegistry;
import co.paralleluniverse.actors.behaviors.RequestReplyHelper;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import io.vavr.control.Try;

public class Main {
    public static void main(String[] args) {
        Channel<String> producerChan = Channels.newChannel(1, Channels.OverflowPolicy.BLOCK);
        new Fiber<Void>() {
            @Override
            protected Void run() throws SuspendExecution, InterruptedException {
                int i = 0;
                //noinspection InfiniteLoopStatement
                for(;;) {
                    Strand.sleep(500);
                    producerChan.send(String.valueOf(i++));
                }
            }
        }.start();

        for (;;) {
            Try.of(producerChan::receive)
                .peek(Main::send);
        }
    }

    @Suspendable
    private static void send(String s) {
        try {
            StringMessage msg = new StringMessage(s);
            Strand.sleep(1000);
            ActorRef<StringMessage> coordinator = ActorRegistry.getOrRegisterActor(
                "Coordinator",
                Coordinator::new
            );
//            coordinator.send(msg);
            System.out.println(RequestReplyHelper.call(coordinator, msg));
        } catch (SuspendExecution suspendExecution) {
            //suspendExecution.printStackTrace();
        } catch (InterruptedException e) {
            Strand.currentStrand().interrupt();
        }
    }
}
