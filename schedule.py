import schedule
import time
import webbrowser

def job():
    url= "https://www.google.co.kr/search?q=national+park&source=lnms&tbm=nws"
    webbrowser.open(url) # Google 뉴스에서 'national park' 검색결과
    
# 매일 특정 HH:MM 및 다음 HH:MM:SS에 작업 실행
schedule.every().day.at("15:02").do(job)

while True:
    schedule.run_pending()
    time.sleep(1)