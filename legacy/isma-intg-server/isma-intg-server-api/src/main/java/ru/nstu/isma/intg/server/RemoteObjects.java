package ru.nstu.isma.intg.server;

import java.util.Arrays;
import java.util.List;

import ru.nstu.isma.intg.server.models.ApplyRequest;
import ru.nstu.isma.intg.server.models.BinaryObject;
import ru.nstu.isma.intg.server.models.CalculateRhsRequest;
import ru.nstu.isma.intg.server.models.CalculateRhsResponse;
import ru.nstu.isma.intg.server.models.LoadDaeSystemRequest;
import ru.nstu.isma.intg.server.models.LoadIntgMethodRequest;
import ru.nstu.isma.intg.server.models.StepRequest;
import ru.nstu.isma.intg.server.models.StepResponse;

public final class RemoteObjects {
	private RemoteObjects() {
	}

	public static final int COMPUTE_ENGINE_SERVER_ID = 1;
	public static final int CLIENT_CLASS_PROVIDER_ID = 2;

	public static List<Class> getTransferableClasses() {
		return Arrays.asList(
				byte[].class,
				double[].class,
				ComputeEngine.class,
                ClassDataProvider.class,
                BinaryObject.class,
                LoadIntgMethodRequest.class,
                StepRequest.class,
                StepResponse.class,
                LoadDaeSystemRequest.class,
                CalculateRhsRequest.class,
                CalculateRhsResponse.class,
                ApplyRequest.class
		);
	}
}
