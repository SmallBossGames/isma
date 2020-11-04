package ru.nstu.isma.intg.server.models;

import com.google.common.base.Preconditions;
import ru.nstu.isma.intg.api.methods.IntgMethod;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class LoadIntgMethodRequest implements Request<IntgMethod> {

    private BinaryObject<IntgMethod> binaryIntgMethod;

    /** Конструктор, необходимый десериализатору. */
    @SuppressWarnings("unused")
    public LoadIntgMethodRequest() {
    }

    public LoadIntgMethodRequest(BinaryObject<IntgMethod> binaryIntgMethod) {
        Preconditions.checkNotNull(binaryIntgMethod, "binaryIntgMethod cannot be null");
        this.binaryIntgMethod = binaryIntgMethod;
    }

    public static LoadIntgMethodRequest create(IntgMethod intgMethod) throws IOException {
        Preconditions.checkNotNull(intgMethod, "intgMethod cannot be null");
        return new LoadIntgMethodRequest(BinaryObject.serialize(intgMethod));
    }

    @Override
    public BinaryObject<IntgMethod> getBinaryObject() {
        return binaryIntgMethod;
    }

}
