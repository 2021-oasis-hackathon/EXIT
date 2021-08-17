// 1. child-process모듈의 spawn 취득
const spawn = require('child_process').spawn;
// 2. spawn을 통해 "python 파이썬파일.py" 명령어 실행 
const result = spawn('python3', ['app-pre.py']);

result.stdout.on('data', function(data) 
{ console.log(data.toString()); }); 
result.stderr.on('data', function(data) 
{ console.log(data.toString()); });


