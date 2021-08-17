import schedule
import googlemaps
import time
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from bs4 import BeautifulSoup, BeautifulStoneSoup
from selenium.webdriver.support.ui import Select
from pyfcm import FCMNotification
import time

def where():
    gmaps = googlemaps.Client(key='AIzaSyC3a8Nl31LCuwBbtXzzYazsz36MhTywyE4')
    reverse_geocode_result = gmaps.reverse_geocode(
        (34.801402985089254, 126.6221388943585),language='ko'
        ) # 수정필요
    B=list(reverse_geocode_result[0].values())
    print(B[1]) #확인용 삭제
    place=B[1].split() # B[1] - 풀네임
    if '광역시' in place[1]:
        locate=place[1]
    else:
        locate=place[2]
    f1=open("locate.txt",'w')
    f1.write(locate)
    f1.close()
    print(locate) # 확인용 - 삭제
    #34.81481207887022, 126.424530487225 - 목포시
    #34.801402985089254, 126.6221388943585 - 영암군
    #35.16322415631088, 126.7543091789832 - 광주광역시

def apppre():
    options = webdriver.ChromeOptions()
    options.add_argument("--incognito")
    options.add_argument("--window-size=1920x1080")
    options.add_argument('--disable-extensions')
    options.add_argument('--headless')
    options.add_argument('--disable-gpu')
    options.add_argument('--no-sandbox')
    driver = webdriver.Chrome(
        executable_path = '/home/pjy/hackerton/chromedriver',
        chrome_options=options    
        ) 
    driver.implicitly_wait(15)

    url = "https://www.safekorea.go.kr/idsiSFK/neo/sfk/cs/sfc/dis/disasterMsgList.jsp?menuSeq=679"
    driver.get(url)
    driver.implicitly_wait(3) 

    f1=open("locate.txt",'r')
    locate=f1.readline()
    f1.close()
    search = driver.find_element_by_class_name('search04_input')
    search.send_keys(locate)

    click1=driver.find_element_by_class_name('search_btn')
    click1.click()
    time.sleep(2)

    table = driver.find_element_by_class_name('boardList_table')
    tbody = table.find_element_by_tag_name("tbody")
    rows = tbody.find_elements_by_tag_name("tr")[0]
    body= rows.find_elements_by_tag_name("td")
    A=[] #가장 최근에 올라온 행 적는것.
    for index, value in enumerate(body):
        h=value.text
        A.append(h)
    print(A[0]) #확인용 - 삭제
    number=A[0]
    f2=open("number.txt",'w')
    f2.write(number+'\n')
    f2.write(locate)
    f2.close()
    driver.implicitly_wait(5)

    driver.quit() 

def app():
    options = webdriver.ChromeOptions()
    options.add_argument("--incognito")
    options.add_argument("--window-size=1920x1080")
    options.add_argument('--disable-extensions')
    options.add_argument('--headless')
    options.add_argument('--disable-gpu')
    options.add_argument('--no-sandbox')
    driver = webdriver.Chrome(
        executable_path = '/home/pjy/hackerton/chromedriver',
        chrome_options=options    
        ) 
    driver.implicitly_wait(15) 

    url = "https://www.safekorea.go.kr/idsiSFK/neo/sfk/cs/sfc/dis/disasterMsgList.jsp?menuSeq=679"
    driver.get(url)
    driver.implicitly_wait(3) 

    f1=open("number.txt",'r')
    number=f1.readline()
    locate=f1.readline()
    f1.close()

    search = driver.find_element_by_class_name('search04_input')
    search.send_keys(locate)

    click1=driver.find_element_by_class_name('search_btn')
    click1.click()
    time.sleep(2)

    table = driver.find_element_by_class_name('boardList_table')
    tbody = table.find_element_by_tag_name("tbody")
    rows = tbody.find_elements_by_tag_name("tr")[0]
    body= rows.find_elements_by_tag_name("td")
    A=[] #가장 최근에 올라온 행 적는것.
    for index, value in enumerate(body):
        h=value.text
        A.append(h)
    print(A[0]) #확인용 - 삭제
    A[0]=int(A[0])  # 가장 최근에 올라온 게시글의 번호
    postcontents=[] # 게시글의 내용이 들어감 삭제
    print("텍스트에 저장된 숫자 number = ",number) #확인용 - 삭제
    print("가장 최근에 올라온 글의 번호 : ",A[0]) #확인용 - 삭제 
    if int(number)==A[0]:
        print("최근에 올라온 글이 없습니다.") #확인용-삭제
    else:
        print(A[0]-int(number),"개의 글이 올라왔습니다.") #확인용 -삭제
        if A[0]-int(number)<10:
            for i in range(A[0]-int(number)):
                postnumber='bbs_tr_'+str(i)+'_bbs_title'
                click1 = driver.find_element_by_id(postnumber) #게시글 조회
                click1.click()
                table2 = driver.find_element_by_id('cn')
                postcontents.append(table2.text) #확인용 삭제
                print(postcontents[i]) #확인용 - 삭제
                if '확진' in table2.text:
                    f3=open("situ.txt",'w')
                    f3.write('확진') #수정필요
                    f3.close 
                else:
                    print("확진없음") #확인용-삭제
                click2=driver.find_element_by_class_name('list_btn')
                click2.click()
        else:
            print("최근 게시물 10개를 조회합니다." ) #확인용삭제
            for i in range(10):
                postnumber='bbs_tr_'+str(i)+'_bbs_title'
                click1 = driver.find_element_by_id(postnumber) #게시글 조회
                click1.click()
                table2 = driver.find_element_by_id('cn')
                postcontents.append(table2.text) #확인용 삭제
                print(postcontents[i]) #확인용 - 삭제
                if '확진' in table2.text:
                    f3=open("situ.txt",'w')
                    f3.write('확진') #수정필요
                    f3.close 
                else:
                    print("확진없음") #확인용-삭제
                click2=driver.find_element_by_class_name('list_btn')
                click2.click()
    f2=open("number.txt",'w')
    f2.write(str(A[0]))
    f2.close()
    # 가장 최근에 올라온 게시글의 번호 저장 및 게시글 저장

    driver.implicitly_wait(5) 

    driver.quit() 

def server():
 
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
    f1.close()
    f2=open("situ.txt",'r')
    message1=f2.readline()
    f2.close()
    sendMessage(title1,message1,"EmergencyMap") 
    
where()
print("1번째실행")
apppre()
print("2번째실행")
app()
print("3번째")
server()
print("4번째")
print("?%")

schedule.every(30).minutes.do(where)
schedule.every(30).minutes.do(apppre)
schedule.every(10).minutes.do(app)

while True:
    schedule.run_pending()
    time.sleep(1)