/**
Initialisation
 */
var api_key = process.env.API_KEY;
var sampleRate = (process.env.ENV === 'production') ? 5 : 10;
var tracer = require('@google-cloud/trace-agent').start({
    samplingRate: sampleRate
});

/**
Entry point
 */
exports.exec = function(req, resp) {
    console.log(req.body);
    /*if (req.header('Authorization') != api_key){
      resp.status(403).send("Unauthorized request");
      return;
    }*/
    var reqJson = req.body;
    var intent = reqJson.queryResult.intent.displayName;
    var command;
    if (intent.includes('[suggest]')){
        var suggest = require('./command/suggest');
        command = suggest.run;
    }
    if (intent.includes('[count]')){
        var count = require('./command/count');
        command = count.run;
    }
    if (intent.includes('[list]')){
        var list = require('./command/list');
        command = list.run;
    }
    tracer.runInRootSpan({ name: 'dialogflow_entrypoint.exec' }, (rootSpan) => {
        span = rootSpan;
        span.addLabel('command', intent);
        command(reqJson)
            .then(function(result){
                console.log(result);
                resp.status(200).send(result);
                span.endSpan();
            }, function(err){
                console.log(err);
                resp.status(500).send(err);
                span.endSpan();
            })
    });
}