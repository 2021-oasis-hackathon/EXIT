const admin = require('firebase-admin')


let serAccount = require('/home/pjy/hackerton/server/exit-saferoute-8b9cb-firebase-adminsdk-ywwcq-08788c5fcd.json'	
    )

    admin.initializeApp({
      credential: admin.credential.cert(serAccount),
    })