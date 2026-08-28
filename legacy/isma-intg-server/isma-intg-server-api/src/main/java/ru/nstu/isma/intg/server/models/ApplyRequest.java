package ru.nstu.isma.intg.server.models;

import com.google.common.base.Preconditions;
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet;

import java.io.IOException;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class ApplyRequest implements Request<DaeSystemChangeSet> {

    private BinaryObject<DaeSystemChangeSet> binaryChangeSet;

    /**
     * Конструктор, необходимый десериализатору.
     */
    @SuppressWarnings("unused")
    public ApplyRequest() {
    }

    public ApplyRequest(BinaryObject<DaeSystemChangeSet> binaryChangeSet) {
        Preconditions.checkNotNull(binaryChangeSet, "binaryChangeSet cannot be null");
        this.binaryChangeSet = binaryChangeSet;
    }

    public static ApplyRequest create(DaeSystemChangeSet changeSet) throws IOException {
        Preconditions.checkNotNull(changeSet, "changeSet cannot be null");
        return new ApplyRequest(BinaryObject.serialize(changeSet));
    }

    @Override
    public BinaryObject<DaeSystemChangeSet> getBinaryObject() {
        return binaryChangeSet;
    }

}
