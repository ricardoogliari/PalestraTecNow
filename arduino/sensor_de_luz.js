var firebase = require("firebase");
var config = {
    apiKey: "AIzaSyCB-74Q-x4UzfVxeiAXgvkqBQc9Un7Qy9k",
    authDomain: "arduinofirebase-21dc5.firebaseapp.com",
    databaseURL: "https://arduinofirebase-21dc5.firebaseio.com",
    projectId: "arduinofirebase-21dc5",
    storageBucket: "arduinofirebase-21dc5.appspot.com",
    messagingSenderId: "355951549273"
};

firebase.initializeApp(config);

var database = firebase.database();

var five = require("johnny-five");
var board = new five.Board();

board.on("ready", function() {
  var light = new five.Light("A0");
  light.on("change", function() {
    console.log(this.level);

    firebase.database().ref().set({
        light: this.level
    });
  });
});