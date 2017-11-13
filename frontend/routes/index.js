var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Home' });
});
router.get('/helloworld', function(req, resp){
  resp.render('helloworld', {title: 'Yo'});
})

module.exports = router;
