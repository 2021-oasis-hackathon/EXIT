from pyfcm import FCMNotification
 
APIKEY = "AAAAX1qnZgA:APA91bG7E7RRdVWZjPh7EEsDMtMeKub5Jparo9x0UUY-Tzug8Z0lMk201DFan70MjPxfbOi1cQWOUWbbEcFFYEL5CqKB5XwJd8EbVwnPEDRQnwzRcZmTmHC9ShIHTC-rKdNogG5dT8AA"
TOKEN = "e1PxtLrCS7q3UQ73O7Pdyg:APA91bGV_LlNDVBE3tQvYpB2w3WjibpDO15DsaPjENc_3_snJ5PlQDytNriNbNGdbNxHNKYMF8IzgYWFujiI_QP6Pg2qyzTlL8oVp_2OtMqlz3gSsP5vYwXl2-RvYPh_s3FeW6YFPpMl"
 
# 파이어베이스 콘솔에서 얻어 온 서버 키를 넣어 줌
push_service = FCMNotification(APIKEY)
 
def sendMessage(title,message,clickAction):
    # 메시지 (data 타입)
    data_message = {
        "title": title,
        "message": message,
        "clickAction":clickAction
    }
    # 토큰값을 이용해 1명에게 푸시알림을 전송함
    result = push_service.single_device_data_message(registration_id=TOKEN, data_message=data_message)
 
    # 전송 결과 출력
    print(result)
f1=open("locate.txt",'r')
title1=f1.readline()
f1.close 
f2=open("situ.txt",'r')
message1=f2.readline()
f2.close 
sendMessage(title1,message1,"EmergencyMap") 