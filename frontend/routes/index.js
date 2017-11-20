var express = require('express');
var router = express.Router();
// var inicio = 1;
// var fim = 20;
var skip = 20;



/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Home' });
});
router.get('/helloworld', function (req, resp) {
  resp.render('helloworld', { title: 'Yo' });
});
router.get('/tabela', function (req, res) {
  var db = req.db;
  var collection = db.get('pessoa');
  var total = -1;
  collection.count({}, function (error, count) {
    console.log(error, count);
    total = count;
  });
  var documentos;
  collection.find({}, { limit: skip, skip: skip*1 }, function (e, docs) {
    console.log("\n\n\nteste\n\n\n");

    console.log(docs);
    console.log("\n\n\n\n");
    for (var key in docs) {
      let value = docs[key];
      console.log(value);
    }
    console.log("\n\n\nteste\n\n\n");
    
    res.render('lista', {
      "titulo": "Tabela",
      "documentos": docs,
      "inicio": (1+skip*(0)),
      "fim": (skip*1),
      "total": total
    });
  });
})

module.exports = router;
