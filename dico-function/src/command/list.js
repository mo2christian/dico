/**
Initialisation
 */
var url = process.env.DICO_URL;

function list(req) {
    var contexts = req.queryResult.outputContexts;
    var word = "";
    contexts.forEach(function(item){
        if (item.name.includes('dico-count')){
            word = item.parameters.word;
        }
    })
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

    return new Promise(function(resole, reject) {

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
function getDialogflowResponse(result, session, word) {
    /* creation de la reponse */
    var resp = {};
    resp.fulfillmentText = '';
    resp.source = "monlabo.biz";
    resp.outputContexts = [];
    var msg = "";
    result.forEach(function (item) {
        msg = msg + item + ", ";
    });
    msg = "Vous pouvez former les mots suivants : " + msg;
    resp.fulfillmentText = msg;
    return resp;
}

/**
Creer le contexte
 */
function createContext(session, name, parameters) {
    var context = {};
    context.name = session + "/contexts/" + name;
    context.lifespanCount = 5;
    context.parameters = parameters;
    return context;
}

exports.run = list;