package com.example.demostream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface Distination {
    String INPUT = "inputD";
    String OUTPUT = "outputD";
    @Input(Distination.INPUT)
    SubscribableChannel input();
    @Output(Distination.OUTPUT)
    SubscribableChannel output();

}
