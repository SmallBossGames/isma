package ru.nstu.isma.intg.server.models;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class CalculateRhsResponse implements Response<double[][]> {

    private BinaryObject<double[][]> binaryRhs;
    private String errorMessage;

    /** Конструктор, необходимый десериализатору. */
    @SuppressWarnings("unused")
    private CalculateRhsResponse() {
    }

    public CalculateRhsResponse(BinaryObject<double[][]> binaryRhs) {
        this.binaryRhs = binaryRhs;
    }

    public static CalculateRhsResponse create(double[][] rhs) throws IOException {
        return new CalculateRhsResponse(BinaryObject.serialize(rhs));
    }

    public static CalculateRhsResponse createError(String errorMessage) {
        CalculateRhsResponse response = new CalculateRhsResponse(null);
        response.errorMessage = errorMessage;
        return response;
    }

    @Override
    public BinaryObject<double[][]> getBinaryObject() {
        return binaryRhs;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
