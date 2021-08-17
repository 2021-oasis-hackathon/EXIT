from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from bs4 import BeautifulSoup, BeautifulStoneSoup
from selenium.webdriver.support.ui import Select
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

f1=open("locate.txt",'r')
locate=f1.readline()
f1.close
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
f2=open("number.txt",'w')
f2.write(A[0]+'\n')
f2.write(locate)
f2.close

driver.implicitly_wait(5)

driver.quit() 
