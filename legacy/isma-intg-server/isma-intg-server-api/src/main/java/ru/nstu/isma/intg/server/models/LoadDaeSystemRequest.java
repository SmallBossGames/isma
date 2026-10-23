package ru.nstu.isma.intg.server.models;

import com.google.common.base.Preconditions;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class LoadDaeSystemRequest implements Request<DaeSystem> {

    private BinaryObject<DaeSystem> binaryOdeSystem;

    /** Конструктор, необходимый десериализатору. */
    @SuppressWarnings("unused")
    public LoadDaeSystemRequest() {
    }

    public LoadDaeSystemRequest(BinaryObject<DaeSystem> binaryOdeSystem) {
        Preconditions.checkNotNull(binaryOdeSystem, "binaryOdeSystem cannot be null");
        this.binaryOdeSystem = binaryOdeSystem;
    }

    public static LoadDaeSystemRequest create(DaeSystem odeSystem) throws IOException {
        Preconditions.checkNotNull(odeSystem, "binaryOdeSystem cannot be null");
        return new LoadDaeSystemRequest(BinaryObject.serialize(odeSystem));
    }

    @Override
    public BinaryObject<DaeSystem> getBinaryObject() {
        return binaryOdeSystem;
    }

}
