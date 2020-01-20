#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
sys.path.append("/")
from message.api import MessageService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import smtplib
from email.mime.text import MIMEText
from email.header import Header

#发送email的邮箱和授权码
sender = "15604288825@163.com"
authCode = "xj199804025511hl"
class MessageServiceHandler:
    def sendMobileMessage(self, mobile, message):
        print("sendMobileMessage,mobile:"+mobile+",message:"+message)
        return True

    def sendEmailMessage(self, email, message):
        print("sendEmailMessage,Email:" + email + ",message:" + message)
        #create text
        messageObj = MIMEText(message,"plain","utf-8")
        messageObj['From'] = sender
        messageObj['To'] = email
        messageObj['Subject'] = Header("徐俊的邮件","utf-8")

        try:
            smtpObj = smtplib.SMTP('smtp.163.com')
            smtpObj.login(sender,authCode)
            #send email
            smtpObj.sendmail(sender,email,messageObj.as_string())
        except smtplib.SMTPException as ex:
            print("send email filed ...")
            print(ex)
            return False

        print("send email success ...")
        return True

if __name__ == '__main__':
    handler = MessageServiceHandler()
    processor = MessageService.Processor(handler)
    transport = TSocket.TServerSocket(None, "9090")
    tfactory = TTransport.TFramedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print ("python thrift server start")
    server.serve()
    print ("python thrift server exit")
