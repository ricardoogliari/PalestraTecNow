var five = require("johnny-five");
var express = require('express');

var led1, led2;
var board = new five.Board();
board.on("ready", function() {
  led1 = new five.Led(7);
  led2 = new five.Led(8);
});

var app = express();

var server = app.listen(8052);

app.get('/ligar', function(req, res){
    console.log('ligando...');
    res.send("{'status': 1}");
    led1.on();
    led2.on();
});

app.get('/desligar', function(req, res){
  console.log('desligando...');
  res.send("{'status': 1}");
  led1.off();
  led2.off();
});