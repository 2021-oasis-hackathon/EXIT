import schedule
import googlemaps
import time
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from bs4 import BeautifulSoup, BeautifulStoneSoup
from selenium.webdriver.support.ui import Select
from pyfcm import FCMNotification
import time

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
A[0]=int(A[0])  # 가장 최근에 올라온 게시글의 번호
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
            print(table2.text) #확인용 - 삭제
            if '확진' in table2.text:
                print("확진자발생")# server()
            elif '지진' in table2.text:
                print("지진발생")# server()
            elif '태풍' in table2.text:
                print("태풍대비")# server()
            elif '산불' in table2.text:
                print("산불발생")# server()
            elif '공습' in table2.text:
                print("공습경보발령")# server()
            elif '전쟁' in table2.text:
                print("공습경보발령")# server()
            else:
                pass
            click2=driver.find_element_by_class_name('list_btn')
            click2.click()
    else:
        print("최근 게시물 10개를 조회합니다." ) #확인용삭제
        for i in range(10):
            postnumber='bbs_tr_'+str(i)+'_bbs_title'
            click1 = driver.find_element_by_id(postnumber) #게시글 조회
            click1.click()
            table2 = driver.find_element_by_id('cn')
            print(table2.text) #확인용 - 삭제
            if '확진' in table2.text:
                print("확진자발생")# server()
            elif '지진' in table2.text:
                print("지진발생")# server()
            elif '태풍' in table2.text:
                print("태풍대비")# server()
            elif '산불' in table2.text:
                print("산불발생")# server()
            elif '공습' in table2.text:
                print("공습경보발령")# server()
            elif '전쟁' in table2.text:
                print("공습경보발령")# server()
            else:
                pass
            click2=driver.find_element_by_class_name('list_btn')
            click2.click()
f2=open("number.txt",'w')
f2.write(str(A[0]))
f2.close()
# 가장 최근에 올라온 게시글의 번호 저장 및 게시글 저장

driver.implicitly_wait(5) 
driver.quit() 