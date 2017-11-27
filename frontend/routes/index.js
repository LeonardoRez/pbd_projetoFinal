var express = require('express');
var router = express.Router();
// var inicio = 1;
// var fim = 20;
var skip = 50;
var teste = "a";
var total = 0;



/* GET home page. */
// router.get('/', function (req, res, next) {
//   res.render('index', { title: 'Home' });
// });
router.get('/helloworld', function (req, resp) {
  resp.render('helloworld', { title: 'Yo' });
});
router.get('/tabela/([0-9]*)?', function (req, res) {
  var s = require('url').parse(req.url, true)['path'].split('/')[2];
  if(s == "")
    s=1;
  // console.log(require('url').parse(req.url, true)['path'].split('/')[2]);  
  var db = req.db;
  var collection = db.get('pessoa');
  var docs;
  total = -1;
  collection.count({}, function (error, count) {
    console.log(error, count);
    total = count;
  });
  collection.find({}, { limit: skip, skip: skip * s-1 }, function (e, d) {
    res.render('lista', {
      "titulo": "Registros",
      "documentos": d,
      "inicio": (1 + skip * (s - 1)),
      "fim": (skip * s),
      "total": total,
      "pg_atual": s,
      "skip": skip
    });
  });
});
/* Home Page */
router.get('/', function (req, res) {
  var db = req.db;
  var collection = db.get('pessoa');
  var docs;
  total = -1;
  collection.count({}, function (error, count) {
    console.log(error, count);
    total = count;
  });
  collection.find({}, { limit: skip, skip: skip * 0 }, function (e, d) {
    res.render('lista', {
      "titulo": "Registros",
      "documentos": d,
      "inicio": (1 + skip * (0)),
      "fim": (skip * 1),
      "total": total,
      "pg_atual": 1,
      "skip": skip
    });
  });
})

module.exports = router;
