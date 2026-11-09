package ru.nstu.isma.intg.server.models;

import ru.nstu.isma.intg.api.methods.IntgPoint;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class StepResponse implements Response<IntgPoint> {

    private BinaryObject<IntgPoint> binaryIntgPoint;
    private String errorMessage;

    /** Конструктор, необходимый десериализатору. */
    @SuppressWarnings("unused")
    private StepResponse() {
    }

    public StepResponse(BinaryObject<IntgPoint> binaryIntgPoint) {
        this.binaryIntgPoint = binaryIntgPoint;
    }

    public static StepResponse create(IntgPoint intgPoint) throws IOException {
        return new StepResponse(BinaryObject.serialize(intgPoint));
    }

    public static StepResponse createError(String errorMessage) {
        StepResponse response = new StepResponse(null);
        response.errorMessage = errorMessage;
        return response;
    }

    @Override
    public BinaryObject<IntgPoint> getBinaryObject() {
        return binaryIntgPoint;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
