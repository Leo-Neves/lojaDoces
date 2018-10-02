'use strict'

const express = require('express');
const router = express.Router();
const controller = require('../controllers/usuario-controller');

router.post('/acessar', controller.acessar);
router.post('/cadastrar', controller.cadastrar);

module.exports = router;