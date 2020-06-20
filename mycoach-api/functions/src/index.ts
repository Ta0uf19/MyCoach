import * as functions from 'firebase-functions';
const { SessionsClient } = require('dialogflow');
const admin = require('firebase-admin');
import * as serviceAccount from "../service-account-key.json";

let db = admin.firestore();

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://mycoach-77f86.firebaseio.com"
});

exports.dialogflowGateway = functions.https.onRequest(async (request, response) => {

    const {queryInput, sessionId} = request.body;

    console.log(request.body);
    console.log(serviceAccount.project_id);

    const sessionClient = new SessionsClient({credentials: serviceAccount});
    const session = sessionClient.sessionPath(serviceAccount.project_id, sessionId);

    const responses = await sessionClient.detectIntent({session, queryInput});

    const result = responses[0].queryResult;

    response.send(result);
});


exports.workouts = functions.https.onRequest(async (request, response) => {
    db.collection("users");

    //response.send("hello")
    // db.collection('users').get()
    //     .then((snapshot) => {
    //         snapshot.forEach((doc) => {
    //             console.log(doc.id, '=>', doc.data());
    //         });
    //     })
    //     .catch((err) => {
    //         console.log('Error getting documents', err);
    //     });

});
