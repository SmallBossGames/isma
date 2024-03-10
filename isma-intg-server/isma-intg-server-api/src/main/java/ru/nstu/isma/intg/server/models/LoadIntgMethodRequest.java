package ru.nstu.isma.intg.server.models;

import com.google.common.base.Preconditions;
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class LoadIntgMethodRequest implements Request<IntegrationMethodRungeKutta> {

    private BinaryObject<IntegrationMethodRungeKutta> binaryIntgMethod;

    /** Конструктор, необходимый десериализатору. */
    @SuppressWarnings("unused")
    public LoadIntgMethodRequest() {
    }

    public LoadIntgMethodRequest(BinaryObject<IntegrationMethodRungeKutta> binaryIntgMethod) {
        Preconditions.checkNotNull(binaryIntgMethod, "binaryIntgMethod cannot be null");
        this.binaryIntgMethod = binaryIntgMethod;
    }

    public static LoadIntgMethodRequest create(IntegrationMethodRungeKutta intgMethod) throws IOException {
        Preconditions.checkNotNull(intgMethod, "intgMethod cannot be null");
        return new LoadIntgMethodRequest(BinaryObject.serialize(intgMethod));
    }

    @Override
    public BinaryObject<IntegrationMethodRungeKutta> getBinaryObject() {
        return binaryIntgMethod;
    }

}
