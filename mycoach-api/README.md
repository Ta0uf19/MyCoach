
# API for MyCoash
 Using Firebase Functions, Firestore and Dialogflow

###  POST /dialogflowGateway
-  Talk with chatbot

Query
```
{
	"sessionId": "50",
    "queryInput": {
		"text": {
			"text": "Salut!",
            "languageCode": "fr-FR"
		}
    }
}
```
### POST /detectIntent

-  Detect intention and update workout data

### POST /workouts
- Get workouts of a user in a specfic date
```
{
    "session": "demo@gmail.com",
    "date": "2020-06-18"
}
```

## Firestore db structure



## License

Copyright © 2020,  [Taoufik Tribki](https://github.com/ta0uf19). Released under the  [MIT License](https://github.com/jonschlinkert/update-copyright/blob/master/LICENSE).
