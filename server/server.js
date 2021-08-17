const admin = require('firebase-admin')


let serAccount = require('/home/pjy/hackerton/server/exit-saferoute-8b9cb-firebase-adminsdk-ywwcq-08788c5fcd.json'	
    )

    admin.initializeApp({
      credential: admin.credential.cert(serAccount),
    })

// push PAGE
router.get('/push_send', function (req, res, next) {
  let target_token =
    'e1PxtLrCS7q3UQ73O7Pdyg:APA91bGV_LlNDVBE3tQvYpB2w3WjibpDO15DsaPjENc_3_snJ5PlQDytNriNbNGdbNxHNKYMF8IzgYWFujiI_QP6Pg2qyzTlL8oVp_2OtMqlz3gSsP5vYwXl2-RvYPh_s3FeW6YFPpMl'
    //target_token은 푸시 메시지를 받을 디바이스의 토큰값입니다

  let message = {
    data: {
      title: '테스트 데이터 발송',
      body: '데이터가 잘 가나요?',
      style: '굳굳',
    },
    token: target_token,
  }

  admin
    .messaging()
    .send(message)
    .then(function (response) {
      console.log('Successfully sent message: : ', response)
    })
    .catch(function (err) {
      console.log('Error Sending message!!! : ', err)
    })
})