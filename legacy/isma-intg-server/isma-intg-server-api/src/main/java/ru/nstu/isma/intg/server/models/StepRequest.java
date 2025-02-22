package ru.nstu.isma.intg.server.models;

import com.google.common.base.Preconditions;
import ru.nstu.isma.intg.api.methods.IntgPoint;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class StepRequest implements Request<IntgPoint> {

    private BinaryObject<IntgPoint> binaryIntgPoint;

    /** Конструктор, необходимый десериализатору. */
    @SuppressWarnings("unused")
    private StepRequest() {
    }

    public StepRequest(BinaryObject<IntgPoint> binaryIntgPoint) {
        Preconditions.checkNotNull(binaryIntgPoint, "binaryIntgPoint cannot be null");
        this.binaryIntgPoint = binaryIntgPoint;
    }

    public static StepRequest create(IntgPoint intgPoint) throws IOException {
        Preconditions.checkNotNull(intgPoint, "intgPoint cannot be null");
        return new StepRequest(BinaryObject.serialize(intgPoint));
    }

    @Override
    public BinaryObject<IntgPoint> getBinaryObject() {
        return binaryIntgPoint;
    }

}
