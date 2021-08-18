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
locate=place[2]
print(place[1])#확인용
f1=open("locate.txt",'w')
f1.write(locate)
f1.close
print(locate) # 확인용 - 삭제
#34.81481207887022, 126.424530487225 - 목포시
#34.801402985089254, 126.6221388943585 - 영암군
#35.16322415631088, 126.7543091789832 - 광주광역시