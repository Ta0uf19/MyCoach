import * as functions from 'firebase-functions';
const { SessionsClient } = require('dialogflow');
const _ = require('lodash');
import * as admin from 'firebase-admin'
import * as serviceAccount from "../service-account-key.json";
import * as moment from 'moment'

const serviceParams = {
    type: serviceAccount.type,
    projectId: serviceAccount.project_id,
    privateKeyId: serviceAccount.private_key_id,
    privateKey: serviceAccount.private_key,
    clientEmail: serviceAccount.client_email,
    clientId: serviceAccount.client_id,
    authUri: serviceAccount.auth_uri,
    tokenUri: serviceAccount.token_uri,
    authProviderX509CertUrl: serviceAccount.auth_provider_x509_cert_url,
    clientC509CertUrl: serviceAccount.client_x509_cert_url
};
admin.initializeApp({
    credential: admin.credential.cert(serviceParams),
    databaseURL: "https://mycoach-77f86.firebaseio.com"
});
let db = admin.firestore();
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

exports.detectIntent = functions.https.onRequest(async (request, response) => {

    //console.info(JSON.stringify(request.body));
    const query = request.body.queryResult;
    const params = query.parameters;
    const session = request.body.session.split('/')[4];

    console.info(request.body);
    // intent to detect free days
    if(query.intent.displayName === "3-detect-free-day") {
        // array of free days
        let days: number[] = [];

        // check dates
        if(params.date1.length>0) {
            const day = new Date(query.parameters.date1[0]).getDay();
            days.push(day);
        }
        if(params.date2.length>0) {
            const day = new Date(query.parameters.date2[0]).getDay();
            days.push(day);
        }
        if(params.date3.length>0) {
            const day = new Date(query.parameters.date3[0]).getDay();
            days.push(day);
        }
        if(params.date4.length>0) {
            const day = new Date(query.parameters.date4[0]).getDay();
            days.push(day);
        }
        console.info(days);

        let workouts: any[] = [];
        let dbWorkouts: any[] = [];
        let workoutsRef = db.collection('workouts');
        let userRef = db.collection('users').doc(session);

        // get workouts data
        await workoutsRef.get().then(async (snapshot: FirebaseFirestore.QuerySnapshot<FirebaseFirestore.DocumentData>) => {
            snapshot.forEach((doc: FirebaseFirestore.QueryDocumentSnapshot<FirebaseFirestore.DocumentData>) => {
                console.log(doc.id, '=>', doc.data());
                dbWorkouts.push(doc.data());
            });

            // construct workouts for 14 days and with only
            // free days of week as request by user

            let today = moment();
            for (let j = 0; j <= 14; j++) {
                const day: string = today.format('YYYY-MM-DD');
                const nday = (new Date(day)).getDay();
                days.forEach(d => {
                    if (nday === d) {
                        //6 workouts per day
                        for(let i = 0; i<=5; i++) {
                            workouts.push({date: day, workout: _.sample(dbWorkouts)});
                        }
                    }
                });
                today.add(1, 'days');
            }

            // update user workouts
            await userRef.update({workouts: workouts , weekly_days: days});

        }).catch((err: any) => {
                console.log('Error getting documents', err);
        });
    }

    response.send("ok");
});

/**
 * Get workout of a specific user
 * by date
 */
exports.workouts = functions.https.onRequest(async (request, response) => {

    const {session} = request.body;
    const date = moment(request.body.date).format('YYYY-MM-DD');
    //let userRef = db.collection("users");

    console.log(date);

    let filtred: any[] = [];
    //let workouts: any[] = [];

    let userRef = db.collection('users').doc(session);
    await userRef.get().then((doc: any) => {

            if (!doc.exists) {
                console.info('No such document!');
            } else {
                console.info('Document data:', doc.data());
                filtred = doc.data().workouts.filter((o: { date: string; }) => o.date === date);

                console.log(filtred);
            }

        }).catch(e => console.log(e));

    response.send({workouts: filtred});

});
