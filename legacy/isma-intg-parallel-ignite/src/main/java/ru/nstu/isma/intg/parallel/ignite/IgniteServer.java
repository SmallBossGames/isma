package ru.nstu.isma.intg.parallel.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class IgniteServer {

    public static void main(String[] args) {
        IgniteConfiguration igniteCfg = new IgniteConfiguration();

        //igniteCfg.setPeerClassLoadingEnabled(true);
        //igniteCfg.setMarshaller(new OptimizedMarshaller(false));

        igniteCfg.setLocalHost("127.0.0.1");

//        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
//        ipFinder.setAddresses(Collections.singleton("127.0.0.1:47500..47509"));
//
//        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
//        tcpDiscoverySpi.setIpFinder(ipFinder);
//
//        igniteCfg.setDiscoverySpi(tcpDiscoverySpi);

        //Map<String, String> attrs = new HashMap<>();
        //attrs.put("ROLE", "worker");
        //igniteCfg.setUserAttributes(attrs);

        Ignite ignite = null;

        try {
            ignite = Ignition.start(igniteCfg);
            System.out.println("Hi! I'm server!");
        } catch (Throwable e) {
            if (ignite != null) {
                ignite.close();
            }
        }

    }

}
