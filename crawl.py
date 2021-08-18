


import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from selenium.webdriver.support.ui import Select
import time

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from urllib.request import urlopen
from urllib import parse
from urllib.request import Request
from urllib.error import HTTPError
import json

#naver api
client_id = 'nsaeqr01dp'
client_pw = '5t2VWdCXQyF7IJc3E9wvqoHVBlO8UCUA8c8By54J'
api_url = 'https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query='

cred = credentials.Certificate("exit-saferoute-8b9cb-firebase-adminsdk-ywwcq-2da185bbb8.json")
firebase_admin.initialize_app(cred)
db = firestore.client()
options = webdriver.ChromeOptions()
options.add_argument("--incognito")
options.add_argument("--window-size=1920x1080")
options.add_argument('--disable-extensions')
options.add_argument('--headless')
options.add_argument('--disable-gpu')
options.add_argument('--no-sandbox')
#크롬 드라이버 선택
driver = webdriver.Chrome(
        executable_path = '/home/pjy/hackerton/chromedriver',
        chrome_options=options    
        ) 

#자신이 크롤링 하고싶은 사이트
driver.get('https://www.safekorea.go.kr/idsiSFK/neo/sfk/cs/contents/civil_defense/SDIJKM1402.html?menuSeq=57')

#사이트가 전부다 열릴 때까지 대기
driver.implicitly_wait(3)

# 순서대로 광주, 전북, 전남
areaArray = ['6290000', '6450000', '6460000']


#지역을 순서대로 검색하는 반복문
for areaCode in areaArray:
    time.sleep(0.001)
    #큰갈래, 중간갈래 선택(광역시, 구)
    select_Area1 = Select(driver.find_element_by_xpath('//*[@id="sbRnArea1"]'))
    select_Area1.select_by_value(areaCode)
    select_Area1.select_by_value(areaCode)

    time.sleep(0.1)
    WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.ID, "sbRnArea2")))
    underArea = driver.find_element_by_xpath('//*[@id="sbRnArea2"]')
    underAreaArray = underArea.find_elements_by_tag_name('option')

    #하위 구,시,군 선택
    for UA in underAreaArray[1:]:
        select_Area2 = Select(driver.find_element_by_xpath('//*[@id="sbRnArea2"]'))
        select_Area2.select_by_visible_text(f'{UA.text}')
        print(f"현재 {UA.text}를 선택했습니다.")

        #조회 버튼 누르기
        search_btn = driver.find_element_by_xpath('//*[@id="btnSearchOk"]')
        search_btn.click()
        time.sleep(0.01)
        search_btn.click()

        #속성 들어가기
        time.sleep(0.01)
        table = driver.find_element_by_tag_name('table')


        #토탈 페이지
        time.sleep(0.01)
        pagenum = driver.find_element_by_xpath('//*[@id="tbpagetotal"]')
        time.sleep(0.01)
        pagenum = int(pagenum.text[1:])
        if type(pagenum) is str:
            pagenum = int(pagenum.text[1:])

        #다음 페이지
        nextpage = driver.find_element_by_xpath('//*[@id="apagenext"]')
        time.sleep(0.01)

        #게시글 수 확인 변수
        num=0

        print(f"\n 총 {pagenum} 페이지\n")
        for i in range(pagenum):
            tbody = table.find_element_by_tag_name("tbody")
            rows = tbody.find_elements_by_tag_name("tr")
            time.sleep(0.01)
            for value in rows:
                time.sleep(0.1)
                area = value.find_elements_by_tag_name("td")
                line = area[0].text
                pasing = line.splitlines()
                add=pasing[0][6:]
                print(add)
                # 아스키코드가 아닌 문자를 url로 인코딩
                add_urlenc = parse.quote(add)
                # url에 주소정보까지 합체
                url = api_url + add_urlenc
                # url로 요청하고 ID와 Secret Key를 헤더에 추가한다.
                request = Request(url)
                request.add_header('X-NCP-APIGW-API-KEY-ID', client_id)
                request.add_header('X-NCP-APIGW-API-KEY', client_pw)

                try:
                    response = urlopen(request)
                except HTTPError as e:
                    print(e)
                    latitude = None
                    longitude = None
                else:
                    rescode = response.getcode()
                    if rescode == 200:
                        response_body = response.read().decode('utf-8')
                        # json형태로 읽어오기
                        time.sleep(0.001)
                        response_body = json.loads(response_body)
                        if 'addresses' in response_body:
                            try:
                                # 위도값
                                latitude = response_body['addresses'][0]['y']
                                # 경도값
                                longitude = response_body['addresses'][0]['x']
                                print("Success!")
                            except IndexError:
                                latitude = -100
                                longitude = -100
                        else:
                            print("'result' not exist")
                            latitude = -100
                            longitude = -100
                    else:
                        print('Response error code : %d' % rescode)
                        latitude = -100
                        longitude = -100
                add_d = add.split()
                try:
                    add_d = add_d[1]
                except IndexError:
                    add_d = 'NO_NAME'
                doc_ref = db.collection(f'{add_d}').document(f'{area[1].text.splitlines()[0]}')
                doc_ref.set({
                    u'신주소' : add,
                    u'명칭' : area[1].text.splitlines()[0],
                    u'lat' : float(latitude),
                    u'lng' : float(longitude)
                })
                print(f"위도 : {latitude}, 경도 : {longitude}")
                print(area[1].text.splitlines()[0])
                num = num+1
            if i != pagenum-1:
                nextpage.click()
            else:
                break
            print("next page")
            time.sleep(0.01)
        print(f"{UA.text}지역의 총 게시글은 {num}건")


#웹 브라우저 종료
driver.quit()