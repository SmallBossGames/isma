package ru.nstu.isma.intg.server.models;

import com.google.common.base.Preconditions;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class CalculateRhsRequest implements Request<double[]> {

    private BinaryObject<double[]> binaryYForDe;

    /**
     * Конструктор, необходимый десериализатору.
     */
    @SuppressWarnings("unused")
    public CalculateRhsRequest() {
    }

    public CalculateRhsRequest(BinaryObject<double[]> binaryYForDe) {
        Preconditions.checkNotNull(binaryYForDe, "binaryYForDe cannot be null");
        this.binaryYForDe = binaryYForDe;
    }

    public static CalculateRhsRequest create(double[] yForDe) throws IOException {
        Preconditions.checkNotNull(yForDe, "yForDe cannot be null");
        return new CalculateRhsRequest(BinaryObject.serialize(yForDe));
    }

    @Override
    public BinaryObject<double[]> getBinaryObject() {
        return binaryYForDe;
    }

}
