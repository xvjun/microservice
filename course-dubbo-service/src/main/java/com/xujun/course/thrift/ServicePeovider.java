package com.xujun.course.thrift;

import com.xujun.thrift.user.UserService;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ServicePeovider {

    @Value("${thrift.userServer.ip}")
    private String serverIp;

    @Value("${thrift.userServer.port}")
    private int serverPort;

    private enum ServiceType {
        USER
    }

    private UserService.Client userServiceClient= null;

    public UserService.Client getUserService(){
        if(userServiceClient == null){
            userServiceClient = getService(serverIp,serverPort, ServiceType.USER);
        }
        return userServiceClient;
    }


    public <T> T getService(String ip, int port, ServiceType serviceType){
        TSocket socket = new TSocket(ip,port,10000);
        TTransport tTransport = new TFramedTransport(socket);
        try {
            tTransport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TProtocol protocol = new TBinaryProtocol(tTransport);
        TServiceClient result = null;
        switch (serviceType){
            case USER:
                result = new UserService.Client(protocol);
                break;
        }
        UserService.Client client = new UserService.Client(protocol);
        return (T) result;
    }


}
