package ru.nstu.isma.intg.server;

import ru.nstu.isma.intg.server.models.ApplyRequest;
import ru.nstu.isma.intg.server.models.CalculateRhsRequest;
import ru.nstu.isma.intg.server.models.CalculateRhsResponse;
import ru.nstu.isma.intg.server.models.LoadDaeSystemRequest;
import ru.nstu.isma.intg.server.models.LoadIntgMethodRequest;
import ru.nstu.isma.intg.server.models.StepRequest;
import ru.nstu.isma.intg.server.models.StepResponse;

public interface ComputeEngine {

    void disconnect(int clientId);

    void loadIntgMethod(int clientId, LoadIntgMethodRequest request);

    void loadDaeSystem(int clientId, LoadDaeSystemRequest request);

    StepResponse step(int clientId, StepRequest request);

    CalculateRhsResponse calculateRhs(int clientID, CalculateRhsRequest request);

    void apply(int clientId, ApplyRequest request);
}
