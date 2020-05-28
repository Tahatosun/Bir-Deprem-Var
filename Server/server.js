const express = require('express')
const app = express()
var getJSON = require('get-json');
var sendNotif = require('send-notification');

var json;

var SonBuyukDepremler=[];
var kontrol = 0;



setInterval(() => {
    getJSON('API_URL', function(error, response){
     json=response;
    
     depremSiddetiKontrolu();

});
}, 60*1000);





function depremSiddetiKontrolu(){
    for(var i = 0; i<10;i++){
        if(json["result"][i]["mag"]>5){

            var deprem = {
                "mag":json["result"][i]["mag"],
                "lat":json["result"][i]["lat"],
                "lng":json["result"][i]["lng"],
                "hash":json["result"][i]["hash"]
            };

            SonBuyukDepremler.forEach(element => {
                if(element.hash == deprem.hash){
                    kontrol++;   
                }
            });
            if(kontrol<1){
                SonBuyukDepremler.push(deprem);
                BildirimGonder(deprem);
                
            }
            kontrol=0;
  
        }       
    }
    
}

function BildirimGonder(deprem){

    var message = { 
        app_id: "",
        headings:{"en": "Lütfen güvende olduğunuzu bildirin!"},
        contents: {"en": "100 KM yakınınızda "+deprem.mag+" büyüklüğünde deprem meydana geldi lütfen güvende olduğunuzu bildirin!"},
        data:{"customkey":"deneme"},
        filters: [
            {"field": "location", "radius": "100000", "lat":""+deprem.lat, "long":""+deprem.lng}
            
        ]
        
    };   
    sendNotif.sendNotification(message);
}




app.get('/SonDepremler',function(req,res){
    res.json(json);
});



app.listen(45000,function(){
    console.log(45000+" nolu portta sunucu başladı");
});


