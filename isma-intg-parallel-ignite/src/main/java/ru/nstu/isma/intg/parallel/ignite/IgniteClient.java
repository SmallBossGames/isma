package ru.nstu.isma.intg.parallel.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;

public class IgniteClient {

    public static Ignite connect() {
        IgniteConfiguration igniteCfg = new IgniteConfiguration();

        igniteCfg.setClientMode(true);
        //igniteCfg.setPeerClassLoadingEnabled(true);
        //igniteCfg.setMarshaller(new OptimizedMarshaller(false));

        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singleton("127.0.0.1:47500..47509"));

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(ipFinder);

        igniteCfg.setDiscoverySpi(tcpDiscoverySpi);

        Ignite ignite = Ignition.start(igniteCfg);

        System.out.println("Hi! I'm client!");

        return ignite;
    }

    public static void disconnect(Ignite ignite) {
        ignite.close();

        System.out.println("Bye-bye!");
    }

}
