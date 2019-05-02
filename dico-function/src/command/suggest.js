/**
Initialisation
 */
var url = process.env.DICO_URL;
var max_length = (process.env.MAX_LENGTH) ? process.env.MAX_LENGTH : 25;

/**
Fonction pour suggerer des mots
 */
function suggest(req) {
    var word = req.queryResult.parameters.word;
    var request = require('request-promise');
    var session = req.session;

    var uri = url + "/suggest";
    const options = {
        method: 'GET',
        uri: uri,
        headers: {
            'Content-Type': 'application/json'
        },
        timeout: 10000,
        qs: {
            word: word
        }
    }

    return new Promise(function(resole, reject){

        request(options)
            .then(function(response) {
                console.log(response);
                resole(getDialogflowResponse(JSON.parse(response), session, word));
            })
            .catch(function(err) {
                reject(err);
            });

    });
}

/**
Produit la reponse de retour à dialogflow
 */
function getDialogflowResponse(result, session, word){
    /* creation de la reponse */
    var resp = {};
    resp.fulfillmentText = '';
    resp.source = "monlabo.biz";
    resp.outputContexts = [];
    /* creation du contexte */
    var parameters = {};
    parameters.word = word;
    resp.outputContexts.push(createContext(session, 'dico-suggest', parameters));
    /* creation du message */
    var msg = "";
    if (result.length > max_length){
        msg = "Vous pouvez former " + result.length + " mots avec ces lettres. Voulez-vous qu'on les envoie par mail ?";
        resp.outputContexts.push(createContext(session, 'dico-suggest-sendemail', parameters));
    }
    else{
        result.forEach(function (item) {
                msg = msg + item + ", ";
        });
        msg = "Vous pouvez former les mots suivants : " + msg;
    }
    resp.fulfillmentText = msg;
    return resp;
}

function createContext(session, name, parameters){
    var context = {};
    context.name = session + "/contexts/" + name;
    context.lifespanCount = 5;
    context.parameters = parameters;
    return context;
}

exports.run = suggest;