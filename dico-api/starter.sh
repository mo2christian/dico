#!/bin/bash
gsutil cp gs://PROJECT_ID/artefacts/APP_VERSION/dico.conf /etc/google-fluentd/config.d/dico.conf
service google-fluentd restart
export WORD_SET_LOCATION=gs://PROJECT_ID/words/liste.de.mots.francais.frgut.txt
gsutil cp gs://PROJECT_ID/artefacts/APP_VERSION/dico-api.jar .
java -jar dico-api.jar > /var/log/dico.log &