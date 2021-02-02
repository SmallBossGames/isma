package ru.nstu.isma.intg.server.services;

public final class ServiceFacade {

    private final MpiService mpiService;
    private final MessageService messageService;

    public ServiceFacade() {
        this.mpiService = new MpiService();
        this.messageService = new MessageService(this.mpiService);
    }

    public MpiService getMpiService() {
        return mpiService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

}
