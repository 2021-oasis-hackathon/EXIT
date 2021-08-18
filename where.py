import googlemaps
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
cred = credentials.Certificate('exit-saferoute-8b9cb-firebase-adminsdk-ywwcq-2da185bbb8.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://exit-saferoute-8b9cb-default-rtdb.firebaseio.com/'
})
lat=db.reference().child('lat').get() #위도
lng=db.reference().child('lng').get() #경도
gmaps = googlemaps.Client(key='AIzaSyC3a8Nl31LCuwBbtXzzYazsz36MhTywyE4')
reverse_geocode_result = gmaps.reverse_geocode(
    (lat,lng),language='ko'
    ) 
B=list(reverse_geocode_result[0].values())
print(B[1]) #확인용 삭제
place=B[1].split() # B[1] - 풀네임 - 삭제 
if "광역시" in place[1]:
    locate=place[1]
else:
    locate=place[2]
f1=open("locate.txt",'w')
f1.write(locate)
f1.close()
print("현재 위치는 ",locate,"입니다") # 확인용 - 삭제