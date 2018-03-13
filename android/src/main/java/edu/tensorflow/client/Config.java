package edu.tensorflow.client;

/**
 * Created by Zoltan Szabo on 3/12/18.
 */

public interface Config {
    String host = "http://10.42.0.1:8080";
    int INPUT_SIZE = 960;   // The input size. A square image of inputSize x inputSize is assumed.
    String LOGGING_TAG = "SET";
}
